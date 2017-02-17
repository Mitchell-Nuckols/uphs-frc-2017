package org.usfirst.frc.team6657.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Robot extends SampleRobot {
	RobotDrive myRobot = new RobotDrive(0, 2, 1, 3); // class that handles basic drive
												// operations
	Joystick js = new Joystick(0); // set to ID 1 in DriverStation
	
	Compressor c = new Compressor(0);
	DoubleSolenoid ds = new DoubleSolenoid(0, 1);
	
	private Gyro gyro;
	static final double Kp = 0.03;
	

	public Robot() {
		myRobot.setExpiration(0.1);
		
		gyro = new AnalogGyro(1);
	}
	
	DoubleSolenoid.Value getOpposite(DoubleSolenoid.Value v) {
		if(v == DoubleSolenoid.Value.kForward) {
			return DoubleSolenoid.Value.kReverse;
		}else if(v == DoubleSolenoid.Value.kReverse) {
			return DoubleSolenoid.Value.kForward;
		}
		
		return DoubleSolenoid.Value.kOff;
	}
	
	@Override
	public void robotInit() {
		CameraServer.getInstance().startAutomaticCapture();
		
		c.setClosedLoopControl(true);
		
		//Calibrate the gyro with a few samples.
		gyro.calibrate();
		
		myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		myRobot.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
	}
	
	
	@Override
    public void autonomous() {
		//Calibrate the gyro
		gyro.calibrate();
		
		//Many not need to reset at this point since we just calibrated it.
		//gyro.reset();
		
    	while (isAutonomous() && isEnabled()) {
    		double angle = gyro.getAngle();
    		myRobot.drive(-0.3, -angle*Kp);
    		myRobot.setExpiration(3.0);

    		Timer.delay(0.01);
    	}
    	
    	//Free up the gyro once we are done with it.
    	gyro.free();
    }
	
	@Override
	public void disabled() {
		ds.set(DoubleSolenoid.Value.kOff);
	}

	//Controller (joy stick) logic
	@Override
	public void operatorControl() {
		gyro.reset();
		gyro.calibrate();
		
		myRobot.setSafetyEnabled(true);
		while (isOperatorControl() && isEnabled()) {
			gyro.reset();
			System.out.println(gyro.getAngle());
			//gyro.reset();
			
			myRobot.arcadeDrive(js, js.getAxisChannel(AxisType.kY), js, js.getAxisChannel(AxisType.kTwist));
			
			//myRobot.arcadeDrive(js);
			Timer.delay(0.005); // wait for a motor update time
			
			if(js.getRawButton(3)) {
				ds.set(DoubleSolenoid.Value.kForward);
			}
			
			if(js.getRawButton(4)) {
				ds.set(DoubleSolenoid.Value.kReverse);
			}
			
			//Spin the robot via button 2
			if(js.getRawButton(2)) {
				//Don't think we need this, might be causing an issue.
				gyro.reset();
				double initialPos = gyro.getAngle();
				double realPos = initialPos;
				//double rate = gyro.getRate();
				
				System.out.println(initialPos + ", " + realPos);
				
				double time = 0.0;
				
				while(Math.abs(realPos - initialPos) <= 180) {
					System.out.println("=====================================================================================");
					System.out.println("While loop start");
					System.out.println("=====================================================================================");
					System.out.println("Current angle:" + initialPos + ", " + realPos);
					
					System.out.println(Math.abs(realPos - initialPos));
					
					myRobot.drive(-0., 1.0);
					realPos = gyro.getAngle(); //get the current heading
					
					//System.out.println(rate);
					System.out.println("New angle: " + initialPos + ", " + realPos);
					
					System.out.println("=====================================================================================");
					System.out.println("While loop end");
					System.out.println("=====================================================================================");
					
					Timer.delay(0.005);
				}
				
				time = 0;
				
			}
			
			//Spin the robot via button 5
			if(js.getRawButton(5)) {
				double testKp = 0.04;
				//Don't think we need this, might be causing an issue.
				gyro.reset();
				double angle = gyro.getAngle(); // get current heading
	            myRobot.drive(-1.0, -angle*testKp); // drive towards heading 0
	            Timer.delay(0.004);
			}
			/*if(js.getRawButton(5)) {
				c.start();
			}
			
			if(js.getRawButton(6)) {
				c.stop();
			}*/
			myRobot.drive(0.0, 0.0);
		}
	}
}
