package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {

  private final IntakeIO io;
  private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

  public Intake(IntakeIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    if (getCurrentCommand() != null) {
      inputs.currentCommand = getCurrentCommand().getName();
    } else {
      inputs.currentCommand = "None";
    }
    Logger.processInputs("Intake", inputs);
  }

  public void setSpeed(double speed) {
    io.setSpeed(speed);
  }

  public double getSpeed() {
    return io.getSpeed();
  }

  public void runIntake(double speed) {
    io.setSpeed(speed);
  }

  public void toggleState() {
    io.toggleState();
  }

  public Command setIntakePosition(double position) {
    return run(() -> io.setIntakeAngle(position));
  }

  public void runIntakeLift(double angle) {
    io.setIntakeAngle(angle);
  }
}
