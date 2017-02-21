package csf.itesm.mx.adhsocios.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.models.UserResults;

/**
 * Created by rubcuadra on 2/20/17.
 */

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder>
{
    private UserResults resultActivities;
    private Activity activity;

    public ResultsAdapter(Activity activity, UserResults res)
    {
        this.resultActivities = res;
        this.activity = activity;
    }

    public void setResults(UserResults res)
    {
        this.resultActivities = res;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_result,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        if (resultActivities==null) return;
        switch (position)
        {
            case 0: //Fat?
                holder.title.setText( String.valueOf( resultActivities.getFat().get(0).getValue() ) );
                break;
            default:
                holder.title.setText( String.valueOf( resultActivities.getBmi().get(0).getValue() ) );
                break;
        }
        //Hacer desmadres
    }

    @Override
    public int getItemCount()
    {
        return resultActivities.getAmountOfResults();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView title;

        public ViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById( R.id.result_activity_name);
        }
    }

}
