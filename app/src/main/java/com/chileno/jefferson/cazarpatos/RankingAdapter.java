package com.chileno.jefferson.cazarpatos;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// La clase hereda de RecyclerView.Adapter y especifica un ViewHolder genérico
public class RankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Player> dataSet;
    // Constantes para los tipos de vista (Header y Item)
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    // Constructor de la clase
    public RankingAdapter(ArrayList<Player> dataSet) {
        this.dataSet = dataSet;
    }

    // ViewHolder para la cabecera
    public static class ViewHolderHeader extends RecyclerView.ViewHolder {
        final TextView textViewPosicion;
        final TextView textViewPatosCazados;
        final TextView textViewUsuario;

        public ViewHolderHeader(View view) {
            super(view);
            textViewPosicion = view.findViewById(R.id.textViewPosicion);
            textViewPatosCazados = view.findViewById(R.id.textViewPatosCazados);
            textViewUsuario = view.findViewById(R.id.textViewUsuario);
        }
    }

    // ViewHolder para los elementos normales de la lista
    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        final TextView textViewPosicion;
        final TextView textViewPatosCazados;
        final TextView textViewUsuario;

        public ViewHolderItem(View view) {
            super(view);
            textViewPosicion = view.findViewById(R.id.textViewPosicion);
            textViewPatosCazados = view.findViewById(R.id.textViewPatosCazados);
            textViewUsuario = view.findViewById(R.id.textViewUsuario);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // La primera posición (0) es la cabecera
        if (position == 0) {
            return TYPE_HEADER;
        }
        // El resto son elementos normales
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout correspondiente según el tipo de vista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_list, parent, false);
        if (viewType == TYPE_HEADER) {
            return new ViewHolderHeader(view);
        }
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Configura la vista según si es una cabecera o un elemento
        if (holder.getItemViewType() == TYPE_HEADER) {
            ViewHolderHeader headerHolder = (ViewHolderHeader) holder;
            headerHolder.textViewPosicion.setText("#");
            headerHolder.textViewPatosCazados.setText("Patos");
            headerHolder.textViewUsuario.setText("Usuario");

            // Aplicar estilo de subrayado y color
            headerHolder.textViewPosicion.setPaintFlags(headerHolder.textViewPosicion.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            headerHolder.textViewPatosCazados.setPaintFlags(headerHolder.textViewPatosCazados.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            headerHolder.textViewUsuario.setPaintFlags(headerHolder.textViewUsuario.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            int colorPrimaryDark = holder.itemView.getContext().getResources().getColor(R.color.colorPrimaryDark, null);
            headerHolder.textViewPosicion.setTextColor(colorPrimaryDark);
            headerHolder.textViewPatosCazados.setTextColor(colorPrimaryDark);
            headerHolder.textViewUsuario.setTextColor(colorPrimaryDark);

        } else {
            ViewHolderItem itemHolder = (ViewHolderItem) holder;
            // Obtenemos el jugador correspondiente. Se resta 1 por la cabecera.
            Player player = dataSet.get(position - 1);

            itemHolder.textViewPosicion.setText(String.valueOf(position));
            itemHolder.textViewPatosCazados.setText(String.valueOf(player.getHuntedDucks()));
            itemHolder.textViewUsuario.setText(player.getUsername());
        }
    }

    @Override
    public int getItemCount() {
        // El tamaño total es el número de jugadores más 1 (la cabecera)
        return dataSet.size() + 1;
    }
}
