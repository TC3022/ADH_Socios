package csf.itesm.mx.adhsocios.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import csf.itesm.mx.adhsocios.Adapters.ResultsAdapter;
import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.Requester;
import csf.itesm.mx.adhsocios.Utils.Parser;
import csf.itesm.mx.adhsocios.models.User;

public class MisResultadosFragment extends Fragment
{
    @BindView(R.id.grafica_bmi) LineChart gbmi;
    @BindView(R.id.grafica_fat) LineChart gfat;
    @BindView(R.id.grafica_muscle) LineChart gmuscle;
    @BindView(R.id.grafica_weight) LineChart gweight;

    private User mUser;
    private Activity CONTEXT;
    private static String TAG="MisResultadosFragment";
    private static final String ep_getResults="GetMyResults?associateId=%s&companyId=%s";
    private onMisResultadosInteractionListener mListener;
    private Unbinder unbinder;

    public MisResultadosFragment() {}

    public static MisResultadosFragment newInstance(User u)
    {
        MisResultadosFragment fragment = new MisResultadosFragment();
        fragment.setArguments( Parser.UserToBundle(u) );
        return fragment;
    }

    @Override public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
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
        unbinder = ButterKnife.bind(this, view);
        loadResults();
        loadGrafica();
        return view;
    }
    void loadGrafica()
    {
        List<Entry> e = new ArrayList<>();

        for (int i = 0; i< 12;++i)
        {
            e.add(new BarEntry(i,i*i));
        }

        LineDataSet ds = new LineDataSet(e,"# Calls");

        ds.enableDashedLine(10f, 5f, 0f);
        ds.enableDashedHighlightLine(10f, 5f, 0f);
        ds.setColor(Color.BLUE);
        ds.setCircleColor(Color.BLACK);
        ds.setLineWidth(1f);
        ds.setCircleRadius(3f);
        ds.setDrawCircleHole(false);
        ds.setValueTextSize(9f);
        //ds.setDrawFilled(true);
        ds.setFormLineWidth(1f);
        ds.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        ds.setFormSize(15.f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(ds); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);
        // set data
        gbmi.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        gbmi.setData(data);
    }
    void loadResults()
    {

        String url = mUser.getHost() + String.format(ep_getResults,mUser.getAssociateId(),mUser.getCompanyid());
        Log.d(TAG,url);
        JsonArrayRequest setPassword = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {

                Log.d(TAG,response.toString());
                //mResultsAdapter.setResults(Parser.parseUserResults(response));
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
