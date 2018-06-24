/*
 * Copyright (c) 2018 Iarratais Development
 *
 * karl.development@gmail.com
 */

package net.karljones.stvtestapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.karljones.stvtestapplication.MainActivity;
import net.karljones.stvtestapplication.R;
import net.karljones.stvtestapplication.templates.Article;

import java.util.ArrayList;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MyViewHolder>{

    private ArrayList<Article> articles;
    private ClickListener clickListener;
    private Context context;

    public MainListAdapter(ArrayList<Article> articles, Context context){
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public MainListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainListAdapter.MyViewHolder holder, int position) {
        final int pos = position;

        Glide.with(((MainActivity)context)).load(articles.get(pos).getArticleImages().getUrl()).into(holder.ImageView_articleImage);

        holder.TextView_articleTitle.setText(articles.get(pos).getTitle());

        holder.TextView_articleSubHeadline.setText(articles.get(pos).getSubHeadline());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.ItemClicked(v, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView TextView_articleTitle, TextView_articleSubHeadline;
        public ImageView ImageView_articleImage;

        public MyViewHolder(View view){
            super(view);

            ImageView_articleImage = view.findViewById(R.id.list_item_main_list_item_article_image);

            TextView_articleTitle = view.findViewById(R.id.list_item_main_list_item_headline);
            TextView_articleSubHeadline = view.findViewById(R.id.list_item_main_list_item_subheadline);
        }
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }
}
