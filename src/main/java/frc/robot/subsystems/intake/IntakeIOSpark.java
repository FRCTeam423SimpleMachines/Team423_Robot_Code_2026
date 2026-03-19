package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

public class IntakeIOSpark implements IntakeIO {
  private final SparkMax m_intakeMotor2;
  private final SparkFlex m_intakeMotor;

  public IntakeIOSpark() {
    m_intakeMotor = new SparkFlex(IntakeConstants.kIntakeCANID, SparkMax.MotorType.kBrushless);
    m_intakeMotor2 = new SparkMax(IntakeConstants.kIntake2CANID, SparkMax.MotorType.kBrushless);
    SparkMaxConfig config = new SparkMaxConfig();
    config.smartCurrentLimit(20);
    m_intakeMotor2.configure(
        config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void setSpeed(double speed) {
    m_intakeMotor.set(speed);
  }

  @Override
  public void setIntakeAngle(double angle) {
    m_intakeMotor2.set(angle);
  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {}
}
