package com.bharatapp.sgvuBus.activities;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.bharatapp.sgvuBus.IsConnected;
import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.adapter.SliderAdapter;
import com.bharatapp.sgvuBus.fragments.admin_link;
import com.bharatapp.sgvuBus.model_class.SliderData;
import com.bharatapp.sgvuBus.fragments.important_link;
import com.bharatapp.sgvuBus.fragments.updates;
import com.bharatapp.sgvuBus.retrofit.RetrofitClient;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
View v,actionbar2;
RetrofitClient retrofitClient;
SharedPreferences sharedPreferences;
String pid,ptitle,pfull_des,img_url,date1,img1,type;
String u_name1,img_u,email,contact;
private  static  final String SHARED_PREF_NAME="sgvu";
private  static  final String KEY_USERID="userid";
private  static  final String KEY_TOKEN="token";
private  static  final String KEY_TYPE="type";
ImageView close,poster,actionimage,actionimage1;
Toolbar toolbar;
TextView maintitle,u_name;
BottomNavigationView bottomNavigationView;
public View dialogView;
public AlertDialog alertDialog;
public DrawerLayout drawerLayout;
NavigationView navigationView;
public ActionBarDrawerToggle actionBarDrawerToggle;
private int PICK_IMAGE_REQUEST = 1;
private static final int STORAGE_PERMISSION_CODE = 123;
private Bitmap bitmap;
private Uri filePath;
ViewPager viewPager;
int currentPage = 0;
int p_count=0;
Timer timer;
Handler mHandler;
final long DELAY_MS = 500;
final long PERIOD_MS = 3000;
String url1 = "https://seekho.live/bharat-sir/slider/h3.jpg";
String url2 = "https://seekho.live/bharat-sir/slider/h2.png";
String url3 = "https://seekho.live/bharat-sir/slider/h1.jpg";
String url4 = "https://seekho.live/bharat-sir/slider/h4.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        maintitle=(TextView)findViewById(R.id.maintitle);

        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        int userid=sharedPreferences.getInt(KEY_USERID,0);
        String token=sharedPreferences.getString(KEY_TOKEN,null);
                type=sharedPreferences.getString(KEY_TYPE,null);

        //viewpager
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));
        sliderDataArrayList.add(new SliderData(url4));
        viewPager = (ViewPager)findViewById(R.id.viewpager1);
        SliderAdapter adapter = new SliderAdapter(dashboard.this,sliderDataArrayList);
        viewPager.setAdapter(adapter);
        /*After setting the adapter use the timer */
        int NUM_PAGES=sliderDataArrayList.size();
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
        //custom dialog
