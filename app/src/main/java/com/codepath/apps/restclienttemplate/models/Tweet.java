package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String mediaUrl;
    public String id;
    public boolean isFavorited;
    public boolean isRetweeted;
    public int numFavorites;
    public int numRetweets;


    public double picSizeRatio;

    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("retweeted_status")) {
            return null;
        }

        Tweet tweet = new Tweet();
        if(jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }


        if (jsonObject.getJSONObject("entities").has("media")) {
            Log.d("adapter", jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https"));
            JSONObject mediaObject = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0);
            tweet.mediaUrl = mediaObject.getString("media_url_https");

            JSONArray mediaSizes = mediaObject.getJSONObject("sizes").names();

            for (int i = 0; i < mediaSizes.length(); i++) {
                String sizeType = mediaSizes.getString(i);
                if (sizeType != "thumb") {
                    JSONObject size = mediaObject.getJSONObject("sizes").getJSONObject(sizeType);
                    tweet.picSizeRatio = (double) size.getInt("h") / size.getInt("w");
                    break;
                }
            }
        } else {
            tweet.mediaUrl = "none";
        }

        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getString("id_str");
        tweet.isFavorited = jsonObject.getBoolean("favorited");
        tweet.isRetweeted = jsonObject.getBoolean("retweeted");
        tweet.numFavorites = jsonObject.getInt("favorite_count");
        tweet.numRetweets = jsonObject.getInt("retweet_count");
        return tweet;
    }

    public static List<Tweet> fromJsonArray (JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Tweet newTweet = fromJson(jsonArray.getJSONObject(i));
            if (newTweet != null) { // skip displaying retweeted tweets
                tweets.add(newTweet);
            }
        }
        return tweets;
    }
}
