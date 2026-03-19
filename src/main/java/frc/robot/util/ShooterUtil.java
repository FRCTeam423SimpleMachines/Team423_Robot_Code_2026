package frc.robot.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.ShooterConstants;
import org.photonvision.PhotonUtils;

public class ShooterUtil {
  public static double calculateRPMForDistance(Drive drive, Pose2d targetPose) {
    double distanceFromTarget = PhotonUtils.getDistanceToPose(getPredictedPose(drive), targetPose);
    double theta = ShooterConstants.shooterAngle; // Angle of the shooter in radians
    return 0.0;
  }

  public static double getRobotAngleToPose(Drive drive, Pose2d targetPose) {
    Pose2d predictedPose = getPredictedPose(drive);
    return PhotonUtils.getYawToPose(predictedPose, targetPose).getDegrees();
  }

  public static Pose2d getPredictedPose (Drive drive){
    
    Pose2d currentPose = drive.getPose();

    // Predict where the robot will be in 20 ms using measured chassis speeds
    ChassisSpeeds speeds = drive.getChassisSpeeds();
    double dt = 0.020; // 20 milliseconds
    double dx = speeds.vxMetersPerSecond * dt;
    double dy = speeds.vyMetersPerSecond * dt;
    double prediction = ShooterConstants.PredictionLoops;
    double dtheta = speeds.omegaRadiansPerSecond * dt * prediction;

    Transform2d predictedTransform =
        new Transform2d(new Translation2d(dx, dy), new Rotation2d(dtheta));

    Pose2d predictedPose = currentPose.transformBy(predictedTransform);
    return predictedPose;
  }
}
