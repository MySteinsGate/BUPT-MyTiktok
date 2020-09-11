package com.example.tiktokbupt;

import com.example.tiktokbupt.Network.RawDataFetcher;
import com.example.tiktokbupt.Network.RawDataFetcherInterface;
import com.example.tiktokbupt.Player.VideoPlayerAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VideoInfoManager implements RawDataFetcherInterface {

    private static ArrayList<VideoInfo> infoList = null;
    private static VideoInfoManager selfInstance = null;
    private Integer lastClickedItem = null;

    private VideoInfoManager() {}
    public static VideoInfoManager getSharedManager() {
        if (selfInstance == null) {
            selfInstance = new VideoInfoManager();
        }
        return selfInstance;
    }

    public List<VideoInfo> getVideoInfo() {
        return infoList;
    }

    public void fetchVideoInfo(URL url) {
        RawDataFetcher fetcher = new RawDataFetcher();
        fetcher.fetch(url, this);
    }

    public void setLastClickedItem(Integer i) {
        lastClickedItem = i;
    }

    public Integer getLastClickedItem() {
        return lastClickedItem;
    }

    @Override
    public void onFetchSuccess(String rawJson) {
        infoList = VideoInfo.fromJson(rawJson);
    }

    @Override
    public void onFetchFailure() {}
}
