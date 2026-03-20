package frc.robot.subsystems.turret;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.FieldConstants.FieldTarget;
import org.littletonrobotics.junction.Logger;

public class Turret extends SubsystemBase {
  private final TurretIO io;
  private final TurretIOInputsAutoLogged inputs = new TurretIOInputsAutoLogged();

  public Turret(TurretIO io) {
    this.io = io;
  }

  public double getTurretAngle() {
    return io.getTurretAngle();
  }

  public void setTargetTurretAngle(double angle) {
    io.setTargetTurretAngle(angle);
  }

  public void setTurretAngle(double angle) {
    io.setTurretAngle(angle);
  }

  public void setTurretAngles(double angle, double targetAngle) {
    io.setTurretAngle(angle);
    io.setTargetTurretAngle(targetAngle);
  }

  public void setFieldTarget(FieldTarget target) {
    io.setFieldTarget(target);
  }

  public FieldTarget getFieldTarget() {
    return io.getFieldTarget();
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Turret", inputs);
  }
}
