package br.bonilha.buscaescola;


import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaAdapter  extends RecyclerView.Adapter {

    private Context context;

    public ListaAdapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.formato_item_lista, parent, false);
        return new ListaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ListaViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatar;
        public TextView nome;
        public TextView email;

        public ListaViewHolder(View v) {
            super(v);

            avatar = (ImageView) v.findViewById(R.id.imgAvatar);
            nome = (TextView) v.findViewById(R.id.txtNome);
            email = (TextView) v.findViewById(R.id.textEmail);

        }
    }
}