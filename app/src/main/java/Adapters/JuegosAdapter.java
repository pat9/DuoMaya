package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pat.sd.duomaya.R;

import java.util.ArrayList;

import Clases.Juego;
import Interfaces.CustomItemClickListener;

/**
 * Created by danie on 21/04/2018.
 */

public class JuegosAdapter extends RecyclerView.Adapter<JuegosAdapter.JuegosViewHolder> {

    Context context;
    ArrayList<Juego> Juegos;
    CustomItemClickListener listener;

    public JuegosAdapter(Context context, ArrayList<Juego> Juegos, CustomItemClickListener listener)
    {
        this.context = context;
        this.Juegos = Juegos;
        this.listener = listener;
    }

    @Override
    public JuegosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_juego, parent, false);
        final JuegosViewHolder holder = new JuegosViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(view, holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(JuegosViewHolder holder, int position) {
        holder.imgJuego.setImageResource(Juegos.get(position).Icono);
        holder.txtNombreJuego.setText(Juegos.get(position).Tipo + "-" + Juegos.get(position).Titulo);
    }

    @Override
    public int getItemCount() {
        return Juegos.size();
    }

    public static class JuegosViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtNombreJuego;
        ImageView imgJuego;

        public JuegosViewHolder(View itemView) {
            super(itemView);
            txtNombreJuego = itemView.findViewById(R.id.txtNombreJuego);
            imgJuego = itemView.findViewById(R.id.imgJuego);
        }
    }

}
