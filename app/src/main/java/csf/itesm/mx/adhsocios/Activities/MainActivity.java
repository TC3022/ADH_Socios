package csf.itesm.mx.adhsocios.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import csf.itesm.mx.adhsocios.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (true) //Not Logged
            startActivity(new Intent().setClass(MainActivity.this, LoginActivity.class));
    }
}
