package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.FieldConstants;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.turret.Turret;
import frc.robot.util.ShooterUtil;
import java.util.Optional;

public class Shoot extends Command {
  private final Drive drive;
  private final Shooter shooter;
  private Pose2d targetPose;
  private double targetHeight;

  public Shoot(Drive drive, Shooter shooter, Turret turret) {
    Optional<Alliance> ally = DriverStation.getAlliance();
    this.targetPose =
        FieldConstants.getTargetPose(
            turret.getFieldTarget(), ally.orElse(DriverStation.Alliance.Red));
    this.targetHeight = FieldConstants.getTargetHeight(turret.getFieldTarget());
    this.drive = drive;
    this.shooter = shooter;
    addRequirements(shooter);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    double RPM = ShooterUtil.calculateRPSForDistance(drive, targetPose, targetHeight);
    shooter.runAtTarget(RPM);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
