package apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by H Betancourt on 16/07/2017.
 */

public class Solicitud {

    //Variables
        private String custid;
        private String fecha;
        private String descuento;
        private String comentario;
        private JSONArray productos;
        private JSONObject respuesta;


    //Constructores
        public Solicitud(String custid, String fecha, String descuento, String comentario, JSONArray productos){
            this.custid = custid;
            this.fecha = fecha;
            this.descuento = descuento;
            this.comentario = comentario;
            this.productos = productos;
        }


    //Métodos
        public JSONObject generarJson() throws JSONException {

            respuesta = new JSONObject();

            respuesta.put("custid", custid);
            respuesta.put("fecha", fecha);
            respuesta.put("descuento", descuento);
            respuesta.put("comentario", comentario);
            respuesta.put("productos", productos);

            return respuesta;
        }


}
