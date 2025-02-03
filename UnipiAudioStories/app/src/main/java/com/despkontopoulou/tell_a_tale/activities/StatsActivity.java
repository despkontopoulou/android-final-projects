package com.despkontopoulou.tell_a_tale.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.despkontopoulou.tell_a_tale.R;
import com.despkontopoulou.tell_a_tale.helpers.NavBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StatsActivity extends NavBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupNavigation();
        SharedPreferences prefs = getSharedPreferences("Stats", MODE_PRIVATE);

        int totalStoriesListened = prefs.getInt("total_listened", 0);
        String mostRecentlyListenedStory = prefs.getString("recent_story", "None");
        String mostListenedStory = prefs.getString("most_listened_story", "None");
        int totalFavourites = getSharedPreferences("Favourites", MODE_PRIVATE).getInt("total_favourites", 0);

        // Format timestamp into readable date
        long lastTimeListened = prefs.getLong("last_listened_time", 0);
        String lastListenedFormatted = lastTimeListened == 0 ? "Never" :
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(lastTimeListened));

        // Set data in the UI
        ((TextView) findViewById(R.id.total_listened)).setText(String.valueOf(totalStoriesListened));
        ((TextView) findViewById(R.id.most_listened)).setText(mostListenedStory);
        ((TextView) findViewById(R.id.most_recent)).setText(mostRecentlyListenedStory);
        ((TextView) findViewById(R.id.timestamp)).setText(lastListenedFormatted);
    }
}