package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
  @AutoLog
  public static class IntakeIOInputs {
    public String currentCommand = "None";
    public boolean state = false;
    public double intakePosition = 0.0;
  }

  public default void updateInputs(IntakeIOInputs inputs) {}

  public default void setVoltage(double volts) {}

  public default void setSpeed(double speed) {}

  public default void setIntakeAngle(double angle) {}

  public default void toggleState() {}
}
