package com.example.odunayo.narrator.Server;

import android.widget.Toast;

import com.example.odunayo.narrator.Framework.Log;
import com.example.odunayo.narrator.Framework.Story;
import com.example.odunayo.narrator.MapsActivity;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Odunayo on 1/5/2016.
 */
public class ServerTests {
    public static MapsActivity mactivity;

    final static String[] authToken = new String[1];
    final static String[] userId = new String[1];

    public static void setActivty (final MapsActivity activity){
        mactivity = activity;
        
    }

    public static void testSignup(){

        NarratorServerCalls.signup("testUserNameFag10", "testPasswordFag", new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                Log.d("This", "STATUS is " + status);
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(mactivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {
                            String userId = json.getString("user_id");

                        }
                    } catch (Exception e) {
                        Toast.makeText(mactivity, "Bad Connection.", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(mactivity, "Bad Connection.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public static void testLogin(){


        Toast.makeText(mactivity, "Creating Session", Toast.LENGTH_SHORT).show();
        NarratorServerCalls.createSession("testUserNameFag", "testPasswordFag", null, 0, new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                Toast.makeText(mactivity, "Status of login " + status, Toast.LENGTH_SHORT).show();
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(mactivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {
                            userId[0] = json.getString("user_id");
                            authToken[0] = json.getString("auth_token");

//                            Toast.makeText(mactivity, "userId " + userId[0]
//                                    + " AuthToken " + authToken[0], Toast.LENGTH_SHORT).show();

                            //ServerTests.deleteSession();
                            //ServerTests.testPostStory();
                            //testGetStories();
                            testGetUserStories();


                        }
                    } catch (Exception e) {
                        Toast.makeText(mactivity, "Bad Connection.", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(mactivity, "Bad Connection.", Toast.LENGTH_SHORT).show();

            }
        });



    }

    public static void deleteSession(){
        Toast.makeText(mactivity, "Deleting Session", Toast.LENGTH_SHORT).show();
        //Delete session

        NarratorServerCalls.deleteSession(authToken[0], userId[0], new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {

                Toast.makeText(mactivity, "status " + status, Toast.LENGTH_SHORT).show();
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_NO_CONTENT) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(mactivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_NO_CONTENT) {

                            Toast.makeText(mactivity, "Logged Out successfully", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(mactivity, "Bad Connection.1", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(mactivity, "Bad Connection.2", Toast.LENGTH_SHORT).show();

            }
        });

    }



    public static void testPostStory(){
        Toast.makeText(mactivity, "Posting Story", Toast.LENGTH_SHORT).show();

        NarratorServerCalls.postStory("God damn it man"
                , 40.3571, -74.6702, authToken[0], userId[0], new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                Toast.makeText(mactivity, " Post status " + status, Toast.LENGTH_SHORT).show();
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(mactivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {
                            String storyid = json.getString("story_id");

                            Toast.makeText(mactivity, "storyId " + storyid, Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        Toast.makeText(mactivity, "Bad Connection.", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(mactivity, "Bad Connection.", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public static void testGetStories(){
        Toast.makeText(mactivity, "Getting Stories", Toast.LENGTH_SHORT).show();

        NarratorServerCalls.getStories(100, 40.3571, -74.6702, authToken[0], userId[0], new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                Toast.makeText(mactivity, " Getting Stories " + status, Toast.LENGTH_SHORT).show();
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_OK) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(mactivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_OK) {

                            ArrayList<Story> storiesList = new ArrayList<Story>();
                            JSONArray stories = json.getJSONArray("stories");

                            Toast.makeText(mactivity, "Array of Stories length " + stories.length(), Toast.LENGTH_SHORT).show();

                            for (int i = 0; i < stories.length(); i++) {
                                Story newStory = new Story(stories.getJSONObject(i));
                                storiesList.add(newStory);
                            }

                            Toast.makeText(mactivity, "Stories list " + storiesList.size(), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {
                        Toast.makeText(mactivity, "Bad Connection1", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(mactivity, "Bad Connection2", Toast.LENGTH_SHORT).show();

            }
        });


    }


    public static void testGetUserStories(){
        Toast.makeText(mactivity, "Getting Stories", Toast.LENGTH_SHORT).show();

        NarratorServerCalls.getStoriesUser("9", authToken[0], userId[0], new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                Toast.makeText(mactivity, " Getting UserStory  " + status, Toast.LENGTH_SHORT).show();
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_OK) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(mactivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_OK) {

                            ArrayList<Story> storiesList = new ArrayList<Story>();
                            JSONArray stories = json.getJSONArray("stories");

                            Toast.makeText(mactivity, "Array of Stories length " + stories.length(), Toast.LENGTH_SHORT).show();

                            for (int i = 0; i < stories.length(); i++) {
                                Story newStory = new Story(stories.getJSONObject(i));
                                storiesList.add(newStory);
                            }

                            Toast.makeText(mactivity, "Stories list " + storiesList.size(), Toast.LENGTH_SHORT).show();

                            // upvoteStory("6");
                            // testPostComment();
                            //upvoteComment("22");


                        }
                    } catch (Exception e) {
                        Toast.makeText(mactivity, "Bad Connection1", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(mactivity, "Bad Connection2", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public static void upvoteStory(String storyId){
        Toast.makeText(mactivity, "Upvoting Story", Toast.LENGTH_SHORT).show();

        NarratorServerCalls.upvoteStory(storyId, authToken[0], userId[0], new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                Toast.makeText(mactivity, " Upvoting Story status " + status, Toast.LENGTH_SHORT).show();
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(mactivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {
                            int numUpvotes = json.getInt("num_upvotes");

                            Toast.makeText(mactivity, "NumUpvotes " + numUpvotes, Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        Toast.makeText(mactivity, "Bad Connection.3", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(mactivity, "Bad Connection.4", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public static void testPostComment(){
        Toast.makeText(mactivity, "Posting Comment", Toast.LENGTH_SHORT).show();

        NarratorServerCalls.postComment("Comment Test Post", "6", authToken[0], userId[0], new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                Toast.makeText(mactivity, " Post Comment status: " + status, Toast.LENGTH_SHORT).show();
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(mactivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {
                            String commentId = json.getString("comment_id");

                            Toast.makeText(mactivity, "CommentId " + commentId, Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        Toast.makeText(mactivity, "Bad Connection.5", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(mactivity, "Bad Connection.6", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public static void upvoteComment(String commentId) {
        Toast.makeText(mactivity, "Upvoting Comment", Toast.LENGTH_SHORT).show();

        NarratorServerCalls.upvoteComment(commentId, authToken[0], userId[0], new Callback() {
            @Override
            public void postExecute(JSONObject json, int status,
                                    String... strings) {
                Toast.makeText(mactivity, " Upvoting Comment status " + status, Toast.LENGTH_SHORT).show();
                if (status == HttpStatus.SC_BAD_REQUEST || status == HttpStatus.SC_CREATED) {
                    try {
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            Toast.makeText(mactivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (status == HttpStatus.SC_CREATED) {
                            int numUpvotes = json.getInt("num_upvotes");

                            Toast.makeText(mactivity, "NumUpvotes Comment " + numUpvotes, Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        Toast.makeText(mactivity, "Bad Connection.3", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(mactivity, "Bad Connection.4", Toast.LENGTH_SHORT).show();

            }
        });


    }




    }
