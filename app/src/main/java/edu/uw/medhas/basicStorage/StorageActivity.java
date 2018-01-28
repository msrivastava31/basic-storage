package edu.uw.medhas.basicStorage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class StorageActivity extends AppCompatActivity {
    private View mContainerview;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_settings:
                    startSettingsFragment();
                    return true;
                case R.id.navigation_media:
                    startMediaFragment();
                    return true;
            }
            return false;
        }
    };

    private void startMediaFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(mContainerview.getId(), new MediaFragment())
                .commit();
    }

    private void startSettingsFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(mContainerview.getId(), new SettingsFragment())
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mContainerview = findViewById(R.id.storage_container);

        startSettingsFragment();
    }

}
