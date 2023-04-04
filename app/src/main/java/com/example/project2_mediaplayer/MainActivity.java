package com.example.project2_mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.project2_mediaplayer.transformer.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION_CODE=10;
    private ViewPager2 mViewpager2;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermiss();
        mViewpager2 = findViewById(R.id.view_pager_2);
        mViewpager2.setPageTransformer(new ZoomOutPageTransformer());
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(this);
        mViewpager2.setAdapter(myViewPagerAdapter);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.bottom_listmusic) {
                    mViewpager2.setCurrentItem(0);

                } else if (id == R.id.bottom_author) {
                    mViewpager2.setCurrentItem(1);

                } else if (id == R.id.bottom_setting) {
                    mViewpager2.setCurrentItem(2);
                }
                return false;
            }
        });

        mViewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_listmusic).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_author).setChecked(true);

                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_setting).setChecked(true);
                        break;

                }
            }
        });
    }
    //checkpermission granted ?
    private void getPermiss(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return;
        }
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        else{
            String[] permission={Manifest.permission.POST_NOTIFICATIONS};
            requestPermissions(permission,REQUEST_PERMISSION_CODE);
        }
    }


}