/*
 * Copyright (c) 2017. Gilang Ramadhan (gilangramadhan96.gr@gmail.com)
 */

package id.codinate.readretrofit.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import id.codinate.readretrofit.R;
import id.codinate.readretrofit.apihelper.BaseApiService;
import id.codinate.readretrofit.apihelper.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadFragment extends Fragment {
    Context mContext;
    BaseApiService mApiService;
    ProgressDialog mProgressDialog;
    private ListView listView;
    private ArrayList<HashMap<String, String>> data;

    public ReadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_read, container, false);
        mContext = getActivity();
        mApiService = UtilsApi.getApiService();
        data = new ArrayList<>();
        listView = (ListView) v.findViewById(R.id.listView);
        mProgressDialog = ProgressDialog.show(mContext, null, "Loading", true, false);
        mApiService.readPohon().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    mProgressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        String result = object.getString("error");
                        if (result.equalsIgnoreCase("false")) {
                            JSONArray jsonArray = object.getJSONArray("pohon");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String uuid = jsonObject.getString("uuid");
                                String jenis_pohon = jsonObject.getString("jenis_pohon");
                                String usia_pohon = jsonObject.getString("usia_pohon");
                                String kondisi_pohon = jsonObject.getString("kondisi_pohon");
                                String longitude = jsonObject.getString("longitude");
                                String latitude = jsonObject.getString("latitude");
                                String foto_pohon = jsonObject.getString("foto_pohon");
                                String tgl = jsonObject.getString("tgl");
                                String keterangan = jsonObject.getString("keterangan");
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("id", id);
                                map.put("uuid", uuid);
                                map.put("jenis_pohon", jenis_pohon);
                                map.put("usia_pohon", usia_pohon);
                                map.put("kondisi_pohon", kondisi_pohon);
                                map.put("longitude", longitude);
                                map.put("latitude", latitude);
                                map.put("foto_pohon", foto_pohon);
                                map.put("tgl", tgl);
                                map.put("keterangan", keterangan);
                                data.add(map);
                                setListAdapter(data);
                            }
                        } else {
                            String error = object.getString("error_msg");
                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("debug", "On Respon : Gagal");
                mProgressDialog.dismiss();
            }
        });
        return v;
    }

    private CustomAdapter adapter;

    private void setListAdapter(final ArrayList<HashMap<String, String>> data) {
        adapter = new CustomAdapter(mContext, data);
//        listView = (ListView) v.findViewById(R.id.listView); di onCreat
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Peringatan");
                alertDialog.setMessage("Lakukan Aksi");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "HAPUS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "LIHAT DATA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        final HashMap<String, String> hasil = data.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("hashmap", hasil);
                        //set Fragmentclass Arguments
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        LihatFragment fragment = new LihatFragment();
                        fragment.setArguments(bundle);

                        transaction.replace(R.id.container, fragment);
                        transaction.addToBackStack(null).commit();
                    }
                });
                alertDialog.show();
            }
        });
    }

    private class CustomAdapter extends BaseAdapter {
        Context context;
        ArrayList<HashMap<String, String>> dataPohon;

        public CustomAdapter(Context Context, ArrayList<HashMap<String, String>> data) {
            this.context = Context;
            this.dataPohon = data;
        }

        @Override
        public int getCount() {
            return dataPohon.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.list_item, null);
            TextView Jenis = (TextView) v.findViewById(R.id.txtJenis);
            TextView Kondisi = (TextView) v.findViewById(R.id.txtKondisi);
            TextView Usia = (TextView) v.findViewById(R.id.txtUsia);
            ImageView gambar = (ImageView) v.findViewById(R.id.imgView);

            final HashMap<String, String> hasil;
            hasil = dataPohon.get(position);
            Jenis.setText(hasil.get("jenis_pohon"));
            Kondisi.setText(hasil.get("kondisi_pohon"));
            Usia.setText(hasil.get("usia_pohon") + " th");
            String url = "http://192.168.43.233/appandroid/gambar/" + hasil.get("foto_pohon");
            Picasso.with(getActivity()).load(url).error(R.mipmap.ic_launcher).into(gambar);
            return v;
        }
    }

}
