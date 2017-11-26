package br.bonilha.buscaescola;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListaViewHolder> {

    private Context context;
    private List<Escola> modelo;

    public ListaAdapter(Context context, List<Escola> modelo) {
        this.context = context;
        this.modelo = modelo;
    }

    @Override
    public ListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.formato_item_lista, parent, false);
        return new ListaViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ListaViewHolder holder, int i) {
        Escola escola = modelo.get(i);
        holder.nome.setText(modelo.get(i).getNome());
        holder.rede.setText(modelo.get(i).getRede());
        holder.setEscola(escola);


    }

    @Override
    public int getItemCount() {
        return modelo.size();

    }

    public class ListaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public View layout;
        private TextView nome, rede;
        private Double latitude,longitude;
        private Context context;
        private Escola escola;

        public ListaViewHolder(View view, Context context) {
            super(view);
            view.setOnClickListener(this);
            layout = view;

            nome = (TextView) view.findViewById(R.id.txtNome);
            rede = (TextView) view.findViewById(R.id.txtRede);
            this.context = context;
        }
        public void setEscola(Escola escola) {
            this.escola = escola;
        }

        @Override
        public void onClick(View view) {
            if (escola != null){
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("escola", escola);
                context.startActivity(intent);
            }
        }


    }
}