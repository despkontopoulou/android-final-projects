package com.despkontopoulou.tell_a_tale.helpers;

import android.content.Intent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.despkontopoulou.tell_a_tale.R;
import com.despkontopoulou.tell_a_tale.activities.FavouritesActivity;
import com.despkontopoulou.tell_a_tale.activities.StatsActivity;
import com.despkontopoulou.tell_a_tale.activities.StoriesMainActivity;

public abstract class NavBar extends AppCompatActivity {
    protected ImageView homeIcon, favouritesIcon, statsIcon;
    public void setupNavigation(){
        homeIcon=findViewById(R.id.homeIcon);
        favouritesIcon=findViewById(R.id.favouritesIcon);
        statsIcon=findViewById(R.id.statsIcon);
        homeIcon.setOnClickListener(view -> navigateTo(StoriesMainActivity.class));
        favouritesIcon.setOnClickListener(view -> navigateTo(FavouritesActivity.class));
        statsIcon.setOnClickListener(view -> navigateTo(StatsActivity.class));
    }
    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);//prevents unnecessary duplicate activities by reopening them if they exist
        startActivity(intent);
    }
}
