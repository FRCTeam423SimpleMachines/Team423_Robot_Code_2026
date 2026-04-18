package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.Intake;

public class RunIntakePID extends Command {
  private final Intake intake;
  private double speed;
  private final PIDController intakeController = new PIDController(0.1, 20.0, 0);

  public RunIntakePID(Intake intake, double speed) {
    this.intake = intake;
    this.speed = speed;
    addRequirements(intake);
  }

  @Override
  public void initialize() {
    intakeController.reset();
  }

  @Override
  public void execute() {
    double calc = intakeController.calculate(intake.getSpeed(), speed);
    intake.setSpeed(calc / (6000.0 / 12.0));
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
