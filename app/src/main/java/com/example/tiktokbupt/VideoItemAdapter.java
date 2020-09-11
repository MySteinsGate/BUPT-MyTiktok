package com.example.tiktokbupt;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.ItemViewHolder> {
    //用于Glide的with参数和响应点击事件(实现了ListItemClickListener接口)
    private final Activity parentActivity;

    /**
     *
     * @param parentAc 父级Activity,用于Glide的with参数和响应点击事件(实现了ListItemClickListener接口)
     */
    public VideoItemAdapter(Activity parentAc) {
        this.parentActivity = parentAc;
    }

    private List<VideoInfo> getVideoInfo() {
        return VideoInfoManager.getSharedManager().getVideoInfo();
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return 已经加载了 "播放图片" 的item
     * 因为每个item肯定都有一个播放按钮, 所以在Create的时候就可把播放图片加载出来
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);


        ItemViewHolder item = new ItemViewHolder(view); //新建itemHolder
        Glide.with(parentActivity)
                .load(R.drawable.play)
                .apply(RequestOptions.circleCropTransform()) //圆形
                .into(item.playImage);

        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(getVideoInfo().get(position));
    }

    @Override
    public int getItemCount() {
        return getVideoInfo().size();
    }

    //ItemViewHolder作为Adapter的内部类
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView coverImage; //video cover image
        private final ImageView maskImage; // mask image
        private final ImageView playImage;  //play image
        private final TextView authorText;  //author text
        //TODO 视频简介和❤数量

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.coverImage = itemView.findViewById(R.id.cover); //封面图控件
            this.maskImage = itemView.findViewById(R.id.mask); //黑边蒙版控件
            this.playImage = itemView.findViewById(R.id.play); //视频播放按钮图控件
            this.authorText = itemView.findViewById(R.id.author); //视频作者信息控件

            itemView.setOnClickListener(this); //设置ClickListener
        }

        public void bind(VideoInfo videoInfo) {
            Glide.with(parentActivity)
                    .load(videoInfo.getCoverUrl())
                    .into(coverImage);

            Glide.with(parentActivity)
                    .load(R.drawable.thumbnail_mask)
                    .into(maskImage);

            authorText.setText(videoInfo.getNickName()); //显示视频作者信息
        }

        @Override
        public void onClick(View v) {
            if(parentActivity != null) {
                ((ListItemClickListener)parentActivity).onListItemClick(getAdapterPosition()); //回调
            }
        }
    }
}
