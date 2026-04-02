package frc.robot.subsystems.indexer;

import org.littletonrobotics.junction.AutoLog;

public interface IndexerIO {
  @AutoLog
  public static class IndexerIOInputs {
    public String currentCommand = "None";
    public double indexerVelocity = 0.0;
    public double hopperVelocity = 0.0;
  }

  public default void updateInputs(IndexerIOInputs inputs) {}

  public default void runIndexer(double velocity) {}
}
