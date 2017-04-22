package csf.itesm.mx.adhsocios.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.models.UserHealthRecord;

/**
 * Created by rubcuadra on 2/20/17.
 */

public class SaludAdapter extends RecyclerView.Adapter<SaludAdapter.ViewHolder>
{
    private List<UserHealthRecord> records;
    private Activity activity;

    public SaludAdapter(Activity activity, List<UserHealthRecord> res)
    {
        this.records = res;
        this.activity = activity;
    }

    public void addRecords(List<UserHealthRecord> recs )
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
        Date d = records.get(position).getDate();
        holder.description.setText( records.get(position).getDescription() );
        holder.title.setText( records.get(position).getTitle() );
        holder.date.setText(d!=null?String.format("%1$tY-%1$tm-%1$td",d) :"");
    }

    @Override
    public int getItemCount()
    {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView description;
        protected TextView title;
        protected TextView date;


        public ViewHolder(View view)
        {
            super(view);
            description = (TextView) view.findViewById( R.id.salud_description);
            title = (TextView) view.findViewById( R.id.salud_title);
            date= (TextView) view.findViewById( R.id.salud_date);
        }
    }

}

