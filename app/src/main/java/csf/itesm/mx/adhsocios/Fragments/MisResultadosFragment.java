package csf.itesm.mx.adhsocios.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csf.itesm.mx.adhsocios.Adapters.ResultsAdapter;
import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.Requester;
import csf.itesm.mx.adhsocios.Utils.Parser;
import csf.itesm.mx.adhsocios.models.ResultPackage;
import csf.itesm.mx.adhsocios.models.UserResults;
import csf.itesm.mx.adhsocios.models.User;

public class MisResultadosFragment extends Fragment
{

    private User mUser;
    private Activity CONTEXT;
    private RecyclerView mRecyclerView;
    private ResultsAdapter mResultsAdapter;
    private static String TAG="MisResultadosFragment";
    private static final String ep_getResults="GetMyResults?associateId=%s&companyId=%s";
    private onMiSaludInteraction mListener;

    public MisResultadosFragment() {}

    public static MisResultadosFragment newInstance(User u)
    {
        MisResultadosFragment fragment = new MisResultadosFragment();
        fragment.setArguments( Parser.UserToBundle(u) );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity();
        if (getArguments() != null)
        {
            mUser = Parser.UserFromBundle(getArguments());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_mi_salud, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_results);
        mResultsAdapter = new ResultsAdapter(CONTEXT,null);
        mRecyclerView.setLayoutManager( new GridLayoutManager(CONTEXT,2) );
        mRecyclerView.setAdapter( mResultsAdapter );
        loadResults();
        return view;
    }

    void loadResults()
    {

        String url = getResources().getString(R.string.api_host) + String.format(ep_getResults,mUser.getAssociateId(),mUser.getCompanyid());
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
                        JSONObject results = response.getJSONArray(1).getJSONObject(0);
                        JSONArray weights = results.getJSONArray("Weight");
                        JSONArray bmi = results.getJSONArray("Bmi");
                        JSONArray fat = results.getJSONArray("Fat");
                        JSONArray muscle = results.getJSONArray("Muscle");

                        JSONObject current = null;
                        UserResults ur = new UserResults();

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
                        for (int i = 0; i < bmi.length(); i++)
                        {
                            current = fat.getJSONObject(i);
                            ur.addFat( new ResultPackage( current.getDouble("Value"), current.getString("Date") ));
                        }
                        for (int i = 0; i < bmi.length(); i++)
                        {
                            current = muscle.getJSONObject(i);
                            ur.addMuscle( new ResultPackage( current.getDouble("Value"), current.getString("Date") ));
                        }
                        mResultsAdapter.setResults(ur);
                    }
                    else
                    {

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onMiSaludInteraction)
        {
            mListener = (onMiSaludInteraction) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }


    public interface onMiSaludInteraction
    {
        void onMiSaludInteraction(Uri uri);
    }
}
