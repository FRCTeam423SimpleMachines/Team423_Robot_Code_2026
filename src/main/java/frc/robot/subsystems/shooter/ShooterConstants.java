package frc.robot.subsystems.shooter;

public class ShooterConstants {
  public static final int shooterCanID = 63;
  public static final int secondShooterCanID = 62;
  public static final int turretCanID = 61;

  public static final double kP = 1.5;
  public static final double kI = 1.0;
  public static final double kD = 0.1;

  public static final double turretkP = 0.0;
  public static final double turretkI = 0.0;
  public static final double turretkD = 0.0;

  public static final double kV = 0.85;
  public static final double kS = 0.0;

  public static final double turretRotationsPerDegree = 42.0;

  public static final double shooterAngle = Math.toRadians(45.0); // insert degrees number
}
