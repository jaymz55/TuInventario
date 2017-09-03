package mx.tuprograma.tuinventario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import Facade.Facade;
import conexiones.JSONfunctions;
import funciones.Funcion;

public class Scan_material_entrada extends AppCompatActivity {

    //Variables
        //Layouts
            private ScrollView mainScrollView;
            private LinearLayout layoutRegistro;

        //Botones
            private Button btnEscanear;
            private Button btnEnviar;

        //Textviews
            private TextView textTitulo;
            private TextView textMaterial;
            private TextView textCandidad;
            private TextView textFechaIngreso;

        //EditText
            private EditText cantidad;
            private EditText costoTotal;

        //Fechas
            private DatePicker fechaIngreso;
            private DatePicker fechaCaducidad;

        //Checks
            private CheckBox checkCaducidad;
            private CheckBox checkIva;
                boolean contieneIva = true;

        //Otros
            private Facade facade;
            private String custid;
            private JSONObject material;
            private JSONfunctions json = new JSONfunctions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_scan_entrada);

        SharedPreferences precargado = getSharedPreferences("usuario", Context.MODE_PRIVATE);

        elementosFormato(precargado);
        listeners();

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

                try {
                    material = facade.obtenerMaterial(result.getContents());

                    System.out.println(material.toString());

                    if(material.has("nombre")){
                        //textMaterial.setTextColor(Color.rgb(35, 88, 19));
                        textMaterial.setTextColor(Color.BLACK);
                        textMaterial.setText(material.getString("nombre"));
                        layoutRegistro.setVisibility(View.VISIBLE);
                        mainScrollView.fullScroll(ScrollView.FOCUS_UP);
                    }else{
                        textMaterial.setTextColor(Color.RED);
                        textMaterial.setText("Material no encontrado");
                        layoutRegistro.setVisibility(View.GONE);
                        mainScrollView.fullScroll(ScrollView.FOCUS_UP);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    //Metodos
    private void elementosFormato(SharedPreferences precargado){

        try {

            mainScrollView = (ScrollView) findViewById(R.id.scrollMain);

            custid = precargado.getString("custid", "");
            facade = new Facade(custid);
            cantidad = (EditText) findViewById(R.id.cantidad);
            cantidad.setFocusableInTouchMode(true);

            costoTotal = (EditText) findViewById(R.id.costo);
            textTitulo = (TextView) findViewById(R.id.textTitulo);
            textFechaIngreso = (TextView) findViewById(R.id.textFechaIngreso);
            layoutRegistro = (LinearLayout) findViewById(R.id.layoutRegistra);
            layoutRegistro.setVisibility(View.GONE);
            fechaIngreso = (DatePicker) findViewById(R.id.fechaIngreso);
            reiniciarFecha(fechaIngreso);
            fechaCaducidad = (DatePicker) findViewById(R.id.fechaCaducidad);
            fechaCaducidad.setFocusableInTouchMode(true);
            fechaCaducidad.setVisibility(View.GONE);

            checkCaducidad = (CheckBox) findViewById(R.id.checkCaducidad);
            checkIva = (CheckBox) findViewById(R.id.checkIva);

            textMaterial = (TextView) findViewById(R.id.textMaterial);
            textCandidad = (TextView) findViewById(R.id.textCantidad);

            btnEscanear = (Button) findViewById(R.id.btnEscanear);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void listeners(){

        try {

            final Activity activity = this;
            btnEscanear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                }

            });

            //Boton enviar
            btnEnviar = (Button) findViewById(R.id.btnEnviar);
            btnEnviar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    try {

                        //Revisar si estan completos los campos
                        if (revisarRegistro()) {

                            //Lleno el material
                            material.put("custid", custid);

                            material.put("fechaIngreso", Funcion.obtenerFecha(fechaIngreso.getYear(), fechaIngreso.getMonth(),
                                    fechaIngreso.getDayOfMonth()));
                            if (checkCaducidad.isChecked())
                                material.put("fechaCaducidad", Funcion.obtenerFecha(fechaCaducidad.getYear(), fechaCaducidad.getMonth(),
                                        fechaCaducidad.getDayOfMonth()));
                            material.put("cantidad", cantidad.getText().toString());
                            material.put("costoTotal", costoTotal.getText().toString());
                            if (checkIva.isChecked())
                                material.put("contieneIva", "SI");
                            else
                                material.put("contieneIva", "NO");


                            if (facade.registrarEntradaMaterial(material)) {
                                Toast.makeText(Scan_material_entrada.this, "Registro correcto", Toast.LENGTH_LONG);
                                borrarCampos();
                            } else {
                                Toast.makeText(Scan_material_entrada.this, "Error al registrar, contacte al administrador", Toast.LENGTH_LONG);
                            }

                        } else {
                            Toast.makeText(Scan_material_entrada.this, "Se deben ingresar todos los datos", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }

                }

            });

            //Check caducidad para mostrar el calendario
            checkCaducidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        fechaCaducidad.setVisibility(View.VISIBLE);
                    } else {
                        fechaCaducidad.setVisibility(View.GONE);
                    }
                }
            });

            //Check IVA
            checkIva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        if (isChecked) {
                                                            contieneIva = true;
                                                        } else {
                                                            contieneIva = false;
                                                        }
                                                    }

                                                }
            );

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean revisarRegistro(){

        if(cantidad.getText().toString().matches("") || costoTotal.getText().toString().matches("")){
            return false;
        }else{
            return true;
        }

    }

    private void borrarCampos(){

        try {
            textMaterial.setText("");
            cantidad.setText("");
            costoTotal.setText("");
            reiniciarFecha(fechaIngreso);
            reiniciarFecha(fechaCaducidad);
            checkCaducidad.setChecked(false);
            layoutRegistro.setVisibility(View.GONE);
            mainScrollView.fullScroll(ScrollView.FOCUS_UP);

        }catch(Exception e){
            Toast.makeText(this, "Error: "+e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private void reiniciarFecha(DatePicker dp){
        Time now = new Time();
        now.setToNow();

        //update the DatePicker
            dp.updateDate(now.year, now.month, now.monthDay);
    }
}
