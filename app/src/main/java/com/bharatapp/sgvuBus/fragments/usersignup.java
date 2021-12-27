package com.bharatapp.sgvuBus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.util.Patterns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.activities.login;
import com.bharatapp.sgvuBus.retrofit.RetrofitClient;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class usersignup extends Fragment {
    View view;
    EditText name,email,number,password,cpassword,otp1,otp2,otp3,otp4,otp5,otp6;
    Button register,verifyotp;
    int otp,userid;
    String msg;
    RetrofitClient retrofitClient;
    TextView time1,resend;
    ProgressBar progressBar;
    Spinner user_type;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_usersignup, container, false);
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        number=view.findViewById(R.id.contact);
        password=view.findViewById(R.id.password);
        cpassword=view.findViewById(R.id.cpassword);
        register=view.findViewById(R.id.signup);
        user_type=view.findViewById(R.id.user_type);
        retrofitClient=new RetrofitClient();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        ArrayAdapter<String> user=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.user_type));
        user.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_type.setAdapter(user);

        return view;
    }

    private void register() {
        String useremail=email.getText().toString();
        String username=name.getText().toString();
        String usernumber=number.getText().toString();
        String userpassword=password.getText().toString();
        String usercpassword=cpassword.getText().toString();
        String u_type=user_type.getSelectedItem().toString();
        if(useremail.isEmpty())
        {
            email.requestFocus();
            email.setError("Enter Email");
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(useremail).matches())
        {
            email.requestFocus();
            email.setError("please enter correct Email");
            return;
        }
        else if(username.isEmpty())
        {
            name.requestFocus();
            name.setError("Enter name");
            return;
        }
        else if(usernumber.isEmpty())
        {
            number.requestFocus();
            number.setError("Enter Number");
            return;
        }
        else if(userpassword.isEmpty())
        {
            password.requestFocus();
            password.setError("Enter Password");
            return;
        }
        else if(userpassword.length()<8)
        {
            password.requestFocus();
            password.setError("Minimum 8 characters");
            return;
        }
        else if(usercpassword.isEmpty())
        {
            cpassword.requestFocus();
            cpassword.setError("Enter Password");
            return;
        }
        else if(usercpassword==userpassword)
        {
            cpassword.requestFocus();
            cpassword.setError("Password not match.");
            return;
        }
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("email",useremail);
        jsonObject.addProperty("name",username);
        jsonObject.addProperty("number",usernumber);
        jsonObject.addProperty("password",userpassword);
        jsonObject.addProperty("type",u_type);
        Log.d("bharat123",jsonObject.toString());
        retrofitClient.getWebService().register(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {
                            otp=Integer.parseInt(obj.get("otp").toString());
                            userid=Integer.parseInt(obj.get("userid").toString());
                            msg= obj.getString("message");
                            Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                            verifyotp(userid,otp);
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

    public void verifyotp(int userid, int otp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.otpverification, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
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
        resendApi(userid,otp);
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
                verifyApi(getotp1,userid,otp);
            }
        });

    }

    private void resendApi(int userid, int otp) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("userid",userid);
        jsonObject.addProperty("otp",otp);
        retrofitClient.getWebService().resendotp(jsonObject).enqueue(new Callback<String>() {
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

    private void verifyApi(int getotp1, int userid, int otp) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("userid",userid);
        jsonObject.addProperty("otp",getotp1);
        retrofitClient.getWebService().verifyotp(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {
                            Toast.makeText(getActivity(),obj.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(getActivity(), login.class);
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