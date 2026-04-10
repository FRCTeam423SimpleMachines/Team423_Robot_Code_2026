package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.Shooter;

public class SimpleShoot extends Command {
  private final Shooter shooter;
  private final double targetRPM;

  public SimpleShoot(Shooter shooter, double targetRPM) {
    this.shooter = shooter;
    this.targetRPM = targetRPM;
    addRequirements(shooter);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    shooter.runAtTarget(targetRPM);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
