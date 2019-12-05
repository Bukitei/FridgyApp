package com.example.fridgy;

import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.*;

public class LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPasswd;
    Button bttnLogin;
    String URL_LOGIN = "https://fridgy.000webhostapp.com/userConnect.php";

    Intent intent = new Intent(this, MenuActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                            JSONArray array = new JSONArray(jsonObject.getJSONArray("user"));
                            JSONObject user = array.getJSONObject(0);
                        String name = user.getString("user");

                        if(!name.isEmpty()){

                            Toast.makeText(LoginActivity.this, "Has conseguido loguearte con exito"+name, LENGTH_SHORT).show();

                            startActivity(intent);


                        }else{
                            makeText(LoginActivity.this, "No existe el usuario", LENGTH_SHORT);
                        }
                        }catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error", LENGTH_SHORT).show();
                            startActivity(intent);
                            }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Error "+error, LENGTH_SHORT).show();
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
