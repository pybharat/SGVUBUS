package com.bharatapp.sgvuBus.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.process;
import com.bharatapp.sgvuBus.retrofit.RetrofitClient;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class update_profile extends AppCompatActivity {
String uname,ucontact,uemail,uimg,img1,ucontact1;
EditText name,contact;
Button upload,update,verify;
ImageButton img;
process process;
RetrofitClient retrofitClient;
SharedPreferences sharedPreferences;
int verifycontact=0;
    Toolbar toolbar;
EditText otp1,otp2,otp3,otp4,otp5,otp6;
Button register,verifyotp;
int otp,userid;
String msg,img21;
TextView time1,resend;
ProgressBar progressBar;
private  static  final String SHARED_PREF_NAME="sgvu";
private  static  final String KEY_USERID="userid";
private  static  final String KEY_TOKEN="token";
private int PICK_IMAGE_REQUEST = 1;
private static final int STORAGE_PERMISSION_CODE = 123;
private Bitmap bitmap;
private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        process=new process(update_profile.this);
        name=findViewById(R.id.name);
        contact=findViewById(R.id.contact1);
        verify=findViewById(R.id.verify_no);
        upload=findViewById(R.id.uploadimg);
        update=findViewById(R.id.update1);
        img=findViewById(R.id.upload_img);
        retrofitClient=new RetrofitClient();
        toolbar=(Toolbar)findViewById(R.id.actionbar1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle=getIntent().getExtras();
        if(bundle!= null)
        {
         uname=bundle.getString("name");
         ucontact1=bundle.getString("contact");
         uemail=bundle.getString("email");
         uimg=bundle.getString("img_url");
        }
        if(!uname.isEmpty())
        {
            name.setText(uname);
        }
        if(!ucontact1.isEmpty())
        {
            contact.setText(ucontact1);
        }
        if(!uimg.isEmpty())
        {
            Glide.with(update_profile.this)
                    .load(uimg)
                    .centerCrop()
                    .into(img);
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
                upload.setVisibility(View.VISIBLE);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process.show();
                uploadImg(img1);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process.show();
                uploadtext();
            }
        });
        contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            verify.setVisibility(View.VISIBLE);
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyContact();
            }
        });
    }

    private void verifyContact() {
        ucontact=contact.getText().toString();
        if(ucontact.isEmpty())
        {
            contact.requestFocus();
            contact.setError("Enter contact");
        }
        else if(ucontact.length()<10)
        {
            contact.requestFocus();
            contact.setError("Enter proper contact");
        }
        else if(ucontact.matches(ucontact1))
        {
            contact.requestFocus();
            contact.setError("Same contact");
        }
        sharedPreferences= getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userid=sharedPreferences.getInt(KEY_USERID,0);
        JsonObject contactdata=new JsonObject();
        contactdata.addProperty("userid",userid);
        contactdata.addProperty("number",ucontact);
        retrofitClient.getWebService().updatemobileotp(contactdata).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {

                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {
                            otp=Integer.parseInt(obj.get("otp").toString());
                            verifyotp(otp);
                        }
                        else if(Integer.parseInt(obj.get("code").toString())==400) {
                            Toast.makeText(update_profile.this,obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(update_profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("bharat",t.toString());
            }
        });

    }

    private void verifyotp(int otp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(update_profile.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.otpverification, viewGroup, false);
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
               resendotp();
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
                    Toast.makeText(update_profile.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                }
                String getotp=o1+o2+o3+o4+o5+o6;
                int getotp1=Integer.parseInt(getotp);
                verifyApi(getotp1,userid,otp,alertDialog);
            }
        });

    }

    private void resendotp() {
        JsonObject contactdata=new JsonObject();
        contactdata.addProperty("userid",userid);
        contactdata.addProperty("number",ucontact);
        retrofitClient.getWebService().updatemobileotp(contactdata).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {

                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {
                            Toast.makeText(update_profile.this,"OTP resend successfully.", Toast.LENGTH_SHORT).show();
                        }
                        else if(Integer.parseInt(obj.get("code").toString())==400) {
                            Toast.makeText(update_profile.this,obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(update_profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("bharat",t.toString());
            }
        });
    }


    private void verifyApi(int getotp1, int userid, int otp,AlertDialog alertDialog) {
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
                            Toast.makeText(update_profile.this,obj.getString("message"), Toast.LENGTH_SHORT).show();
                            verifycontact=1;
                            verify.setVisibility(View.INVISIBLE);
                            contact.setText(ucontact);
                            alertDialog.dismiss();
                        }
                        else if(Integer.parseInt(obj.get("code").toString())==400) {
                            msg= obj.getString("message");
                            Toast.makeText(update_profile.this,msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(update_profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    private void uploadtext() {
        uname=name.getText().toString();
        if(uname.isEmpty())
        {
            name.requestFocus();
            name.setError("Enter Name");
            return;
        }
        if(img1==null) {
            uimg = "default.jpeg";
        }
        else
        {
            uimg=img21;
        }

        sharedPreferences= getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int userid=sharedPreferences.getInt(KEY_USERID,0);
        JsonObject profiledata=new JsonObject();
        profiledata.addProperty("userid",userid);
        profiledata.addProperty("name",uname);
        if(verifycontact==1)
        {
            profiledata.addProperty("number",ucontact);
        }
        else
        {
            profiledata.addProperty("number",ucontact1);
        }
        profiledata.addProperty("email",uemail);
        profiledata.addProperty("img",uimg);
        retrofitClient.getWebService().updateuserinfo1(profiledata).enqueue(new Callback<String>() {
         @Override
         public void onResponse(Call<String> call, Response<String> response) {
                process.dismiss();
             if(response.isSuccessful()) {
                 try {
                     JSONObject obj = new JSONObject(response.body());
                     if(Integer.parseInt(obj.getString("code"))==200)
                     {
                         Toast.makeText(update_profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                         Intent i=new Intent(update_profile.this,dashboard.class);
                         i.putExtra("poster",1);
                         startActivity(i);
                     }
                    else
                     {
                         Toast.makeText(update_profile.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
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

    private void uploadImg(String img1) {
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
            Toast.makeText(this, "Login Again", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(update_profile.this, login.class);
            startActivity(i);
        }
        JsonObject image=new JsonObject();
        image.addProperty("userid",userid);
        image.addProperty("img",img1);
        image.add("auth",auth);
        retrofitClient.getWebService().updateprofileimg(image).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                process.dismiss();
                if(response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.getString("code"))==200)
                        {
                            img21=obj.getString("message");
                        }
                        else
                        {
                            Toast.makeText(update_profile.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("bharat",t.toString());
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                img1 = Base64.encodeToString(byteArray, Base64.DEFAULT);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(update_profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(update_profile.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(update_profile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request

        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(update_profile.this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(update_profile.this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}