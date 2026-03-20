package frc.robot.subsystems.indexer;

import static frc.robot.util.PhoenixUtil.tryUntilOk;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

public class IndexerIOKraken implements IndexerIO {
  private final TalonFX m_indexer;

  private static final Slot0Configs indexerGains =
      new Slot0Configs()
          .withKP(IndexerConstants.IndexerkP)
          .withKI(IndexerConstants.IndexerkI)
          .withKD(IndexerConstants.IndexerkD);

  final VelocityVoltage m_indexer_request = new VelocityVoltage(0).withSlot(0);

  public IndexerIOKraken() {
    m_indexer = new TalonFX(IndexerConstants.kIndexerCanID);
    tryUntilOk(5, () -> m_indexer.getConfigurator().apply(indexerGains));
  }

  @Override
  public void updateInputs(IndexerIOInputs inputs) {
    inputs.indexerVelocity = m_indexer.getVelocity().getValueAsDouble();
  }

  public void runClimber(double speed) {
    m_indexer.setControl(m_indexer_request.withVelocity(speed));
  }
}
