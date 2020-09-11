package com.example.tiktokbupt.Player;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tiktokbupt.VideoInfo;
import com.example.tiktokbupt.VideoInfoManager;

import java.util.List;

public class VideoPlayerAdapter extends FragmentStateAdapter {

    VideoPlayerAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    public Fragment createFragment(int position) {
        return new VideoPlayerFragment(position);
    }

    @Override
    public int getItemCount() {
        return getVideoInfo().size();
    }

    private List<VideoInfo> getVideoInfo() {
        return VideoInfoManager.getSharedManager().getVideoInfo();
    }
}
