package frc.robot.subsystems.climber;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class ClimberIOSim implements ClimberIO {
  private final DCMotorSim m_climber;

  public ClimberIOSim() {
    m_climber =
        new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getNEO(1), 0.004, 10), DCMotor.getNEO(1));
  }

  @Override
  public void setClimberPosition(double volts) {
    m_climber.setInputVoltage(volts);
  }

  @Override
  public void updateInputs(ClimberIOInputs inputs) {}
}
