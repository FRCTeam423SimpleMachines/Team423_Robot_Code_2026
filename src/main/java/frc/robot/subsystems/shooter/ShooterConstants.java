package frc.robot.subsystems.shooter;

import edu.wpi.first.math.util.Units;

public class ShooterConstants {
  public static final int shooterCanID = 61;
  public static final int secondShooterCanID = 62;

  public static final double shooterkP = 0.78;
  public static final double shooterkI = 0.0;
  public static final double shooterkD = 0.0;

  public static final double shooterkV = 0.0;
  public static final double shooterkS = 0.31;
  public static final double shooterkA = 0.41;

  public static final double shooterAngle = Math.toRadians(60.0); // insert degrees number
  public static final double shooterHeight = Units.inchesToMeters(20.0);
  public static final double shooterRadius = Units.inchesToMeters(1.875);
  public static final double maxFlywheelRPM = 6000.0;
  // Offset of the shooter exit relative to the robot pose (robot coordinate frame):
  // +X is forward, +Y is to the left (WPILib standard). Adjust these to match your
  // real robot measurement. Default 0 means shooter is at robot origin.
  public static final double shooterOffsetXMeters = Units.inchesToMeters(5.0);
  public static final double shooterOffsetYMeters = Units.inchesToMeters(0.0);

  // Tolerance (meters) used when checking descending-impact threshold. A shot whose
  // distance is within +/- this tolerance of the strict threshold will be allowed.
  public static final double shooterRangeToleranceMeters = 0.5;

  public static final double PredictionLoops = 40.0;

  public static final double slipCoefficient = 2.9;
  // public static final double slipCoefficient = 2.5;
}
