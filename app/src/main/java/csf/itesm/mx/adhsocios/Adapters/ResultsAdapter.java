package csf.itesm.mx.adhsocios.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.models.Result;

/**
 * Created by rubcuadra on 2/20/17.
 */

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder>
{
    private List<Result> results;
    private Activity activity;

    public ResultsAdapter(Activity activity, List<Result> res)
    {
        this.results = res;
        this.activity = activity;
    }

    public void addResults(List<Result> res)
    {
        this.results.addAll(results);
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
        Result current = results.get(position);
        holder.title.setText(current.getTitle());
        //Hacer desmadres
    }

    @Override
    public int getItemCount()
    {
        return results.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView title;

        public ViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById( R.id.result_title);
        }
    }

}
