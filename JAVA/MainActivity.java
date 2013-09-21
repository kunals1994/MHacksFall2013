package com.example.mhacks;

import java.io.*;
import java.util.concurrent.*;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorListener {

	SensorManager sm = null;

	TextView xacc= null;
	TextView yacc = null;
	TextView zacc = null;
	TextView xorient = null;
	TextView yorient = null;
	TextView zorient = null;
	TextView text = null;

	/* VARIABLES THAT WE ACTUALLY NEEED!!!!!!!*/
	int reps = 0;
	long initialConcentricTempoTime = 0;
	long initialEccentricTempoTime = 0;
	int initialRepCount = reps;
	boolean initialPositionAfterRep = false;
	boolean startedWorkout = false;
	boolean concentricTempoTimeStarted = false;
	boolean concentricTempoTimeFinished = false;
	boolean eccentricTempoTimeStarted = false;
	boolean eccentricTempoTimeFinished = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		setContentView(R.layout.activity_main);

		xacc = (TextView) findViewById(R.id.xvalue);
		yacc = (TextView) findViewById(R.id.yvalue);
		zacc = (TextView) findViewById(R.id.zvalue);
		xorient = (TextView) findViewById(R.id.xvalues);
		yorient = (TextView) findViewById(R.id.yvalues);
		zorient = (TextView) findViewById(R.id.zvalues);


	}



	public void onSensorChanged(int sensor, float[] values) {
		synchronized (this) {

			if (sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

				Log.d("value 1", values[1]+"");

				if (values[1] > 0) {
					// Person is at the bottom of the rep
					// Reset tempo flags on eccentric phase of rep
					concentricTempoTimeFinished = false;
					concentricTempoTimeStarted = false;
					if (startedWorkout == true && initialPositionAfterRep == false) { reps++; initialPositionAfterRep = true; }
					Log.d("reps", reps + "");
				}

				if (values[1] < 9 && values[1] > 6) {
					if (concentricTempoTimeStarted == false && concentricTempoTimeFinished == false) {
						startConcentricTempoTime();
						stopEccentricTempoTime();
					}
				}

				if (values[1] < 0) {
					// Person is at the top of the rep
					//float endConcentricTime = System.nanoTime();
					//float concentricTempo = endConcentricTime - startConcentricTime;
					startedWorkout = true;
					initialPositionAfterRep = false;

					//Log.d("concentricTempo", concentricTempo + "");

				}

				if (values[1] < -7 && values[1] > -9) {
					stopConcentricTempoTime();
					startEccentricTempoTime();
				}

				xorient.setText("Orientation X: " + values[0]);
				yorient.setText("Orientation Y: " + values[1]);
				zorient.setText("Orientation Z: " + values[2]);
			}
			if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
				xacc.setText("Accel X: " + values[0]);
				yacc.setText("Accel Y: " + values[1]);
				zacc.setText("Accel Z: " + values[2]);
			}            
		}
	}

	public void onAccuracyChanged(int sensor, int accuracy) {
		Log.d("Accuracy","onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);

	}
	
	public void startEccentricTempoTime(){
		initialEccentricTempoTime = System.nanoTime();
		eccentricTempoTimeStarted = true;
	}
	
	public void stopEccentricTempoTime(){
		synchronized (this) {
			if (eccentricTempoTimeFinished == false) {
				long currentTime = System.nanoTime(); 
				long eccentricTempoTime = currentTime - initialEccentricTempoTime;
				Log.d("initialEccentricTempoTime", initialEccentricTempoTime + "");
				Log.d("endingEccentricTempoTime", currentTime + "");
				double seconds = (double)eccentricTempoTime/ 1000000000.0;
				Log.d("eccentricTempoTime", seconds + " seconds");
				eccentricTempoTimeFinished = true;
			}
		}
	}

	public void startConcentricTempoTime() {
		initialConcentricTempoTime = System.nanoTime();
		concentricTempoTimeStarted = true;
	}

	public void stopConcentricTempoTime() {
		synchronized (this) {
			if (concentricTempoTimeFinished == false) {
				long currentTime = System.nanoTime(); 
				long concentricTempoTime = currentTime - initialConcentricTempoTime;
				Log.d("initialConcentricTempoTime", initialConcentricTempoTime + "");
				Log.d("endingConcentricTempoTime", currentTime + "");
				double seconds = (double)concentricTempoTime/ 1000000000.0;
				Log.d("concentricTempoTime", seconds + " seconds");
				concentricTempoTimeFinished = true;
			}
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		sm.registerListener(this, 
				SensorManager.SENSOR_ORIENTATION |
				SensorManager.SENSOR_ACCELEROMETER,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		sm.unregisterListener(this);
		super.onStop();
	}

	private void calibrate() {
		//int restingStateX = null;
		//int restingStateY = null;
		//int restingStateZ = null;
	}


}