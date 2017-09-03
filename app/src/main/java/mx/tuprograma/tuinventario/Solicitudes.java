package mx.tuprograma.tuinventario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Solicitudes extends AppCompatActivity {

    private String[] opciones = {"Nueva"};
    private ListView listaSolicitudes;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);

        listaSolicitudes = (ListView)findViewById(R.id.listaSolicitudes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, opciones);
        listaSolicitudes.setAdapter(adapter);

        listaSolicitudes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                System.out.println(listaSolicitudes.getItemAtPosition(i));
                if(listaSolicitudes.getItemAtPosition(i).toString().matches("Nueva")){
                    System.out.println("Entra a nueva");
                    intent = new Intent(Solicitudes.this, solicitudNueva.class);
                    startActivity(intent);
                }else if(listaSolicitudes.getItemAtPosition(i).toString().matches("Anteriores")){
                    System.out.println("Entra a anteriores");
                }
            }
        });

    }
}
