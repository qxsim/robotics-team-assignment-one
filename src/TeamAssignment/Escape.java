package TeamAssignment;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import rp.systems.RobotProgrammingDemo;
import rp.util.Rate;

public class Escape extends RobotProgrammingDemo implements SensorPortListener {

	private int turnCount;
	private boolean pressed;
	private final SensorPort touch1;
	private final SensorPort touch4;
	private final SensorPort ulsound2;
	private static UltrasonicSensor ulsensor;
	
	public Escape(SensorPort touch1, SensorPort touch4, SensorPort ulsound2) {
		this.touch1 = touch1;
		this.touch4 = touch4;
		this.ulsound2 = ulsound2;
		this.pressed = false;
		turnCount = 0;
	}
	
	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		if (aNewValue < aOldValue) {
			pressed = true;
		}
	}
	
	@Override
	public void run() {
		this.touch1.addSensorPortListener(this);
		this.touch4.addSensorPortListener(this);
		this.ulsound2.addSensorPortListener(this);

		DifferentialPilot DP = new DifferentialPilot(5.5f, 17.3f, Motor.A, Motor.B);
		float infinity = Float.POSITIVE_INFINITY;
		Rate rate;
		
		while(m_run) {
			DP.travel(infinity, true);
			rate = new Rate(40);
			
			if(ulsensor.getDistance() < 17) {
				rate.sleep();
				DP.stop();
				DP.travel(-4);
				turnCount++;
					if (turnCount%2==0 && turnCount!=0) {
						DP.rotate(Math.toDegrees(Math.PI));
					}
					else {
						DP.rotate(Math.toDegrees(Math.PI/2));
					}
			}

			else if (pressed) {
				rate.sleep();
				DP.stop();
				DP.travel(-10);
				turnCount++;
					if (turnCount%2==0 && turnCount!=0) {
						DP.rotate(Math.toDegrees(Math.PI));
					}
					else {
						DP.rotate(Math.toDegrees(Math.PI/2));
					}
				pressed = false;
			}
		}
	}

	public static void main (String[] args) throws InterruptedException {
		Button.waitForAnyPress();
		RobotProgrammingDemo escape = new Escape(SensorPort.S1, SensorPort.S4, SensorPort.S2);
		ulsensor = new UltrasonicSensor(SensorPort.S2);
		escape.run();
	}
}