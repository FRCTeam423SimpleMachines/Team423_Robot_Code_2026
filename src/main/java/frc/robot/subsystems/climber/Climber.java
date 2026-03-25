package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Climber extends SubsystemBase {
  private final ClimberIO io;
  private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();

  public Climber(ClimberIO io) {
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
    Logger.processInputs("Climber", inputs);
  }

  public void setClimberPosition(double position) {
    io.runClimber(position);
  }
}
