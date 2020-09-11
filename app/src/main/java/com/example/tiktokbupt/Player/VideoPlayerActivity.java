package com.example.tiktokbupt.Player;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tiktokbupt.R;
import com.example.tiktokbupt.VideoInfo;
import com.example.tiktokbupt.VideoInfoManager;

public class VideoPlayerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);

        ViewPager2 pager = findViewById(R.id.viewpager);
        FragmentStateAdapter pagerAdapter = new VideoPlayerAdapter(this);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(VideoInfoManager.getSharedManager().getLastClickedItem(), false);
        pager.setOffscreenPageLimit(1); //预加载
    }
}
