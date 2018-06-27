package com.example.gosia.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class FeedAdapter extends ArrayAdapter {


    public FeedAdapter(@NonNull Context context, ArrayList<Feed> feedArrayList) {
        super(context, 0, feedArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Feed currentFeed = (Feed) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.feed, parent, false);
        }

        TextView sectionTv = convertView.findViewById(R.id.feed_section);
        sectionTv.setText(currentFeed.getFeedSection());

        String originalDate = currentFeed.getFeedDate();
        String[] dateParts = originalDate.split("T|Z");
        TextView dateTv = convertView.findViewById(R.id.feed_date);
        String dateString = dateParts[0] + " " + dateParts[1];
        dateTv.setText(dateString);

        TextView titleTv = convertView.findViewById(R.id.feed_title);
        titleTv.setText(currentFeed.getFeedTitle());

        TextView authorTv = convertView.findViewById(R.id.feed_author);
        authorTv.setText(currentFeed.getFeedAuthor());

        return convertView;
    }
}
