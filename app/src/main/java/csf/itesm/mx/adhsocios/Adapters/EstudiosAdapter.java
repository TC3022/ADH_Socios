package csf.itesm.mx.adhsocios.Adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import csf.itesm.mx.adhsocios.R;
import csf.itesm.mx.adhsocios.models.Estudio;

/**
 * Created by rubcuadra on 4/22/17.
 */

public class EstudiosAdapter extends RecyclerView.Adapter<EstudiosAdapter.ViewHolder>
{
    private List<Estudio> estudios;
    private Activity activity;

    public EstudiosAdapter(Activity activity, List<Estudio> res)
    {
        this.estudios = res;
        this.activity = activity;
    }

    public void addEstudio(List<Estudio> recs )
    {
        this.estudios.addAll(recs);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_miestudio,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Estudio c = estudios.get(position);

        holder.title.setText( c.getName() );
        holder.id.setText( c.getId() );

        if (c.isDone())
        {
            holder.date.setText(String.format("%1$tY-%1$tm-%1$td", c.getApplied()));
        }
        else
        {
            //android:textColor="@android:color/holo_green_light"
            holder.id.setTextColor( ContextCompat.getColor(activity, android.R.color.holo_red_light ) );
            holder.date.setText( activity.getResources().getString( R.string.pendingTest )   );
            //holder.date.setVisibility( View.INVISIBLE );
        }

    }

    @Override
    public int getItemCount()
    {
        return estudios.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView id;
        protected TextView title;
        protected TextView date;


        public ViewHolder(View view)
        {
            super(view);
            id = (TextView) view.findViewById( R.id.estudio_id);
            title = (TextView) view.findViewById( R.id.estudio_title);
            date= (TextView) view.findViewById( R.id.estudio_applied_date);
        }
    }

}

