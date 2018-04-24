package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pat.sd.duomaya.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Clases.Leccion;
import Interfaces.CustomItemClickListener;

/**
 * Created by danie on 21/04/2018.
 */

public class LeccionesAdapter extends RecyclerView.Adapter<LeccionesAdapter.LeccionViewHolder> implements View.OnClickListener {

    Context context;
    ArrayList<Leccion> Lecciones;
    private CustomItemClickListener listener;

    public LeccionesAdapter(Context context, ArrayList<Leccion> Lecciones, CustomItemClickListener listener)
    {
        this.context = context;
        this.Lecciones = Lecciones;
        this.listener = listener;
    }

    @Override
    public LeccionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_leccion,parent, false);
        final LeccionViewHolder holder = new LeccionViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(view, holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(LeccionViewHolder holder, int position) {
        Picasso.get().load(Lecciones.get(position).Imagen).into(holder.imgLeccion);
        holder.txtNombreLec.setText(Lecciones.get(position).Nombre);



    }


    @Override
    public int getItemCount() {
        return Lecciones.size();
    }

    @Override
    public void onClick(View view) {

    }

    public static class LeccionViewHolder extends RecyclerView.ViewHolder{

        TextView txtNombreLec;
        ImageView imgLeccion;

        public LeccionViewHolder(View itemView) {
            super(itemView);
            txtNombreLec = itemView.findViewById(R.id.txtNombreLec);
            imgLeccion = itemView.findViewById(R.id.imgLeccion);
        }
    }

}
