package br.com.deliciasgeladas.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by 16254855 on 11/12/2017.
 */

public class LojaAdapter extends ArrayAdapter<Loja>{
    public LojaAdapter(Context context, ArrayList<Loja> lstLoja){
        super (context, 0, lstLoja);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_loja,null);
        }

        Loja l = getItem(position);

        TextView txtNomeL = (TextView)v.findViewById(R.id.txtNomeL);
        TextView txtEnd = (TextView)v.findViewById(R.id.txtEnd);
        TextView txtHr = (TextView)v.findViewById(R.id.txtHr);
        TextView txtTel = (TextView)v.findViewById(R.id.txtTel);

        ImageView imgImagem = (ImageView) v.findViewById(R.id.imgLoja);

        //Trabalhando com Picasso
        String teste = "http://10.0.2.2/inf3m/turmaB/Projeto%20-%20Delicias%20Geladas/cms/"+l.getImagem();
        Picasso.with(getContext()).load(teste).into(imgImagem);

        txtNomeL.setText(l.getNome());
        txtEnd.setText(l.getLogradouro());
        txtHr.setText(l.getHorario());
        txtTel.setText(l.getTelefone());

        return v;
    }
}
