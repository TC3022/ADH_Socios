package csf.itesm.mx.adhsocios.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apradanas.simplelinkabletext.LinkableEditText;
import com.apradanas.simplelinkabletext.LinkableTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.Requester;
import csf.itesm.mx.adhsocios.Utils.JSONParser;
import csf.itesm.mx.adhsocios.models.Datos_Model;
import io.realm.Realm;

//TODO : Create JSONParser
//TODO : Constantes de URLs
//TODO : LAYOUTs -> Crear widgets

public class LoginActivity extends AppCompatActivity
{
    @BindView(R.id.hyperlinkPrivacyPolicy) LinkableTextView hlink_policy;
    @BindView(R.id.hyperlinkForgottenPwd) LinkableTextView  hlink_f_password;

    private static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setHyperlinks();
    }

    void setHyperlinks()
    {
        hlink_f_password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog( LoginActivity.this );
                dialog.setContentView(R.layout.dialog_forgotten_password);
                dialog.setTitle(getResources().getString(R.string.forgottenPwdTitle));

                Button button = (Button) dialog.findViewById(R.id.bttn_dismiss_fgtPwd);
                button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();
                    }
                });

                final EditText emailEditText = (EditText) dialog.findViewById(R.id.forgottenEmail);
                Button buttonEnviarCorreo = (Button) dialog.findViewById(R.id.bttn_send_fgtPwd);
                buttonEnviarCorreo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        VerifyEmail(emailEditText.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        hlink_policy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                final Dialog dialog = new Dialog( LoginActivity.this );
                dialog.setContentView(R.layout.dialog_aviso_privacidad);
                dialog.setTitle( getResources().getString(R.string.privacyPolicyTitle) );
                Button button = (Button) dialog.findViewById(R.id.bttn_closePrivacyPolicy);
                button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void VerifyEmail(String email)
    {
        String CorreoRecuperar = "email="+email;

        if (email.trim().equals(""))
        {
            //Enviar mensaje que debe ingresar correo
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Mensaje.");
            builder.setMessage("Escribe un correo válido. ");
            builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog,int which)
                {
                    Log.e("info", "Aceptar");
                }
            });
            builder.show();
        }
        else
        {
            //MANDAR LA REQUEST USANDO VOLLEY

            //String url = URLS.GET_BASIC_ASOCIATE_INFO + CorreoRecuperar;
            //Log.e(TAG, url);
            //new AsyncHttpTaskEmail().execute(url);
        }

    }
}