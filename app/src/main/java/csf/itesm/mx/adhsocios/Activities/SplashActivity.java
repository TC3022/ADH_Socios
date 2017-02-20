package csf.itesm.mx.adhsocios.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import csf.itesm.mx.adhsocios.R;

public class SplashActivity extends AppCompatActivity {


    private long wait = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imgv = (ImageView)findViewById(R.id.iv1);

        //obtener número aleatorio
        Random rand = new Random();
        int  n = rand.nextInt(4) + 1;

        //mostrar imagen aleatoria
        switch (n) {
            case 1:
                imgv.setImageResource(R.drawable.bg1_cropped);
                break;
            case 2:
                imgv.setImageResource(R.drawable.bg2_cropped);
                break;
            case 3:
                imgv.setImageResource(R.drawable.bg3_cropped);
                break;
            case 4:
                imgv.setImageResource(R.drawable.bg4_cropped);
                break;
        }

        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                finish();
                Intent intent = new Intent().setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };

        (new Timer()).schedule(task, wait);
    }
}
