package com.example.hinadadebank.unverif;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hinadadebank.R;
import com.example.hinadadebank.api.Client;
import com.example.hinadadebank.api.Interface;
import com.example.hinadadebank.model.CheckResponse;
import com.example.hinadadebank.model.UploadResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {
    ImageView imgChoose;
    AppCompatButton btnChoose, btnUpload;
    RelativeLayout lyLoading;
    Interface mApiInterface;
    String part_image,usersId;
    TextView tvUsersId;
    Uri selectedImage;
    Boolean hasUpload=false;
    private static final int PICK_IMAGE_REQUEST = 9544;
    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        hideTaskbar();
        shared();
        initial();
        getData();
    }
    public void hideTaskbar(){
        getSupportActionBar().hide();
    }
    void initial(){
        imgChoose = findViewById(R.id.img_show);
        btnChoose = findViewById(R.id.btn_choose);
        btnUpload = findViewById(R.id.btn_upload);
        lyLoading = findViewById(R.id.ly_loading);
        tvUsersId = findViewById(R.id.users_id);
        String uid = "ID : "+usersId;
        tvUsersId.setText(uid);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pick(view);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyLoading.setVisibility(View.VISIBLE);
                if(!selectedImage.equals(Uri.EMPTY)){
                    uploadImage(view);
                }
            }
        });
    }
    void shared(){
        SharedPreferences sgSharedPref = getApplicationContext().getSharedPreferences("sg_shared_pref", getApplicationContext().MODE_PRIVATE);
         usersId= String.valueOf(sgSharedPref.getInt("id",1));
    }
    public void pick(View view) {
        verifyStoragePermissions(UploadActivity.this);
        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    imgChoose.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                 selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                 part_image = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(part_image));
                Log.w("Path ", part_image+"");
                imgChoose.setImageBitmap(thumbnail);
            }
        }else{
            Log.w("Gagal ", part_image+"");
        }
    }

    // Upload the image to the remote database
    public void uploadImage(View view) {
        System.out.println("The fileName is `" + part_image+"`");
        File imageFile = new File(part_image);                                                          // Create a file using the absolute path of the image
        RequestBody reqBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("value", imageFile.getName(), reqBody);
        RequestBody users_id = RequestBody.create(MediaType.parse("text/plain"), usersId);
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), "Foto Identitas");
        Interface api = Client.getApiServices();
        Call<UploadResponse> uploadImage = api.uploadImage(partImage,users_id,desc);
        uploadImage.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                Log.d(TAG, "onCall: "+call.request().body());
                if(response.isSuccessful()){
                    lyLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: "+response.body().getStatus());
                    if(response.body().getStatus().equals("success")){
                        /*Log.v("log softgain : ", String.valueOf(response.body().getSuccess().getToken()));*/
                        Toast.makeText(getApplicationContext(),
                                "Upload berhasil",Toast.LENGTH_SHORT).show();
                        Intent nn = new Intent(UploadActivity.this, UploadResultActivity.class);
                        startActivity(nn);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext() ,response.body().getMessages(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext() ,"Upload gagal",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                /*Log.v("log softgain : ", String.valueOf(t));*/
                lyLoading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error "+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    void getData(){
        lyLoading.setVisibility(View.VISIBLE);
        mApiInterface = Client.getClient().create(Interface.class);
        Call<CheckResponse> postLoginExe = mApiInterface.checkResponse(usersId);
        postLoginExe.enqueue(new Callback<CheckResponse>() {
            @Override
            public void onResponse(Call<CheckResponse> call, Response<CheckResponse> response) {
                if(response.isSuccessful()){
                    lyLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: "+response.body().getStatus());
                    if(response.body().getStatus().equals("success")){
                        /*Log.v("log softgain : ", String.valueOf(response.body().getSuccess().getToken()));*/
                        hasUpload=true;
                        Intent nn = new Intent(UploadActivity.this, UploadResultActivity.class);
                        startActivity(nn);
                        finish();
                    }else{
                        hasUpload=false;
                    }
                }else{
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext() ,"Login gagal",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckResponse> call, Throwable t) {
                /*Log.v("log softgain : ", String.valueOf(t));*/
                lyLoading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error "+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}