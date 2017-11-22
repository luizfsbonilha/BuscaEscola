package br.bonilha.buscaescola;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListaViewHolder> {

    private Context context;
    private ArrayList<Escola> modelo;

    public ListaAdapter(Context context, ArrayList<Escola> modelo) {
        this.context = context;
        this.modelo = modelo;
    }

    @Override
    public ListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.formato_item_lista, parent, false);
        return new ListaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListaViewHolder holder, int i) {
        holder.nome.setText(modelo.get(i).getNome());
        holder.rede.setText(modelo.get(i).getRede());

    }
    @Override
    public int getItemCount() {
        return modelo.size();

    }

    public class ListaViewHolder extends RecyclerView.ViewHolder {

        private TextView nome,rede;

        public ListaViewHolder(View view) {
            super(view);

            // avatar = (ImageView) v.findViewById(R.id.imgAvatar);
            nome = (TextView) view.findViewById(R.id.txtNome);
            rede = (TextView) view.findViewById(R.id.txtRede);

        }
    }
}