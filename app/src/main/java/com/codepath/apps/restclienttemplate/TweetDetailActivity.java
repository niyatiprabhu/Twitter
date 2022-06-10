package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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

        tvScreenNameDetails.setText(tweet.user.screenName);
        tvNameDetails.setText(tweet.user.name);
        tvTimeStampDetails.setText(tweet.createdAt);
        tvBodyDetails.setText(tweet.body);
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .transform(new RoundedCorners(400))
                .into(ivProfilePictureDetails);
        if (!tweet.mediaUrl.equals("none")) {
            Glide.with(this)
                    .load(tweet.mediaUrl)
                    .transform(new RoundedCorners(20))
                    .into(ivMediaDetails);
        } else {
            ivMediaDetails.setVisibility(View.GONE);

        }
    }
}