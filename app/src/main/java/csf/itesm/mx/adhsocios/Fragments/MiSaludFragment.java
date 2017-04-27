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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import csf.itesm.mx.adhsocios.Adapters.SaludAdapter;
import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.Requester;
import csf.itesm.mx.adhsocios.Utils.Parser;
import csf.itesm.mx.adhsocios.models.User;
import csf.itesm.mx.adhsocios.models.UserHealthRecord;
import csf.itesm.mx.adhsocios.models.UserResults;
import io.realm.Realm;

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

    public static MiSaludFragment newInstance()
    {
        MiSaludFragment fragment = new MiSaludFragment();
        //fragment.setArguments( Parser.UserToBundle(u) );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity();
        mUser = Realm.getDefaultInstance().where(User.class).findFirst();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_mi_salud, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_salud);
        mSaludAdapter = new SaludAdapter(CONTEXT,new ArrayList<UserHealthRecord>());
        mRecyclerView.setLayoutManager( new LinearLayoutManager(CONTEXT) );
        mRecyclerView.setAdapter( mSaludAdapter );
        if (mUser.isProd()) loadSalud();
        else                loadOtherSalud();
        return view;
    }

    void loadOtherSalud()
    {
        String url = mUser.getHost() + String.format(ep_getExpediente,mUser.getAssociateId(),mUser.getCompanyid());
        Log.d(TAG,url);
        JsonObjectRequest getResponse = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.d(TAG,response.toString());
                mSaludAdapter.addRecords( Parser.parseUserRecordsUbiquitous(response)  );
            }
        } ,new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic amF2aWVyOjEyMw=="); //BIEN NACO HARDCODEADO
                return headers;
            }
        };

        Requester.getInstance().addToRequestQueue(getResponse);
    }

    void loadSalud()
    {
        String url = mUser.getHost() + String.format(ep_getExpediente,mUser.getAssociateId(),mUser.getCompanyid());
        Log.d(TAG,url);

        JsonArrayRequest setPassword = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                mSaludAdapter.addRecords( Parser.parseUserRecords(response) );
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
