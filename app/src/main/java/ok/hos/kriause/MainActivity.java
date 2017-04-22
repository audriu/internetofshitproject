package ok.hos.kriause;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static AppCompatActivity app;
    public static ImageView image;
    public static long secondsLastMoved;
    public static MediaPlayer mp;
    private SensorManager sensorManager;
    float ax, ay, az;   // these are the acceleration in x,y and z axis
    UsbDevice device;
    UsbDeviceConnection usbConnection;

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float newx = event.values[0];
            float newy = event.values[1];
            float newz = event.values[2];
            float diffx = newx-ax;
            float diffy = newy-ay;
            float diffz = newz-az;
            if(diffx > 1 || diffy >1 || diffz > 1) {
                System.out.println("---" + (newx - ax) + "---" + (newy - ay) + "---" + (newz - az));
                secondsLastMoved = System.currentTimeMillis();
            }
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = this;
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.something);
        MainActivity.image.setVisibility(View.GONE);
        mp = MediaPlayer.create(this, R.raw.screem);
        new DownloadFilesTask().execute(null, null, null);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                secondsLastMoved = System.currentTimeMillis();
            case MotionEvent.ACTION_MOVE:
                secondsLastMoved = System.currentTimeMillis();
            case MotionEvent.ACTION_UP:
                secondsLastMoved = System.currentTimeMillis();
        }
        return false;
    }

}


class DownloadFilesTask extends AsyncTask<String, Integer, Long> {

    protected Long doInBackground(String... urls) {
        System.out.println("AssyncTask started-------------------------------");
        while (true) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if ((System.currentTimeMillis() - MainActivity.secondsLastMoved) < 1000) {
                publishProgress(0);
            } else if ((System.currentTimeMillis() - MainActivity.secondsLastMoved) > 1000) {
                publishProgress(1);
            }
        }
    }

    protected void onProgressUpdate(Integer... progress) {
        if (progress[0].equals(1)) {
            MainActivity.image.setVisibility(View.GONE);
            if (MainActivity.mp != null) {
                if (MainActivity.mp.isPlaying()) {
                    MainActivity.mp.reset();
                    MainActivity.mp = MediaPlayer.create(MainActivity.app, R.raw.screem);
                }
            }
        } else {
            MainActivity.image.setVisibility(View.VISIBLE);
            if (MainActivity.mp != null)
                if (!MainActivity.mp.isPlaying())
                    MainActivity.mp.start();
        }
    }

}