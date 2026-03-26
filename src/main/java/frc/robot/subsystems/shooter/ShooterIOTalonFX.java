package frc.robot.subsystems.shooter;

import static frc.robot.util.PhoenixUtil.tryUntilOk;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

public class ShooterIOTalonFX implements ShooterIO {
  private final TalonFX m_shooter;
  private final TalonFX m_shooter2;

  private static final Slot0Configs flywheeelGains =
      new Slot0Configs()
          .withKP(ShooterConstants.shooterkP)
          .withKI(ShooterConstants.shooterkI)
          .withKD(ShooterConstants.shooterkD)
          .withKS(ShooterConstants.shooterkS)
          .withKV(ShooterConstants.shooterkV)
          .withKA(ShooterConstants.shooterkA)
          .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);

  final VelocityVoltage m_shooter_request = new VelocityVoltage(0).withSlot(0).withEnableFOC(false);

  private double TargetRPM = 0;

  public ShooterIOTalonFX() {
    m_shooter = new TalonFX(ShooterConstants.shooterCanID);
    m_shooter2 = new TalonFX(ShooterConstants.secondShooterCanID);
    tryUntilOk(5, () -> m_shooter.getConfigurator().apply(flywheeelGains));
    tryUntilOk(
        5,
        () ->
            m_shooter2.setControl(
                new Follower(ShooterConstants.shooterCanID, MotorAlignmentValue.Opposed)));
  }

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    inputs.flywheelRPM = m_shooter.getVelocity().getValueAsDouble();
    inputs.targetRPM = TargetRPM;
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
  public void runAtTarget(double RPM) {
    TargetRPM = RPM;
    m_shooter.setControl(m_shooter_request.withVelocity(RPM / 22.0));
  }

  @Override
  public void setTargetRPM(double RPM) {
    TargetRPM = RPM;
  }

  @Override
  public void incrementTargetRPM(double increment) {
    TargetRPM = TargetRPM + increment;
  }

  @Override
  public void setTargetRun(double RPM) {
    TargetRPM = RPM;
  }
}
