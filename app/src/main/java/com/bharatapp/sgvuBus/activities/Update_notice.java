package com.bharatapp.sgvuBus.activities;

import androidx.annotation.NonNull;
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
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update_notice extends AppCompatActivity {
    String nid,ntitle,nfull_des,img_url,date2,nshort_des,img1,setImg,nimag,img_url2;
    EditText title,short_des,full_des;
    Button upload,update;
    ImageButton img;
    int count=0;
    RetrofitClient retrofitClient;
    SharedPreferences sharedPreferences;
    private  static  final String SHARED_PREF_NAME="sgvu";
    private  static  final String KEY_USERID="userid";
    private  static  final String KEY_TOKEN="token";
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;
    public process process;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notice);
        title=findViewById(R.id.title);
        short_des=findViewById(R.id.s_des);
        full_des=findViewById(R.id.f_des);
        upload=findViewById(R.id.uploadimg);
        img=findViewById(R.id.upload_img);
        update=findViewById(R.id.update_notice);
        Toolbar toolbar=(Toolbar)findViewById(R.id.actionbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        retrofitClient=new RetrofitClient();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nid = bundle.getString("nid");
            ntitle = bundle.getString("ntitle");
            nshort_des = bundle.getString("nshort_des");
            nfull_des = bundle.getString("nfull_des");
            img_url = bundle.getString("img_url");
            date2 = bundle.getString("date1");
        }
        img_url2=img_url.replaceAll("https://seekho.live/bharat-sir/sgvuapi/assets/notices/","");

        title.setText(ntitle);
        short_des.setText(nshort_des);
        full_des.setText(nfull_des);
        Glide.with(Update_notice.this)
                .load(img_url)
                .into(img);
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
                uploadImg(img1, nid);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtext();
            }
        });
    }

    private void uploadtext() {
        ntitle=title.getText().toString();
        nshort_des=short_des.getText().toString();
        nfull_des=full_des.getText().toString();
        if(ntitle.isEmpty())
        {
            title.requestFocus();
            title.setError("Enter Title");
            return;
        }
        else if(nshort_des.isEmpty())
        {
            short_des.requestFocus();
            short_des.setError("Enter Short Description.");
        }
        else if(nfull_des.isEmpty())
        {
            full_des.requestFocus();
            full_des.setError("Enter Full Description.");
        }
        if(img1==null) {
            nimag = img_url2;
        }
        else
        {
            nimag=setImg;
        }
        sharedPreferences= getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
            Toast.makeText(getApplicationContext(), "Login Again", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getApplicationContext(), login.class);
            startActivity(i);
        }
        JsonObject noticedata=new JsonObject();
        noticedata.addProperty("nid",nid);
        noticedata.addProperty("title",ntitle);
        noticedata.addProperty("short_des",nshort_des);
        noticedata.addProperty("full_des",nfull_des);
        noticedata.addProperty("img_url",nimag);
        noticedata.add("auth",auth);

        retrofitClient.getWebService().updatenotice(noticedata).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {

                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {
                            process.dismiss();
                            Toast.makeText(Update_notice.this,"Notice Updated", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(Update_notice.this,dashboard.class);
                            i.putExtra("poster",1);
                            startActivity(i);
                        }
                        else if(Integer.parseInt(obj.get("code").toString())==400) {
                            Toast.makeText(Update_notice.this,obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(Update_notice.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("bharat",t.toString());
            }
        });
    }

    private void uploadImg(String img1, String nid) {
        sharedPreferences= getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
            Toast.makeText(getApplicationContext(), "Login Again", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getApplicationContext(), login.class);
            startActivity(i);
        }
        JsonObject image=new JsonObject();
        image.addProperty("nid",nid);
        image.addProperty("img",img1);
        image.add("auth",auth);
        retrofitClient.getWebService().updatenoticeimg(image).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {

                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {
                            setImg=obj.get("message").toString();
                            Toast.makeText(getApplicationContext(),"Image Uploaded", Toast.LENGTH_SHORT).show();
                            process.dismiss();
                        }
                        else if(Integer.parseInt(obj.get("code").toString())==400) {
                            Toast.makeText(getApplicationContext(),obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
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
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getApplicationContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(Update_notice.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(Update_notice.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request

        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getApplicationContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getApplicationContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}