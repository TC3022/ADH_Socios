package csf.itesm.mx.adhsocios.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.models.Datos_Model;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "Main";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm mRealm = Realm.getDefaultInstance();
        Datos_Model user = mRealm.where(Datos_Model.class).findFirst();
        if ( user == null ) //Not Logged, enviar al login
        {
            startActivity(new Intent().setClass(MainActivity.this, LoginActivity.class)); //Llamar Login
            finish();                                                                     //Y matar main
        }


        Log.d(TAG, String.format("%s %s %sm",user.getAssociateId(),user.getNmComplete(),user.getEstatura()) );
    }
}
