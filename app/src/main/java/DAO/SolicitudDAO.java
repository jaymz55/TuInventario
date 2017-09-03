package DAO;

import org.json.JSONObject;

import apis.Solicitud;
import conexiones.JSONfunctions;
import conf.Conf;

/**
 * Created by Desarrollador on 21/08/2017.
 */

public class SolicitudDAO {

    //Variables
        private String custid;
        private JSONfunctions json;
        private JSONObject respuesta;

    //Constructores
        public SolicitudDAO(String custid){
            this.custid = custid;
        }

    //Metodos
        public boolean registrarNuevaSolicitud(Solicitud solicitud){

            json = new JSONfunctions();

            try{

                respuesta = new JSONObject(json.getJSON("http://"+ Conf.obtenerApi()+"/inventario/rest/solicitudes/registrar/"+solicitud.generarJson(), 20000));

                if(respuesta.get("respuesta").toString().matches("OK")){
                    return true;
                }else{
                    return false;
                }

            }catch(Exception e){
                e.printStackTrace();
                return false;
            }finally{
                json = null;
                respuesta = null;
            }

        }


}
