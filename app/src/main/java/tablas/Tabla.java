package tablas;

import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by H Betancourt on 14/07/2017.
 * Clase para armar un TableLayout y para tomar la info de una ya hecho y armar un JSONObject
 */

public class Tabla {

    //Variables
    private TableLayout tabla;

    //Constructor
    public Tabla(TableLayout tabla){
        this.tabla = tabla;
    }

    //Métodos
    /*
    * Obtiene los montos para sumar a total
    */
    public double obtenerSumaMontos(int posicionAObtener){

        double monto = 0;

        try {

            for (int i = 1, j = tabla.getChildCount(); i < j; i++) {
                View view = tabla.getChildAt(i);
                if (view instanceof TableRow) {
                    // then, you can remove the the row you want...
                    // for instance...
                    TableRow row = (TableRow) view;
                    TextView firstTextView = (TextView) row.getChildAt(posicionAObtener);

                    monto = monto + Double.parseDouble(firstTextView.getText().toString().replace("$","").replace(",",""));

                    firstTextView = null;

                }
            }

            return monto;

        }catch(Exception e){
            e.printStackTrace();
            return 0.0;
        }finally{
            monto = 0.0;
        }

    }

    public JSONArray generarArrayDeTable(String[] cabeceras, int columnas, int columnaDeMonto) throws JSONException {

        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();

        TableRow row;
        TextView textView;

        try{

            System.out.println("Numero de filas: "+tabla.getChildCount());

            for (int i = 1, j = tabla.getChildCount(); i < j; i++) {
                View view = tabla.getChildAt(i);
                if (view instanceof TableRow) {
                    // then, you can remove the the row you want...
                    // for instance...
                    row = (TableRow) view;

                    row.setVisibility(View.VISIBLE);
                    System.out.println("Numero de columnas: "+row.getChildCount());

                    object = new JSONObject();

                    for(int a = 0; a < columnas; a++) {

                        if(a == (columnaDeMonto-1)){
                            textView = (TextView) row.getChildAt(a);
                            object.put(cabeceras[a], textView.getText().toString().replace("$","").replace(",",""));
                        }else {
                            textView = (TextView) row.getChildAt(a);
                            object.put(cabeceras[a], URLEncoder.encode(textView.getText().toString(), "UTF-8"));
                        }
                    }

                    //Agrego object
                    array.put(object);

                }
            }

            return array;

        }catch(Exception e){
            e.printStackTrace();
            array = new JSONArray();
            object = new JSONObject();
            object.put("respuesta","error");
            array.put(object);
            return array;
        }finally{
            array = null;
            object = null;
            row = null;
            textView = null;
        }

    }

}
