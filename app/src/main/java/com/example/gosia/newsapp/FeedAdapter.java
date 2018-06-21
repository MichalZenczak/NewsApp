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

    private static final String TITLE_SEPARATOR = "|";

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

        String titleMain;
        String titleAuthor = "";
        String originalTitle = currentFeed.getFeedTitle();

        if (originalTitle.contains(TITLE_SEPARATOR)){
            String[] titleParts = originalTitle.split(Pattern.quote(TITLE_SEPARATOR));
            titleMain = titleParts[0];
            titleAuthor = titleParts[1];
        }else {
            titleMain = originalTitle;
        }

        TextView titleTv = convertView.findViewById(R.id.feed_title);
        titleTv.setText(titleMain);

        TextView authorTv = convertView.findViewById(R.id.feed_author);

        if (!TextUtils.isEmpty(titleAuthor)){
            String authorString = getContext().getString(R.string.author) + ": " + titleAuthor;
            authorTv.setText(authorString);
        }else {
            authorTv.setVisibility(View.GONE);
            titleTv.setPadding(0,0,0, 32);
        }

        return convertView;
    }
}
