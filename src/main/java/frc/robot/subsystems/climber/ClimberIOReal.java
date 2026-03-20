package frc.robot.subsystems.climber;

import static frc.robot.util.PhoenixUtil.tryUntilOk;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

public class ClimberIOReal implements ClimberIO {
  private final TalonFX m_climber;

  private static final Slot0Configs climberGains =
      new Slot0Configs()
          .withKP(ClimberConstants.ClimberkP)
          .withKI(ClimberConstants.ClimberkI)
          .withKD(ClimberConstants.ClimberkD);

  final PositionVoltage m_climber_request = new PositionVoltage(0).withSlot(0);

  public ClimberIOReal() {
    m_climber = new TalonFX(ClimberConstants.ClimberCanID);
    tryUntilOk(5, () -> m_climber.getConfigurator().apply(climberGains));
  }

  @Override
  public void updateInputs(ClimberIOInputs inputs) {
    inputs.climberPosition =
        m_climber.getPosition().getValueAsDouble() / ClimberConstants.climberRotationsPerExtension;
  }

  public void runClimber(double position) {
    m_climber.setControl(
        m_climber_request.withPosition(position * ClimberConstants.climberRotationsPerExtension));
  }
}
