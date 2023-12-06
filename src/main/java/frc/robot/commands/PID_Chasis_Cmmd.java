// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chasis_Sub;

public class PID_Chasis_Cmmd extends CommandBase {
  private final Chasis_Sub Chasis;
  private  Double Setpoint , Dt, LastDt, I_Zone;
  private double RightErrorP, RightErrorI, RightErrorD, RightLastError, RightSpeed;
  private double kP, kI, kD;
  private double Time, TimeInit;
  public PID_Chasis_Cmmd(Chasis_Sub chasis_Sub,Double setpoint) {
    addRequirements(chasis_Sub);
    this.Chasis=chasis_Sub;
    this.Setpoint=setpoint;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Chasis.resetencoder();
    kP = 0.2;
    kI = 0.03;
    kD = 0.001; // I=0.0625,D=.02
    TimeInit = Timer.getFPGATimestamp();
    LastDt = Timer.getFPGATimestamp();
    I_Zone=Math.abs(Setpoint*0.1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
        Time = Timer.getFPGATimestamp() - TimeInit;
        Dt = Timer.getFPGATimestamp() - LastDt;
        // P
        RightErrorP = Setpoint - Chasis.getLeftEncoder();

        // I
        if (Math.abs(RightErrorP) <= I_Zone) {
          RightErrorI += RightErrorP * Dt;
        } else {
          RightErrorI = 0;
        }
    
        // D
        RightErrorD = (RightErrorP - RightLastError) / Dt;
    
        // Control de velocidad
        RightSpeed = (RightErrorP * kP) + (RightErrorI * kI) + (RightErrorD * kD);
    
        // Set a los motores
        //System.out.println(RightSpeed);
        Chasis.setspeed(RightSpeed, RightSpeed);
    
        // Retroalimentacion de errores y tiempos
        RightLastError = RightErrorP;
        LastDt = Timer.getFPGATimestamp();
    

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //if (Math.abs(Setpoint - Chasis.getLeftEncoder() ) < 5) {
      //return true;
   // } else {
      return false;
    //}
  }
}
