package br.com.deliciasgeladas.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class DetalhesProduto extends AppCompatActivity {
    private static final String AVALIADO = "avaliado";
    private static final String PRODUTO = "produto";
    ImageView imgDetalhes;
    TextView txtNome, txtDescricao, txtIngredientes, txtPreco, txtAvaliacao;
    RatingBar ratingBar;
    Button btnAvaliacao;
    Produto produto;
    int idProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        imgDetalhes = (ImageView)findViewById(R.id.imagemDetalhada);
        txtNome = (TextView)findViewById(R.id.txtNmProduto);
        txtDescricao = (TextView)findViewById(R.id.txtDescricao);
        txtIngredientes = (TextView)findViewById(R.id.txtIngredientes);
        txtPreco = (TextView)findViewById(R.id.txtValor);
        txtAvaliacao = (TextView)findViewById(R.id.txtAvaliacao);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnAvaliacao = (Button)findViewById(R.id.btnAvaliar);

        Intent intent = getIntent();
        produto = (Produto) intent.getSerializableExtra("produto");
        idProduto = produto.getId();
        retornaAv(idProduto);

        SharedPreferences preferencias = getSharedPreferences(produto.getNome(), MODE_PRIVATE);
        boolean avaliado = preferencias.getBoolean(AVALIADO, false);
        int idProd = preferencias.getInt(PRODUTO, 0);

        if (avaliado == true && idProd == idProduto){
            btnAvaliacao.setVisibility(View.GONE);
            ratingBar.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        String url = "http://10.0.2.2/inf3m/turmaB/Projeto%20-%20Delicias%20Geladas/cms/"+produto.getImagem();
        Picasso.with(this).load(url).into(imgDetalhes);

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

        txtNome.setText(produto.getNome());
        txtDescricao.setText(produto.getDescricao());
        txtPreco.setText(preco);
        txtIngredientes.setText(produto.getIngredientes());

        //Rating Bar
        ratingBar.setRating(produto.getMedia());
        txtAvaliacao.setText(String.valueOf(ratingBar.getRating()));

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtAvaliacao.setText(String.valueOf(rating));
            }
        });
    }

    public void avaliarProduto(View view) {
        final String url = "http://10.0.2.2/inf3m/turmaB/Projeto%20-%20Delicias%20Geladas/api/inserirAv.php";

        final HashMap<String, String> valores = new HashMap<>();
        valores.put("avaliacao", ratingBar.getRating()+"");
        valores.put("idProduto", idProduto+"");

        new AsyncTask<Void, Void, Void>(){

            Boolean  sucesso = false;
            String mensagem = null;

            //Roda em 2nd plano
            @Override
            protected Void doInBackground(Void... voids) {
                String resultado = Http.post(url, valores);

                try{
                    //Transforma Json em objeto
                    JSONObject jsonObject = new JSONObject(resultado);

                    sucesso = jsonObject.getBoolean("sucesso");
                    mensagem = jsonObject.getString("mensagem");

                }catch (Exception ex){
                    ex.printStackTrace();
                    sucesso = false;
                    mensagem = "Erro ao inserir!";
                }
                return null;
            }
        }.execute();

        SharedPreferences preferencias = getSharedPreferences(txtNome.getText().toString(),MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean(AVALIADO, true);
        editor.putInt(PRODUTO, idProduto);
        editor.commit();

        finish();
    }

    private void retornaAv(int idProduto){

        final String url = "http://10.0.2.2/inf3m/turmaB/Projeto%20-%20Delicias%20Geladas/api/selecionarAvaliacao.php?idProduto="+idProduto;

        new AsyncTask<Void, Void, Void>(){
            //Roda em 2nd plano
            @Override
            protected Void doInBackground(Void... voids) {
                String resultado = Http.get(url);
                Log.d("TAG", resultado);
                try{
                    //Transforma Json em objeto
                    JSONObject item = new JSONObject(resultado);

                   produto.setMedia((float) item.getDouble("media"));

                }catch (Exception e){
                    Log.e("ERROU:", e.getMessage());
                }
                return null;
            }
        }.execute();
    }
}
