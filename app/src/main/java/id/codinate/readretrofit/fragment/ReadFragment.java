/*
 * Copyright (c) 2017. Gilang Ramadhan (gilangramadhan96.gr@gmail.com)
 */

package id.codinate.readretrofit.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
                if(response.isSuccessful()){
                    mProgressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        String result = object.getString("error");
                        if (result.equalsIgnoreCase("false")){
                            JSONArray jsonArray = object.getJSONArray("pohon");
                            for(int i = 0; i <jsonArray.length(); i++){
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
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
                        }else {
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
    private void setListAdapter(ArrayList<HashMap<String, String>> data) {
        adapter = new CustomAdapter(mContext, data);
//        listView = (ListView) v.findViewById(R.id.listView); di onCreat
        listView.setAdapter(adapter);
    }

    private class CustomAdapter extends BaseAdapter {
        Context context;
        ArrayList<HashMap<String,String>> dataPohon;
        public CustomAdapter(Context Context, ArrayList<HashMap<String, String>> data){
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
            LayoutInflater inflater= (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.list_item, null);
            TextView ID = (TextView) v.findViewById(R.id.txtID);
            TextView Jenis = (TextView) v.findViewById(R.id.txtJenis);
            TextView Kondisi = (TextView) v.findViewById(R.id.txtKondisi);
            TextView Usia = (TextView) v.findViewById(R.id.txtUsia);

            final HashMap<String, String> hasil;
            hasil = dataPohon.get(position);
            ID.setText(hasil.get("id"));
            Jenis.setText(hasil.get("jenis_pohon"));
            Kondisi.setText(hasil.get("kondisi_pohon"));
            Usia.setText(hasil.get("usia_pohon"));
            return v;
        }
    }

}
