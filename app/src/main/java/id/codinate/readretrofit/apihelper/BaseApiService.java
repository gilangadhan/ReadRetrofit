/*
 * Copyright (c) 2017. Gilang Ramadhan (gilangramadhan96.gr@gmail.com)
 */

package id.codinate.readretrofit.apihelper;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Gilang Ramadhan on 17/05/2017.
 */

public interface BaseApiService {
    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registerRequest(@Field("email") String email,
                                       @Field("password") String password,
                                       @Field("nama") String nama);

    @FormUrlEncoded
    @POST("insert.php")
    Call<ResponseBody> insertPohon(@Field("id") String id,
                                   @Field("uuid") String uuid,
                                   @Field("jenis_pohon") String jenis_pohon,
                                   @Field("usia_pohon") String usia_pohon,
                                   @Field("kondisi_pohon") String kondisi_pohon,
                                   @Field("latitude") String latitude,
                                   @Field("longitude") String longitude,
                                   @Field("foto_pohon") String foto_pohon,
                                   @Field("keterangan") String keterangan
    );






    //nambah

    @GET("lihat.php")
    Call<ResponseBody> readPohon();

    @Multipart
    @POST("upload.php")
    Call<UploadObject> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);
}
