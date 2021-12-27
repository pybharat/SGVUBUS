package com.bharatapp.sgvuBus.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.activities.change_password;
import com.bharatapp.sgvuBus.activities.dashboard;
import com.bharatapp.sgvuBus.process;
import com.bharatapp.sgvuBus.retrofit.RetrofitClient;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class emailLogin extends Fragment {
    View view;
    EditText email,password,email2,otp1,otp2,otp3,otp4,otp5,otp6,pass1,cpass;
    Button login1,sendotp,verifyotp,change;
    int userid,otp;
    String msg,token,email1,email3,c_pass,cc_pass,type;
    TextView for_pass;
    RetrofitClient retrofitClient;
    TextView time1,resend;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    private  static  final String SHARED_PREF_NAME="sgvu";
    private  static  final String KEY_USERID="userid";
    private  static  final String KEY_TOKEN="token";
    private  static  final String KEY_TYPE="type";
    public process process;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_email_login, container, false);
        email=(EditText)view.findViewById(R.id.email);
        password=view.findViewById(R.id.password);
        login1=view.findViewById(R.id.login2);
        for_pass=view.findViewById(R.id.forgotpass);
        retrofitClient=new RetrofitClient();
        sharedPreferences= getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        process=new process(getActivity());
        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process.show();
                loginCall();
            }
        });
        for_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword();
            }
        });
        return view;
    }

    private void forgotpassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.forgotpassword, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        email2=dialogView.findViewById(R.id.email12);
        sendotp=dialogView.findViewById(R.id.otp1);
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpass();
            }
        });


    }

    private void forgotpass() {
        email1=email2.getText().toString();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("email",email1);
        retrofitClient.getWebService().forgotpass(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {
                            Toast.makeText(getActivity(),obj.getString("message"), Toast.LENGTH_SHORT).show();
                            otp=Integer.parseInt(obj.get("otp").toString());
                            email3=obj.get("email").toString();
                            process.dismiss();
                            verifyotp(otp,email3);
                        }
                        else if(Integer.parseInt(obj.get("code").toString())==400) {
                            msg= obj.getString("message");
                            Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("bharat",t.toString());
            }
        });
    }

    private void loginCall() {
        String useremail=email.getText().toString();
        String userpassword=password.getText().toString();

        if(useremail.isEmpty())
        {
            email.requestFocus();
            email.setError("Enter Email");
            return;
        }

        else if(userpassword.isEmpty())
        {
            password.requestFocus();
            password.setError("Enter Password");
            return;
        }


        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("email",useremail);
        jsonObject.addProperty("password",userpassword);

        retrofitClient.getWebService().loginApi(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                process.dismiss();
                if(response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {
                            msg= obj.getString("message");
                            Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                            userid=Integer.parseInt(obj.get("userid").toString());
                            token=obj.getString("Token");
                            type=obj.getString("user_type");
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putInt(KEY_USERID,userid);
                            editor.putString(KEY_TOKEN,token);
                            editor.putString(KEY_TYPE,type);
                            editor.apply();
                            email.setText("");
                            password.setText("");
                            Intent i=new Intent(getActivity(), dashboard.class);
                            startActivity(i);
                        }
                        else if(Integer.parseInt(obj.get("code").toString())==400) {
                            process.dismiss();
                            msg= obj.getString("message");
                            Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    process.dismiss();
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                process.dismiss();
                Log.d("bharat",t.toString());
            }
        });
    }

    public void verifyotp(int otp,String email3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.otpverification, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        timer(dialogView);
        alertDialog.show();
        otp1=dialogView.findViewById(R.id.otp_no_1);
        otp2=dialogView.findViewById(R.id.otp_no_2);
        otp3=dialogView.findViewById(R.id.otp_no_3);
        otp4=dialogView.findViewById(R.id.otp_no_4);
        otp5=dialogView.findViewById(R.id.otp_no_5);
        otp6=dialogView.findViewById(R.id.otp_no_6);
        autoTextMover(otp1,otp2,otp3,otp4,otp5,otp6);
        verifyotp=dialogView.findViewById(R.id.login3);
        resend=dialogView.findViewById(R.id.resend_otp_tv);
        verifyotp.setText("Verify");
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendApi(email3,otp);
            }
        });
        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String o1=otp1.getText().toString();
                String o2=otp2.getText().toString();
                String o3=otp3.getText().toString();
                String o4=otp4.getText().toString();
                String o5=otp5.getText().toString();
                String o6=otp6.getText().toString();
                if(o1.isEmpty() || o2.isEmpty() || o3.isEmpty() || o4.isEmpty() || o5.isEmpty()|| o6.isEmpty() )
                {
                    otp1.setError("");
                    Toast.makeText(getActivity(), "Enter OTP", Toast.LENGTH_SHORT).show();
                }
                String getotp=o1+o2+o3+o4+o5+o6;
                int getotp1=Integer.parseInt(getotp);
                verifyApi(getotp1,email3,otp);
            }
        });

    }

    private void resendApi(String email3, int otp) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("email",email3);
        jsonObject.addProperty("otp",otp);
        retrofitClient.getWebService().resendforgototp(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {
                            Toast.makeText(getActivity(),obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        else if(Integer.parseInt(obj.get("code").toString())==400) {
                            msg= obj.getString("message");
                            Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("bharat",t.toString());
            }
        });
    }

    private void verifyApi(int getotp1, String email3, int otp) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("email",email3);
        jsonObject.addProperty("otp",getotp1);
        retrofitClient.getWebService().verifyforgototp(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {
                            Intent i=new Intent(getActivity(), change_password.class);
                            i.putExtra("base","login");
                            i.putExtra("email",email3);
                            startActivity(i);
                        }
                        else if(Integer.parseInt(obj.get("code").toString())==400) {
                            msg= obj.getString("message");
                            Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("bharat",t.toString());
            }
        });


    }





    private void timer(View view) {
        time1=view.findViewById(R.id.duration_tv);
        resend=view.findViewById(R.id.resend_otp_tv);
        progressBar=view.findViewById(R.id.progress_bar);
        long duaration= TimeUnit.MINUTES.toMillis(1);
        new CountDownTimer(duaration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String sDuration=String.format(Locale.ENGLISH,"%02d:%02d"
                        ,TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                        , (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MICROSECONDS.toMinutes(millisUntilFinished)));
                time1.setText(sDuration);
            }

            @Override
            public void onFinish() {
                resend.setVisibility(view.getVisibility());
                progressBar.setVisibility(view.INVISIBLE);
            }
        }.start();
    }

    private void autoTextMover(EditText otp1, EditText otp2, EditText otp3, EditText otp4, EditText otp5, EditText otp6) {


        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(otp1.getText().toString().length()==1)
                {
                    otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(otp2.getText().toString().length()==1)
                {
                    otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(otp3.getText().toString().length()==1)
                {
                    otp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(otp4.getText().toString().length()==1)
                {
                    otp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(otp5.getText().toString().length()==1)
                {
                    otp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }



}