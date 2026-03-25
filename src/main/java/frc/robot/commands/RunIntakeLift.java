package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.Intake;

public class RunIntakeLift extends Command {
  private final Intake intake;
  private double speed;

  public RunIntakeLift(Intake intake, double speed) {
    this.intake = intake;
    this.speed = speed;
    addRequirements(intake);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    intake.runIntakeLift(speed);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
