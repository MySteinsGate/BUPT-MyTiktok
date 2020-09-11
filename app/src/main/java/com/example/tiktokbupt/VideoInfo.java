package com.example.tiktokbupt;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class VideoInfo {
    @SerializedName("_id")
    private final String id; //视频id
    @SerializedName("feedurl")
    private final String videoUrl; //视频url
    @SerializedName("nickname")
    private final String nickName; //作者昵称
    @SerializedName("description")
    private final String description; //视频描述
    @SerializedName("likecount")
    private final int likeCount; //此视频被喜欢的次数
    @SerializedName("avatar")
    private final String coverUrl; //封面url

    //得到详细信息进行构造
    public VideoInfo(String id, String videoUrl, String nickName,
                     String description, int likeCount, String coverUrl) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.nickName = nickName;
        this.description = description;
        this.likeCount = likeCount;
        this.coverUrl = coverUrl;
    }

    public static ArrayList<VideoInfo> fromJson(String rawJson) {
        ArrayList<VideoInfo> allVideoInfo
                = new Gson().fromJson(rawJson, new TypeToken<ArrayList<VideoInfo>>(){}.getType());

        return allVideoInfo;
    }

    //以下为访问器
    public String getId() {
        return this.id;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getDescription() {
        return this.description;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }
}
