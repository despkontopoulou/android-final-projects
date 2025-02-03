package com.despkontopoulou.tell_a_tale.activities;

import android.content.SharedPreferences;
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
import com.despkontopoulou.tell_a_tale.helpers.NavBar;
import com.despkontopoulou.tell_a_tale.helpers.Story;
import com.despkontopoulou.tell_a_tale.helpers.StoryAdapter;
import com.despkontopoulou.tell_a_tale.helpers.StoryRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavouritesActivity extends NavBar {
    private RecyclerView favouritesRecycle;
    private List<Story> allStories;
    private List<Story> favouriteStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourites);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupNavigation();
        favouritesRecycle = findViewById(R.id.favoritesRecyclerView);
        favouriteStories=new ArrayList<>();
        StoryRepository repo= new StoryRepository(this);

        favouritesRecycle.setLayoutManager(new LinearLayoutManager(this));

        StoryAdapter storyAdapter = new StoryAdapter(this, favouriteStories);
        favouritesRecycle.setAdapter(storyAdapter);

        // Fetch all stories from Firebase
        repo.getStories(new StoryRepository.StoryResponse() {
            @Override
            public void onSuccess(List<Story> stories) {
                // Filter the stories to get only the favourites
                favouriteStories.clear();
                favouriteStories.addAll(getFavouriteStories(stories));
                storyAdapter.notifyDataSetChanged();  // Update the RecyclerView
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FavouritesActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private List<Story> getFavouriteStories(List<Story> allStories) {
        List<Story> favouriteStories = new ArrayList<>();

        // Get the set of favourited story IDs from shared preferences
        SharedPreferences prefs = getSharedPreferences("Favourites", MODE_PRIVATE);
        Set<String> favouriteStoryIds = prefs.getStringSet("favourite_stories", new HashSet<>());

        // Filter the stories and add only the ones that are favourited
        for (Story story : allStories) {
            if (favouriteStoryIds.contains(story.getId())) {
                favouriteStories.add(story);
            }
        }

        return favouriteStories;
    }
}