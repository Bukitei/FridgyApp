package com.example.fridgy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.*;

public class LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPasswd;
    Button bttnLogin;


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
                validarUsuario("https://fridgy.000webhostapp.com/userConnect.php");
            }
        });
    }

    private void validarUsuario(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    makeText(LoginActivity.this, "Conseguistes entrar", LENGTH_SHORT);
                }else{
                    makeText(LoginActivity.this, "Usuario o contraseña incorrecto", LENGTH_SHORT);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                makeText(LoginActivity.this, error.toString(), LENGTH_SHORT);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("user", edtUser.getText().toString());
                paramMap.put("password", edtPasswd.getText().toString());
                return paramMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
