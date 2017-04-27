package csf.itesm.mx.adhsocios.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.Requester;
import csf.itesm.mx.adhsocios.Utils.Parser;
import csf.itesm.mx.adhsocios.models.ResultPackage;
import csf.itesm.mx.adhsocios.models.User;
import csf.itesm.mx.adhsocios.models.UserResults;
import io.realm.Realm;

public class MisResultadosFragment extends Fragment
{
    @BindView(R.id.grafica_bmi) CombinedChart gbmi;
    @BindView(R.id.grafica_fat) CombinedChart gfat;
    @BindView(R.id.grafica_muscle) CombinedChart gmuscle;
    @BindView(R.id.grafica_weight) CombinedChart gweight;

    private User mUser;
    private Activity CONTEXT;
    private static String TAG="MisResultadosFragment";
    private static final String ep_getResults="GetMyResults?associateId=%s&companyId=%s";
    private onMisResultadosInteractionListener mListener;
    private Unbinder unbinder;

    private String[] mMonths;

    public MisResultadosFragment() {}

    public static MisResultadosFragment newInstance()
    {
        MisResultadosFragment fragment = new MisResultadosFragment();
        //fragment.setArguments( Parser.UserToBundle(u) );
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
        mUser = Realm.getDefaultInstance().where(User.class).findFirst();
        mMonths = getResources().getStringArray(R.array.months);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_mis_resultados, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (mUser.isProd()) loadResults();
        else                loadOtherResults();
        return view;
    }

    void loadOtherResults()
    {
        String url = mUser.getHost() + String.format(ep_getResults,mUser.getAssociateId(),mUser.getCompanyid());
        Log.d(TAG,url);
        JsonObjectRequest getResponse = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                UserResults ur = Parser.parseUserResultsUbiquitos(response);
                setCombinedChart( getString(R.string.bmi) ,ur.getBmi() , gbmi );
                setCombinedChart( getString(R.string.fat),ur.getFat() , gfat );
                setCombinedChart( getString(R.string.muscle),ur.getMuscle() , gmuscle);
                setCombinedChart( getString(R.string.weight),ur.getWeight() , gweight );

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

    void loadResults()
    {
        String url = mUser.getHost() + String.format(ep_getResults,mUser.getAssociateId(),mUser.getCompanyid());
        Log.d(TAG,url);
        JsonArrayRequest setPassword = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                UserResults ur = Parser.parseUserResults(response);

                setCombinedChart( getString(R.string.bmi) ,ur.getBmi() , gbmi );
                setCombinedChart(getString(R.string.fat),ur.getFat() , gfat );
                setCombinedChart(getString(R.string.muscle),ur.getMuscle() , gmuscle);
                setCombinedChart(getString(R.string.weight),ur.getWeight() , gweight );
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

    private LineData generateLineData(String t, List<ResultPackage> lrp) //LRP NO debe estar vacio o tronara, eso se valida antes de enviar a llamar la funcion
    {
        LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<Entry>();

        int initialYear = lrp.get(0).getDate().getYear(); // + 1900 y es la fecha real
        for (int j = 0; j < lrp.size() ; j++)
            entries.add(new Entry(((lrp.get(j).getDate().getYear()-initialYear)*12) + lrp.get(j).getDate().getMonth()+1, (float) lrp.get(j).getValue()));

        LineDataSet set = new LineDataSet(entries, t );
        set.setColor(  ContextCompat.getColor(CONTEXT,R.color.colorAccent) );
        set.setLineWidth(2.5f);
        set.setCircleColor( Color.rgb(240, 238, 70));
        set.setCircleRadius(4f);
        set.setFillColor(   Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(0,0,0));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }


    private void setCombinedChart(String s, final List<ResultPackage> lrp, CombinedChart chrt)
    {
        chrt.getDescription().setEnabled(false);
        chrt.setBackgroundColor(Color.WHITE);
        chrt.setDrawGridBackground(false);
        chrt.setDrawBarShadow(false);
        chrt.setHighlightFullBarEnabled(false);

        Legend l = chrt.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        //AXIS
        chrt.getAxisRight().setEnabled(false);//Quitar RIGHT

        YAxis leftAxis = chrt.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)


        XAxis xAxis = chrt.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //Only Bottom
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                if ( value % mMonths.length == 0.0f ) //Nuestro comodin para el year
                    return String.valueOf(lrp.get(0).getDate().getYear()+1900+  (int)(value/mMonths.length));
                return mMonths[(int) value % mMonths.length];
            }
        });

        CombinedData data = new CombinedData();
        if (!lrp.isEmpty()) data.setData( generateLineData(s,lrp) );

        //data.setValueTypeface(mTfLight);
        xAxis.setAxisMaximum(data.getXMax() + 1f);

        chrt.setData(data);
        chrt.invalidate(); //Refresh
    }

    @Override
    public void onAttach(Context context)
    {
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
