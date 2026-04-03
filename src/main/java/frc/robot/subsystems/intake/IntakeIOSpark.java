package frc.robot.subsystems.intake;

import static frc.robot.util.PhoenixUtil.tryUntilOk;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

public class IntakeIOSpark implements IntakeIO {
  private final TalonFX m_intakeMotor2;
  private final SparkFlex m_intakeMotor;
  private boolean state;

  private static final Slot0Configs intakeGains =
      new Slot0Configs()
          .withKP(IntakeConstants.IntakeKP)
          .withKI(IntakeConstants.IntakeKI)
          .withKD(IntakeConstants.IntakeKD);

  final PositionVoltage m_intake_request = new PositionVoltage(0).withSlot(0);

  public IntakeIOSpark() {
    m_intakeMotor = new SparkFlex(IntakeConstants.kIntakeCANID, SparkMax.MotorType.kBrushless);
    m_intakeMotor2 = new TalonFX(IntakeConstants.kIntake2CANID);
    SparkMaxConfig config = new SparkMaxConfig();
    config.smartCurrentLimit(20);
    tryUntilOk(5, () -> m_intakeMotor2.getConfigurator().apply(intakeGains));
  }

  @Override
  public void setSpeed(double speed) {
    //if (state) {
      m_intakeMotor.set(speed);
    //}
  }

  @Override
  public void setIntakeAngle(double angle) {
    m_intakeMotor2.setControl(m_intake_request.withPosition(angle));
  }

  @Override
  public void toggleState() {
    state = !state;
  }

  @Override
  public void runIntakeLift(double speed) {
    m_intakeMotor2.set(speed);
  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    inputs.state = state;
    inputs.intakePosition = m_intakeMotor2.getPosition().getValueAsDouble();
  }
}
