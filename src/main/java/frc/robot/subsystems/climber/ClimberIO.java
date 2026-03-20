package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

public interface ClimberIO {
  @AutoLog
  public static class ClimberIOInputs {
    public double climberPosition = 0.0;
    public String currentCommand = "None";
  }

  public default void updateInputs(ClimberIOInputs inputs) {}

  public default void setClimberPosition(double position) {}
}
