package csf.itesm.mx.adhsocios.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
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

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import csf.itesm.mx.adhsocios.R;

//TODO : Creaate JSONParser
//TODO : Constantes de URLs
//TODO : LAYOUTs
//TODO : Add Realm
//TODO : VOLLEYS es el singleton que realiza requests
//TODO : Datos_Model funciona con realm, como funciona esa madre?

public class LoginActivity extends AppCompatActivity
{

    private static EditText LoginUser;

    private static EditText LoginPassword;

    private static Button Boton;

    private static TextView Aviso;

    private static Button botonAceptaAviso;

    private static Button botonCancelaAviso;

    private static TextView Recuperar;

    private static EditText EnviarCorreo;

    private static Button Button01;

    private static Button botonEnviarCorreo;

    private VolleyS volley;

    private String[] blogTitles;

    private Realm realm;

    private String blog;

    protected RequestQueue fRequestQueue;

    final Context context = this;

    private ProgressDialog pdia;

    private Dialog dialog;

    private static final String TAG = "Http Connection";

    private String FirstName;

    private String StatusAccount;

    private String LastName;

    private Boolean NoticePrivacyFlag;

    private Double Stature;

    private String Gender;

    private Long CompanyId;

    private String AssociateImage;

    private String AssociateId;

    private String nmComplete;

    //Survey
    private Boolean  AccesSurvey;

    private Boolean BanSurvey;

    private Long IdSurvey;

    private String UrlSurvey;

    private String NameSurvey;

