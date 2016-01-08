package com.example.odunayo.narrator;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odunayo.narrator.Framework.Comment;
import com.example.odunayo.narrator.Framework.Story;

import java.util.ArrayList;

public class ViewStoryFragment extends Fragment {


    private static final String TAG = "ViewStoryFragment";

    private MapsActivity activity;


    // Session ids
    private String authToken;
    private String userId;

    private static TextView storyText;
    private static String storyString;
    private ListView commentsListView;
    private Story story;

    private CommentsAdapter commentsadapter;

    public static ViewStoryFragment newInstance(Story story) {
        final ViewStoryFragment f = new ViewStoryFragment();

        final Bundle args = new Bundle();
        args.putParcelable("Story", story);
        f.setArguments(args);

        return f;
    }

    //unused constructor
    public ViewStoryFragment() {
    }

    //Get Story from Bundle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        story = (Story) (getArguments() != null ? getArguments().getParcelable("Story") : null);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (MapsActivity) getActivity();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.view_story, container, false);

        commentsListView = (ListView)rootView.findViewById(R.id.comments_list);
        storyText = (TextView) rootView.findViewById(R.id.story);

        commentsadapter = new CommentsAdapter(activity);

        commentsListView.setAdapter(commentsadapter);

        if (story.getStoryContent() != null)
        storyText.setText(story.getStoryContent());
        setComments(story.getComments());


        return rootView;

    }

    public static void setStory(String story){
        storyString = story;
        storyText.setText(storyString);

    }

    public void setComments(ArrayList<Comment> comments){
        //populate comments
        for (int i = 0; i < comments.size(); i++){
           commentsadapter.add(comments.get(i));
        }
    }

    public static class CommentsAdapter extends ArrayAdapter<Comment> {
        // keeps track of the context reference
        private Context context;

        // Caches ids, idea from: https://github.com/thecodepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
        private class ViewHolder {
            TextView comment;
            Button numUpvotes;


        }

        public CommentsAdapter(Context context) {
            super(context, R.layout.comment_layout, R.id.comment_text);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {

                viewHolder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.comment_layout, parent, false);

                viewHolder.comment = (TextView) convertView.findViewById(R.id.comment_text);
                viewHolder.numUpvotes = (Button) convertView.findViewById(R.id.upvote_text);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //Populate Comments
            final Comment comment = getItem(position);

            viewHolder.comment.setText(comment.getCommentContent());
           // Toast.makeText(getContext(),"Num of upvotes " + comment.getUpvotes() , Toast.LENGTH_SHORT).show();
            viewHolder.numUpvotes.setText(String.valueOf(comment.getUpvotes()));



          //  Toast.makeText(getContext(), "Count of comment " + getCount(), Toast.LENGTH_SHORT).show();




            return convertView;
        }
    }


}
