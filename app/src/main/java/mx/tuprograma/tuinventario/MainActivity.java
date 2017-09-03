package mx.tuprograma.tuinventario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import conexiones.JSONfunctions;

public class MainActivity extends AppCompatActivity {

    private EditText correo;
    private EditText contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences precargado = getSharedPreferences("usuario", Context.MODE_PRIVATE);

        if(precargado.getString("custid","").matches("")){
            setContentView(R.layout.activity_main);
            correo = (EditText) findViewById(R.id.correo);
            contraseña = (EditText) findViewById(R.id.contraseña);
        }else{
            //setContentView(R.layout.activity_menu);
            final View view = findViewById(android.R.id.content);
            ver(view, precargado.getString("custid", "").toString());
            finish();

        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    public void verificar(View view) throws JSONException {

        Toast notification;
        SharedPreferences precargado = getSharedPreferences("usuario", Context.MODE_PRIVATE);

            if (correo.getText().toString().matches("") || contraseña.getText().toString().matches("")) {
                notification = Toast.makeText(this, "Debes ingresar los datos completos", Toast.LENGTH_LONG);
                notification.show();

            } else {

                //Sí hay datos ingresados
                JSONfunctions json = new JSONfunctions();

                //System.out.println(json.getJSON("http://www.tuprograma.mx/admin/rest/usuarios/autenticar/"+correo+"/"+contraseña, 20000));
                JSONObject respuesta = new JSONObject(json.getJSON("http://www.tuprograma.com.mx/admin/rest/usuarios/autenticar/" + correo.getText() + "/" + contraseña.getText(), 20000));

                if (respuesta.get("respuesta").equals("NO")) {
                    notification = Toast.makeText(this, "Correo o contraseña incorrecta", Toast.LENGTH_LONG);
                    notification.show();
                } else {

                    //Registro en BD el acceso
                        SharedPreferences.Editor editor = precargado.edit();
                        editor.putString("custid", respuesta.get("respuesta").toString());
                        editor.commit();
                        finish();

                    //Abro menú
                        ver(view, respuesta.get("respuesta").toString());
                }

            }
    }

    public void ver (View v, String custid) {
        Intent i=new Intent(this,Menu.class);
        i.putExtra("custid", custid);
        startActivity(i);
    }

}
