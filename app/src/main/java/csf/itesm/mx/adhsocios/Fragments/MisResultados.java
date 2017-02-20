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

import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.Utils.Parser;
import csf.itesm.mx.adhsocios.models.User;

public class MisResultados extends Fragment
{

    private User mUser;
    private Activity CONTEXT;
    private RecyclerView mRecyclerView;
    private static String TAG="MisResultados";
    private onMiSaludInteraction mListener;
    public MisResultados() {}

    public static MisResultados newInstance(User u)
    {
        MisResultados fragment = new MisResultados();
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
            Log.d(TAG,mUser.getNmComplete());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_mi_salud, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_results);
        mResultsAdapter = new ResultsAdapter(CONTEXT,new ArrayList<Auto>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CONTEXT));
        mRecyclerView.setAdapter( mResultsAdapter );
        loadResults();
        return view;
    }

    void loadResults()
    {
        //Pegar al endpoint para obtener results
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
