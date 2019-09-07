package com.anshultiwari.androidassignment.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anshultiwari.androidassignment.Model.Celebrity;
import com.anshultiwari.androidassignment.R;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CelebsAdapter extends RecyclerView.Adapter<CelebsAdapter.ChatUserViewHolder> {
    private static final String TAG = "CelebsAdapter";
    private Context mContext;
    private List<Celebrity> mCelebrityList;


    public CelebsAdapter(Context context, List<Celebrity> celebrityList) {
        mContext = context;
        this.mCelebrityList = celebrityList;
    }

    @NonNull
    @Override
    public ChatUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.celeb_item, parent, false);
        return new ChatUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatUserViewHolder holder, int position) {
        Celebrity celeb = mCelebrityList.get(position);
        String height = celeb.getHeight();

        holder.heightTextView.setText(centimeterToFeet(height));
        holder.ageTextView.setText(String.format("%s years old", celeb.getAge()));
        holder.popularityTextView.setText(String.format("%s%% popularity", celeb.getPopularity()));
        Glide.with(mContext).load(celeb.getImageUrl()).into(holder.celebImageView);

    }

    @Override
    public int getItemCount() {
        return mCelebrityList.size();
    }

    class ChatUserViewHolder extends RecyclerView.ViewHolder {
        private TextView heightTextView;
        private TextView ageTextView;
        private TextView popularityTextView;
        private ImageView celebImageView;

        ChatUserViewHolder(View view) {
            super(view);

            heightTextView = view.findViewById(R.id.celeb_height);
            ageTextView = view.findViewById(R.id.celeb_age);
            popularityTextView = view.findViewById(R.id.celeb_popularity);
            celebImageView = view.findViewById(R.id.celeb_image);

        }

    }

    private String centimeterToFeet(String centemeter) {
        int feetPart = 0;
        int inchesPart = 0;
        if(!TextUtils.isEmpty(centemeter)) {
            double dCentimeter = Double.valueOf(centemeter);
            feetPart = (int) Math.floor((dCentimeter / 2.54) / 12);
            System.out.println((dCentimeter / 2.54) - (feetPart * 12));
            inchesPart = (int) Math.ceil((dCentimeter / 2.54) - (feetPart * 12));
        }
        return String.format("%d' %d''", feetPart, inchesPart);
    }

}
