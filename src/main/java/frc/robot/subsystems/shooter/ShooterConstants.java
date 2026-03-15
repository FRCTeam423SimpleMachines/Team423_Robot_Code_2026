package frc.robot.subsystems.shooter;

public class ShooterConstants {
  public static final int shooterCanID = 63;
  public static final int secondShooterCanID = 62;
  public static final int turretCanID = 61;

  public static final double shooterkP = 0.82;
  public static final double shooterkI = 0.0;
  public static final double shooterkD = 0.0;

  public static final double turretkP = 0.8;
  public static final double turretkI = 0.1;
  public static final double turretkD = 0.1;

  public static final double shooterkV = 0.0;
  public static final double shooterkS = 0.33;
  public static final double shooterkA = 0.80;

  public static final double turretRotationsPerDegree = 280.0 / 360.0;

  public static final double shooterAngle = Math.toRadians(45.0); // insert degrees number
}
