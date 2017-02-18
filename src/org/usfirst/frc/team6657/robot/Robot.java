package org.usfirst.frc.team6657.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Robot extends SampleRobot {
	//RobotDrive myRobot = new RobotDrive(0, 2, 1, 3); // class that handles basic drive
	RobotDrive myRobot = new RobotDrive(0, 1);
	
	Jaguar box = new Jaguar(2);
												// operations
	Joystick js = new Joystick(0); // set to ID 1 in DriverStation
	
	private Gyro gyro;
	static final double Kp = 0.03;
	

	public Robot() {
		myRobot.setExpiration(0.1);
		
		gyro = new AnalogGyro(1);
	}
	
	@Override
	public void robotInit() {
		CameraServer.getInstance().startAutomaticCapture();
		
		//Calibrate the gyro with a few samples.
		gyro.calibrate();
		
		box.setInverted(true);
		
		//myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		//myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		myRobot.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
	}
	
	/*
	 * 0.5: 2.5 ft/s
	 * 0.6: 3.25 ft/s
	 * 0.7: 4 ft/s
	 * 0.8: 4.6 ft/s
	 * 0.9: 5.25 ft/s
	 * 1.0: Pretty much a 90 degree turn
	 */
	
	@Override
    public void autonomous() {
		//Calibrate the gyro
		//gyro.calibrate();
		
		//Many not need to reset at this point since we just calibrated it.
		//gyro.reset();
		
		autoPath(10); // Indicates which path to take
		
    	/*while (isAutonomous() && isEnabled()) {
    		double angle = gyro.getAngle();
    		myRobot.drive(-0.3, -angle*Kp);
    		myRobot.setExpiration(3.0);

    		Timer.delay(0.01);
    	}*/
    	
    	//Free up the gyro once we are done with it.
    	gyro.free();
    }
	
	private void autoPath(int scenario) {
		double acc = 0.0; // NOT a good idea. However, idk how to do it any other way
		
		while(isAutonomous() && isEnabled()) {
			double angle = gyro.getAngle();
		
			switch(scenario) {
			case 0: {
				myRobot.drive(-0.4, 0.0); // Kinda straight, switch to gyro driving later
				break;
			}
			case 1: {
				if(acc <= 2.0) myRobot.drive(-0.4, -0.4);
				else if(acc > 2.0) myRobot.drive(-0.4, 0.0);
				break;
			}
			case 2: {
				if(acc <= 2.0) myRobot.drive(-0.4, 0.0);
				else if(acc > 2.0 && acc <= 10.0) myRobot.drive(-1.0, -1.0);
				else if(acc > 10.0 && acc <= 15.0) myRobot.drive(-0.4, 1.0);
				break;
			}
			case 3: {
				if(acc <= 1.0) myRobot.drive(-0.4, -angle*Kp);
				if(acc > 1.0 && acc <= 2.0) myRobot.drive(-0.4, 0.0);
				else if(acc > 2.0) myRobot.drive(0.0, 0.0);
				break;
			}
			case 4: {
				if(acc <= 1.0) myRobot.drive(-0.5, -angle*Kp);
				else if(acc > 1.0) myRobot.drive(0.0, 0.0);
				break;
			}
			case 5: {
				 //myRobot.drive(-0.6, -angle*Kp);
				 if(acc <= 1.0) myRobot.drive(-0.4, 1/Math.pow(Math.E, (48/28)));
				else if(acc > 1.0) myRobot.drive(0.0, 0.0);
				break;
			}
			case 6: {
				if(acc <= 1.0) myRobot.drive(-0.7, -angle*Kp);
				else if(acc > 1.0) myRobot.drive(0.0, 0.0);
				break;
			}
			case 7: {
				if(acc <= 1.0) myRobot.drive(-0.8, -angle*Kp);
				else if(acc > 1.0) myRobot.drive(0.0, 0.0);
				break;
			}
			case 8: {
				if(acc <= 1.0) myRobot.drive(-0.9, -angle*Kp);
				else if(acc > 1.0) myRobot.drive(0.0, 0.0);
				break;
			}
			case 9: {
				if(acc <= 1.0) myRobot.drive(-1.0, -angle*Kp);
				else if(acc > 1.0) myRobot.drive(0.0, 0.0);
				break;
			}
			case 10: {
				if(acc <= 0.5) myRobot.drive(-0.8, -angle*1.5);
			}
			}
			
			acc += 0.005;
			Timer.delay(0.005);
		}
	}
	
	@Override
	public void disabled() {
		//gyro.free();
	}

	//Controller (joy stick) logic
	@Override
	public void operatorControl() {
		//gyro.reset();
		//gyro.calibrate();
		
		myRobot.setSafetyEnabled(true);
		while (isOperatorControl() && isEnabled()) {
			//gyro.reset();
			System.out.println(gyro.getAngle());
			//gyro.reset();
			
			myRobot.arcadeDrive(js, js.getAxisChannel(AxisType.kY), js, js.getAxisChannel(AxisType.kTwist));
			
			//myRobot.arcadeDrive(js);
			Timer.delay(0.005); // wait for a motor update time
			
			//Spin the robot via button 2
			if(js.getRawButton(2)) {
				//Don't think we need this, might be causing an issue.
				//gyro.reset();
				double initialPos = gyro.getAngle();
				double realPos = initialPos;
				//double rate = gyro.getRate();
				
				System.out.println("Begin: " + initialPos + ", " + realPos);
				
				//double time = 0.0;
				
				while(Math.abs(realPos - initialPos) <= 180) {
					//System.out.println("====================================");
					//System.out.println("While loop start");
					
					//System.out.println("Current angle:" + initialPos + ", " + realPos);
					
					//System.out.println(Math.abs(realPos - initialPos));
					
					myRobot.drive(-0.4, 1.0);
					realPos = gyro.getAngle(); //get the current heading
					
					//System.out.println(rate);
					//System.out.println("New angle: " + initialPos + ", " + realPos);
					
					//System.out.println("While loop end");
					//System.out.println("====================================");
					if(js.getRawButton(7)) {
						break;
					}
					Timer.delay(0.005);
				}
				
				System.out.println("End" + initialPos + ", " + realPos);
				
				//time = 0;
				
			}
			
			//Spin the robot via button 5
			if(js.getRawButton(5)) {
				double testKp = 0.04;
				//Don't think we need this, might be causing an issue.
				gyro.reset();
				double angle = gyro.getAngle(); // get current heading
	            myRobot.drive(-0.4, -angle*testKp); // drive towards heading 0
	            Timer.delay(0.005);
			}
			
			if(js.getRawButton(9)) {
				box.setSpeed(-0.4);
				box.set(-0.4);
			}
			
			if(js.getRawButton(10)) {
				box.setSpeed(0.0);
				box.set(0.0);
			}
			
		}
	}
}