Bundle bundle=getIntent().getExtras();
if(bundle!=null)
{
    p_count=bundle.getInt("poster");
}
if(p_count==0) {
    AlertDialog.Builder builder = new AlertDialog.Builder(dashboard.this);
    ViewGroup viewGroup = findViewById(android.R.id.content);
    dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customview, viewGroup, false);
    retrofitClient = new RetrofitClient();
    fetchpost(dialogView);
    builder.setView(dialogView);
    alertDialog = builder.create();
    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    alertDialog.show();
}
        Handler mhandler;
        close = (ImageView) dialogView.findViewById(R.id.clo);
        poster = (ImageView) dialogView.findViewById(R.id.pos);
        toolbar = findViewById(R.id.actionbar1);
        setSupportActionBar(toolbar);
        //navigation drawer
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.navi_blue));
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
//load notice fragment
        loadfragment(new updates());
        View hView =  navigationView.inflateHeaderView(R.layout.header_navigation_drawer);

        actionimage1=hView.findViewById(R.id.aq);
        u_name=hView.findViewById(R.id.user_name);
        getuserinfo();
        if(!type.matches("admin")) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.admin).setVisible(false);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p_count=1;
                alertDialog.dismiss();
            }
        });
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(dashboard.this, detail_notice.class);
                i.putExtra("nid",pid);
                i.putExtra("ntitle",ptitle);
                i.putExtra("nfull_des",pfull_des);
                i.putExtra("img_url",img_url);
                i.putExtra("date1",date1);
                startActivity(i);
            }
        });
        //bottem navigation
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        Intent i2=new Intent(dashboard.this,dashboard.class);
                        i2.putExtra("poster",1);
                        startActivity(i2);
                        return true;
                    case R.id.profile:
                        Intent i1=new Intent(dashboard.this, profile.class);
                        startActivity(i1);
                        return true;
                    case R.id.lms:
                       Intent i=new Intent(dashboard.this, webview.class);
                       i.putExtra("url","https://mygyanvihar.com/2020");
                       startActivity(i);
                        return true;
                    default:return false;
                }

            }

        });

        IsConnected connected=new IsConnected(dashboard.this);
        if(!connected.isConnected())
        {
            this.mHandler = new Handler();

           this.mHandler.postDelayed(m_Runnable,50000);
        }

    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            Intent i6=new Intent(dashboard.this,dashboard.class);
            i6.putExtra("poster",1);
            startActivity(i6);

            dashboard.this.mHandler.postDelayed(m_Runnable, 5000);
        }

    };//runnable


    @Override
    protected void onPause() {
        super.onPause();
       alertDialog.dismiss();


    }

    private void upload_img() {
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
            Toast.makeText(getApplicationContext(), "Login Again", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getApplicationContext(), login.class);
            startActivity(i);
        }
        JsonObject image=new JsonObject();
        image.addProperty("userid",userid);
        image.addProperty("img",img1);
        image.add("auth",auth);
        retrofitClient.getWebService().updateprofileimg(image).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {

                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {
                            Toast.makeText(getApplicationContext(),"Image Uploaded", Toast.LENGTH_SHORT).show();
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

    private void fetchpost(View dialogView) {

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
        noticedata.add("auth",auth);
        retrofitClient.getWebService().getposter(noticedata).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        { JSONArray jsonArray= obj.getJSONArray("message");
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                pid = jsonObject1.get("id").toString();
                                ptitle = jsonObject1.get("title").toString();
                                pfull_des = jsonObject1.get("full_des").toString();
                                date1 = jsonObject1.get("created").toString();
                                img_url = "https://seekho.live/bharat-sir/sgvuapi/assets/poster/" + jsonObject1.get("img_url").toString();
                                Glide.with(getApplicationContext())
                                        .load(img_url)
                                        .fitCenter()
                                        .into(poster);
                            }
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


    private void loadfragment(Fragment fragment) {
        FragmentManager fm=getFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.updates,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.up:
                loadfragment(new updates());
                maintitle.setText("IMPORTANT UPDATES");
                break;
            case R.id.links:
                loadfragment(new important_link());
                maintitle.setText("IMPORTANT LINKS");
                break;
            case R.id.admin:
                if(type.matches("admin")) {
                    loadfragment(new admin_link());
                    maintitle.setText("ADMIN PANEL");
                }
                else
                {

                    Toast.makeText(this, "You are not Admin.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.shareapp:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case R.id.logout:
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(dashboard.this,"LogOut Successfully.",Toast.LENGTH_SHORT).show();
                finish();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
                actionimage.setImageBitmap(bitmap);
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
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(dashboard.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(dashboard.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
    private void getuserinfo() {
        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int userid = sharedPreferences.getInt(KEY_USERID, 0);
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        JsonObject auth = new JsonObject();

        if (userid != 0 || token != null) {
            auth.addProperty("id", userid);
            auth.addProperty("token", token);
        } else {
            Toast.makeText(getApplicationContext(), "Login Again", Toast.LENGTH_SHORT).show();
        }
        JsonObject user = new JsonObject();
        user.addProperty("userid", userid);
        user.add("auth", auth);

        retrofitClient.getWebService().getuserinfo(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if (Integer.parseInt(obj.get("code").toString()) == 200) {
                            JSONArray jsonArray=obj.getJSONArray("message");
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            u_name1=jsonObject.getString("user_firstname");
                            contact=jsonObject.getString("user_mobile");
                            email=jsonObject.getString("user_email");
                            img_u=jsonObject.getString("profile_image");
                            if(img_u.isEmpty())
                            {
                                img_u="https://www.seekho.live/bharat-sir/sgvuapi/assets/profile/default.jpeg";
                            }
                            else
                            {
                                img_u="https://www.seekho.live/bharat-sir/sgvuapi/assets/profile/"+img_u;
                                Glide.with(dashboard.this)
                                        .load(img_u)
                                        .centerCrop()
                                        .into(actionimage1);
                            }

                            String name[]=u_name1.split(" ");
                            String name1="Hello "+name[0];
                            u_name.setText(name1);

                        } else if (Integer.parseInt(obj.get("code").toString()) == 400) {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("bharat",t.toString());
            }
        });
    }

}