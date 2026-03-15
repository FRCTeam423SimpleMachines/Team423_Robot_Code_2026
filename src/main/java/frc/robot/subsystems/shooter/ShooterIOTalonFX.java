package frc.robot.subsystems.shooter;

import static frc.robot.util.PhoenixUtil.tryUntilOk;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
import edu.wpi.first.wpilibj.DigitalInput;

public class ShooterIOTalonFX implements ShooterIO {
  private final TalonFX m_shooter;
  private final TalonFX m_shooter2;
  private final TalonFX m_turret;

  private final DigitalInput homeFlag;

  private static final Slot0Configs turretGains =
      new Slot0Configs()
          .withKP(ShooterConstants.turretkP)
          .withKI(ShooterConstants.turretkI)
          .withKD(ShooterConstants.turretkD);

  private static final Slot0Configs flywheeelGains =
      new Slot0Configs()
          .withKP(ShooterConstants.shooterkP)
          .withKI(ShooterConstants.shooterkI)
          .withKD(ShooterConstants.shooterkD)
          .withKS(ShooterConstants.shooterkS)
          .withKV(ShooterConstants.shooterkV)
          .withKA(ShooterConstants.shooterkA)
          .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);

  final PositionVoltage m_turret_request = new PositionVoltage(0).withSlot(0);

  final VelocityVoltage m_shooter_request = new VelocityVoltage(0).withSlot(0);

  private double TargetRPM = 0;
  public double TurretTargetAngle = 0;

  public ShooterIOTalonFX() {
    m_shooter = new TalonFX(ShooterConstants.shooterCanID);
    m_shooter2 = new TalonFX(ShooterConstants.secondShooterCanID);
    m_turret = new TalonFX(ShooterConstants.turretCanID);
    tryUntilOk(5, () -> m_shooter.getConfigurator().apply(flywheeelGains));
    tryUntilOk(
        5,
        () ->
            m_shooter2.setControl(
                new Follower(ShooterConstants.shooterCanID, MotorAlignmentValue.Opposed)));
    tryUntilOk(5, () -> m_turret.getConfigurator().apply(turretGains));
    homeFlag = new DigitalInput(0);
  }

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    inputs.flywheelRPM = m_shooter.getVelocity().getValueAsDouble();
    inputs.turretAngle = getTurretAngle();
    inputs.targetRPM = TargetRPM;
    inputs.turretTargetAngle = TurretTargetAngle;
    inputs.magnet1 = homeFlag.get();
  }

  @Override
  public double getTurretAngle() {
    return m_turret.getPosition().getValueAsDouble() / ShooterConstants.turretRotationsPerDegree;
  }

  @Override
  public void runShooter(double voltage) {
    m_shooter.setVoltage(voltage);
  }

  @Override
  public void runAtSpeed(double speed) {
    m_shooter.set(speed);
  }

  @Override
  public void zeroTurret() {
    m_turret.set(0.5);
    if (homeFlag.get()) {
      m_turret.set(0);
      m_turret.setPosition(0);
    }
  }

  @Override
  public void runAtTarget() {
    m_shooter.setControl(m_shooter_request.withVelocity(TargetRPM));
  }

  @Override
  public void runAtTarget(double RPM) {
    TargetRPM = RPM;
    m_shooter.setControl(m_shooter_request.withVelocity(RPM));
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
  }

  @Override
  public void setTurretSpeed(double speed) {
    m_turret.set(speed);
  }

  @Override
  public void setTurretAngle(double angle) {
    m_turret.setControl(
        m_turret_request.withPosition(angle * ShooterConstants.turretRotationsPerDegree));
  }
}
