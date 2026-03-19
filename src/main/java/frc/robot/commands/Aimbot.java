package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.FieldConstants;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.util.ShooterUtil;
import java.util.Optional;

public class Aimbot extends Command {
  private final Drive drive;
  private final Shooter shooter;
  private Pose2d targetPose;

  public Aimbot(Drive drive, Shooter shooter, FieldConstants.FieldTarget target) {
    this.shooter = shooter;
    this.drive = drive;
    Optional<Alliance> ally = DriverStation.getAlliance();
    this.targetPose = FieldConstants.getTargetPose(target, ally.orElse(DriverStation.Alliance.Red));
    addRequirements(shooter);
  }

  @Override
  public void initialize() {
    // no clue what I want here honestly, you want to command to continuously update so getting
    // angle and stuff here is a bad idea
  }

  // Control logic: While button held
  // set shooter state to be: turret(drive.getAngleToHub) and shooterSpeed(drive.getSpeedForHub)
  // REMEMBER TO ADD THE OFFSET FROM TURRET TO ROBOT
  @Override
  public void execute() {
    // turretAngle = shooter.getTurretAngle();
    // shooter.setTargetState(
    // ShooterUtil.calculateRPMForDistance(),
    // ShooterUtil.getRobotAngleToPose(targetPose) + turretAngle);
    double targetAngle = ShooterUtil.getRobotAngleToPose(drive, targetPose);
    shooter.setTargetTurretAngle(targetAngle);
    shooter.setTurretAngle(targetAngle);
  }

  @Override
  public boolean isFinished() {
    // this should essentially return opposite of "are there any balls in the hopper"
    // return (shooter.emptyHopper());
    // OR it might be interrupted by crossing the hub line?
    return false;
  }
}
