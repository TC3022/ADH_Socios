package csf.itesm.mx.adhsocios.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import android.support.v4.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csf.itesm.mx.adhsocios.Adapters.EstudiosAdapter;
import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.Requester;
import csf.itesm.mx.adhsocios.Utils.Parser;
import csf.itesm.mx.adhsocios.models.Estudio;
import csf.itesm.mx.adhsocios.models.User;
import io.realm.Realm;

/**
 * Created by rubcuadra on 4/22/17.
 */

public class MisEstudiosFragment extends Fragment
{
    private User mUser;

    private static String TAG="MisEstudiosFragment";
    private Activity CONTEXT;
    private static final String ep_getEstudios="GetControlChecKAssociate?associateId=%s&companyId=%s";
    private RecyclerView mRecyclerView;
    private EstudiosAdapter mEstudiosAdapter;
    private OnMisEstudiosInteractionListener mListener;

    public MisEstudiosFragment() {}

    public static MisEstudiosFragment newInstance()
    {
        MisEstudiosFragment fragment = new MisEstudiosFragment();
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
        View view = inflater.inflate(R.layout.fragmento_mis_estudios, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_estudios);
        mEstudiosAdapter = new EstudiosAdapter(CONTEXT,new ArrayList<Estudio>());
        mRecyclerView.setLayoutManager( new LinearLayoutManager(CONTEXT) );
        mRecyclerView.setAdapter( mEstudiosAdapter );

        if (mUser.isProd()) loadEstudios();
        else                loadOtherEstudios();

        return view;
    }

    void loadOtherEstudios() //TODO HACER QUE LE PEGUE AL ENDPOINT CORRECTO Y USAR EL PARSER DONDE SE DEBE
    {
        String resp_string = "{\n" +
                "  \"success\": true,\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"title\": \"Sangre\",\n" +
                "      \"date\": \"2017-02-09T12:20:20\"\n" +
                "    }, \n" +
                "    {\n" +
                "      \"title\": \"Ego\",\n" +
                "      \"date\": null\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        try
        {
            JSONObject response = new JSONObject(resp_string);
            Log.d(TAG,response.toString());
            mEstudiosAdapter.addEstudio( Parser.parseEstudiosUbiquitous(response)  );
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    void loadEstudios()
    {
        String url = mUser.getHost() + String.format(ep_getEstudios,mUser.getAssociateId(),mUser.getCompanyid());
        Log.d(TAG,url);
        JsonArrayRequest setPassword = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                mEstudiosAdapter.addEstudio( Parser.parseEstudios(response) );
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
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnMisEstudiosInteractionListener)
        {
            mListener = (OnMisEstudiosInteractionListener) context;
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

    public interface OnMisEstudiosInteractionListener
    {
        void OnMisEstudiosInteraction();
    }
}
