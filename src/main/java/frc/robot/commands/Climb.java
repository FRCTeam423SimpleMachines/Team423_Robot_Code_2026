package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.climber.Climber;

public class Climb extends Command {
  private final Climber climber;
  private double position;

  public Climb(Climber climber, double position) {
    this.climber = climber;
    this.position = position;
    addRequirements(climber);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    climber.setClimberPosition(position);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
