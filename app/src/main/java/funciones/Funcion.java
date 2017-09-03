package funciones;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.NumberFormat;

/**
 * Created by Desarrollador on 15/07/2017.
 */

public class Funcion {

    public static String formatoPesos(double monto){

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        try {

            String moneyString = formatter.format(monto);

            return moneyString;

        }catch(Exception e){
            e.printStackTrace();
            return "Error";
        }finally{
            formatter = null;
        }


    }

    public static String descuento(Object monto, Object descuento){

        monto = Double.parseDouble(monto.toString().replace("$","").replace(",",""));
        descuento = (Double.parseDouble(descuento.toString().replace("$","").replace(",",""))/100);

        return formatoPesos(Double.parseDouble(monto.toString()) -Double.parseDouble(descuento.toString())*Double.parseDouble(monto.toString()));

    }

    public static String obtenerFecha(int anio, int mes, int dia){
        return String.valueOf(anio)+"-"+String.format("%02d", (mes+1))+"-"+String.format("%02d", dia);
    }

    /*private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean conexionInternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;

    }*/

}
