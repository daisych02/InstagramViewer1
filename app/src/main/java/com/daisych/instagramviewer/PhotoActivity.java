package com.daisych.instagramviewer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotoActivity extends ActionBarActivity {
    public static final String CLIENT_ID = "035fd409fcdb4f1e9c8b854d103ee7cf";
    private ArrayList<InstagramPhoto> photos = new ArrayList<InstagramPhoto>();
    private ArrayAdapter<InstagramPhoto> aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        fetchPopularPhotos();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchPopularPhotos() {
        // https://api.instagram.com/v1/media/popular?client_id=035fd409fcdb4f1e9c8b854d103ee7cf

        // Create adapter, bind it to data
        aPhotos = new InstagramPhotosAdaptor(this, photos);
        // Import list view
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // Attach adapter to list view
        lvPhotos.setAdapter(aPhotos);

        String popularURL = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        // Trigger network request
        client.get(popularURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                JSONArray photosJSON = null;
                try {
                    photos.clear();
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.userPicture = photoJSON.getJSONObject("user").getString("profile_picture");
                        if (!(photoJSON.getJSONObject("caption").isNull("text"))) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
//                        photo.commentCount = photoJSON.getJSONObject("comments").getInt("count");
//                        JSONArray comments = photoJSON.getJSONObject("comments").getJSONArray("data");
//                        photo.lastComment = comments.getJSONObject(comments.length()-1).getString("text");
//                        photo.commentUser = comments.getJSONObject(comments.length()-1).getJSONObject("user").getString("username");
//                        photo.commentUserPicture = comments.getJSONObject(comments.length()-1).getJSONObject("user").getString("profile_picture");

                        photos.add(photo);
                    }
                    aPhotos.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }
}
