package frc.robot.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Vision;
import frc.robot.subsystems.vision.VisionConstants;
import java.util.LinkedList;
import java.util.List;

public class SearchAndDestroy extends Command {
  private final Drive drive;
  private final Vision vision;
  private Transform2d targetTransform;
  private Pose2d robotPose;
  private Pose2d targetPose;
  Command dynamicPathCommand;
  private boolean dynamicCommandStarted = false;
  private LinkedList<Boolean> balls;

  public SearchAndDestroy(Drive drive, Vision vision) {
    this.drive = drive;
    this.vision = vision;
    addRequirements(drive, vision);
    balls = new LinkedList<Boolean>();
    for (int i = 0; i < 50; i++) {
      balls.push(true);
    }
  }

  @Override
  public void initialize() {

    // If there are no vision targets, don't attempt to generate a path.
    if (!vision.getHasTargets()) {
      return;
    }

    generatePath();
  }

  @Override
  public void execute() {
    // Maintain a sliding window of the last 4 vision frames
    balls.addLast(vision.getHasTargets());
    if (balls.size() > 4) {
      balls.removeFirst();
    }
    if (dynamicPathCommand != null) {
      if (!dynamicCommandStarted) {
        System.out.println("[SearchAndDestroy] Starting dynamicPathCommand execution");
        dynamicCommandStarted = true;
      }
      dynamicPathCommand.execute();
    }
  }

  @Override
  public boolean isFinished() {
    // If we haven't created a dynamic path command, we're finished.
    if (dynamicPathCommand == null) {
      return true;
    }

    // If the dynamic path command has completed, we're finished.
    if (dynamicPathCommand.isFinished()) {
      return true;
    }

    // Only finish due to vision loss if ALL recent frames show no target.
    // Previously we returned true if any frame was false which caused the
    // command to end immediately as soon as a single frame lost the target.
    boolean allLost = true;
    for (int i = 0; i < balls.size(); i++) {
      if (balls.get(i)) {
        allLost = false;
        break;
      }
    }
    return allLost;
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    if (dynamicPathCommand != null) {
      System.out.println(
          "[SearchAndDestroy] Ending dynamicPathCommand, interrupted=" + interrupted);
      dynamicPathCommand.end(interrupted);
    }
  }

