package com.example.phpmysql;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    CardView cardView;
    ImageView image;
    Dialog dialog;
    FurnitureAdapter furnitureAdapter;
    int REQUEST_CODE_PMFOLDER = 123;
    String realpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        cardView = findViewById(R.id.add);

        furnitureAdapter = new FurnitureAdapter(this, R.layout.card);

        LoadData();

        listView.setAdapter(furnitureAdapter);

        cardView.setOnClickListener(v ->{
           OpenDiaLog(true, null);
        });
    }

    private void LoadData() {
        furnitureAdapter.clear();
        Call<List<Furniture>> call = Retrofit.retrofit.getFurnitures();

        call.enqueue(new Callback<List<Furniture>>() {
            @Override
            public void onResponse(Call<List<Furniture>> call, Response<List<Furniture>> response) {
                furnitureAdapter.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Furniture>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        furnitureAdapter.notifyDataSetChanged();
    }


    public void OpenDiaLog(Boolean isAdd, Furniture f) {
        realpath = "";

        CreateDialog(R.layout.dialog);

        Button cancel = dialog.findViewById(R.id.dialog_cancel);
        TextView typeDialog = dialog.findViewById(R.id.type_dialog);
        Button add = dialog.findViewById(R.id.dialog_add);
        image = dialog.findViewById(R.id.dialog_img);
        EditText edtname = dialog.findViewById(R.id.dialog_name);
        EditText edtdetail = dialog.findViewById(R.id.dialog_detail);
        EditText edtprice = dialog.findViewById(R.id.dialog_price);

        cancel.setOnClickListener(v->{
            CloseDialog();
        });

        if(!isAdd){
            typeDialog.setText("Update Furniture");
            add.setText("Update");
        }

        if(f != null){
            edtname.setText(f.getName());
            Picasso.get().load(f.getImage()).into(image);
            edtdetail.setText(f.getDetail());
            edtprice.setText(f.getPrice().toString());
        }

        add.setOnClickListener(v->{
            String name = edtname.getText().toString();
            String detail = edtdetail.getText().toString();
            String price = edtprice.getText().toString();
            if(!isAdd){
                if(realpath.equals("")){
                    UpdateFurniture(name, detail, Float.parseFloat((price)),f.getImage(), f.getId());
                }else{
                    UpdateFurniture(name, detail, Float.parseFloat((price)),UploadImage(), f.getId());
                }
            }else{
                if(name.trim().equals("")){
                    edtname.setError("Please input name");
                }else if(detail.trim().equals("")){
                    edtdetail.setError("Please input detail");
                }else if(price.trim().equals(""))
                {
                    edtprice.setError("Please input price");
                }else{
                    AddFurniture(name, detail, Float.parseFloat((price)));
                }
            }
        });

        image.setOnClickListener(v->{
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PMFOLDER);
        });

        dialog.show();
    }

    private String UploadImage() {
        if (!realpath.equals("")){
            File file = new File(realpath);
            String filePath = file.getAbsolutePath();


            String[] arrayFileName = filePath.split("\\.");
            filePath = arrayFileName[0] + System.currentTimeMillis() + "." + arrayFileName[1];
            String[] fileNameArray = filePath.split("/");
            String filePathSv = Retrofit.url+ "img/" + fileNameArray[fileNameArray.length - 1];

            RequestBody requestBody =  RequestBody.create(MediaType.parse("multipart/form-data"),file);

            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("uploaded_file",filePath,requestBody);

            Call<String> call = Retrofit.retrofit.uploadImage(multipartBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Upload image fail: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            return filePathSv;
        }
        return "";
    }

    public void CreateDialog(int layout){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.setCancelable(false);

        final Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setDimAmount(0.5f);
        window.setAttributes(wlp);

        ((ViewGroup)dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(
                        this,android.R.anim.slide_in_left));
    }



    public void CloseDialog() {
        ((ViewGroup)dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(
                        MainActivity.this,android.R.anim.slide_out_right));

        CountDownTimer countDownTimer = new CountDownTimer(400,400) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        };
        countDownTimer.start();
    }


    private void AddFurniture(String name, String detail, Float price) {
        Call<String> call = Retrofit.retrofit.insertFuniture(name, detail, price, UploadImage());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body().equals("Insert Sucess")){
                    Toast.makeText(MainActivity.this, "Add success", Toast.LENGTH_SHORT).show();
                    CloseDialog();
                    LoadData();
                }else{
                    Toast.makeText(MainActivity.this, "Add failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Add failed: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void UpdateFurniture(String name, String detail, Float price,String image, int id) {
        Call<String> call = Retrofit.retrofit.updateFuniture(name,detail,price,image,id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body().equals("Update Sucess")){
                    Toast.makeText(MainActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                    CloseDialog();
                    LoadData();
                }else{
                    Toast.makeText(MainActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Update failed: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void OpenDialogDelete(int id){
        CreateDialog(R.layout.dialog_delete);

        Button yes = dialog.findViewById(R.id.dialog_yes);
        Button no = dialog.findViewById(R.id.dialog_no);

        yes.setOnClickListener(v->{
            DeleteFurniture(id);
            CloseDialog();
            LoadData();
        });

        no.setOnClickListener(v->{
            CloseDialog();
        });

        dialog.show();
    }

    public void DeleteFurniture(int id){
        Call<String> call = Retrofit.retrofit.deleteFuniture(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body().equals("Delete Sucess")){
                    Toast.makeText(MainActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Delete Failed: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PMFOLDER && permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            someActivityResultLauncher.launch(intent);
        }else{
            Toast.makeText(getApplicationContext(),"Ban chua cap quyen chon hinh anh",Toast.LENGTH_LONG).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        realpath = getRealPathFromURI(uri);

                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            image.setImageBitmap(bitmap);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            });

    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}