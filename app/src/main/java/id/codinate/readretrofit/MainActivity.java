/*
 * Copyright (c) 2017. Gilang Ramadhan (gilangramadhan96.gr@gmail.com)
 */

package id.codinate.readretrofit;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import id.codinate.readretrofit.fragment.ReadFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = new ReadFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null).commit();
    }
}
