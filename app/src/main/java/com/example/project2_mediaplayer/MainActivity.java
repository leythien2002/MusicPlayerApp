package com.example.project2_mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2_mediaplayer.transformer.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION_CODE=10;
    private ViewPager2 mViewpager2;
    private BottomNavigationView mBottomNavigationView;
    //layout music
    private boolean isPlaying;
    //prev and next btn

    private Music music;
    static public LinearLayout layoutMusic;
    private ImageView imgMusic;
    private TextView tvTitleSong,tvAuthorSong;
    private ImageButton btnStart,btnPrev,btnNext;
    private int index;
    static public ArrayList<Music> mListmusic;
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            if(bundle==null){
                return;
            }
            else{

                music= (Music) bundle.get("MusicObject");
//                isPlaying= (boolean) bundle.get("checkPlaying");
                index= (int) bundle.get("index");
                layoutMusic.setVisibility(View.VISIBLE);
                showInfo();
                startPlaying(music,index);

            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListmusic=MusicAdapter.mListmusic;
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter("send_song"));
        initUi();

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
    private void initUi(){
        layoutMusic=findViewById(R.id.layout_music);
        imgMusic=layoutMusic.findViewById(R.id.imgSong);
        tvAuthorSong=layoutMusic.findViewById(R.id.tvAuthorSong);
        tvTitleSong=layoutMusic.findViewById(R.id.tvTitleSong);
        btnStart=layoutMusic.findViewById(R.id.button_toggle_play_pause);
        //getSongfromAdapter
        layoutMusic.setVisibility(View.GONE);
        layoutMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaying(music,index);
            }
        });




    }
    private void showInfo(){

        if(music==null){
            return;
        }

        Picasso.with(this).load(music.getSongimage()).into(imgMusic);
        tvTitleSong.setText(music.getSongTitle());
        tvAuthorSong.setText(music.getAuthorName());

        if(isPlaying){
            btnStart.setImageResource(R.drawable.pause);
        }
        else{
            btnStart.setImageResource(R.drawable.ic_play_white);
        }
    }

    private void startPlaying(Music music, int index) {
        mListmusic=MusicAdapter.mListmusic;
        if( MusicPlaying.mediaPlayer!=null){
            MusicPlaying.mediaPlayer.reset();
            MusicPlaying.mediaPlayer.release();
            MusicPlaying.mediaPlayer=null;
        }
        Intent i=new Intent(this,MusicPlaying.class);
        Bundle bundle=new Bundle();
        bundle.putInt("index",index);
        bundle.putParcelableArrayList("ListSong", (ArrayList) mListmusic);

        i.putExtras(bundle);
        this.startActivity(i);
    }

    //checkpermission granted ?
//    private void getPermiss(){
//        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            String[] permission={Manifest.permission.POST_NOTIFICATIONS};
//            requestPermissions(permission,REQUEST_PERMISSION_CODE);
//        }
//    }


}