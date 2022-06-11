package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    private ImageView ivProfilePictureDetails;
    private ImageView ivMediaDetails;
    private TextView tvScreenNameDetails;
    private TextView tvNameDetails;
    private TextView tvTimeStampDetails;
    private TextView tvBodyDetails;
    private ImageButton ibFavoriteDetails;
    private ImageButton ibReplyDetail;
    private ImageButton ibRetweetDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        ivProfilePictureDetails = findViewById(R.id.ivProfilePictureDetails);
        ivMediaDetails = findViewById(R.id.ivMediaDetails);
        tvScreenNameDetails = findViewById(R.id.tvScreenNameDetails);
        tvNameDetails = findViewById(R.id.tvNameDetails);
        tvTimeStampDetails = findViewById(R.id.tvTimestampDetails);
        tvBodyDetails = findViewById(R.id.tvBodyDetails);
        ibFavoriteDetails = findViewById(R.id.ibFavoriteDetails);
        ibReplyDetail = findViewById(R.id.ibReplyDetail);
        ibRetweetDetail = findViewById(R.id.ibRetweetDetail);



        tvScreenNameDetails.setText(tweet.user.screenName);
        tvNameDetails.setText(tweet.user.name);
        tvTimeStampDetails.setText(tweet.createdAt);
        tvBodyDetails.setText(tweet.body);
        if (tweet.isFavorited) {
            Drawable newImage = this.getDrawable(R.drawable.fullheart);
            ibFavoriteDetails.setBackground(newImage);
        } else {
            Drawable newImage = this.getDrawable(R.drawable.emptyheart);
            ibFavoriteDetails.setBackground(newImage);
        }

        Drawable newImage = this.getDrawable(R.drawable.reply);
        ibReplyDetail.setBackground(newImage);

        newImage = this.getDrawable(R.drawable.retweet);
        ibRetweetDetail.setBackground(newImage);

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .transform(new RoundedCorners(400))
                .into(ivProfilePictureDetails);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        (this).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        int mediaWidth = displayMetrics.widthPixels - 200;
        int mediaHeight = (int)(tweet.picSizeRatio * mediaWidth);

        if (!tweet.mediaUrl.equals("none")) {
            Glide.with(this)
                    .load(tweet.mediaUrl)
                    .transform(new RoundedCorners(30))
                    .override(mediaWidth, mediaHeight)
                    .into(ivMediaDetails);
        } else {
            ivMediaDetails.setVisibility(View.GONE);
        }
    }
}