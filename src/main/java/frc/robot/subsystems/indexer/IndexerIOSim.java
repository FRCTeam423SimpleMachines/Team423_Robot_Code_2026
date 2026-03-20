package frc.robot.subsystems.indexer;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class IndexerIOSim implements IndexerIO {
  private final DCMotorSim m_indexerMotor;

  public IndexerIOSim() {
    m_indexerMotor =
        new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getNeo550(1), 0.004, 10),
            DCMotor.getNeo550(1));
  }

  @Override
  public void runIndexer(double speed) {
    m_indexerMotor.setInputVoltage(speed);
  }

  @Override
  public void updateInputs(IndexerIOInputs inputs) {}
}
