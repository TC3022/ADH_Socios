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

import csf.itesm.mx.adhsocios.models.Estudio;
import csf.itesm.mx.adhsocios.models.ResultPackage;
import csf.itesm.mx.adhsocios.models.User;
import csf.itesm.mx.adhsocios.models.UserHealthRecord;
import csf.itesm.mx.adhsocios.models.UserResults;

/**
 * Created by rubcuadra on 2/20/17.
 */

public class Parser
{
    public final static String FORMAT  = "yyyy-MM-dd'T'hh:mm:ss";

    public final static String ALTERNATE_FORMAT  = "dd/MM/yyyy hh:mm:ss";

    public static Date getDateFromString(String s, String format) //Usa el formato que nos regresa la base
    {
        Date d = null;
        try
        {
            SimpleDateFormat dt = new SimpleDateFormat(format);
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
    public static UserResults parseUserResultsUbiquitos(JSONObject response)
    {
        UserResults ur = new UserResults();
        try
        {
            if ( response.getBoolean("success") )
            {
                JSONObject results = response.getJSONObject("data");

                JSONArray weights = results.getJSONArray("weight");
                JSONArray bmi = results.getJSONArray("bmi");
                JSONArray fat = results.getJSONArray("fat");
                JSONArray muscle = results.getJSONArray("muscle");
                JSONObject current = null;

                for (int i = 0; i < weights.length(); i++)
                {
                    current = weights.getJSONObject(i);
                    ur.addWeight( new ResultPackage(  current.getDouble("value"),  getDateFromString(current.getString("date"),FORMAT) ));
                }
                for (int i = 0; i < bmi.length(); i++)
                {
                    current = weights.getJSONObject(i);
                    ur.addBmi( new ResultPackage( current.getDouble("value"), getDateFromString(current.getString("date"),FORMAT) ));
                }
                for (int i = 0; i < fat.length(); i++)
                {
                    current = fat.getJSONObject(i);
                    ur.addFat( new ResultPackage( current.getDouble("value"), getDateFromString(current.getString("date"),FORMAT) ));
                }
                for (int i = 0; i < muscle.length(); i++)
                {
                    current = muscle.getJSONObject(i);
                    ur.addMuscle( new ResultPackage( current.getDouble("value"), getDateFromString(current.getString("date"),FORMAT) ));
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
        finally
        {
            return ur;
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
                    ur.addWeight( new ResultPackage(  current.getDouble("Value"),  getDateFromString(current.getString("Date"),FORMAT) ));
                }
                for (int i = 0; i < bmi.length(); i++)
                {
                    current = weights.getJSONObject(i);
                    ur.addBmi( new ResultPackage( current.getDouble("Value"), getDateFromString(current.getString("Date"),FORMAT) ));
                }
                for (int i = 0; i < fat.length(); i++)
                {
                    current = fat.getJSONObject(i);
                    ur.addFat( new ResultPackage( current.getDouble("Value"), getDateFromString(current.getString("Date"),FORMAT) ));
                }
                for (int i = 0; i < muscle.length(); i++)
                {
                    current = muscle.getJSONObject(i);
                    ur.addMuscle( new ResultPackage( current.getDouble("Value"), getDateFromString(current.getString("Date"),FORMAT) ));
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
    public static List<UserHealthRecord> parseUserRecordsUbiquitous(JSONObject response)
    {
        List<UserHealthRecord> records = new ArrayList<>();
        try
        {
            if ( response.getBoolean("success") )
            {
                JSONArray expediente = response.getJSONArray("data");
                UserHealthRecord current;
                for (int i = 0; i < expediente.length() ; i++)
                {
                    current = new UserHealthRecord();
                    current.setTitle( expediente.getJSONObject(i).getString("title") );
                    current.setDescription( expediente.getJSONObject(i).getString("description") );
                    current.setDate( getDateFromString( expediente.getJSONObject(i).getString("date") ,FORMAT));
                    records.add(current);
                }
            }
            else
            {
                Log.e("UserHealthRecordParser",response.toString());
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return records;
        }
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
                        current.setDate( getDateFromString( expediente.getJSONObject(i).getString("DateFile") ,FORMAT));
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

    public static List<Estudio> parseEstudiosUbiquitous(JSONObject response)
    {
        List<Estudio> est= new ArrayList<>();
        try
        {
            if ( response.getBoolean("success") )
            {
                JSONArray estudios = response.getJSONArray("data");
                Estudio current;
                for (int i = 0; i < estudios.length(); i++)
                {
                    current = new Estudio();
                    current.setName(  estudios.getJSONObject(i).getString("title") );
                    current.setId( String.valueOf(i+1) );

                    if ( estudios.getJSONObject(i).getString("date").compareTo("null") == 0  ) //Es Null, no se ha aplicado
                        current.setApplied(null);
                    else
                        current.setApplied( getDateFromString( estudios.getJSONObject(i).getString("date"), FORMAT)  );
                    est.add(current);
                }
            }
            else
            {
                Log.e("parseEstudioUbiquitos",response.toString());
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return est;
        }
    }

    public static List<Estudio> parseEstudios(JSONArray response)
    {
        List<Estudio> est= new ArrayList<>();

        try
        {
            if (  response.getJSONObject(0).getString("Code").equals("01"))  //Supongo que 01 es exito
            {
                JSONArray estudios = response.getJSONArray(1);
                Estudio current;
                for (int i = 0; i < estudios.length(); i++)
                {
                    current = new Estudio();
                    current.setName(  estudios.getJSONObject(i).getString("DescriptionReasonDetail") );
                    current.setId( String.valueOf(i+1) );


                    current.setStart( getDateFromString( estudios.getJSONObject(i).getString("FechaInicio").substring(0,19),FORMAT));
                    current.setEnd( getDateFromString( estudios.getJSONObject(i).getString("FechaFin").substring(0,19),FORMAT));

                    if ( estudios.getJSONObject(i).getString("FechaAplico").compareTo("null") == 0  ) //Es Null, no se ha aplicado
                        current.setApplied(null);
                    else
                        current.setApplied( getDateFromString( estudios.getJSONObject(i).getString("FechaAplico").substring(0,19), ALTERNATE_FORMAT )  );

                    est.add(current);
                }
            }
            else
            {
                Log.e("parseEstudios","Respuesta error del servicio");
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return est;
    }
}
