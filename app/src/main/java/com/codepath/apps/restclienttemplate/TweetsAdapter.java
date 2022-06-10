package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;



    Context context;
    List<Tweet> tweets;

    // pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweet) {
        this.context = context;
        this.tweets = tweet;
    }

    // for each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // bind values based one the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with viewholder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }


    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    public String getRelativeTimeAgo(String rawJsonDate) throws ParseException {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (ParseException e) {
            Log.i("adapter", "getRelativeTimeAgo failed");
            e.printStackTrace();
        }
        return "";
    }

    // define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivProfilePicture;
        TextView tvBody;
        TextView tvName;
        TextView tvScreenName;
        ImageView ivMedia;
        TextView tvTimestamp;
        ImageButton ibFavorite;
        TextView tvNumFavorites;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ibFavorite = itemView.findViewById(R.id.ibFavorite);
            tvNumFavorites = itemView.findViewById(R.id.tvNumFavorites);
            itemView.setOnClickListener(this);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.name);
            tvScreenName.setText("@" + tweet.user.screenName);
            tvNumFavorites.setText(String.valueOf(tweet.numFavorites));
            //tvNumRetweets.setText(String.valueOf(tweet.numFavorites));

            if (tweet.isFavorited) {
                Drawable newImage = context.getDrawable(android.R.drawable.star_big_on);
                ibFavorite.setImageDrawable(newImage);
            } else {
                Drawable newImage = context.getDrawable(android.R.drawable.star_big_off);
                ibFavorite.setImageDrawable(newImage);
            }


            ibFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // IF NOT ALREADY FAVORITED
                    if (!tweet.isFavorited) {
                        // Tell Twitter API that I want to favorite
                        TwitterApp.getTwitterClient(context).favoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("adapter", "onSucess, tweet favorited, go check");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("adapter", "onFailure, tweet not favorited");
                            }
                        });
                        // change the drawable to a on button
                        tvNumFavorites.setText(String.valueOf(tweet.numFavorites));
                        Drawable newImage = context.getDrawable(android.R.drawable.star_big_on);
                        ibFavorite.setImageDrawable(newImage);
                        // increment the text inside of tvNumFavorites
                        tweet.isFavorited = true;
                        tweet.numFavorites++;

                    } else {
                        // tell twitter I want to unfavorite
                        TwitterApp.getTwitterClient(context).unfavoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("adapter", "onSucess, tweet unfavorited, go check");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("adapter", "onFailure, tweet not unfavorited");
                            }
                        });
                        // change the drawable to off button
                        tvNumFavorites.setText(String.valueOf(tweet.numFavorites));
                        Drawable newImage = context.getDrawable(android.R.drawable.star_big_off);
                        ibFavorite.setImageDrawable(newImage);
                        // decrement the text inside of tvNumFavorites
                        tweet.isFavorited = false;
                        tweet.numFavorites--;
                    }



                }
            });
            try {
                tvTimestamp.setText("· " + getRelativeTimeAgo(tweet.createdAt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .transform(new RoundedCorners(400))
                    .into(ivProfilePicture);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            int mediaWidth = displayMetrics.widthPixels - 100;
            int mediaHeight = (int) tweet.picSizeRatio + mediaWidth;

            if (!tweet.mediaUrl.equals("none")) {
                Glide.with(context)
                        .load(tweet.mediaUrl)
                        .transform(new RoundedCorners(30))
                        .override(mediaWidth, mediaHeight)
                        .into(ivMedia);
            } else {
                ivMedia.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet tweet = tweets.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, TweetDetailActivity.class);
                // serialize the tweet using parceler, use its short name as a key
                intent.putExtra("tweet", Parcels.wrap(tweet));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}


