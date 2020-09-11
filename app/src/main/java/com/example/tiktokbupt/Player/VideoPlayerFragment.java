package com.example.tiktokbupt.Player;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.tiktokbupt.R;
import com.example.tiktokbupt.VideoInfo;
import com.example.tiktokbupt.VideoInfoManager;

import java.util.ArrayList;
import java.util.Locale;

public class VideoPlayerFragment extends Fragment implements VideoPlayerView.PlayerOnPreparedCallBack {

    private TextView nicknameTv; // 昵称
    private TextView descriptionTv; // 简介
    private ImageView likeImgview; // 点赞图标
    private TextView likeCountTv; // 点赞数
    private ImageView loadPlaceholder;
    private VideoPlayerView vidPlayerView; //视频播放
    private GestureDetector detector; //手势检测
    private RelativeLayout relativeLayout;
    private ArrayList<ImageView> allHearts; //爱心s

    private boolean liked = false;
    private final Integer position;

    private int curProgress;

    VideoPlayerFragment(Integer position) {
        this.position = position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(
                R.layout.fragment_videoplayer, container, false
        );

        relativeLayout = rootView.findViewById(R.id.relative_layout);
        allHearts = new ArrayList<ImageView>();

        // 视频信息控件
        VideoInfo info = VideoInfoManager.getSharedManager().getVideoInfo().get(position);
        nicknameTv = rootView.findViewById(R.id.nickname_tv);
        nicknameTv.setText("@" + info.getNickName());
        descriptionTv = rootView.findViewById(R.id.description_tv);
        descriptionTv.setText(info.getDescription());
        likeImgview = rootView.findViewById(R.id.like_icon_imgv);
        likeImgview.setImageResource(R.drawable.heart);
        likeCountTv = rootView.findViewById(R.id.like_count_tv);
        likeCountTv.setText(likeCountToString(info.getLikeCount()));
        likeImgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like(false);
            }
        });

        // 加载动画
        loadPlaceholder = rootView.findViewById(R.id.loading_imgview);
        loadPlaceholder.setImageResource(R.drawable.loading_placeholder);
        Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        loadPlaceholder.startAnimation(rotate);

        // 播放器与点击事件
        vidPlayerView = rootView.findViewById(R.id.video_player_view);
        vidPlayerView.callBack = this;
        //获取VideoUri
        VideoInfoManager manager = VideoInfoManager.getSharedManager();
        final Uri videoUri =
                Uri.parse(manager.getVideoInfo().get(position).getVideoUrl());
        vidPlayerView.setVideoURI(videoUri); //设置VideoUri
        vidPlayerView.InitPreparedListener(); //初始化视频监听
        detector = new GestureDetector(this.getContext(), new videoClickGesture());
        vidPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        for (ImageView iv : allHearts) {
            iv.setVisibility(View.GONE);
        }
        //暂停并保存进度
        Log.d("black", "onPause");
        curProgress = vidPlayerView.getCurrentPosition();
        vidPlayerView.pause();
        vidPlayerView.resume();
    }

    @Override
    public void onStop() {
        super.onStop();
        vidPlayerView.setCurPosition(curProgress);
    }

    @Override
    public void onResume() {
        super.onResume();
        vidPlayerView.start();
    }

    @Override
    public void playerOnPrepared() {
        loadPlaceholder.clearAnimation();
        loadPlaceholder.setVisibility(View.GONE);
        nicknameTv.setVisibility(View.VISIBLE);
        descriptionTv.setVisibility(View.VISIBLE);
        likeImgview.setVisibility(View.VISIBLE);
        likeCountTv.setVisibility(View.VISIBLE);
    }

    private class videoClickGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            like(true);
            heartOnTouchCoordinate(e.getRawX(), e.getRawY());
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (vidPlayerView.isPlaying()) {
                vidPlayerView.pause();
            }
            else{
                vidPlayerView.start();
            }
            return true;
        }
    }

    private void like(boolean isDoubleClick) {
        if(liked && isDoubleClick) {
            return;
        }

        int color = liked ? R.color.colorWhite : R.color.colorLiked;
        Animation bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_anim);
        likeImgview.startAnimation(bounce);
        likeImgview.setColorFilter(getContext().getResources().getColor(color));

        liked = !liked;
    }

    private void heartOnTouchCoordinate(float rawX, float rawY) {
        final ImageView heartImgView = new ImageView(getContext()); //新建ImageView
        heartImgView.setLayoutParams(new RelativeLayout.LayoutParams(280, 280));
        heartImgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        int imgHeight = heartImgView.getHeight(); //控件高度 pixels
        int imgWidth = heartImgView.getWidth(); //控件宽度 pixels
        heartImgView.setX((float) (rawX - imgWidth / 2.0)); //控件坐标 X
        heartImgView.setY((float) (rawY - imgHeight / 2.0)); //控件坐标 Y

        heartImgView.setImageResource(R.drawable.heart); //心图
        heartImgView.setColorFilter(getContext().getResources().getColor(R.color.colorLiked));
        relativeLayout.addView(heartImgView);
        allHearts.add(heartImgView);

        Animation zoomOut = AnimationUtils.loadAnimation(getContext(), R.anim.zoomout_anim);
        heartImgView.startAnimation(zoomOut);
        zoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                allHearts.remove(heartImgView); //移除
                heartImgView.setVisibility(View.GONE); //移除(see onPause)
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    private String likeCountToString(int n) {
        if (n >= 1000000) {
            return String.format(Locale.getDefault(), "%.1fM", (float)n / 1000000);
        } else if (n >= 1000) {
            return String.format(Locale.getDefault(), "%.1fK", (float)n / 1000);
        } else {
            return String.valueOf(n);
        }
    }
}