/*
 * Copyright (c) 2017. Gilang Ramadhan (gilangramadhan96.gr@gmail.com)
 */

package id.codinate.readretrofit;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import id.codinate.readretrofit.apihelper.BaseApiService;
import id.codinate.readretrofit.apihelper.UploadObject;
import id.codinate.readretrofit.apihelper.UtilsApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class FormActivity extends AppCompatActivity {
    EditText usia, jenis, keterangan;
    ProgressDialog mProgressDialog;
    Spinner kondisi;
    Context mContext;
    ImageView gambar;
    Button cari, submit;
    private int PICK_IMAGE_REQUEST = 1;
    Uri uri = null;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    String USIA, JENIS, KETERANGAN, KONDISI;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        usia = (EditText) findViewById(R.id.edtUsiaPohon);
        jenis = (EditText) findViewById(R.id.edtJenisPohon);
        keterangan = (EditText) findViewById(R.id.edtKeteranganPohon);
        kondisi = (Spinner) findViewById(R.id.kondisiPohon);
        gambar = (ImageView) findViewById(R.id.gambarPreview);
        cari = (Button) findViewById(R.id.btnCariGambar);
        submit = (Button) findViewById(R.id.btnSubmit);
        mApiService = UtilsApi.getApiService();
        mContext = FormActivity.this;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.jenis_pohon, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kondisi.setAdapter(adapter);
        kondisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] sArray = getResources().getStringArray(R.array.jenis_pohon);
                KONDISI = sArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                KONDISI = "";
            }
        });

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USIA = usia.getText().toString();
                JENIS = jenis.getText().toString();
                KETERANGAN = keterangan.getText().toString();
                if (USIA.matches("")) {
                    Toast.makeText(getApplicationContext(), "USIA MASIH KOSONG", Toast.LENGTH_SHORT).show();
                } else if (KONDISI.matches("")) {
                    Toast.makeText(getApplicationContext(), "KONDISI MASIH KOSONG", Toast.LENGTH_SHORT).show();
                } else if (KETERANGAN.matches("")) {
                    Toast.makeText(getApplicationContext(), "KETERANGAN MASIH KOSONG", Toast.LENGTH_SHORT).show();
                } else if (JENIS.matches("")) {
                    Toast.makeText(getApplicationContext(), "JENIS MASIH KOSONG", Toast.LENGTH_SHORT).show();
                } else if (uri == null) {
                    Toast.makeText(getApplicationContext(), "PILIH GAMBAR", Toast.LENGTH_SHORT).show();
                } else {
                    //Upload
                    mProgressDialog = ProgressDialog.show(mContext, null, "Loading", true, false);
                    String filePath = getRealPathFromURIPath(uri, FormActivity.this);
                    File file = new File(filePath);
                    Log.d("HASIL", "File Name " + file.getName());
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                    RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                    mContext = getApplicationContext();
                    mApiService = UtilsApi.getApiService();
                    mApiService.uploadFile(fileToUpload, filename).enqueue(new Callback<UploadObject>() {
                        @Override
                        public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                            Toast.makeText(getApplicationContext(), "Response " + response.raw().message(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "Success " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<UploadObject> call, Throwable t) {
                            Log.d("TAG", "Error " + t.getMessage());
                            mProgressDialog.dismiss();
                        }
                    });

//                    Retrofit retrofit = new Retrofit.Builder()
//                            .baseUrl("http://192.168.43.233/appandroid/")
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build();
//                    BaseApiService uploadImage = retrofit.create(BaseApiService.class);
//                    Call<UploadObject> fileUpload = uploadImage.uploadFile(fileToUpload, filename);
//                    fileUpload.enqueue(new Callback<UploadObject>() {
//                        @Override
//                        public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
//
//                        }
//                        @Override
//                        public void onFailure(Call<UploadObject> call, Throwable t) {
//
//                        }
//                    });
                }
            }
        });
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                gambar.setImageBitmap(bitmap);
                gambar.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
