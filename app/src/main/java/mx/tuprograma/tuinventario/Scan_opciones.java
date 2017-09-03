package mx.tuprograma.tuinventario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Scan_opciones extends AppCompatActivity {

    private String[] opciones = {"Material"};
    private ListView listaOpciones;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_opciones);

        listaOpciones = (ListView)findViewById(R.id.listaOpciones);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, opciones);
        listaOpciones.setAdapter(adapter);

        listaOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                System.out.println(listaOpciones.getItemAtPosition(i));
                if(listaOpciones.getItemAtPosition(i).toString().matches("Material")){
                    intent = new Intent(Scan_opciones.this, Scan_material_opciones.class);
                    startActivity(intent);
                }
            }
        });

    }
}
