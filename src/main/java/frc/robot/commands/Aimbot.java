package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.FieldConstants;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.turret.Turret;
import frc.robot.util.ShooterUtil;
import java.util.Optional;

public class Aimbot extends Command {
  private final Drive drive;
  private final Turret turret;
  private Pose2d targetPose;
  private double pattern;
  private FieldConstants.FieldTarget target;

  public Aimbot(Drive drive, Turret turret, FieldConstants.FieldTarget target) {
    this.turret = turret;
    this.drive = drive;
    this.target = target;
    addRequirements(turret);
  }

  @Override
  public void initialize() {
    Optional<Alliance> ally = DriverStation.getAlliance();
    Alliance alliance;
    if (ally.isPresent()) {
      alliance = ally.get();
      this.targetPose = FieldConstants.getTargetPose(target, alliance);
      this.pattern = FieldConstants.getTargetLED(target, alliance);
      turret.setFieldTarget(target);
      turret.setAlliance(alliance);
    }
  }

  // Control logic: While button held
  // set shooter state to be: turret(drive.getAngleToHub) and shooterSpeed(drive.getSpeedForHub)
  // REMEMBER TO ADD THE OFFSET FROM TURRET TO ROBOT
  @Override
  public void execute() {
    // turretAngle = shooter.getTurretAngle();
    // shooter.setTargetState(ShooterUtil.calculateRPMForDistance(drive, targetPose, targetHeight));

    double targetAngle = ShooterUtil.getRobotAngleToPose(drive, targetPose);
    turret.setTargetTurretAngle(targetAngle);
    turret.setTurretAngle(targetAngle);
    turret.setLights(pattern);
  }

  @Override
  public boolean isFinished() {
    // this should essentially return opposite of "are there any balls in the hopper"
    // return (shooter.emptyHopper());
    // OR it might be interrupted by crossing the hub line?
    return false;
  }
}
