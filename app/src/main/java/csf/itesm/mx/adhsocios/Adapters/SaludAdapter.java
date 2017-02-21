package csf.itesm.mx.adhsocios.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.models.UserRecord;

/**
 * Created by rubcuadra on 2/20/17.
 */

public class SaludAdapter extends RecyclerView.Adapter<SaludAdapter.ViewHolder>
{
    private List<UserRecord> records;
    private Activity activity;

    public SaludAdapter(Activity activity, List<UserRecord> res)
    {
        this.records = res;
        this.activity = activity;
    }

    public void addRecords(List<UserRecord> recs )
    {
        this.records.addAll(recs);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_misalud,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.description.setText( records.get(position).getDescription() );
    }

    @Override
    public int getItemCount()
    {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView description;


        public ViewHolder(View view)
        {
            super(view);
            description = (TextView) view.findViewById( R.id.salud_description);
        }
    }

}

