package com.despkontopoulou.tell_a_tale.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.despkontopoulou.tell_a_tale.R;
import com.despkontopoulou.tell_a_tale.helpers.Story;
import com.despkontopoulou.tell_a_tale.helpers.StoryAdapter;
import com.despkontopoulou.tell_a_tale.helpers.StoryRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class StoriesMainActivity extends AppCompatActivity {

    private List<Story> storyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stories_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        storyList=new ArrayList<>();
        StoryRepository storyRepository = new StoryRepository();
        storyRepository.getStories(new StoryRepository.StoryResponse(){
            @Override
            public void onSuccess(List<Story> stories){
                //Toast.makeText(StoriesMainActivity.this, "Stories loaded: " + stories.size(), Toast.LENGTH_SHORT).show();
            }

            public void onFailure(String errorMessage){
                Toast.makeText(StoriesMainActivity.this, "Error: "+ errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.storyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with an empty list initially
        StoryAdapter storyAdapter = new StoryAdapter(this, storyList);
        recyclerView.setAdapter(storyAdapter);

        // Fetch stories from Firebase
        storyRepository.getStories(new StoryRepository.StoryResponse() {
            @Override
            public void onSuccess(List<Story> stories) {
                // Update the existing list instead of reassigning the adapter
                storyList.clear();
                storyList.addAll(stories);
                storyAdapter.notifyDataSetChanged(); // Notify RecyclerView of data change
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(StoriesMainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }



}
