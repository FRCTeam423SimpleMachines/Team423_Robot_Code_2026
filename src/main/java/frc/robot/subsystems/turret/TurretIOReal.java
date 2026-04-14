package frc.robot.subsystems.turret;

import static frc.robot.util.PhoenixUtil.tryUntilOk;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.CoastOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class TurretIOReal implements TurretIO {

  private final TalonFX m_turret;
  private final Spark m_blinkin;

  private final DigitalInput homeFlag;

  private DriverStation.Alliance alliance;

  private static final Slot0Configs turretGains =
      new Slot0Configs()
          .withKP(TurretConstants.turretkP)
          .withKI(TurretConstants.turretkI)
          .withKD(TurretConstants.turretkD);

  final PositionVoltage m_turret_request = new PositionVoltage(0).withSlot(0);

  final CoastOut m_turret_coastout = new CoastOut();

  public double TurretTargetAngle = 0;

  public TurretIOReal() {
    m_blinkin = new Spark(0);
    m_turret = new TalonFX(TurretConstants.turretCanID);
    tryUntilOk(5, () -> m_turret.getConfigurator().apply(turretGains));
    homeFlag = new DigitalInput(0);
  }

  @Override
  public void updateInputs(TurretIOInputs inputs) {
    inputs.turretAngle = getTurretAngle();
    inputs.turretTargetAngle = TurretTargetAngle;
    inputs.magnet1 = homeFlag.get();
    inputs.alliance = alliance;
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
      m_turret.setControl(m_turret_coastout);
    } else {
      m_turret.setControl(
          m_turret_request.withPosition(angle * TurretConstants.turretRotationsPerDegree));
    }
  }

  @Override
  public void setLights(double pattern) {
    m_blinkin.set(pattern);
  }

  @Override
  public void setTargetTurretAngle(double angle) {
    TurretTargetAngle = angle;
  }

  @Override
  public void setTurretAngles(double angle) {
    setTargetTurretAngle(angle);
    setTurretAngle(angle);
  }

  @Override
  public void setAlliance(DriverStation.Alliance alliance) {
    this.alliance = alliance;
  }
}
