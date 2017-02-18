package csf.itesm.mx.adhsocios.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.apradanas.simplelinkabletext.LinkableTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.Requester;

//TODO FINALIZAR EL PROCESO DE CAMBIO DE PASSWORD
//TODO Agregar XML de estilos/Colores/Imagenes de estos weyes (https://www.materialpalette.com)
//TODO CAMBIAR IDIOMA
//TODO LOGIN CON ENDPOINT
    //TODO GUARDAR RESULTADO DE LOGIN EN BASE REALM
    //TODO OBTENER USUARIO EN REALM DESDE OTRA ACTIVIDAD
//TODO Disenar nuevas pantallas
    //TODO Agregar Nuevas Pantallas



public class LoginActivity extends AppCompatActivity
{
    @BindView(R.id.hyperlinkPrivacyPolicy) LinkableTextView hlink_policy;
    @BindView(R.id.hyperlinkForgottenPwd) LinkableTextView  hlink_f_password;
    @BindView(R.id.login_username) EditText input_username;
    @BindView(R.id.login_password) EditText input_password;
    @BindView(R.id.login_button) Button bttn_login;

    private static final String TAG = "LOGIN";
    private static final String ep_recoverPassword="SetRecoverPassword?email=%s&companyid=%s";
    private static final String ep_associateInfo="BasicAsociateInfo?email=%s";
    private static final String regex_email = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setButtons();
    }

    void setButtons()
    {
        bttn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String username = input_username.getText().toString();
                String password = input_password.getText().toString();
                if ( username.length()!=0 && password.length()!=0 ) //No sabemos con que mas validar
                    login(username,password);
            }
        });

        hlink_f_password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog( LoginActivity.this );
                dialog.setContentView(R.layout.dialog_forgotten_password);
                dialog.setTitle(getString(R.string.forgottenPwdTitle));

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
    public void login(String username,String password)
    {
        ProgressDialog pdia = new ProgressDialog(LoginActivity.this);
        pdia.setMessage("Autentificando...");
        pdia.show();

                


    }

    public void VerifyEmail(String email)
    {
        if (!email.matches(regex_email))
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
            prepareToSendEmail(email); //Esta cosa obtiene el CompanyId y despues invoca SendEmail()
        }
    }
    //El endpoint se diseno para que tengas que obtener primero empresa y luego mandar correo
    void prepareToSendEmail(final String email)
    {
        String url = getResources().getString(R.string.api_host) + String.format(ep_associateInfo, email);
        Log.d(TAG,url);
        JsonArrayRequest preResetPwd = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    JSONObject resp = response.getJSONObject(0); //Hecha delav el endpoint then hacemos esto
                    if (  resp.getString("Code").equals("01"))  //Supongo que 01 es exito
                    {
                        Long companyId = response.getJSONObject(1).getLong("IdEmpresa");
                        sendEmail(email,companyId);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic amF2aWVyOjEyMw=="); //BIEN NACO HARDCODEADO
                return headers;
            }
        };
        Requester.getInstance().addToRequestQueue(preResetPwd);
    }

    //Le pega al endpoint para mandar un correo
    void sendEmail(String email,Long companyId)
    {
        String url = getResources().getString(R.string.api_host) + String.format(ep_recoverPassword,email,companyId);
        Log.d(TAG,url);
        JsonArrayRequest resetPwd = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    JSONObject resp = response.getJSONObject(0); //Hecha delav el endpoint then hacemos esto
                    if (  resp.getString("Code").equals("01"))  //Supongo que 01 es exito
                    {
                        //TODO
                        //FUE EXITOSO, AVISAR CON UN MENSAJE O ALGO, EN ESTE PUNTO
                        //UNO DEBE ESPERAR A QUE LE LLEGUE EL CORREO, AQUI SE DEBE DE PRESENTAR UNA VISTA
                        //CON 3 CAMPOS
                            //UNO PARA CODIGO ENVIADO POR CORREO
                            //UNO PARA NUEVA CONTRA
                            //UNO PARA CONFIRMAR NUEVA CONTRA
                        //SI COINCIDEN CONTRASEÑAS PEGARLE AL ENDPOINT DE GET_RECOVER_CODE QUE CAMBIARA LA CONTRASEÑA
                    }
                    else
                    {
                        //ALGO FRACASO, decirle que no mame
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic amF2aWVyOjEyMw=="); //BIEN NACO HARDCODEADO
                return headers;
            }
        };

        Requester.getInstance().addToRequestQueue(resetPwd);
    }
}