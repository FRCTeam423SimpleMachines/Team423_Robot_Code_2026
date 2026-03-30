package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.Intake;

public class RunIntakeLift extends Command {
  private final Intake intake;
  private double angle;

  public RunIntakeLift(Intake intake, double angle) {
    this.intake = intake;
    this.angle = angle;
    addRequirements(intake);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    intake.runIntakeLift(angle);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
