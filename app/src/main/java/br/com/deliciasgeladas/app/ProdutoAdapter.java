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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by 16254855 on 27/11/2017.
 */

public class ProdutoAdapter extends ArrayAdapter<Produto> {
    public ProdutoAdapter(Context context, ArrayList<Produto> lstProdutos){
        super(context, 0, lstProdutos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_item,null);
        }

        Produto produto = getItem(position);

        TextView txtProd = (TextView)v.findViewById(R.id.txtNomeProd);
        TextView txtPreco = (TextView)v.findViewById(R.id.txtPreco);
        TextView txtDesc = (TextView)v.findViewById(R.id.txtDesc);

        double precoSuco;
        if (produto.promocao){
            precoSuco = produto.getPreco() - (produto.getPreco()*produto.getDesconto()/100);
        }else{
            precoSuco = produto.getPreco();
        }

        //Converte para real
        Locale l = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(l);

        String preco = nf.format(precoSuco);

        ImageView imgImagem = (ImageView) v.findViewById(R.id.imgProduto);

        //Trabalhando com Picasso
        String teste = "http://10.0.2.2/inf3m/turmaB/Projeto%20-%20Delicias%20Geladas/cms/"+produto.getImagem();
        Picasso.with(getContext()).load(teste).into(imgImagem);

        txtProd.setText(produto.getNome());
        txtPreco.setText(preco);
        txtDesc.setText(produto.getDescricao());

        return v;
    }
}
