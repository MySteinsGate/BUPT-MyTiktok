package com.example.tiktokbupt.Player;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;


public class VideoPlayerView extends VideoView {
    private int curPosition = 0; //当前播放的位置
    public PlayerOnPreparedCallBack callBack;

    public VideoPlayerView(Context context) {
        super(context);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void InitPreparedListener() {
        this.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.seekTo(curPosition, MediaPlayer.SEEK_CLOSEST);
                mp.setLooping(true);
                curPosition = 0;
                callBack.playerOnPrepared();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public void holdPosition() {
        curPosition = this.getCurrentPosition();
    }

    public void setCurPosition(int position) {
        this.curPosition = position;
    }

    public interface PlayerOnPreparedCallBack {
        void playerOnPrepared();
    }
}