  public void generatePath() {
    // Get camera-frame translations to all detected balls
    java.util.List<edu.wpi.first.math.geometry.Translation2d> ballTranslations =
        vision.getTranslationsToBalls();

    robotPose = drive.getPose();

    // VisionConstants provides a robot->camera transform (robotToCameraFront).
    Transform3d robotToCameraTransform = VisionConstants.robotToCameraFront;
    Transform2d robotToCamera =
        new Transform2d(
            robotToCameraTransform.getX(),
            robotToCameraTransform.getY(),
            new Rotation2d(robotToCameraTransform.getRotation().getZ()));

    // If multiple balls are detected, build an ordered path through their approach poses
    if (ballTranslations != null && !ballTranslations.isEmpty()) {
      double approachMeters = 0.45; // tune per robot

      // Build target poses in field coordinates
      java.util.List<Pose2d> targetFieldPoses = new java.util.ArrayList<>();
      for (var t : ballTranslations) {
        Transform2d tt = new Transform2d(t.getX(), t.getY(), new Rotation2d());
        Pose2d fieldTarget = robotPose.plus(robotToCamera).plus(tt);
        targetFieldPoses.add(fieldTarget);
      }

      // Compute approach poses sequentially: each approach pose is `approachMeters` away
      // from its target along the line from the previous waypoint to the target.
      java.util.List<Pose2d> approachPoses = new java.util.ArrayList<>();
      Pose2d prev = robotPose;
      for (Pose2d tgt : targetFieldPoses) {
        double dx = tgt.getX() - prev.getX();
        double dy = tgt.getY() - prev.getY();
        double dist = Math.hypot(dx, dy);
        if (dist < 1e-6) {
          // Skip degenerate target
          continue;
        }
        double ux = dx / dist;
        double uy = dy / dist;
        double ax = tgt.getX() - ux * Math.min(approachMeters, Math.max(0.0, dist - 0.05));
        double ay = tgt.getY() - uy * Math.min(approachMeters, Math.max(0.0, dist - 0.05));
        Rotation2d heading = new Rotation2d(Math.atan2(uy, ux));
        Pose2d ap = new Pose2d(ax, ay, heading);
        approachPoses.add(ap);
        prev = ap;
      }

      if (!approachPoses.isEmpty()) {
        // Build an array of poses starting with the current robot pose
        Pose2d[] poses = new Pose2d[approachPoses.size() + 1];
        poses[0] = robotPose;
        for (int i = 0; i < approachPoses.size(); i++) poses[i + 1] = approachPoses.get(i);

        List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(poses);
        PathPlannerPath path =
            new PathPlannerPath(
                waypoints,
                DriveConstants.kDefaultConstraints,
                null,
                new GoalEndState(0.0, poses[poses.length - 1].getRotation()));
        path.preventFlipping = true;
        dynamicPathCommand = AutoBuilder.followPath(path);
        dynamicPathCommand.initialize();
        System.out.println(
            "[SearchAndDestroy] multi-target dynamicPathCommand created; waypoints="
                + waypoints.size());
      } else {
        System.out.println("[SearchAndDestroy] no valid approach poses from translations");
        dynamicPathCommand = null;
      }
    } else {
      // Fallback to single-target behavior
      targetTransform =
          new Transform2d(
              vision.getTranslationToBall().getX(),
              vision.getTranslationToBall().getY(),
              new Rotation2d());
      targetPose = robotPose.plus(robotToCamera).plus(targetTransform);
      double approachMeters = 0.45;
      dynamicPathCommand = buildPathToApproach(robotPose, targetPose, approachMeters);
      if (dynamicPathCommand != null) {
        System.out.println("[SearchAndDestroy] dynamicPathCommand created; initializing");
        dynamicPathCommand.initialize();
      } else {
        System.out.println("[SearchAndDestroy] dynamicPathCommand is NULL (no path generated)");
      }
      System.out.println("TargetPose: " + targetPose.toString());
    }

    System.out.println("RobotPose: " + robotPose.toString());
  }

  /**
   * Build a PathPlanner command to approach a target pose but stop a distance away. Returns null if
   * inputs are invalid or path cannot be generated.
   */
  private Command buildPathToApproach(
      Pose2d robotPose, Pose2d targetPose, double approachDistance) {
    if (robotPose == null || targetPose == null) {
      System.out.println("[SearchAndDestroy] buildPathToApproach: robotPose or targetPose is null");
      return null;
    }

    double dx = targetPose.getX() - robotPose.getX();
    double dy = targetPose.getY() - robotPose.getY();
    double dist = Math.hypot(dx, dy);
    if (Double.isNaN(dist) || dist < 1e-6) {
      System.out.println("[SearchAndDestroy] buildPathToApproach: invalid dist=" + dist);
      return null;
    }

    // Clamp approachDistance to something less than the full distance
    double d = Math.max(0.0, Math.min(approachDistance, Math.max(0.0, dist - 0.05)));

    double ux = dx / dist;
    double uy = dy / dist;
    double approachX = targetPose.getX() - ux * d;
    double approachY = targetPose.getY() - uy * d;

    // Face toward the target by default
    Rotation2d finalHeading = new Rotation2d(Math.atan2(uy, ux));
    Pose2d approachPose = new Pose2d(approachX, approachY, finalHeading);

    // Build waypoints and path
    List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(robotPose, approachPose);
    PathPlannerPath path =
        new PathPlannerPath(
            waypoints,
            DriveConstants.kDefaultConstraints,
            null,
            new GoalEndState(drive.getLinearSpeed(), approachPose.getRotation()));
    path.preventFlipping = true;

    System.out.println(
        "ApproachPose: " + approachPose.toString() + " dist=" + dist + " approachD=" + d);
    System.out.println("Waypoints: " + waypoints.size());
    return AutoBuilder.followPath(path);
  }
}
