package csf.itesm.mx.adhsocios.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        //Date d = records.get(position).getDate();
        //holder.description.setText( records.get(position).getDescription() );
        holder.title.setText( estudios.get(position).getName() );
        //holder.date.setText(d!=null?String.format("%1$tY-%1$tm-%1$td",d) :"");
    }

    @Override
    public int getItemCount()
    {
        return estudios.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView description;
        protected TextView title;
        protected TextView date;


        public ViewHolder(View view)
        {
            super(view);
            description = (TextView) view.findViewById( R.id.estudio_description);
            title = (TextView) view.findViewById( R.id.estudio_title);
            date= (TextView) view.findViewById( R.id.estudio_date);
        }
    }

}

