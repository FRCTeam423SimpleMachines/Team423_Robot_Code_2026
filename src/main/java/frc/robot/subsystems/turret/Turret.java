package frc.robot.subsystems.turret;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
}
