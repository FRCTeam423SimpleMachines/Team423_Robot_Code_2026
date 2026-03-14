package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.DigitalInput;

public class ShooterIOTalonFX implements ShooterIO {
  private final TalonFX shooter = new TalonFX(ShooterConstants.shooterCanID);
  private final TalonFX shooter2 = new TalonFX(ShooterConstants.secondShooterCanID);
  private final TalonFX turret = new TalonFX(ShooterConstants.turretCanID);

  private final PIDController pid =
      new PIDController(ShooterConstants.kP, ShooterConstants.kI, ShooterConstants.kD);
  private final SimpleMotorFeedforward feedforward =
      new SimpleMotorFeedforward(ShooterConstants.kS, ShooterConstants.kV);
  private final PIDController turretPID =
      new PIDController(
          ShooterConstants.turretkP, ShooterConstants.turretkI, ShooterConstants.turretkD);
  // private final SparkFlex shooter2 =
  //   new SparkFlex(ShooterConstants.secondShooterCanID, MotorType.kBrushless);

  private final DigitalInput laser1 = new DigitalInput(0);
  private final DigitalInput laser2 = new DigitalInput(1);
  private final DigitalInput magnometer1 = new DigitalInput(2);

  private double TargetRPM = 0;
  public double TurretTargetAngle = 0;

  public ShooterIOTalonFX() {}

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    inputs.flywheelRPM = shooter.getVelocity().getValueAsDouble();
    inputs.turretAngle = getTurretAngle();
    inputs.targetRPM = TargetRPM;
    inputs.turretTargetAngle = TurretTargetAngle;
    pid.setTolerance(500);
    turretPID.setTolerance(3);
    inputs.laser1 = laser1.get();
    inputs.laser2 = laser2.get();
    inputs.magnet1 = magnometer1.get();
  }

  @Override
  public double getTurretAngle() {
    return turret.getPosition().getValueAsDouble() / ShooterConstants.turretRotationsPerDegree;
  }

  @Override
  public void runShooter(double voltage) {
    shooter.setVoltage(voltage);
  }

  @Override
  public void runAtSpeed(double speed) {
    shooter.set(speed);
  }

  @Override
  public void magnetTest() {
    turret.set(0.5);
    if (magnometer1.get()) {
      turret.set(0);
      turret.setPosition(0);
    }
  }

  @Override
  public void runAtTarget() {
    double output =
        ((pid.calculate(shooter.getVelocity().getValueAsDouble(), TargetRPM)
                + feedforward.calculate(TargetRPM))
            / 6000.0);
    shooter.set(output);
    shooter2.set(-output);
    double turretOutput = (turretPID.calculate(getTurretAngle(), TurretTargetAngle));
    turret.set(turretOutput);
  }

  @Override
  public void runAtTarget(double RPM) {
    shooter.set(
        (pid.calculate(shooter.getVelocity().getValueAsDouble(), RPM) + feedforward.calculate(RPM))
            / 6000.0);
  }

  @Override
  public void setTargetRPM(double RPM, double turretPos) {
    TargetRPM = RPM;
    TurretTargetAngle = turretPos;
  }

  @Override
  public void incrementTargetRPM(double increment) {
    TargetRPM = TargetRPM + increment;
  }

  @Override
  public void setTargetRun(double RPM) {
    TargetRPM = RPM;
    shooter.set(
        (pid.calculate(shooter.getVelocity().getValueAsDouble(), TargetRPM)
                + feedforward.calculate(TargetRPM))
            / 6000.0);
  }

  @Override
  public void setSecondFlywheel(double speed) {
    shooter2.set(speed);
  }

  @Override
  public void setTurretSpeed(double speed) {
    turret.set(speed);
  }

  @Override
  public void setTurretAngle(double angle) {
    double output = (turretPID.calculate(getTurretAngle(), angle));
    turret.set(output);
  }
}
