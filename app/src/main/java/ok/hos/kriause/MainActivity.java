package ok.hos.kriause;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static AppCompatActivity app;
    public static ImageView image;
    public static long secondsLastMoved;
    public static MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = this;
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.something);
        MainActivity.image.setVisibility(View.GONE);
        mp = MediaPlayer.create(this, R.raw.screem);
        new DownloadFilesTask().execute(null, null, null);
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