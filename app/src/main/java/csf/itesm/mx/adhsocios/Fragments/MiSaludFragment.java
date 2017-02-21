package csf.itesm.mx.adhsocios.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csf.itesm.mx.adhsocios.Adapters.SaludAdapter;
import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.Requester;
import csf.itesm.mx.adhsocios.Utils.Parser;
import csf.itesm.mx.adhsocios.models.User;
import csf.itesm.mx.adhsocios.models.UserRecord;

public class MiSaludFragment extends Fragment
{
    private User mUser;

    private static String TAG="MiSaludFragment";
    private Activity CONTEXT;
    private static final String ep_getExpediente="GetExpedienteAssociate?associateId=%s&companyId=%s";
    private RecyclerView mRecyclerView;
    private SaludAdapter mSaludAdapter;
    private OnMiSaludInteractionListener mListener;
    public MiSaludFragment() {}

    public static MiSaludFragment newInstance(User u)
    {
        MiSaludFragment fragment = new MiSaludFragment();
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_salud);
        mSaludAdapter = new SaludAdapter(CONTEXT,new ArrayList<UserRecord>());
        mRecyclerView.setLayoutManager( new LinearLayoutManager(CONTEXT) );
        mRecyclerView.setAdapter( mSaludAdapter );
        loadSalud();
        return view;
    }

    void loadSalud()
    {
        String url = getResources().getString(R.string.api_host) + String.format(ep_getExpediente,mUser.getAssociateId(),mUser.getCompanyid());
        Log.d(TAG,url);
        JsonArrayRequest setPassword = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                Log.d(TAG,response.toString());
                try
                {
                    if (  response.getJSONObject(0).getString("Code").equals("01"))  //Supongo que 01 es exito
                    {
                        JSONArray expediente = response.getJSONArray(1);
                        List<UserRecord> records = new ArrayList<>();
                        UserRecord current;
                        for (int i = 0; i < expediente.length() ; i++)
                        {
                            current = new UserRecord();
                            current.setDescription(expediente.getJSONObject(i).getString("Description"));
                            records.add(current);
                        }
                        //Updatear Adapter
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
        if (context instanceof OnMiSaludInteractionListener)
        {
            mListener = (OnMiSaludInteractionListener) context;
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

    public interface OnMiSaludInteractionListener
    {
        void OnMiSaludInteraction(Uri uri);
    }
}
