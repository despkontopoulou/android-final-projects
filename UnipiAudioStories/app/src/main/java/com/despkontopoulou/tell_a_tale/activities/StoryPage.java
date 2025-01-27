package com.despkontopoulou.tell_a_tale.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.despkontopoulou.tell_a_tale.R;
import com.despkontopoulou.tell_a_tale.helpers.Chapter;
import com.despkontopoulou.tell_a_tale.helpers.Story;
import com.despkontopoulou.tell_a_tale.helpers.StoryRepository;

import java.util.ArrayList;
import java.util.List;

public class StoryPage extends AppCompatActivity {
    private int currentChapterIndex = 0;
    private List<Chapter> chapters = new ArrayList<>();
    private TextView chapterText, storyTitle;
    private ImageView chapterImage, arrowLeft, arrowRight;
    private StoryRepository storyRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        chapterImage = findViewById(R.id.chapterImage);
        chapterText = findViewById(R.id.chapterText);
        storyTitle = findViewById(R.id.textView6);
        arrowLeft = findViewById(R.id.arrowLeft);
        arrowRight = findViewById(R.id.arrowRight);
        storyRepository = new StoryRepository();

        loadStoryData();
        displayChapter(currentChapterIndex);

        // Set Previous button click listener
        arrowLeft.setOnClickListener(v -> {
            if (currentChapterIndex > 0) {
                currentChapterIndex--;
                displayChapter(currentChapterIndex);
            }
        });

        // Set Next button click listener
        arrowRight.setOnClickListener(v -> {
            if (currentChapterIndex < chapters.size() - 1) {
                currentChapterIndex++;
                displayChapter(currentChapterIndex);
            }
        });
    }

    // Load chapters (You can replace this with your actual data)
    private void loadStoryData() {
        Intent intent = getIntent();
        String storyId = intent.getStringExtra("storyId"); // Get the passed storyId

        storyRepository.getStories(new StoryRepository.StoryResponse() {
            @Override
            public void onSuccess(List<Story> stories) {
                for (Story story : stories) {
                    if (story.getId().equals(storyId)) { // Match the storyId
                        storyTitle.setText(story.getTitle());
                        chapters = story.getChapters(); // Update the chapters list
                        displayChapter(currentChapterIndex);
                        return;
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("StoryPage", "Error fetching stories: " + errorMessage);
            }
        });
    }

    // Display the chapter data on the screen
    private void displayChapter(int index) {
        if (chapters != null && index >= 0 && index < chapters.size()) {
            Chapter currentChapter = chapters.get(index);
            chapterText.setText(currentChapter.getText());

            Glide.with(this)
                    .load(currentChapter.getPicture())
                    .into(chapterImage);

            // Disable buttons based on chapter index
            arrowLeft.setEnabled(index > 0);
            arrowRight.setEnabled(index < chapters.size() - 1);
        }
    }
}