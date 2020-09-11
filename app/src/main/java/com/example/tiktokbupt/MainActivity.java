package com.example.tiktokbupt;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.tiktokbupt.Network.RawDataFetcher;
import com.example.tiktokbupt.Network.RawDataFetcherInterface;
import com.example.tiktokbupt.Player.VideoPlayerActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {
    private TextView statusTv;
    private RecyclerView videoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        if (bar != null) { bar.hide(); }

        videoListView = findViewById(R.id.video_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        videoListView.setLayoutManager(layoutManager); //设置布局管理器

        statusTv = findViewById(R.id.status_tv);
        statusTv.setText("加载中...");

        URL url = null;
        try {
            url = new URL("https://beiyou.bytedance.com/api/invoke/video/invoke/video");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        VideoInfoManager.getSharedManager().fetchVideoInfo(url);
        List<VideoInfo> vidInfo = VideoInfoManager.getSharedManager().getVideoInfo();
        if (vidInfo == null) {
            statusTv.setText("加载错误，请检查网络连接");
        } else {
            statusTv.setVisibility(View.INVISIBLE);
            VideoItemAdapter adapter = new VideoItemAdapter(this);
            videoListView.setAdapter(adapter);
        }
    }

    @Override
    public void onListItemClick(Integer pos) {
        VideoInfoManager.getSharedManager().setLastClickedItem(pos);
        startActivity(new Intent(MainActivity.this, VideoPlayerActivity.class));
    }
}
