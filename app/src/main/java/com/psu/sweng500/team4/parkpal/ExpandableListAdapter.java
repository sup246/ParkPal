package com.psu.sweng500.team4.parkpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.psu.sweng500.team4.parkpal.Models.ParkNote;
import com.psu.sweng500.team4.parkpal.Models.ParkRating;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ShansMcB on 7/8/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<Object>> expandableListDetail;

    public ExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<Object>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition));
    }

    public String getGroupName(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = getGroupName(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group_layout, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.list_item);
        listTitleTextView.setText(listTitle);

        if (getChildrenCount(groupPosition) == 0) {
            listTitleTextView.setTextColor(R.color.grey);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Object expandedListItem = getChild(groupPosition, childPosition);
        Class itemClass = expandedListItem.getClass();

        // If it's a Park note
        if (itemClass.equals(ParkNote.class)) {
            ParkNote parkNote = (ParkNote) expandedListItem;

            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.park_notes_layout, null);

            // Set username
            TextView expandedListTextView_username = (TextView) convertView
                    .findViewById(R.id.note_username);
            if (expandedListTextView_username != null) {
                expandedListTextView_username.setText(parkNote.getUsername());
            }

            // Set note
            TextView expandedListTextView_note = (TextView) convertView
                    .findViewById(R.id.note);
            if (expandedListTextView_note != null) {
                expandedListTextView_note.setText(parkNote.getNote_message());
            }

            // Set date
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
            String dateString = sdf.format(parkNote.getDate());
            TextView expandedListTextView_date = (TextView) convertView
                    .findViewById(R.id.note_date);
            if (expandedListTextView_date != null) {
                expandedListTextView_date.setText(dateString);
            }

        } else if (itemClass.equals(ParkRating.class)) {
            ParkRating parkRating = (ParkRating) expandedListItem;

            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.park_review_layout, null);

            // Set username
            TextView expandedListTextView_username = (TextView) convertView
                    .findViewById(R.id.ratingUsername);
            if (expandedListTextView_username != null) {
                expandedListTextView_username.setText(parkRating.getUsername());
            }

            // Set rating
            RatingBar userRating = (RatingBar) convertView.findViewById(R.id.reviewRatingBar);
            if (userRating != null) {
                userRating.setRating(parkRating.getRating());
            }

            // Set comment
            TextView userComment = (TextView) convertView.findViewById(R.id.reviewComment);
            //userComment.setText(parkRating.getComment());
            if (userComment != null) {
                userComment.setText("TestComment");
            }
        }

        // else if it's an Alert??

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
