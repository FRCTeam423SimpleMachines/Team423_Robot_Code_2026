package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;

public final class FieldConstants {

  public static enum FieldTarget {
    HUB,
    OUTPOST,
    DEPOT
  }

  public static final Pose2d RED_HUB_POSE = new Pose2d(11.9, 4.0, new Rotation2d());
  public static final Pose2d BLUE_HUB_POSE = new Pose2d(4.6, 4.0, new Rotation2d());
  public static final Pose2d BLUE_OUTPOST_POSE = new Pose2d(2.0, 7.1, new Rotation2d());
  public static final Pose2d RED_OUTPOST_POSE = new Pose2d(11.4, 7.1, new Rotation2d());
  public static final Pose2d BLUE_DEPOT_POSE2D = new Pose2d(2.0, 0.9, new Rotation2d());
  public static final Pose2d RED_DEPOT_POSE2D = new Pose2d(11.4, 0.9, new Rotation2d());

  public static final double HUB_HEIGHT = Units.inchesToMeters(72.0);


  public static Pose2d getTargetPose(FieldTarget target, DriverStation.Alliance alliance) {
    switch (target) {
      case HUB:
        return alliance == DriverStation.Alliance.Red ? RED_HUB_POSE : BLUE_HUB_POSE;
      case OUTPOST:
        return alliance == DriverStation.Alliance.Red ? RED_OUTPOST_POSE : BLUE_OUTPOST_POSE;
      case DEPOT:
        return alliance == DriverStation.Alliance.Red ? RED_DEPOT_POSE2D : BLUE_DEPOT_POSE2D;
      default:
        throw new IllegalArgumentException("Unknown target: " + target);
    }
  }
}
