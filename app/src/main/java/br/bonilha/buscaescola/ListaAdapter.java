package br.bonilha.buscaescola;


import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListaAdapter  extends RecyclerView.Adapter<ListaAdapter.ListaViewHolder> {

    private Context context;
    private ArrayList<Contato> modelo;

    public ListaAdapter(Context context, ArrayList<Contato> modelo) {
        this.context = context;
        this.modelo = modelo;
    }


    @Override
    public ListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.formato_item_lista, parent, false);
        return new ListaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListaViewHolder holder, int position) {
        Contato c = modelo.get(position);
        holder.nome.setText(c.getNome());
        holder.email.setText(c.getEmail());

    }

    @Override
    public int getItemCount() {
        return modelo.size();
    }

    public class ListaViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatar;
        public TextView nome;
        public TextView email;

        public ListaViewHolder(View v) {
            super(v);

            avatar = (ImageView) v.findViewById(R.id.imgAvatar);
            nome = (TextView) v.findViewById(R.id.txtNome);
            email = (TextView) v.findViewById(R.id.txtEmail);

        }
    }
}