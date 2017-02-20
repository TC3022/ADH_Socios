package csf.itesm.mx.adhsocios.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mi_salud, container, false);
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
