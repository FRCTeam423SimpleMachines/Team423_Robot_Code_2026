package frc.robot.subsystems.turret;

import static frc.robot.util.PhoenixUtil.tryUntilOk;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;

public class TurretIOReal implements TurretIO {

  private final TalonFX m_turret;

  private final DigitalInput homeFlag;

  private static final Slot0Configs turretGains =
      new Slot0Configs()
          .withKP(TurretConstants.turretkP)
          .withKI(TurretConstants.turretkI)
          .withKD(TurretConstants.turretkD);

  final PositionVoltage m_turret_request = new PositionVoltage(0).withSlot(0);

  public double TurretTargetAngle = 0;

  public TurretIOReal() {
    m_turret = new TalonFX(TurretConstants.turretCanID);
    tryUntilOk(5, () -> m_turret.getConfigurator().apply(turretGains));
    homeFlag = new DigitalInput(0);
  }

  @Override
  public void updateInputs(TurretIOInputs inputs) {
    inputs.turretAngle = getTurretAngle();
    inputs.turretTargetAngle = TurretTargetAngle;
    inputs.magnet1 = homeFlag.get();
  }

  @Override
  public double getTurretAngle() {
    return m_turret.getPosition().getValueAsDouble() / TurretConstants.turretRotationsPerDegree;
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
  public void setTurretSpeed(double speed) {
    m_turret.set(speed);
  }

  @Override
  public void setTurretAngle(double angle) {
    if (Math.abs(angle - getTurretAngle()) < 2.5) {
      m_turret.set(0);
    } else {
      m_turret.setControl(
          m_turret_request.withPosition(angle * TurretConstants.turretRotationsPerDegree));
    }
  }

  @Override
  public void setTargetTurretAngle(double angle) {
    TurretTargetAngle = angle;
  }

  @Override
  public void setTurretAngles(double angle){
    setTargetTurretAngle(angle);
    setTurretAngle(angle);
  }
}
