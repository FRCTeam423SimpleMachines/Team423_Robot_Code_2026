package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.Intake;

public class ToggleIntake extends Command {
  private final Intake intake;

  public ToggleIntake(Intake intake) {
    this.intake = intake;
    addRequirements(intake);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    intake.toggleState();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
