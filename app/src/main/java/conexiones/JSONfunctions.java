package conexiones;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Admin on 27/06/2017.
 */

public class JSONfunctions {

    /*Obtiene json Get
    * @Param: url de la api
    * @Param: tiempo de vencimiento de response
    */

    public String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public String postJSON(String url, JSONObject material, int timeout) {

        HttpURLConnection con = null;

        try {
            URL myurl = new URL("http://www.tuprograma.mx/inventario/rest/materiales/entrada/material");
            con = (HttpURLConnection)myurl.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=utf8");
            //con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Method", "POST");


            OutputStream os = con.getOutputStream();
            os.write(material.toString().getBytes("UTF-8"));
            os.close();

            StringBuilder sb = new StringBuilder();
            int HttpResult =con.getResponseCode();

            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new   InputStreamReader(con.getInputStream(),"utf-8"));

                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                System.out.println("Respuesta Post: "+sb.toString());
                return sb.toString();

            }else{
                System.out.println(con.getResponseCode());
                System.out.println(con.getResponseMessage());
                return "ERROR";
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }


    /* Desglosa de un JSONArray a un String[]
    @Param: Cadena json del array
    @Param: Key que se desea obtener del array
     */

    public String[] getStringArray(String json, String dato){

        List<String> parcial = new ArrayList<>();
        String[] respuestaArray = null;
        JSONArray array = new JSONArray();
        JSONObject interno;

        try{

            array = new JSONArray(json);

            for (int i = 0; i < array.length(); ++i) {
                interno = array.getJSONObject(i);
                parcial.add(interno.getString(dato));

            }

            respuestaArray = new String[parcial.size()];
            respuestaArray = parcial.toArray(respuestaArray);

            return respuestaArray;

        }catch(Exception e){
            e.printStackTrace();
            return respuestaArray;
        }finally{
            respuestaArray = null;
            parcial = null;
            array = null;
            interno = null;
        }


    }

}
