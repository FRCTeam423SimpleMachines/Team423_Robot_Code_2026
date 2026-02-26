package frc.robot.subsystems.intake;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class IntakeIOSim implements IntakeIO {
  private final DCMotorSim m_intakeMotor;

  public IntakeIOSim() {
    m_intakeMotor =
        new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getNeo550(1), 0.004, 10),
            DCMotor.getNeo550(1));
  }

  @Override
  public void setVoltage(double volts) {
    m_intakeMotor.setInputVoltage(volts);
  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {}
}
