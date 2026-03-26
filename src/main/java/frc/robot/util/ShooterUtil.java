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
  public static double calculateRPMForDistance(
      Drive drive, Pose2d targetPose, double targetHeight) {
    // getPredictedPose now returns the shooter exit pose (robot prediction + shooter offsets)
    Pose2d shooterPose = getPredictedPose(drive);
    double distanceFromTarget = PhotonUtils.getDistanceToPose(shooterPose, targetPose);

    // Shooter geometry and launch angle
    double theta = ShooterConstants.shooterAngle; // radians
    double shooterExitHeight = ShooterConstants.shooterHeight; // meters

    // Vertical difference between target and shooter exit
    double h = targetHeight - shooterExitHeight;

    final double g = 9.81; // gravity (m/s^2)

    double cosTheta = Math.cos(theta);
    double tanTheta = Math.tan(theta);

    // Forbid ascending impacts: for our fixed launch angle theta and vertical offset h,
    // the impact is descending iff d > 2*h / tan(theta). Apply a tolerance so shots
    // within +/- shooterRangeToleranceMeters of the threshold are allowed.
    double eps = 1e-6;
    double tol = ShooterConstants.shooterRangeToleranceMeters;
    if (Math.abs(tanTheta) < eps) {
      // tan(theta) is effectively zero (flat shooter). If the target is above the shooter,
      // we cannot have a descending impact — forbid the shot.
      if (h > 0.0) {
        return 0.0;
      }
    } else {
      double thresholdD = 2.0 * h / tanTheta;
      // Allow within tolerance: only forbid if distance is less than threshold - tol
      if (distanceFromTarget <= thresholdD - tol) {
        // Shot would still be ascending at the target (beyond tolerance)
        return 0.0;
      }
    }

    // v^2 = (g * d^2) / (2 cos^2(theta) * (d tan(theta) - h))
    double denom = 2.0 * cosTheta * cosTheta * (distanceFromTarget * tanTheta - h);
    if (denom <= 0.0 || Double.isNaN(denom)) {
      // No physical solution for this geometry and angle
      return 0.0;
    }

    double vSquared = (g * distanceFromTarget * distanceFromTarget) / denom;
    if (vSquared <= 0.0 || Double.isNaN(vSquared)) {
      return 0.0;
    }

    double v = Math.sqrt(vSquared); // required linear exit speed (m/s)

    // Convert exit linear speed to wheel RPM: v = omega * r, omega = 2*pi*(rpm/60)
    double wheelRadius = ShooterConstants.shooterRadius; // meters
    if (wheelRadius <= 0.0 || Double.isNaN(wheelRadius)) {
      return 0.0;
    }

    double rpm = v * 60.0 / (2.0 * Math.PI * wheelRadius);
    if (Double.isNaN(rpm) || rpm <= 0.0) {
      return 0.0;
    }

    // If computed RPM exceeds hardware capability, clamp to maxFlywheelRPM.
    if (rpm > ShooterConstants.maxFlywheelRPM) {
      return ShooterConstants.maxFlywheelRPM;
    }

    return rpm;
  }

  public static double getRobotAngleToPose(Drive drive, Pose2d targetPose) {
    Pose2d shooterPose = getPredictedPose(drive);
    return PhotonUtils.getYawToPose(shooterPose, targetPose).getDegrees();
  }

  public static Pose2d getPredictedPose(Drive drive) {

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

    // Apply shooter offset so callers receive the shooter exit Pose2d directly.
    Transform2d shooterOffsetTransform =
        new Transform2d(
            new Translation2d(
                ShooterConstants.shooterOffsetXMeters, ShooterConstants.shooterOffsetYMeters),
            new Rotation2d());

    Pose2d shooterPose = predictedPose.transformBy(shooterOffsetTransform);
    return shooterPose;
  }
}
