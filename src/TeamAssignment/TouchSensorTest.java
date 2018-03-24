package TeamAssignment;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;
import rp.systems.RobotProgrammingDemo;
import rp.util.Rate;

public class TouchSensorTest extends RobotProgrammingDemo implements SensorPortListener {

	private boolean pressed;
	private final SensorPort port1;
	private final SensorPort port4;
	
	
	public TouchSensorTest(SensorPort port1, SensorPort port4) {
		this.port1 = port1;
		this.port4 = port4;
		this.pressed = false;
	}
	
	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		if (aNewValue < aOldValue) {
			pressed = true;
		}
	}
	
	@Override
	public void run() {
		this.port1.addSensorPortListener(this);
		this.port4.addSensorPortListener(this);

		DifferentialPilot DP = new DifferentialPilot(5.5f, 17.3f, Motor.A, Motor.B);
		float infinity = Float.POSITIVE_INFINITY;
		Rate rate;
		
		while(m_run) {
			DP.travel(infinity, true);
			rate = new Rate(40);
			if(pressed) {
				rate.sleep();
				DP.stop();
				DP.travel(-10);
				DP.rotate(Math.toDegrees(Math.PI));
				pressed = false;
			}
		}
	}

	public static void main (String[] args) throws InterruptedException {
		Button.waitForAnyPress();
		RobotProgrammingDemo tst = new TouchSensorTest(SensorPort.S1, SensorPort.S4);
		tst.run();
	}
}