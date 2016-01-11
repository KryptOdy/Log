package com.example.odunayo.narrator;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.odunayo.narrator.Framework.UiUtils;
import com.example.odunayo.narrator.Server.Callback;
import com.example.odunayo.narrator.Server.NarratorServerCalls;
import com.example.odunayo.narrator.Server.ServerUtils;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

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
    private Button postComment;
    private Button upvoteStory;

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

    //Get Story saved from Bundle
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
        postComment = (Button) rootView.findViewById(R.id.postComment);
        upvoteStory = (Button) rootView.findViewById(R.id.upvote_story);

        upvoteStory.setText(String.valueOf(story.getNumUpvotes()));

        upvoteStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean wasLiked = story.getLikedByUser();
                story.setLikedByUser(!wasLiked);
                story.setNumUpvotes(story.getNumUpvotes() + (wasLiked ? -1 : 1));

                upvoteStory(story);
                upvoteStory.setText(String.valueOf(story.getNumUpvotes()));

            }
        });


        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCommentDialog();

            }
        });

        commentsadapter = new CommentsAdapter(activity);

        commentsListView.setAdapter(commentsadapter);

        if (story.getStoryContent() != null)
        storyText.setText(story.getStoryContent());
        setComments(story.getComments());


        return rootView;

    }

    public void postCommentDialog(){

        View signupView = getActivity().getLayoutInflater().inflate(R.layout.comment_dialog, null);
        final EditText commentText = (EditText) signupView .findViewById(R.id.commentText);

        AlertDialog.Builder alertDialogBuilder = UiUtils.getNewAlertDialogBuilder(activity);

        alertDialogBuilder
                .setView(signupView)
                .setMessage("Post Comment")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String sendComment = commentText.getText().toString();

                        if (postComment(sendComment)) {
                            UiUtils.hideKeyboard(activity, commentText);
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UiUtils.hideKeyboard(activity, commentText);
                        dialog.cancel();
                    }
                });


        UiUtils.showAlertDialog(alertDialogBuilder);

    }

    public boolean postComment(final String comment){

        if (!ServerUtils.isConnected(activity)) {
            Toast.makeText(activity, "No Internet Connection.", Toast.LENGTH_SHORT).show();
            return false;
        }

        NarratorServerCalls.postComment(comment, story.getStoryId(), activity.authToken, activity.userId, new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {

                            String commentId = json.getString("comment_id");
                            Comment newComment = new Comment(commentId, story.getStoryId(),
                                    userId, comment, 0, false, null);

                            story.addComment(newComment);
                            addCommentToAdapter(newComment);

                            Toast.makeText(activity, "Successfully posted your comment!", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        Toast.makeText(activity, "Bad Connection", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(activity, "Bad Connection, HTTPStatus = " + status, Toast.LENGTH_SHORT).show();

            }
        });




        return true;

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

    //Add individual Comment to the adapter and update the List
    public void addCommentToAdapter(Comment comment){
        if (comment != null)
        commentsadapter.add(comment);
    }

    public void upvoteStory(Story story){

        NarratorServerCalls.upvoteStory(story.getStoryId(), activity.authToken, activity.userId, new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {
                            int numUpvotes = json.getInt("num_upvotes");

                        }
                    } catch (Exception e) {
                        Toast.makeText(activity, "Bad Connection.3", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(activity, "Bad Connection.4: Status = " + status, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void upvoteComment(Comment comment){


        NarratorServerCalls.upvoteComment(comment.getCommentId(), activity.authToken, activity.userId, new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {

                            int numUpvotes = json.getInt("num_upvotes");

//                            Toast.makeText(activity, "NumUpvotes Comment " + numUpvotes, Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        Toast.makeText(activity, "Bad Connection.3", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(activity, "Bad Connection: Status " + status, Toast.LENGTH_SHORT).show();

            }
        });



    }



    public class CommentsAdapter extends ArrayAdapter<Comment> {
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
            viewHolder.numUpvotes.setText(String.valueOf(comment.getUpvotes()));

            viewHolder.numUpvotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean wasLiked = comment.getLikedByUser();
                    comment.setLikedByUser(!wasLiked);
                    comment.setNumUpvotes(comment.getUpvotes() + (wasLiked ? -1 : 1));

                    upvoteComment(comment);

                    commentsadapter.notifyDataSetChanged();

                }
            });



          //  Toast.makeText(getContext(), "Count of comment " + getCount(), Toast.LENGTH_SHORT).show();


            return convertView;
        }
    }


}
