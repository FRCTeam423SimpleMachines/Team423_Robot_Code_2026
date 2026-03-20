package frc.robot.subsystems.turret;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class TurretIOSim implements TurretIO {
  private final DCMotorSim m_turret;

  public TurretIOSim() {
    m_turret =
        new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getNeo550(1), 0.004, 10),
            DCMotor.getNeo550(1));
  }

  @Override
  public void setTurretAngle(double volts) {
    m_turret.setInputVoltage(volts);
  }

  @Override
  public void updateInputs(TurretIOInputs inputs) {}
}
