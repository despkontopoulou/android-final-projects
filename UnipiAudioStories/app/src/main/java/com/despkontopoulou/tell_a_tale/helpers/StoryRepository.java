package com.despkontopoulou.tell_a_tale.helpers;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Context;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class StoryRepository {
    private final DatabaseReference ref;
    private final List<Story> stories = new ArrayList<>();
    private final Context context;//added context for image preloading

    public StoryRepository(Context context) {
        this.context = context;
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://tell-a-tale-74c6a-default-rtdb.europe-west1.firebasedatabase.app/");
        ref = database.getReference("stories");
    }

    public interface StoryResponse {
        void onSuccess(List<Story> stories);

        void onFailure(String errorMessage);
    }

    public void getStories(final StoryResponse response) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stories.clear();
                List<String> imageUrls = new ArrayList<>();//collection of all urls for preloading

                for (DataSnapshot snap : snapshot.getChildren()) {
                    String id = snap.getKey(); // Get the unique database key as the story ID
                    String title = snap.child("title").getValue(String.class);
                    List<Chapter> chapterList = new ArrayList<>();

                    for (DataSnapshot chapterSnap : snap.child("chapters").getChildren()) {
                        String picture = chapterSnap.child("picture").getValue(String.class);
                        String text = chapterSnap.child("text").getValue(String.class);
                        //image url to list for preload
                        if (picture != null) {
                            imageUrls.add(picture);
                        }
                        chapterList.add(new Chapter(picture, text));
                    }

                    // Create a Story object and set its ID
                    Story story = new Story(title, chapterList);
                    story.setId(id); // Assign the unique ID to the story
                    stories.add(story);
                }
                preloadImages(imageUrls);
                response.onSuccess(stories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                response.onFailure(error.getMessage());
            }
        });

    }

    private void preloadImages(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache both original and resized images
                    .preload();
        }
    }
}




