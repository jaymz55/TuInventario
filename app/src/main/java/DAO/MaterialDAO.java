package DAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import conexiones.JSONfunctions;
import conf.Conf;

/**
 * Created by Hector Betancourt on 08/08/2017.
 */

public class MaterialDAO {

    //Variables
        private String custid;
        private JSONfunctions json = new JSONfunctions();
        private JSONObject respuesta;

    //Constructores
        public MaterialDAO(String custid){
            this.custid = custid;
        }

    //Metodos
        public JSONObject obtenerMaterial(String barCode) throws JSONException {

            try {
                return new JSONObject(json.getJSON("http://www.tuprograma.mx/inventario/rest/materiales/codigo/" + custid + "/" + barCode, 20000));
            }catch (Exception e){
                e.printStackTrace();
                respuesta.put("respuesta", "ERROR");
                return respuesta;
            }
        }

        public JSONArray obtenerMateriales() throws JSONException {

            try {
                return new JSONArray(json.getJSON("http://www.tuprograma.mx/inventario/rest/materiales/custid/" + custid, 20000));
            }catch (Exception e){
                e.printStackTrace();
                respuesta.put("respuesta", "ERROR");
                return new JSONArray().put(respuesta);
            }
        }

        public boolean registrarBarcode(String idMaterial, String barCode) throws JSONException {

            try {
                respuesta = new JSONObject(json.getJSON("http://"+ Conf.obtenerApi()+"/inventario/rest/materiales/codigo/" + custid + "/" + idMaterial + "/" +barCode, 20000));
                if(respuesta.getString("respuesta").matches("ERROR")){
                    throw new Exception("ERROR");
                }else{
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        public boolean registrarEntradaMaterial(JSONObject material){

            if(json.postJSON("http://"+ Conf.obtenerApi()+"/inventario/rest/materiales/entrada/material", material, 20000).matches("OK")) {
                return true;
            }else{
                return false;
            }
        }


}
