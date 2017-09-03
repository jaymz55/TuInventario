package mx.tuprograma.tuinventario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import Facade.Facade;
import conexiones.JSONfunctions;
import conf.Conf;

public class Scan_material_alta extends AppCompatActivity {

        private Button btnEscanear;
        private Button btnRegistrar;
        private TextView textMaterial;
        private Facade facade;
        private String custid;
        private JSONObject respuesta;
        private Spinner material;
        private String[] materiales;
        private String[] ids;
        private JSONfunctions json;
        private JSONArray api;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_material_scan_alta);

            SharedPreferences precargado = getSharedPreferences("usuario", Context.MODE_PRIVATE);
            custid = precargado.getString("custid","");
            textMaterial = (TextView)findViewById(R.id.textMaterial);
                //Para test
                //textMaterial.setText("9999");


            material = (Spinner)findViewById(R.id.material);
            material.setVisibility(View.INVISIBLE);

            //Cargo la lista de materiales
                json = new JSONfunctions();

                try {
                    api = new JSONArray(json.getJSON("http://"+ Conf.obtenerApi()+"/inventario/rest/materiales/custid/" + custid, 20000));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                materiales = json.getStringArray(api.toString(), "nombre");
                ids = json.getStringArray(api.toString(), "idMaterial");

                ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, materiales);

                material.setAdapter(adaptador);

            btnEscanear = (Button)findViewById(R.id.btnEscanear);
            btnRegistrar = (Button)findViewById(R.id.btnEnviar);
                btnRegistrar.setVisibility(View.INVISIBLE);

            final Activity activity = this;
            btnEscanear.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view){
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                    integrator.setOrientationLocked(false);
                }

            });


            //Boton enviar
            btnRegistrar.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    facade = new Facade(custid);

                    //Llamo a la API para obtener info
                    if(facade.registrarBarcode(ids[buscarPosicionArray(materiales, material.getSelectedItem().toString())], textMaterial.getText().toString())){
                        //textMaterial.setText("* Material *");
                        material.setVisibility(View.INVISIBLE);
                        btnRegistrar.setVisibility(View.INVISIBLE);
                        textMaterial.setTextColor(Color.BLACK);
                        textMaterial.setText("Material registrado correctamente");
                    }else{
                        //textMaterial.setText("* Material *");
                        material.setVisibility(View.INVISIBLE);
                        btnRegistrar.setVisibility(View.INVISIBLE);
                        textMaterial.setTextColor(Color.RED);
                        textMaterial.setText("Error al conectar, contacte al administrador");
                    }

                }
            });

            //Aviso sobre funcionamiento en horizontal
            Toast.makeText(this, "Opción sólo con pantalla horizontal", Toast.LENGTH_LONG).show();

        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null){

                if(result.getContents()==null){
                    Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
                }else{
                    textMaterial.setTextColor(Color.BLACK);
                    textMaterial.setText(result.getContents());

                    //Hago visible Material
                    material.setVisibility(View.VISIBLE);
                    btnRegistrar.setVisibility(View.VISIBLE);
                }

            }else{
                super.onActivityResult(requestCode, resultCode, data);
            }

        }




    //Metodos externos
    private int buscarPosicionArray(String[] array, String texto){
        return Arrays.asList(array).indexOf(texto);
    }

}
