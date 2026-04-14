package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.turret.Turret;

public class ZeroTurret extends Command {
  private final Turret turret;

  public ZeroTurret(Turret turret) {
    this.turret = turret;
    addRequirements(turret);
  }

  @Override
  public void initialize() {}

  // Control logic: While button held
  // set shooter state to be: turret(drive.getAngleToHub) and shooterSpeed(drive.getSpeedForHub)
  // REMEMBER TO ADD THE OFFSET FROM TURRET TO ROBOT
  @Override
  public void execute() {
    turret.setTargetTurretAngle(0.0);
    turret.setTurretAngle(0.0);
  }

  @Override
  public boolean isFinished() {
    // this should essentially return opposite of "are there any balls in the hopper"
    // return (shooter.emptyHopper());
    // OR it might be interrupted by crossing the hub line?
    return false;
  }
}
