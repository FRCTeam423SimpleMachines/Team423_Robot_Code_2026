package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.indexer.Indexer;

public class RunIndexer extends Command {
  private final Indexer indexer;
  private double RPM;

  public RunIndexer(Indexer indexer, double RPM) {
    this.indexer = indexer;
    this.RPM = RPM;
    addRequirements(indexer);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    indexer.runIndexer(RPM);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
