package csf.itesm.mx.adhsocios.Utils;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import csf.itesm.mx.adhsocios.models.ResultPackage;
import csf.itesm.mx.adhsocios.models.User;
import csf.itesm.mx.adhsocios.models.UserHealthRecord;
import csf.itesm.mx.adhsocios.models.UserResults;

/**
 * Created by rubcuadra on 2/20/17.
 */

public class Parser
{
    public static Bundle UserToBundle(User u )
    {
        Bundle b = new Bundle();
        b.putString("fname",u.getFirtname());
        b.putString("lname",u.getLastname());
        b.putString("gender",u.getGender());
        b.putString("assoc_img",u.getAssociateimage());
        b.putString("assoc_id",u.getAssociateId());
        b.putString("nmcomp",u.getNmComplete());
        b.putLong("compId",u.getCompanyid());
        b.putBoolean("logged",u.isLogged());
        b.putDouble("estat",u.getEstatura());
        b.putString("host",u.getHost());
        return b;
    }
    public static User UserFromBundle(Bundle b)
    {
        User u = new User();
        u.setFirtname( b.getString("fname") );
        u.setLastname( b.getString("lname") );
        u.setGender( b.getString("gender") );
        u.setAssociateimage( b.getString("assoc_img") );
        u.setAssociateId( b.getString("assoc_id") );
        u.setNmComplete( b.getString("nmcomp") );
        u.setCompanyid( b.getLong("compId") );
        u.setLogged( b.getBoolean("logged") );
        u.setEstatura( b.getDouble("estat") );
        u.setHost(b.getString("host"));
        return u;
    }

    public static Date getDateFromString(String s) //Usa el formato que nos regresa la base
    {
        Date d = null;
        try
        {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            d = dt.parse(s);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return d;
        }
    }

    public static UserResults parseUserResults(JSONArray response)
    {
        UserResults ur = new UserResults();
        try
        {
            JSONObject resp = response.getJSONObject(0); //Hecha delav el endpoint then hacemos esto
            if (  resp.getString("Code").equals("01"))  //Supongo que 01 es exito
            {
                JSONObject results = response.getJSONArray(1).getJSONObject(0);
                JSONArray weights = results.getJSONArray("Weight");
                JSONArray bmi = results.getJSONArray("Bmi");
                JSONArray fat = results.getJSONArray("Fat");
                JSONArray muscle = results.getJSONArray("Muscle");
                JSONObject current = null;

                for (int i = 0; i < weights.length(); i++)
                {
                    current = weights.getJSONObject(i);
                    ur.addWeight( new ResultPackage( current.getDouble("Value"), current.getString("Date") ));
                }
                for (int i = 0; i < bmi.length(); i++)
                {
                    current = weights.getJSONObject(i);
                    ur.addBmi( new ResultPackage( current.getDouble("Value"), current.getString("Date") ));
                }
                for (int i = 0; i < fat.length(); i++)
                {
                    current = fat.getJSONObject(i);
                    ur.addFat( new ResultPackage( current.getDouble("Value"), current.getString("Date") ));
                }
                for (int i = 0; i < muscle.length(); i++)
                {
                    current = muscle.getJSONObject(i);
                    ur.addMuscle( new ResultPackage( current.getDouble("Value"), current.getString("Date") ));
                }
            }
            else
            {
                Log.e("parseUserResults","Respuesta error del servicio");
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return ur;
    }


    public static List<UserHealthRecord> parseUserRecords(JSONArray response)
    {
        List<UserHealthRecord> records = new ArrayList<>();
        try
        {
            if (  response.getJSONObject(0).getString("Code").equals("01"))  //Supongo que 01 es exito
            {
                JSONArray expediente = response.getJSONArray(1);
                UserHealthRecord current;
                try
                {
                    for (int i = 0; i < expediente.length() - 1; i+=2) //0 , 2, 4 seran titulos, los nones son descripciones
                    {
                        current = new UserHealthRecord();
                        current.setTitle( expediente.getJSONObject(i).getString("Description") );
                        current.setDescription( expediente.getJSONObject(i+1).getString("Description") );
                        current.setDate( getDateFromString( expediente.getJSONObject(i).getString("DateFile")  ));
                        records.add(current);
                    }
                }
                catch (Exception e)
                {
                    Log.e("parseaMiSalud","ERROR AL GENERAR ARREGLO DE RESULTADOS DE SALUD, Checar size del array");
                    e.printStackTrace();
                }

            }
            else
            {
                Log.e("parseMiSalud","Respuesta error del servicio");
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return records;

    }
}
