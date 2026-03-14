package frc.robot.util;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.ShooterConstants;
import org.photonvision.PhotonUtils;

public class ShooterUtil {
  public static double calculateRPMForDistance(Drive drive, Pose2d targetPose) {
    double distance = PhotonUtils.getDistanceToPose(drive.getPose(), targetPose);
    double theta = ShooterConstants.shooterAngle; // Angle of the shooter in radians
    return 0.0;
  }

  public static double getRobotAngleToPose(Drive drive, Pose2d targetPose) {
    return PhotonUtils.getYawToPose(drive.getPose(), targetPose).getDegrees();
  }
}
