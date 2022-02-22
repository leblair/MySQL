package com.example.mysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    public Button login;

    public TextInputEditText name;
    public TextInputEditText email;
    public TextInputEditText password;
    public TextInputEditText phone;
    public Button signup;

    static String URL_REGISTER = "https://www.joanseculi.com/edt69/createuser2.php";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        login = findViewById(R.id.login2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(getApplicationContext(), name, email, password, phone);
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        
        signup = findViewById(R.id.signup2);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
    }

    static void registerUser(Context context, TextInputEditText input_name, TextInputEditText input_email, TextInputEditText input_password, TextInputEditText input_phone) {

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "User registered!", Toast.LENGTH_SHORT).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", input_name.getText().toString().trim());
                params.put("email", input_email.getText().toString().trim());
                params.put("password", input_password.getText().toString().trim());
                params.put("phone", input_phone.getText().toString().trim());
                return params;
            }
        };
        queue.add(stringRequest);
    }

}



