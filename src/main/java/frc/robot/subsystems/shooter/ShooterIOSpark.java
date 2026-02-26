package frc.robot.subsystems.shooter;

import static frc.robot.util.SparkUtil.ifOk;

import org.photonvision.PhotonUtils;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DigitalInput;

public class ShooterIOSpark implements ShooterIO {
  private final SparkFlex shooter =
      new SparkFlex(ShooterConstants.shooterCanID, MotorType.kBrushless);
  private final SparkFlex shooter2 =
      new SparkFlex(ShooterConstants.secondShooterCanID, MotorType.kBrushless);
  private final SparkMax turret =
      new SparkMax(
          ShooterConstants.turretCanID, com.revrobotics.spark.SparkLowLevel.MotorType.kBrushless);

  private final RelativeEncoder turretEncoder = turret.getEncoder();
  private final RelativeEncoder shooterEncoder = shooter.getEncoder();

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

  public ShooterIOSpark() {}

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    ifOk(shooter, shooterEncoder::getVelocity, (value) -> inputs.flywheelRPM = value);
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
    return turretEncoder.getPosition() / 42.0;
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
    if(magnometer1.get()) {
      turret.set(0);
      turretEncoder.setPosition(0);
    }
  }

  @Override
  public void runAtTarget() {
    double output =
        ((pid.calculate(shooterEncoder.getVelocity(), TargetRPM) + feedforward.calculate(TargetRPM))
            / 6000.0);
    shooter.set(output);
    shooter2.set(-output);
    double turretOutput = (turretPID.calculate(getTurretAngle(), TurretTargetAngle));
    turret.set(turretOutput);
  }

  @Override
  public void runAtTarget(double RPM) {
    shooter.set(
        (pid.calculate(shooterEncoder.getVelocity(), RPM) + feedforward.calculate(RPM)) / 6000.0);
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
        (pid.calculate(shooterEncoder.getVelocity(), TargetRPM) + feedforward.calculate(TargetRPM))
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

  //TODO: Use actual robot pose and hub pose, also ensure that the pose2d is the robot pose with the rotation of the turret
  
}
