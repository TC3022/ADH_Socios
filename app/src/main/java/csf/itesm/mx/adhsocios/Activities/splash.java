package csf.itesm.mx.adhsocios.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import csf.itesm.mx.adhsocios.R;

public class splash extends AppCompatActivity {


    private long wait = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                finish();
                Intent intent = new Intent().setClass(splash.this, LoginActivity.class);
                startActivity(intent);
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, wait);
    }
}
