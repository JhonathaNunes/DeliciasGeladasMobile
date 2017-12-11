package br.com.deliciasgeladas.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LojasActivity extends AppCompatActivity implements DialogInterface.OnClickListener{

    ListView listView;
    ArrayAdapter<Loja> adapter;
    Uri uri;
    Double lat;
    Double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lojas);

        listView = (ListView) findViewById(R.id.lstLojas);
        adapter = new LojaAdapter(this, new ArrayList<Loja>());

        listView.setAdapter(adapter);

        new AsyncTask<Void, Void, Void>() {

            ArrayList<Loja> lstLojas = new ArrayList<Loja>();

            @Override
            protected Void doInBackground(Void... voids) {
                String jsonReturn = Http.get("http://10.0.2.2/inf3m/turmaB/Projeto%20-%20Delicias%20Geladas/api/selecionarLoja.php");

                Log.d("TAG", jsonReturn);

                try {
                    JSONArray jsonArray = new JSONArray(jsonReturn);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);

                        Loja l = Loja.criar(item.getInt("idLoja"), item.getString("nomeLoja"), item.getString("logradouro"), item.getString("telefone"),
                                item.getString("imagem"), item.getString("horario"), item.getDouble("latitude"), item.getDouble("longitude"));

                        lstLojas.add(l);
                    }

                    Log.d("TAG", lstLojas.size() + "");
                } catch (Exception e) {
                    Log.e("ERROU:", e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.addAll(lstLojas);
            }
        }.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Loja l = adapter.getItem(position);
                uri = Uri.parse("tel:" + l.getTelefone());
                criaAlertDialog().show();
                lat = l.lat;
                lon = l.lon;
            }
        });
    }

    private AlertDialog criaAlertDialog() {
        final CharSequence[] items = {
                "Ligar",
                "Ver no Mapa"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opções");
        builder.setItems(items, (DialogInterface.OnClickListener) this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case 0:

                Intent intent = new Intent(Intent.ACTION_CALL,uri);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(LojasActivity.this, new String[]{Manifest.permission.CALL_PHONE},10);
                    return;
                }else{
                    startActivity(intent);
                }
                break;
            case 1:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                startActivity(intent);
                break;
        }
    }
}
