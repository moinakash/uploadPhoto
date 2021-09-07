package com.project.update;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    Button PickImage, SaveImage;

    FileService fileService;

    String imagePath;

    int IMG_REQUEST = 21;

    ImageView imageView;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PickImage = findViewById(R.id.idPickImage);
        SaveImage = findViewById(R.id.idUpload);
        imageView = findViewById(R.id.idImageView);

        fileService = APIUtils.getFileServices();


        PickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




/*
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,27);
*/

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMG_REQUEST);



            }
        });

      //  String User_id = "36";

        SaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), ""+imagePath, Toast.LENGTH_SHORT).show();


               file = new File(imagePath);

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("photo",file.getName(),requestBody);

                RequestBody User_id = RequestBody.create(MediaType.parse("text/plain"), "36");
                RequestBody Name = RequestBody.create(MediaType.parse("text/plain"), "This is a new Image");
                RequestBody Contact = RequestBody.create(MediaType.parse("text/plain"), "01818121241");
                RequestBody Type = RequestBody.create(MediaType.parse("text/plain"), "user");
                RequestBody Password = RequestBody.create(MediaType.parse("text/plain"), "12345678");


                Call<FileInfo> call = fileService.upload(body,User_id,Name,Contact,Type,Password);

                call.enqueue(new Callback<FileInfo>() {
                    @Override
                    public void onResponse(Call<FileInfo> call, Response<FileInfo> response) {

                        if (response.isSuccessful()){

                            Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<FileInfo> call, Throwable t) {

                        Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK){

            if (data == null){

                Toast.makeText(this, "Unable to choose image!", Toast.LENGTH_SHORT).show();

                return;
            }

            Uri imageUri = data.getData();
            imagePath = getRealPathFrom(imageUri);

            Toast.makeText(getApplicationContext(), ""+imagePath, Toast.LENGTH_SHORT).show();
        }
    }*/

   /* private String getRealPathFrom(Uri uri){

        String[] projection = {MediaStore.Images.Media.DATA};

        CursorLoader loader = new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int Colum_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(Colum_idx);
        cursor.close();

        return result;
    }*/







    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){

            Uri path = data.getData();

            imagePath = getPath(path);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);

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


}