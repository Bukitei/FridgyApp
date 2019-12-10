package com.example.fridgy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.widget.Toast.*;

public class LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPasswd;
    Button bttnLogin;
    String URL_LOGIN = "https://fridgy.000webhostapp.com/userConnect.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handleSSLHandshake();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUser = findViewById(R.id.edtUser);
        edtPasswd = findViewById(R.id.edtPasswd);
        bttnLogin = findViewById(R.id.btnLogin);

        bttnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String user = edtUser.getText().toString().trim();
                String pass = edtPasswd.getText().toString().trim();

                if(!user.isEmpty() || !pass.isEmpty()){
                    Login(user, pass);
                }else{
                    makeText(LoginActivity.this, "Introduce todos los campos para continuar", LENGTH_SHORT);
                }
            }
        });
    }

    private void Login(final String email, final String password){

        bttnLogin.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray object = new JSONArray(jsonObject.getString("user"));
                            JSONObject definitive = object.getJSONObject(0);
                            String name = definitive.getString("user");

                            if(!name.isEmpty()){

                                Toast.makeText(LoginActivity.this, "Has conseguido loguearte con exito: "+name, LENGTH_SHORT).show();
                                bttnLogin.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                intent.putExtra("name", name);
                                startActivity(intent);

                            }else{
                                makeText(LoginActivity.this, "No existe el usuario", LENGTH_SHORT);
                                bttnLogin.setVisibility(View.VISIBLE);
                            }
                        }catch(JSONException e){
                            Log.e("error2", e.toString());
                            Toast.makeText(LoginActivity.this, "Error", LENGTH_SHORT).show();
                            bttnLogin.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1","Error"+error.toString());
                bttnLogin.setVisibility(View.VISIBLE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
            Log.e("ssl", e.toString());
        }
    }
}