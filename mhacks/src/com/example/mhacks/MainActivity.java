package com.example.mhacks;

<<<<<<< HEAD
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.R;
=======
>>>>>>> c30bacac39d5068e16c42ce796aebc0ec48af2c7
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;


public class MainActivity extends Activity implements SensorListener {

	SensorManager sm = null;

	TextView xacc= null;
	TextView yacc = null;
	TextView zacc = null;
	TextView xorient = null;
	TextView yorient = null;
	TextView zorient = null;
	TextView text = null;
	
	NfcAdapter mAdapter;

	/* VARIABLES THAT WE ACTUALLY NEEED!!!!!!!*/
	int reps = 0;
	long initialConcentricTempoTime = 0;
	long eccentricTempoTime = 0;
	int initialRepCount = reps;
	boolean initialPositionAfterRep = false;
	boolean startedWorkout = false;
	boolean concentricTempoTimeStarted = false;
	boolean concentricTempoTimeFinished = false;
	private AQuery aq;

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
		text = (TextView) findViewById(R.id.textView1);
	}

	@SuppressLint("NewApi") @Override
	public void onStart(){
	    super.onStart();
	    //setContentView(R.layout.activity_main);
	    
	    mAdapter = NfcAdapter.getDefaultAdapter(this);
	    IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
	    try{
	        ndef.addDataType("text/plain");
	    }catch(MalformedMimeTypeException e){
	        throw new RuntimeException("fail", e);
	    }
	    
	    Intent intent = getIntent();
	    text.setText(getNdefMessages(intent));
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

					if (startedWorkout == true && initialPositionAfterRep == false) { 
						reps++; 
						initialPositionAfterRep = true; 
						new UploadWorkoutData().execute(reps + "");
						
						MediaPlayer mp = new MediaPlayer();
						
						String path = "";
						String fileName = "";
					}

					if (startedWorkout == true && initialPositionAfterRep == false) { 
						reps++; 
						initialPositionAfterRep = true; 
						new UploadWorkoutData().execute(reps + "");
					}

					Log.d("reps", reps + "");
				}

				if (values[1] < 9 && values[1] > 6) {
					if (concentricTempoTimeStarted == false && concentricTempoTimeFinished == false) {
						startConcentricTempoTime();
					}
				}

				if (values[1] < 0) {
					// Person is at the top of the rep
					startedWorkout = true;
					initialPositionAfterRep = false;
				}

				if (values[1] < -7 && values[1] > -9) {
					stopConcentricTempoTime();
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
		
		
		//Ndef ndef = Ndef.get(detectedTag);

		
	    Intent intent = getIntent();

        try{
            //ndef.connect();

            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (messages != null) {
                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                for (int i = 0; i < messages.length; i++) {
                    ndefMessages[i] = (NdefMessage) messages[i];
                }
            NdefRecord record = ndefMessages[0].getRecords()[0];

            byte[] payload = record.getPayload();
            String text_2 = new String(payload);
            text.setText("NFC: " + text_2);
            return;
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot Read From Tag.", Toast.LENGTH_LONG).show();
        }
		
	
		
		
		NdefMessage[] msgs = null;
		
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
	        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	        if (rawMsgs != null) {
	            msgs = new NdefMessage[rawMsgs.length];
	            for (int i = 0; i < rawMsgs.length; i++) {
	                msgs[i] = (NdefMessage) rawMsgs[i];
	            }
	            
	            text.setText("NFC: " + msgs[0].getRecords()[0].getPayload().toString());
	        }
	    }
	}

	@Override
	protected void onStop() {
		sm.unregisterListener(this);
		super.onStop();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") public String getNdefMessages(Intent intent){
	    NdefMessage[] msgs = null;
	    String action = intent.getAction();
	    if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)||
	            NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
	        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	        if(rawMsgs != null){
	            msgs = new NdefMessage[rawMsgs.length];
	            for(int i=0; i<rawMsgs.length; i++){
	                msgs[i] = (NdefMessage) rawMsgs[i];
	            }
	        }else{
	            byte[] empty = new byte[]{};
	            NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
	            NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
	            msgs = new NdefMessage[]{msg};
	        }

	    }
	    if(msgs==null)
	        return "No Tag discovered!";
	    else
	        return msgs.toString();
	}

	@Override
	public void onNewIntent(Intent intent){
	    Log.i("Foreground dispatch", "Discovered tag with intent:" + intent);
	    zacc.setText(getNdefMessages(intent));
	}

	private class UploadWorkoutData extends AsyncTask<String, String, String> {

		@Override
		  protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = "http://hospitaldocumentviewer.appspot.com/new";
			List parameters = new ArrayList();
			parameters.add(new BasicNameValuePair("reps", "" + reps));
			String paramString = URLEncodedUtils.format(parameters, "utf-8");
			url += "?" + paramString;
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("Accept", "application/javascript");
			httpget.setHeader("Content-Type", "application/javascript");
			try {
			    HttpResponse response = httpclient.execute(httpget);
			} catch (ClientProtocolException e) {
			    // TODO Auto-generated catch block
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			}
		        return null;
		  }
	}
}