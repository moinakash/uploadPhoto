package com.project.update;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileService {

    @Multipart
    @POST("update")
    Call<FileInfo> upload(
            @Part MultipartBody.Part file,
            @Part("user_id") RequestBody user_id,
            @Part("name") RequestBody name,
            @Part("contact") RequestBody contact,
            @Part("type") RequestBody type,
            @Part("password") RequestBody password
           );

}
