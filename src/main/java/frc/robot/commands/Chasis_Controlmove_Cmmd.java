// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chasis_Sub;
import java.util.function.Supplier;

public class Chasis_Controlmove_Cmmd extends CommandBase {
  private final Chasis_Sub Chasis;
  private final Supplier <Double> RT, LT, Xaxis;
  public Chasis_Controlmove_Cmmd(Chasis_Sub chasis_sub, Supplier<Double> RT,Supplier<Double> LT,Supplier<Double>Xaxis) {
    addRequirements(chasis_sub);
    this.Chasis=chasis_sub;this.LT=LT;this.RT=RT;this.Xaxis=Xaxis;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Chasis.resetencoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double RightSpeed, LeftSpeed, Trigger, Turn;

    //Limpieza de ruido
    Trigger = RT.get() - LT.get(); if(Math.abs(Trigger)<0.15){Trigger = 0;}
    Turn = Xaxis.get(); if(Math.abs(Turn)<0.25){Turn = 0;}

    //Calculo de velocidad
    RightSpeed = (Trigger + Turn);
    LeftSpeed = (Trigger - Turn);

    //Set a los motores
    Chasis.setspeed(RightSpeed, LeftSpeed);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
