package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.indexer.Indexer;
import frc.robot.subsystems.turret.Turret;

public class RunIndexer extends Command {
  private final Indexer indexer;
  private double RPM;
  private Turret turret;

  public RunIndexer(Indexer indexer, double RPM, Turret turret) {
    this.indexer = indexer;
    this.RPM = RPM;
    this.turret = turret;
    addRequirements(indexer);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    if (turret.lockedOn()) {
      indexer.runIndexer(RPM);
    } else {
      indexer.runIndexer(0);
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
