package com.bharatapp.sgvuBus.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.process;
import com.bharatapp.sgvuBus.retrofit.RetrofitClient;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class change_password extends AppCompatActivity {
String base="bharat";
EditText pass1,cpass;
Button change;
String email3;
Toolbar toolbar;
SharedPreferences sharedPreferences;
private  static  final String SHARED_PREF_NAME="sgvu";
private  static  final String KEY_USERID="userid";
private  static  final String KEY_TOKEN="token";
com.bharatapp.sgvuBus.process process;
RetrofitClient retrofitClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        pass1=findViewById(R.id.pass1);
        cpass=findViewById(R.id.cpass1);
        change=findViewById(R.id.change1);
        Bundle bundle=getIntent().getExtras();
        retrofitClient=new RetrofitClient();
        process=new process(change_password.this);
        toolbar=(Toolbar)findViewById(R.id.actionbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(bundle != null)
        {
            base=bundle.getString("base");

        }
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(base.matches("login"))
                {
                    login_changepassword(bundle);

                }
                else if(base.matches("profile"))
                {

                    profile_changepassword(bundle);
                }
            }
        });

    }

    public void profile_changepassword(Bundle bundle) {
        sharedPreferences= getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int userid=sharedPreferences.getInt(KEY_USERID,0);
        String token=sharedPreferences.getString(KEY_TOKEN,null);
        JsonObject auth=new JsonObject();
        if(userid != 0 || token!=null)
        {
            auth.addProperty("id",userid);
            auth.addProperty("token",token);
        }
        else
        {
            Toast.makeText(change_password.this, "Login Again", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(change_password.this, login.class);
            startActivity(i);
        }
        Toast.makeText(change_password.this, "Password changed successfully profile.", Toast.LENGTH_SHORT).show();
        String c_pass=pass1.getText().toString();
        String cc_pass=cpass.getText().toString();
        if(c_pass.isEmpty())
        {
            pass1.requestFocus();
            pass1.setError("Enter Password");
            return;
        }
        else if (cc_pass.isEmpty())
        {
            cpass.requestFocus();
            cpass.setError("Enter Password");
            return;
        }
        else if (!c_pass.matches(cc_pass))
        {
            cpass.requestFocus();
            cpass.setError("Password didn't Match");
            return;
        }
        else if (c_pass.length()<8)
        {
            pass1.requestFocus();
            pass1.setError("Minimum 8 characters");
            return;
        }
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("userid",userid);
        jsonObject.addProperty("password",c_pass);
        jsonObject.add("auth",auth);
        process.show();
        retrofitClient.getWebService().updateuserpassword(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                process.dismiss();
                if (response.isSuccessful())
                {

                    try {
                        JSONObject jsonObject1=new JSONObject(response.body());
                        if(Integer.parseInt(jsonObject1.get("code").toString())==200)
                        {
                            Toast.makeText(change_password.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(change_password.this,login.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(change_password.this, "Something gone wrong.", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(change_password.this,login.class);
                            startActivity(i);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(change_password.this, "Something gone wrong.", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(change_password.this,login.class);
                        startActivity(i);
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            Log.d("bharat",t.toString());
            }
        });
    }

    public void login_changepassword(Bundle bundle) {
        String c_pass=pass1.getText().toString();
        String cc_pass=cpass.getText().toString();
        email3=bundle.getString("email");
        if(c_pass.isEmpty())
        {
            pass1.requestFocus();
            pass1.setError("Enter Password");
            return;
        }
        else if (cc_pass.isEmpty())
        {
            cpass.requestFocus();
            cpass.setError("Enter Password");
            return;
        }
        else if (!c_pass.matches(cc_pass))
        {
            cpass.requestFocus();
            cpass.setError("Password didn't Match");
            return;
        }
        else if (c_pass.length()<8)
        {
            pass1.requestFocus();
            pass1.setError("Minimum 8 characters");
            return;
        }
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("email",email3);
        jsonObject.addProperty("password",c_pass);
        process.show();
        retrofitClient.getWebService().resetpassword(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                process.dismiss();
                if (response.isSuccessful())
                {

                    try {
                        JSONObject jsonObject1=new JSONObject(response.body());
                        if(Integer.parseInt(jsonObject1.get("code").toString())==200)
                        {
                            Toast.makeText(change_password.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(change_password.this,login.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(change_password.this, "Something gone wrong.", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(change_password.this,login.class);
                            startActivity(i);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(change_password.this, "Something gone wrong.", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(change_password.this,login.class);
                        startActivity(i);
                    }

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                process.dismiss();
                Log.d("bharat",t.toString());
            }
        });
    }


}