package csf.itesm.mx.adhsocios.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.HashMap;
import java.util.Map;

import csf.itesm.mx.adhsocios.Adapters.ResultsAdapter;
import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.Requester;
import csf.itesm.mx.adhsocios.Utils.Parser;
import csf.itesm.mx.adhsocios.models.User;

public class MisResultadosFragment extends Fragment
{

    private User mUser;
    private Activity CONTEXT;
    private RecyclerView mRecyclerView;
    private ResultsAdapter mResultsAdapter;
    private static String TAG="MisResultadosFragment";
    private static final String ep_getResults="GetMyResults?associateId=%s&companyId=%s";
    private onMisResultadosInteractionListener mListener;

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
        View view = inflater.inflate(R.layout.fragment_mis_resultados, container, false);
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
                mResultsAdapter.setResults(Parser.parseUserResults(response));
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
        if (context instanceof onMisResultadosInteractionListener)
        {
            mListener = (onMisResultadosInteractionListener) context;
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


    public interface onMisResultadosInteractionListener
    {
        void onMisResultadosInteraction(Uri uri);
    }
}
