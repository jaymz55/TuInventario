package mx.tuprograma.tuinventario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.R.id.list;

public class Menu extends AppCompatActivity {

    private String[] opciones = {"Solicitudes","Escanear"};
    private ListView listaMenu;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        listaMenu = (ListView)findViewById(R.id.lista);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, opciones);
        listaMenu.setAdapter(adapter);

        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                System.out.println(listaMenu.getItemAtPosition(i));
                if(listaMenu.getItemAtPosition(i).toString().matches("Solicitudes")){
                    System.out.println("Entra a solicitudes");
                    intent = new Intent(Menu.this, Solicitudes.class);
                    startActivity(intent);
                } else if(listaMenu.getItemAtPosition(i).toString().matches("Escanear")){
                    System.out.println("Entra a barras");
                    intent = new Intent(Menu.this, Scan_opciones.class);
                    startActivity(intent);
                } //Agrego las otras opciones
            }
        });

    }

    public void ver (View v, String custid) {
        Intent i=new Intent(this,Menu.class);
        i.putExtra("custid", custid);
        startActivity(i);
    }

}
