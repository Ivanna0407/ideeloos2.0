// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.NavX.AHRS;
public class Chasis_Sub extends SubsystemBase {
  //Constructores de motores 
  private final CANSparkMax MotorRight_S= new CANSparkMax(4, MotorType.kBrushless);
  private final CANSparkMax MotorRight_M= new CANSparkMax(2, MotorType.kBrushless);
  private final CANSparkMax MotorLeft_S= new CANSparkMax(5, MotorType.kBrushless);
  private final CANSparkMax MotorLeft_M= new CANSparkMax(3, MotorType.kBrushless);
  //Encoders
  private final RelativeEncoder Encoder_R=MotorRight_S.getEncoder();
  private final RelativeEncoder Encoder_L=MotorLeft_M.getEncoder();
  //Giroscopio y accelerometro
  AHRS ahrs = new AHRS(SPI.Port.kMXP, (byte)66);

  public Chasis_Sub() {
    //Configuraciónes de fábrica
    MotorLeft_M.restoreFactoryDefaults();
    MotorLeft_S.restoreFactoryDefaults();
    MotorRight_M.restoreFactoryDefaults();
    MotorRight_S.restoreFactoryDefaults();
    //Maestros y esclavos
    MotorLeft_S.follow(MotorLeft_M);
    MotorRight_S.follow(MotorRight_M);
    //Modo IDLE
    MotorLeft_M.setIdleMode(IdleMode.kCoast);
    MotorLeft_S.setIdleMode(IdleMode.kCoast);
    MotorRight_M.setIdleMode(IdleMode.kCoast);
    MotorRight_S.setIdleMode(IdleMode.kCoast);
    //Invertir
    MotorLeft_M.setInverted(true);
    MotorLeft_S.setInverted(true);
    //Set en 0
    MotorLeft_M.set(0);
    MotorLeft_S.set(0);
    MotorRight_M.set(0);
    MotorRight_S.set(0);
    //Encoders en 0
    Encoder_L.setPosition(0);
    Encoder_R.setPosition(0);
    //Factor de conversión en encoders
    Encoder_L.setPositionConversionFactor(6*Math.PI*(1/7.14));//1/9.5238 nueva transmisión
    Encoder_R.setPositionConversionFactor(6*Math.PI*(1/7.14));
    Encoder_R.getInverted();
  }

  @Override
  public void periodic() {
    //Valores reportados en SmartDashboard
    SmartDashboard.putNumber("Rightspeed", MotorRight_M.get());
    SmartDashboard.putNumber("Leftspeed", MotorLeft_M.get());
    SmartDashboard.putNumber("Rightencoder", Encoder_R.getPosition());
    SmartDashboard.putNumber("Leftencoder", Encoder_L.getPosition());
    SmartDashboard.putNumber("AccelerometroX", ahrs.getWorldLinearAccelX());
    SmartDashboard.putNumber("AccelerometroY", ahrs.getWorldLinearAccelY());
    SmartDashboard.putNumber("AccelerometroZ", ahrs.getWorldLinearAccelZ());
    SmartDashboard.putNumber("Magnitud", ahrs.getAccelFullScaleRangeG());
    SmartDashboard.putNumber("DisplacementX", ahrs.getDisplacementX());
    SmartDashboard.putNumber("DisplacementY", ahrs.getDisplacementY());
    SmartDashboard.putNumber("DisplacementZ", ahrs.getDisplacementZ());
    SmartDashboard.putNumber("VelocityX", ahrs.getVelocityX());
    SmartDashboard.putNumber("VelocityY", ahrs.getVelocityY());
    SmartDashboard.putNumber("VelocityZ", ahrs.getVelocityZ());
  }
  //Funciones para usar en el comando
  public void resetencoder(){
    Encoder_L.setPosition(0);
    Encoder_R.setPosition(0);
  }
  public double getLeftEncoder(){
    return Encoder_L.getPosition();
  }
  public double getRightEncoder(){
    return Encoder_R.getPosition();
  }
  public void setspeed(double rightSpeed,double leftSpeed){
    if (Math.abs(leftSpeed) >= 0.8) {
      leftSpeed = (leftSpeed / Math.abs(leftSpeed)) * 0.8;
    }
    if (Math.abs(rightSpeed) >= 0.8) {
      rightSpeed = (rightSpeed / Math.abs(rightSpeed)) * 0.8;
    }
    MotorLeft_M.set(leftSpeed*.5);
    MotorRight_M.set(rightSpeed*.5);
  }
  public float getAccelerometer_X(){
   return ahrs.getWorldLinearAccelX();
  }
  public float getAccelerometer_Y(){
    return ahrs.getWorldLinearAccelY();
  }
  public float getAccelerometer_Z(){
    return ahrs.getWorldLinearAccelZ();
  }
  public float getAccelerometerVector(){
    return ahrs.getAccelFullScaleRangeG();
  }
}
