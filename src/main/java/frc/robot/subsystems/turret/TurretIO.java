package frc.robot.subsystems.turret;

import frc.robot.FieldConstants.FieldTarget;
import org.littletonrobotics.junction.AutoLog;

public interface TurretIO {

  @AutoLog
  public class TurretIOInputs {
    public double turretTargetAngle = 0;
    public double turretAngle = 0;
    public boolean magnet1 = false;
  }

  public default void updateInputs(TurretIOInputs inputs) {}

  public default double getTurretAngle() {
    return 0.0;
  }

  public default void setTurretAngle(double angle) {}

  public default void setTurretSpeed(double speed) {}

  public default void setTargetTurretAngle(double angle) {}

  public default void setTurretAngles(double angle){}

  public default void setFieldTarget(FieldTarget target) {}

  public default FieldTarget getFieldTarget() {
    return FieldTarget.HUB;
  }

  public default void zeroTurret() {}
}
