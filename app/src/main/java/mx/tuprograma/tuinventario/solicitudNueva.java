package mx.tuprograma.tuinventario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import apis.Solicitud;
import conexiones.JSONfunctions;
import conf.Conf;
import funciones.Funcion;
import tablas.Tabla;
import Facade.Facade;

public class solicitudNueva extends AppCompatActivity {

    private ScrollView mainScrollView;
    private Spinner producto;
    private EditText cantidad;
    String[] datos;
    String[] precios;
    String[] ids;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private DatePicker fecha;
    private EditText descuento;
    private EditText comentario;
    private TableLayout tabla;
    private Button btnAgregar;
    private Button btnDeshacer;
    private TextView total;
    private Button enviar;
    private String custid;
    private JSONArray api;
    private Facade facade;

    JSONfunctions json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_nueva);
        SharedPreferences precargado = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        custid = precargado.getString("custid","");
        producto = (Spinner)findViewById(R.id.producto);
        cantidad = (EditText)findViewById(R.id.cantidad);
            //cantidad.requestFocus();
        fecha = (DatePicker)findViewById(R.id.fecha);
            fecha.setFocusableInTouchMode(true);
        tabla = (TableLayout)findViewById(R.id.tabla);
        btnAgregar = (Button)findViewById(R.id.botonAgregar);
        btnDeshacer = (Button)findViewById(R.id.botonEliminar);
        total = (TextView)findViewById(R.id.textTotal);
        descuento = (EditText)findViewById(R.id.descuento);
        enviar = (Button)findViewById(R.id.enviar);

        facade = new Facade(custid);

        mainScrollView = (ScrollView)findViewById(R.id.scrollMain);

        //listener de descuento
        descuento.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {

                if(!descuento.getText().toString().matches("")){
                    total.setText(Funcion.descuento(actualizarTotal(tabla), descuento.getText()));
                }else{
                    System.out.println("Entra a vacio");
                    total.setText(Funcion.formatoPesos(actualizarTotal(tabla)));
                }

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        comentario = (EditText)findViewById(R.id.comentario);


        //Cargo info de API
        json = new JSONfunctions();

        //Obtengo Json con info
        try {
            api = new JSONArray(json.getJSON("http://www.tuprograma.mx/inventario/rest/productos/custid/" + custid, 20000));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        datos = json.getStringArray(api.toString(), "nombre");
        precios = json.getStringArray(api.toString(), "total");
        ids = json.getStringArray(api.toString(), "id");

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, datos);
        producto.setAdapter(adaptador);

        btnAgregar.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(producto.getSelectedItem().toString().matches("") || cantidad.getText().toString().matches("")){
                    Toast notification;
                    notification = Toast.makeText(solicitudNueva.this, "Debes ingresar los datos completos", Toast.LENGTH_LONG);
                    notification.show();
                    notification = null;
                }else{

                    //Obtengo precio
                    int ubicacion = producto.getSelectedItemPosition();

                    agregar(tabla, ids[buscarPosicionArray(datos, producto.getSelectedItem().toString())], cantidad.getText().toString(), producto.getSelectedItem().toString(), String.valueOf(formatter.format(Double.parseDouble(precios[ubicacion])*Double.parseDouble(cantidad.getText().toString()))));
                    cantidad.setText("");
                }

                //mainScrollView.fullScroll(ScrollView.FOCUS_UP);

            }
        });

        btnDeshacer.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(tabla.getChildCount() <= 1){
                    Toast notification;
                    notification = Toast.makeText(solicitudNueva.this, "La tabla está vacía...", Toast.LENGTH_LONG);
                    notification.show();
                    notification = null;
                }else{
                    deshacer(tabla);
                }

            }
        });

        enviar.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                JSONArray array = new JSONArray();
                Solicitud sol;
                Tabla table;

                try {


                    table = new Tabla(tabla);
                    array = table.generarArrayDeTable(new String[]{"id", "cantidad", "producto", "precio"}, 4, 4);

                    sol = new Solicitud(custid, Funcion.obtenerFecha(fecha.getYear(), fecha.getMonth(), fecha.getDayOfMonth()),
                            (descuento.getText().toString().matches(""))?"0.0":descuento.getText().toString(),
                            URLEncoder.encode(comentario.getText().toString(), "UTF-8"), array);

                    //Envio solicitud

                    if(facade.registrarNuevaSolicitud(sol)){
                        Toast notification;
                        notification = Toast.makeText(solicitudNueva.this, "Solicitud registrada correctamente", Toast.LENGTH_LONG);
                        notification.show();
                        notification = null;

                        //Borro datos
                        descuento.setText("");
                        comentario.setText("");
                        borrarTabla(tabla);

                    }else{
                        Toast notification;
                        notification = Toast.makeText(solicitudNueva.this, "Error en la conexión, contacte al administrador", Toast.LENGTH_LONG);
                        notification.show();
                        notification = null;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } finally{
                    array = null;
                    sol = null;
                }


            }
        });

    }

    public void agregar(TableLayout tabla, String id, String cantidad, String producto, String precio){

        TableRow tr1;
        TextView x;
        TextView a;
        TextView b;
        TextView c;
        TableRow.LayoutParams params;

        try {

            tr1 = new TableRow(this);
            tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr1.setWeightSum(10f);

            //Agrego ids ocultos
            x = new TextView(this);
            x.setText(id);
            params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0f);
            x.setLayoutParams(params);
            x.setVisibility(View.INVISIBLE);

            a = new TextView(this);
            a.setText(cantidad);
            a.setTextSize(10f);
            params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
            a.setLayoutParams(params);
            a.setGravity(Gravity.LEFT);
            //a.setVisibility(View.INVISIBLE);

            b = new TextView(this);
            b.setText(producto);
            b.setTextSize(10f);
            params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 6f);
            b.setLayoutParams(params);

            c = new TextView(this);
            c.setText(precio);
            c.setTextSize(10f);
            params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
            c.setLayoutParams(params);
            c.setGravity(Gravity.RIGHT);

            tr1.addView(x);
            tr1.addView(a);
            tr1.addView(b);
            tr1.addView(c);
            tabla.addView(tr1);

            //Actualizo total
            total.setText(Funcion.formatoPesos(actualizarTotal(tabla)));

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            tr1 = null;
            a = null;
            b = null;
            c = null;
            params = null;
        }

    }

    public void deshacer(TableLayout tabla){

        tabla.removeView(tabla.getChildAt(tabla.getChildCount()-1));
        total.setText(Funcion.formatoPesos(actualizarTotal(tabla)));

    }

    public void borrarTabla(TableLayout tabla){

        System.out.println("numero de filas: "+tabla.getChildCount());

        while(tabla.getChildCount() > 1){
            deshacer(tabla);
        }

        return;

    }

    public double actualizarTotal(TableLayout tabla){

        Tabla tab = new Tabla(tabla);

        try {
            return tab.obtenerSumaMontos(3);
        }catch(Exception e){
            e.printStackTrace();
            return 0.0;
        }finally{
            tab = null;
        }
    }

    //Para buscar el id y agregarlo a la tabla

    private int buscarPosicionArray(String[] array, String texto){

        return Arrays.asList(array).indexOf(texto);
    }

}