    //CodeRecupera contraseña
    private static EditText CodeContraseña;
    private static EditText PasswordNuevoCode ;
    private static EditText PasswordNuevoCodeConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Controlador();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            View v = getCurrentFocus();
            if ( v instanceof EditText)
            {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                {
                    Log.d("focus", "touchevent");
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }}}
        return super.dispatchTouchEvent(event);
    }



    public void Controlador()
    {

        /**Aviso de provacidad inicio*/
        Aviso = (TextView) findViewById(R.id.);
        Aviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.aviso_privacidad);
                dialog.setTitle("Aviso de privacidad");
                Button button = (Button) dialog.findViewById(R.id.Button01);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        /**Recuperar contraseña inicio*/
        Recuperar = (TextView) findViewById(R.id.recuperar);
        Recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arf0)
            {
                dialog = null;
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.recuperar_datos);
                // dialog.setTitle("¿OLVIDASTE TU CONTRASEÑA?");

                Button button = (Button) dialog.findViewById(R.id.btn_cerrar);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                EnviarCorreo = (EditText) dialog.findViewById(R.id.CorreoUsuario);
                Button buttonEnviarCorreo = (Button) dialog.findViewById(R.id.rec_btn_enviar);
                buttonEnviarCorreo.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        VerifEmail();
                        //dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        /**login inicio*/
        Boton = (Button) findViewById(R.id.btn_login);
        Boton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Verifuser();
                pdia = new ProgressDialog(context);
                pdia.setMessage("Autentificando...");
                pdia.show();
            }
        });
        volley = VolleyS.getInstance(this.getApplicationContext());
        fRequestQueue = volley.getRequestQueue();
    }

    public void Verifuser(){

        LoginUser = (EditText) findViewById(R.id.LoginUser);

        LoginPassword = (EditText) findViewById(R.id.LoginPassword);

        String usuario = "username="+LoginUser.getText().toString();
        String contra = "&password="+LoginPassword.getText().toString();

        if(LoginUser.getText().toString().trim().equals("")){

            pdia.dismiss();
            LoginUser.setError("Es necesario colocar el usuario");

        }else if(LoginPassword.getText().toString().trim().equals("")){

            pdia.dismiss();
            LoginPassword.setError("Es necesario colocar la contraseña");

        }else{

            String url= URLS.GET_LOGIN+usuario+contra;
            Log.e("URL: ", url);
            new AsyncHttpTask().execute(url);

        }
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream ;
            HttpURLConnection urlConnection;
            Integer result = 0;
            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization","Basic amF2aWVyOjEyMw==");
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                if (statusCode ==  200) {
                    result = 1; // Successful
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jsonObject = (JSONArray) jsonParser.parse(
                            new InputStreamReader(inputStream, "UTF-8"));

                    JSONObject jsonObject1;
                    jsonObject1 = (JSONObject) jsonObject.get(0);
                    blog = (String) jsonObject1.get("Code");

                    if (blog.equals("01")) {

                        JSONObject jsonObject2;
                        jsonObject2 = (JSONObject) jsonObject.get(1);
                        StatusAccount = (String) jsonObject2.get("StatusAccount");

                        JSONObject jsonObject3 ;
                        jsonObject3 = (JSONObject) jsonObject.get(2);

                        FirstName = (String) jsonObject3.get("FirstName");

                        LastName = (String) jsonObject3.get("LastName");

                        NoticePrivacyFlag = (Boolean) jsonObject3.get("NoticePrivacyFlag");

                        Stature = (Double) jsonObject3.get("Stature");

                        Gender = (String) jsonObject3.get("Gender");

                        CompanyId = (Long) jsonObject3.get("CompanyId");

                        AssociateImage = (String) jsonObject3.get("AssociateImage");

                        AssociateId = (String) jsonObject3.get("AssociateId");

                        nmComplete = FirstName + "" + LastName;

                        if(NoticePrivacyFlag.booleanValue())
                        {

                            realm = Realm.getInstance(LoginActivity.this);
                            realm.beginTransaction();

                            Datos_Model datos_model = realm.createObject(Datos_Model.class);

                            datos_model.setFirtname(FirstName.toString());

                            datos_model.setLastname(LastName.toString());

                            datos_model.setEstatura(Stature.toString());

                            datos_model.setGender(Gender.toString());

                            datos_model.setCompanyid(CompanyId.toString());

                            datos_model.setAssociateimage(AssociateImage.toString());

                            datos_model.setAssociateId(AssociateId.toString());

                            datos_model.setNmComplete(nmComplete);

                            datos_model.setLogged(true);


                            realm.commitTransaction();

                            result = 3;

                        }else{
                            result = 5;
                        }

                    } else if (blog.equals("04")) {

                        result = 4;
                    }

                    //Log.d("Code",blog);
                    //Log.d("NoticePrivacyFlag",NoticePrivacyFlag);
                    //Log.d("json",jsonObject1.toString());
                    //Log.d("json2",jsonObject2.toString());
                    //Log.d("json3",jsonObject3.toString());

                    // Log.e("codigo",resultado.getJSONObject("").getString("Code"));
                }else{
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

            if(result == 1){
                Log.e("success","exitou");
            }else if(result ==3) {
                //Intent i = new Intent(LoginActivity.this, IndexActivity.class);
                //startActivity(i);
                pdia.dismiss();

                GetInfoEncuesta();


                //realm.close();

            }else if (result == 4){
                pdia.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LoginActivity.this);
                builder.setTitle("Error.");
                builder.setMessage("Favor de validar usuario y/o password ");

                builder.setPositiveButton("Cerrar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.e("info", "Cerrar");

                            }
                        });
                builder.show();

            }else if (result == 5){

                pdia.dismiss();
                Log.e("Muestra", "Muestra aviso privacidad");
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.acept_aviso_privacidad);
                dialog.setTitle("Aviso de privacidad");
                Button buttonCancelAviso = (Button) dialog.findViewById(R.id.btnCancelAviso);
                buttonCancelAviso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                LoginActivity.this);
                        builder.setTitle("Alerta.");
                        builder.setMessage("Es necesario aceptar el aviso de privacidad");

                        builder.setPositiveButton("Cerrar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Log.e("info", "Cerrar");

                                    }
                                });
                        builder.show();

                        dialog.dismiss();
                    }
                });

                Button buttonAceptarAviso = (Button) dialog.findViewById(R.id.btnAceptEncuesta);
                buttonAceptarAviso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Guarda estatus de aviso de privacidad
                        CambiaEstatus();

                        dialog.dismiss();
                    }
                });

                dialog.show();


                //AlertDialog.Builder builder = new AlertDialog.Builder(
                //        LoginActivity.this);
                //builder.setTitle("Error.");
                //builder.setMessage("Es necesario aceptar el aviso de provacidad");

                //builder.setPositiveButton("Cerrar",
                //        new DialogInterface.OnClickListener() {
                //            public void onClick(DialogInterface dialog,
                //                                int which) {
                //                Log.e("info", "Cerrar");

                //            }
                //       });
                //builder.show();

            }else {
                pdia.dismiss();
                Log.e(TAG, "Failed to fetch data!");
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LoginActivity.this);
                builder.setTitle("Error.");
                builder.setMessage("No se puede conectar, por favor intentelo de nuevo");

                builder.setPositiveButton("Cerrar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.e("info", "Cerrar");

                            }
                        });
                builder.show();

            }
        }
    }

    public void VerifEmail(){

        String CorreoRecuperar = "email="+EnviarCorreo.getText().toString();

        if (EnviarCorreo.getText().toString().trim().equals("")){
            //Enviar mensaje que debe ingresar correo
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    LoginActivity.this);
            builder.setTitle("Mensaje.");
            builder.setMessage("Escribe un correo válido. ");

            builder.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            Log.e("info", "Aceptar");

                        }
                    });
            builder.show();



        }
        else
        {
            String url = URLS.GET_BASIC_ASOCIATE_INFO+CorreoRecuperar;
            Log.e("UrlBasicAsociate:", url);
            new AsyncHttpTaskEmail().execute(url);

            dialog.dismiss();
        }

    }

    public class AsyncHttpTaskEmail extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... params) {

            InputStream inputStream = null;

            HttpURLConnection urlConnection = null;

            Integer result = 0;
            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization","Basic amF2aWVyOjEyMw==");
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                if (statusCode ==  200) {

                    result = 1; // Successful
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jsonObject = (JSONArray) jsonParser.parse(
                            new InputStreamReader(inputStream, "UTF-8"));

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1 = (JSONObject) jsonObject.get(0);
                    blog = (String) jsonObject1.get("Code");

                    if (blog.equals("01")) {

                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2 = (JSONObject) jsonObject.get(1);
                        CompanyId = (Long) jsonObject2.get("IdEmpresa");

                    }
                }else{
                    result = 0; //"Failed to fetch data!";
                }



            }
            catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result;
        }

        @Override
        protected  void onPostExecute(Integer results){

            if(results == 1){
                Log.e("Envia Correo","OK");
                //Manda correo
                EnviaCorreo();
            }
            else {

                //Enviar mensaje que no se envio el correo
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LoginActivity.this);
                builder.setTitle("Mensaje.");
                builder.setMessage("No pudo ser procesada tu solicitud, intenta mas tarde. ");

                builder.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.e("info", "Aceptar");

                            }
                        });
                builder.show();
            }
        }


    }

    public void EnviaCorreo(){

        String email = "email="+EnviarCorreo.getText().toString();
        String IdEmpresa = "&companyid="+CompanyId;
        String url= URLS.SET_RECOVER_PASSWORD+email+IdEmpresa;
        Log.e("UrlRecoverPassword", url);
        new AsyncHttpTaskRecover().execute(url);
    }

    public class AsyncHttpTaskRecover extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            InputStream inputStream = null;

            HttpURLConnection urlConnection = null;

            Integer result = 0;
            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic amF2aWVyOjEyMw==");
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {

                    result = 1; // Successful
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jsonObject = (JSONArray) jsonParser.parse(
                            new InputStreamReader(inputStream, "UTF-8"));

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1 = (JSONObject) jsonObject.get(0);
                    blog = (String) jsonObject1.get("Code");
                    if (blog.equals("01")) {
                        Log.e("Envio correo", "OK");
                    }
                } else {
                    result = 0; //"Failed to fetch data!";
                }


            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer results) {

            if (results == 1) {
                //Envia mensaje que se envio la información a su correo
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LoginActivity.this);
                builder.setTitle("Recuperar Contraseña.");
                builder.setMessage("Revisa tu correo a donde te llegar un link donde podras cambiar tu contraseña ");

                builder.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.e("info", "Aceptar");

                                MuestraCode();
                            }
                        });
                builder.show();


            } else {
                Log.e("Error", "No se envio el correo");
            }
        }

    }

    public void CambiaEstatus(){
        String associateId = "associateId="+AssociateId;
        String companyId = "&companyId=" +CompanyId;
        String PrivacyFlag ="&privacyFlag=true";
        String url = URLS.SET_RIVACY_STATUS +associateId+companyId+ PrivacyFlag;

        Log.e("URL_CAMBIASTATUS", url);
        new AsyncHttpTaskFlag().execute(url);

    }

    public class AsyncHttpTaskFlag extends AsyncTask<String, Void, Integer>{
        @Override
        protected Integer doInBackground(String... params){
            InputStream inputStream;

            HttpURLConnection urlConnection;

            Integer result = 0;
            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic amF2aWVyOjEyMw==");
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {

                    Log.e("Entro Flag", "Si cambio estatus aviso");

                    result = 1; // Successful
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jsonObject = (JSONArray) jsonParser.parse(
                            new InputStreamReader(inputStream, "UTF-8"));

                    JSONObject jsonObject1;
                    jsonObject1 = (JSONObject) jsonObject.get(0);
                    blog = (String) jsonObject1.get("Code");
                    if (blog.equals("01")) {
                        Log.e("Cambio estatus", "OK");
                    }
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            }
            catch (Exception e){
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result){


            if (result == 1) {
                //Envia encuesta
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.aviso_enuesta);
                dialog.setTitle("Instrucciones de Acceso");
                Button buttonCancelAviso = (Button) dialog.findViewById(R.id.btnAceptEncuesta);
                buttonCancelAviso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.e("info", "Aceptar");
                        GetInfoEncuesta();

                        dialog.dismiss();
                    }
                });
                dialog.show();

            } else {
                Log.e("Error", "No se envio el correo");
            }

        }
    }

    public void GetInfoEncuesta() {

        String associateId = "associateId="+AssociateId;
        String companyId = "&companyId=" +CompanyId;

        String url = URLS.GET_SURVEY +associateId+companyId;

        Log.e("URL_ENCUESTA", url);
        new AsyncHttpTaskSurvey().execute(url);
    }

    public class AsyncHttpTaskSurvey extends AsyncTask<String, Void, Integer>{
        @Override
        protected Integer doInBackground(String... params){
            InputStream inputStream;

            HttpURLConnection urlConnection;

            Integer result = 0;
            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic amF2aWVyOjEyMw==");
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {

                    result = 1; // Successful
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jsonObject = (JSONArray) jsonParser.parse(
                            new InputStreamReader(inputStream, "UTF-8"));

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1 = (JSONObject) jsonObject.get(0);
                    blog = (String) jsonObject1.get("Code");
                    if (blog.equals("01")) {
                        Log.e("Encuesta encontrada", "OK");

                        //Obtengo información
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2 = (JSONObject) jsonObject.get(1);
                        BanSurvey = (Boolean) jsonObject2.get("BanSurvey");
                        AccesSurvey = (Boolean) jsonObject2.get("AccesSurvey");
                        Log.e("BanSurvey",BanSurvey.toString());
                        if (!BanSurvey) {
                            Log.e("AccesSurvey",AccesSurvey.toString());
                            if ( !AccesSurvey ){
                                IdSurvey = (Long) jsonObject2.get("IdSurvey");
                                UrlSurvey = (String)jsonObject2.get("Url");
                                NameSurvey = (String)jsonObject2.get("NameSurvey");

                                result =3;
                            }
                            else{

                                result = 2;

                            }


                        } else{
                            //Direcciona a plan del dia
                           /* byte[] b = AssociateImage.getBytes();

                            Bitmap bmp= BitmapFactory.decodeByteArray(b,0,b.length);

                            ByteArrayOutputStream bs = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.PNG, 50, bs);
*/

                            Intent i = new Intent(LoginActivity.this, IndexActivity.class);
                            i.putExtra("nombre", nmComplete );
                            //  i.putExtra("image", AssociateImage);
                            startActivity(i);
                            realm.close();
                        }


                    }
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            }
            catch (Exception e){
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result){


            if (result == 3) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.aviso_enuesta);
                dialog.setTitle("Instrucciones de Acceso");
                Button buttonCancelAviso = (Button) dialog.findViewById(R.id.btnAceptEncuesta);
                buttonCancelAviso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.e("info", "Aceptar");
                        dialog.dismiss();
                        InsertEncuesta();


                    }
                });
                dialog.show();

                //Envia mensaje que se envio la información a su correo
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LoginActivity.this);
                builder.setTitle("Alerta.");
                builder.setMessage("Es necesario contestar en su totalidad el cuestionario de riesgos de salud.  Gracias por dedicarle tiempo a tu salud.");

                builder.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.e("info", "Aceptar");
                                GetInfoEncuesta();

                            }
                        });
                builder.show();
                */


            }
            else if(result==2){
                Log.e("Alert","Gracias");
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LoginActivity.this);
                builder.setTitle("Instrucciones de Acceso.");
                builder.setMessage("Gracias por contestar el cuestionario, se están analizando tus resultados, en 24 horas podrás accesar a la plataforma.");

                builder.setPositiveButton("Cerrar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.e("info", "Cerrar");


                            }
                        });
                builder.show();
            }
            else {
                Log.e("Error", "No se envio el correo");
            }

        }
    }

    public void InsertEncuesta(){

        String IdEncuesta = "IdEncuesta="+IdSurvey;
        String IdSocio = "&IdSocio="+AssociateId;
        String IdEmpresa = "&IdEmpresa="+CompanyId;
        String EstatusEncuesta = "&EstatusEncuesta=false";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        //System.out.println(dateFormat.format(date));
        String FEcha = dateFormat.format(date);

        Log.e("dateFormat:", FEcha);
        String FechaRecuperacion ="&FechaRecuperacion="+ FEcha;
        String FechaRealizacion = "&FechaRealizacion="+FEcha;
        String url = URLS.SET_SURVEY +IdEncuesta+IdSocio+IdEmpresa+EstatusEncuesta+FechaRecuperacion+FechaRealizacion;

        Log.e("URL_INSERTAENCUESTA", url);
        new AsyncHttpTaskInsertSurvey().execute(url);

    }

    public class AsyncHttpTaskInsertSurvey extends AsyncTask<String, Void, Integer>{
        @Override
        protected Integer doInBackground(String... params){
            InputStream inputStream = null;

            HttpURLConnection urlConnection = null;

            Integer result = 0;
            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic amF2aWVyOjEyMw==");
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {

                    result = 1; // Successful
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jsonObject = (JSONArray) jsonParser.parse(
                            new InputStreamReader(inputStream, "UTF-8"));

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1 = (JSONObject) jsonObject.get(0);
                    blog = (String) jsonObject1.get("Code");
                    if (blog.equals("01")) {
                        Log.e("Encuesta Inserta", "OK");
                    }
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            }
            catch (Exception e){
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result){


            if (result == 1) {
                String UrlEncuesta = UrlSurvey.replace("{idEncuesta}",IdSurvey.toString()).replace("{nombreEncuesta}",NameSurvey.replace(" ","-")).replace("{associateId}",AssociateId).replace("{companyId}",CompanyId.toString());
                Log.e("UrlEncuesta:", UrlEncuesta);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlEncuesta));
                startActivity(browserIntent);
            } else {
                Log.e("Error", "No se envio a la encuesta");
            }

        }
    }


    public void MuestraCode()
    {
        dialog = null;
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.recuperar_contrasenia_code);

        CodeContraseña = (EditText) dialog.findViewById(R.id.txt_Code_contrasenia);
        PasswordNuevoCode = (EditText) dialog.findViewById(R.id.txt_Nueva_Contrasenia_code);
        PasswordNuevoCodeConfirm = (EditText) dialog.findViewById(R.id.txt_Confirm_contrasenia_code);
        Button buttonCambiaContra = (Button) dialog.findViewById(R.id.btn_cambia_contrasenia);

        buttonCambiaContra.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (  PasswordNuevoCode.getText().toString().equals(PasswordNuevoCodeConfirm.getText().toString())  )
                {
                    RecuperaCode();
                    dialog.dismiss();
                }
                else
                {
                    //Envia mensaje que se actualizo  la contraseña de recovery password code
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            LoginActivity.this);
                    builder.setTitle("Alerta.");
                    builder.setMessage("No coinciden las contraseñas,favor de validarlas.");

                    builder.setPositiveButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Log.e("info", "Aceptar");
                                }
                            });
                    builder.show();
                }
            }
        });
        dialog.show();
    }

    public void RecuperaCode(){

        String codeContra = "code="+CodeContraseña.getText().toString();
        String passwordCode = "&password=" +PasswordNuevoCode.getText().toString() ;
        String companyidCode ="&companyid="+CompanyId;
        String urlCode = URLS.GET_RECOVER_CODE +codeContra+passwordCode+ companyidCode;

        Log.e("urlCode", urlCode);
        new AsyncHttpTaskRecoverCode().execute(urlCode);

    }

    public class AsyncHttpTaskRecoverCode extends AsyncTask<String, Void, Integer>
    {
        @Override
        protected Integer doInBackground(String... params){
            InputStream inputStream = null;

            HttpURLConnection urlConnection = null;

            Integer result = 0;
            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic amF2aWVyOjEyMw==");
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {

                    result = 1; // Successful
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jsonObject = (JSONArray) jsonParser.parse(new InputStreamReader(inputStream, "UTF-8"));
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1 = (JSONObject) jsonObject.get(0);
                    blog = (String) jsonObject1.get("Code");
                    if (blog.equals("01")) {
                        Log.e("Contrasenia Code", "OK");
                    }
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            }
            catch (Exception e)
            {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            if (result == 1)
            {
                //Envia mensaje que se actualizo  la contraseña de recovery password code
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                builder.setTitle("Recuperación de contraseña.");
                builder.setMessage("Se actualizo tu contraseña de manera exitosa ");

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Log.e("info", "Aceptar");
                    }
                });
                builder.show();

            } else {
                Log.e("Error", "No se actualizo code");
            }

        }
    }

}