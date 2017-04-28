package csf.itesm.mx.adhsocios.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apradanas.simplelinkabletext.LinkableTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.Requester;
import csf.itesm.mx.adhsocios.models.User;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity
{
    @BindView(R.id.login_username) EditText input_username;
    @BindView(R.id.login_password) EditText input_password;
    @BindView(R.id.activity_login) ConstraintLayout mainContainer;
    @BindView(R.id.switch1) Switch host_switch;

    private Realm mRealm;

    private static final String TAG = "LOGIN";
    private static final String ep_recoverPassword="SetRecoverPassword?email=%s&companyid=%s";
    private static final String ep_associateInfo="BasicAsociateInfo?email=%s";
    private static final String ep_getLogin="GetLogin?username=%s&password=%s";
    private static final String ep_setPrivacyFlag="SetPrivacyFlagStatus?associateId=%s&companyId=%s&privacyFlag=%s";
    private static final String ep_setPassword = "GetRecoverCodeValidator?code=%s&password=%s&companyid=%s";

    private static final String regex_email = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mRealm = Realm.getDefaultInstance();
    }

    @OnClick(R.id.login_button)
    public void login(View v)
    {
        String username = input_username.getText().toString();
        String password = input_password.getText().toString();
        if ( username.length()!=0 && password.length()!=0 ) //No sabemos con que mas validar
        {
            if (host_switch.isChecked()) //Usar Ubiquitos
                otherLogin(username,password);
            else
                login(username, password);
        }
    }
    public void otherLogin(String uName,String uPass)
    {
        final ProgressDialog pdia = new ProgressDialog(LoginActivity.this);
        pdia.setMessage(getResources().getString(R.string.authenticate));
        pdia.show();

        String url = getResources().getString(R.string.other_host) + String.format(ep_getLogin,uName,uPass);
        Log.d(TAG,url);
        JsonObjectRequest loginR = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    if (response.getBoolean("success"))
                    {
                        JSONObject data = response.getJSONObject("data");

                        final User datos_usuario = new User();
                        datos_usuario.setFirtname( data.getString("firstname") );
                        datos_usuario.setLastname( data.getString("lastname") );
                        datos_usuario.setLogged(true);
                        datos_usuario.setProd( false );
                        datos_usuario.setHost( getResources().getString(R.string.other_host) );
                        datos_usuario.setNmComplete( datos_usuario.getFirtname()+" "+datos_usuario.getLastname() );
                        datos_usuario.setAssociateId( data.getString("id"));

//                        datos_usuario.setEstatura( data2.getDouble("Stature") );
//                        datos_usuario.setGender(data2.getString("Gender") );
//                        datos_usuario.setCompanyid( data2.getLong("CompanyId"));
//                        datos_usuario.setAssociateimage( data2.getString("AssociateImage"));

                        grantAccess(datos_usuario);
                    }
                    else
                    {
                        Snackbar.make(mainContainer,response.getString("msg"),Snackbar.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    pdia.dismiss();
                }
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                pdia.dismiss();
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

        Requester.getInstance().addToRequestQueue(loginR);
    }

    @OnClick(R.id.hyperlinkForgottenPwd)
    public void forgottenPwd(View v)
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

    @OnClick(R.id.hyperlinkPrivacyPolicy)
    public void showPrivacyPolicy(View v)
    {
        final Dialog dialog = new Dialog( LoginActivity.this );
        dialog.setContentView(R.layout.dialog_aviso_privacidad);
        dialog.setTitle( getResources().getString(R.string.privacyPolicyTitle) );
        Button button = (Button) dialog.findViewById(R.id.bttn_closePrivacyPolicy);
        Button button2 = (Button) dialog.findViewById(R.id.bttn_acceptPrivacyPolicy);
        View.OnClickListener dms = new View.OnClickListener() {@Override public void onClick(View view){dialog.dismiss();}};
        button.setOnClickListener(dms);
        button2.setOnClickListener(dms);
        dialog.show();
    }

    public void login(String username,String password)
    {
        final ProgressDialog pdia = new ProgressDialog(LoginActivity.this);
        pdia.setMessage(getResources().getString(R.string.authenticate));
        pdia.show();

        String url = getResources().getString(R.string.api_host) + String.format(ep_getLogin,username,password);
        Log.d(TAG,url);
        JsonArrayRequest loginRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    JSONObject codes = response.getJSONObject(0);
                    if (codes.getString("Code").equals("01")) //Fue exitoso y se puede logear
                    {
                        Log.d("LOGIN",response.toString());
                        JSONObject data1 = response.getJSONObject(1);
                        JSONObject data2 = response.getJSONObject(2);
                        Boolean noticePrivacyFlag = data2.getBoolean("NoticePrivacyFlag");

                        final User datos_usuario = new User();
                        datos_usuario.setFirtname( data2.getString("FirstName") );
                        datos_usuario.setLastname( data2.getString("LastName") );
                        datos_usuario.setEstatura( data2.getDouble("Stature") );
                        datos_usuario.setGender(data2.getString("Gender") );
                        datos_usuario.setCompanyid( data2.getLong("CompanyId"));
                        datos_usuario.setAssociateimage( data2.getString("AssociateImage"));
                        datos_usuario.setAssociateId( data2.getString("AssociateId"));
                        datos_usuario.setNmComplete( datos_usuario.getFirtname()+" "+datos_usuario.getLastname() );
                        datos_usuario.setLogged(true);

                        datos_usuario.setProd( true );
                        datos_usuario.setHost( getResources().getString(R.string.api_host) );


                        //PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString("token", response.getString("token")).apply();

                            if (noticePrivacyFlag)
                            {
                                grantAccess(datos_usuario);
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,getString(R.string.mustAcceptPP),Toast.LENGTH_SHORT).show();
                                //AQUI LE MOSTRAREMOS EL AVISO DE PRIVACIDAD
                                //SI LO ACEPTA LE DEBEMOS DECIR AL API QUE UPDATEE ESA BANDERA
                                final Dialog dialog = new Dialog( LoginActivity.this );
                                dialog.setContentView(R.layout.dialog_aviso_privacidad);
                                dialog.setTitle( getResources().getString(R.string.privacyPolicyTitle) );

                                Button dismiss = (Button) dialog.findViewById(R.id.bttn_closePrivacyPolicy);
                                dismiss.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        dialog.dismiss();
                                    }
                                });

                                Button accept = (Button) dialog.findViewById(R.id.bttn_closePrivacyPolicy);
                                accept.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        //Cambia en base el status a aceptado y lo deja pasar
                                        dialog.dismiss();
                                        changePrivacyPolicyStatus( datos_usuario.getAssociateId(),datos_usuario.getCompanyid(),true);
                                        grantAccess(datos_usuario);
                                    }
                                });
                                dialog.show();
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, getString( R.string.msg_wrong_user_password ),Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        pdia.dismiss();
                    }
                }

            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    error.printStackTrace();
                    pdia.dismiss();
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
            Requester.getInstance().addToRequestQueue(loginRequest);
    }
    public void changePrivacyPolicyStatus(String associateId,long companyid,boolean privacyFlag)
    {
        String url = getResources().getString(R.string.api_host) + String.format(ep_setPrivacyFlag,associateId,companyid,privacyFlag?"true":"false");
        Log.d(TAG,url);
        JsonArrayRequest updatePPolicy = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    JSONObject resp = response.getJSONObject(0);
                    if (resp.getString("Code").equals("01"))
                    {
                        Log.d(TAG,"FUE EXITOSO EL CAMBIO DE ESTADO DE POLITICA DE PRIVACIDAD");
                    }
                }
                catch (Exception e)
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
        Requester.getInstance().addToRequestQueue(updatePPolicy);
    }
    public void grantAccess(User user)
    {
        mRealm.beginTransaction(); //Guardamos en base al usuario que pasara
            User realm_datos = mRealm.copyToRealm(user);
        mRealm.commitTransaction();
        //TODO Algo de una encuesta?
        startActivity(new Intent().setClass(LoginActivity.this,MainActivity.class)); //Llamar Main
        finish();                                                                    //Y matar Login
    }
    public void VerifyEmail(String email)
    {
        if (!email.matches(regex_email))
        {
            //Enviar mensaje que debe ingresar correo
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("");
            builder.setMessage( getString(R.string.wrong_email) );
            builder.setPositiveButton(getString(R.string.accept),new DialogInterface.OnClickListener()
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
                    else
                    {
                        Toast.makeText(LoginActivity.this, getString(R.string.wrong_email), Toast.LENGTH_SHORT).show();
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
    void sendEmail(String email, final Long companyId)
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
                        //UNO DEBE ESPERAR A QUE LE LLEGUE EL CORREO CON UN CODIGO QUE SE METE EN EL CAMPO DE TEXTO DEDICADO A ESO

                        final Dialog dialog = new Dialog( LoginActivity.this );
                        dialog.setContentView(R.layout.dialog_change_password);
                        dialog.setTitle(getString(R.string.change_password_title));

                        final EditText sentCode = (EditText) dialog.findViewById(R.id.change_sentCode);
                        final EditText pass = (EditText) dialog.findViewById(R.id.change_pass);
                        final EditText passConfirm = (EditText) dialog.findViewById(R.id.change_pass_conf);

                        Button button = (Button) dialog.findViewById(R.id.bttn_confirm_chgPwd);
                        button.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                //Valdiar que lo que se metio en ambos campos de contraseña es igual
                                if (pass.getText().toString().equals( passConfirm.getText().toString() ))
                                {
                                    setNewPassword(pass.getText().toString(),companyId,sentCode.getText().toString());
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this,getString(R.string.passwords_match_error),Toast.LENGTH_LONG);
                                }
                            }
                        });
                        dialog.show();
                    }
                    else
                    {
                        Log.e(TAG,"ERROR EN sendEmail , PROBABLEMENTE ALGO DEL ENDPOINT");
                        Log.e(TAG,response.toString());
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
    public void setNewPassword(String password, Long companyId, String code_from_mail)
    {
        String url = getResources().getString(R.string.api_host) + String.format(ep_setPassword,code_from_mail,password,companyId);
        Log.d(TAG,url);
        JsonArrayRequest setPassword = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    JSONObject resp = response.getJSONObject(0); //Hecha delav el endpoint then hacemos esto
                    if (  resp.getString("Code").equals("01"))  //Supongo que 01 es exito
                    {
                        Toast.makeText(LoginActivity.this,getString(R.string.change_password_success),Toast.LENGTH_LONG);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,getString(R.string.change_password_fail),Toast.LENGTH_LONG);
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
        Requester.getInstance().addToRequestQueue(setPassword);
    }
}