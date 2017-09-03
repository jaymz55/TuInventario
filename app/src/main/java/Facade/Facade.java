package Facade;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DAO.MaterialDAO;
import DAO.SolicitudDAO;
import apis.Solicitud;

/**
 * Created by Hector Betancourt on 08/08/2017.
 */

public class Facade {

    //Variables
        private String custid;

    //Constructores
        public Facade(String custid){
            this.custid = custid;
        }

    //Metodos
        //Metodos de Materiales
            public JSONObject obtenerMaterial(String barCode) throws JSONException {
                MaterialDAO dao;

                try{

                    dao = new MaterialDAO(custid);
                    return dao.obtenerMaterial(barCode);

                }catch (Exception e){
                    e.printStackTrace();
                    return new JSONObject("{\"respuesta\":\"ERROR\"}");
                }finally {
                    dao = null;
                }

            }

            public JSONArray obtenerMateriales(String custid) throws JSONException {
                MaterialDAO dao;
                JSONObject error;
                JSONArray arrayError;

                try{

                    dao = new MaterialDAO(custid);
                    return dao.obtenerMateriales();

                }catch (Exception e){
                    e.printStackTrace();
                    error = new JSONObject("{\"respuesta\":\"ERROR\"}");
                    arrayError = new JSONArray();
                    arrayError.put(error);
                    return arrayError;
                }finally {
                    dao = null;
                    error = null;
                    arrayError = null;
                }

            }

            public boolean registrarBarcode(String idMaterial, String barCode){
                MaterialDAO dao = new MaterialDAO(custid);

                try{

                    return dao.registrarBarcode(idMaterial, barCode);

                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }finally {
                    dao = null;
                }

            }

            public boolean registrarEntradaMaterial(JSONObject material){

                MaterialDAO dao = new MaterialDAO(custid);

                if(dao.registrarEntradaMaterial(material)){
                    return true;
                }else{
                    return false;
                }

            }


        //Metodos de Solicitudes
            public boolean registrarNuevaSolicitud(Solicitud solicitud){

                SolicitudDAO dao = new SolicitudDAO(custid);

                try{

                    if(dao.registrarNuevaSolicitud(solicitud)){
                        return true;
                    }else{
                        return false;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }finally{
                    dao = null;
                }

            }


}
