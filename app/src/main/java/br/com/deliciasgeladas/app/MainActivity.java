package br.com.deliciasgeladas.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ArrayAdapter<Produto> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView)findViewById(R.id.gridview);
        adapter = new ProdutoAdapter(this, new ArrayList<Produto>());

        gridView.setAdapter(adapter);

        //Preencher lista com API
        new AsyncTask<Void, Void, Void>(){

            ArrayList<Produto> lstProdutos = new ArrayList<Produto>();
            @Override
            protected Void doInBackground(Void... voids) {
                String jsonReturn = Http.get("http://10.0.2.2/inf3m/turmaB/Projeto%20-%20Delicias%20Geladas/api/selecionarProdutos.php");

                Log.d("TAG", jsonReturn);

                try {
                    JSONArray jsonArray = new JSONArray(jsonReturn);

                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject item = jsonArray.getJSONObject(i);

                        Produto p = Produto.create(item.getString("nome"), item.getDouble("preco"), item.getString("imagem"));
                        p.setId(item.getInt("id"));
                        p.setIngredientes(item.getString("ingredientes"));

                        if(item.getInt("promocao")==1){
                            p.setPromocao(true);
                            p.setDesconto(item.getInt("desconto"));
                        }else{
                            p.setPromocao(false);
                        }

                        p.setDescricao(item.getString("descricao"));

                        lstProdutos.add(p);
                    }

                    Log.d("TAG", lstProdutos.size()+"");
                } catch (Exception e) {
                    Log.e("ERROU:", e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.addAll(lstProdutos);
            }
        }.execute();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Produto prod = adapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), DetalhesProduto.class);

                intent.putExtra("produto", prod);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.lojas) {
            startActivity(new Intent(getApplicationContext(), LojasActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
