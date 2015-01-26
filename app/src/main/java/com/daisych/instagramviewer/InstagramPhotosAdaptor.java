package com.daisych.instagramviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by daisych on 1/23/15.
 */
public class InstagramPhotosAdaptor extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdaptor(Context context, List<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
    }

    // getView
    // take the model Instagram toString()
    // Takes data in position(i), convert into view
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        // Get data item
        InstagramPhoto photo = getItem(position);
        // Look for recycled view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Look up the subview within the template
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvNumComments = (TextView) convertView.findViewById(R.id.tvNumComments);
        TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);
        TextView tvCommentUser = (TextView) convertView.findViewById(R.id.tvCommentUser);
        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
        ImageView imgUser = (ImageView) convertView.findViewById(R.id.imgUser);
        // Populate data into the view
        tvCaption.setText(photo.caption);
        tvLikes.setText(photo.likesCount + " Likes");
        tvUserName.setText(photo.username);
        tvNumComments.setText("view all " + photo.commentCount + " comments");
        tvComment.setText(photo.lastComment);
        tvCommentUser.setText(photo.commentUser);

        imgPhoto.getLayoutParams().height = photo.imageHeight;
        // Reset image from recycled view
        imgPhoto.setImageResource(0);
        imgUser.setImageResource(0);
        // Ask for photo to be added to image view based on photo url
        // Send network request, download image, convert image into bitmap, insert bitmap into image view
        Picasso.with(getContext()).load(photo.userPicture).into(imgUser);
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.placeholder).into(imgPhoto);
        Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);

        return convertView;
    }
}
