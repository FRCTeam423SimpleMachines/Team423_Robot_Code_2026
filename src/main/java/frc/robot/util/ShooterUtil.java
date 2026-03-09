package frc.robot.util;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Constants;
import frc.robot.subsystems.drive.Drive;
import org.photonvision.PhotonUtils;

public class ShooterUtil {
  private final Drive drive;

  public ShooterUtil(Drive drive) {
    this.drive = drive;
  }

  public double calculateRPMForDistance(Pose2d targetPose) {
    double distance = PhotonUtils.getDistanceToPose(drive.getPose(), targetPose);
    double theta = Constants.shooterAngle; // Angle of the shooter in radians
    return 0.0;
  }

  public double getRobotAngleToHub(Pose2d targetPose) {
    return PhotonUtils.getYawToPose(drive.getPose(), targetPose).getDegrees();
  }
}
