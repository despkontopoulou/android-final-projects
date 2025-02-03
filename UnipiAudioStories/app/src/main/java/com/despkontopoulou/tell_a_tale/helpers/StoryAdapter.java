package com.despkontopoulou.tell_a_tale.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.despkontopoulou.tell_a_tale.R;
import com.despkontopoulou.tell_a_tale.activities.FavouritesActivity;
import com.despkontopoulou.tell_a_tale.activities.StoryPage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    private List<Story> stories;
    private Context context;

    public StoryAdapter(Context context, List<Story> stories) {
        this.context= context;
        this.stories = stories;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//creates a storyview holder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_card, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.StoryViewHolder holder, int position) {
        Story story = stories.get(position);

        holder.titleTextView.setText(story.getTitle());// set the story title

        // load the image of the first chapter
        if (story.getChapters() != null && !story.getChapters().isEmpty()) {
            Chapter firstChapter = story.getChapters().get(0);  // gett first chapter
            if (firstChapter.getPicture() != null && !firstChapter.getPicture().isEmpty()) {
                Glide.with(context)
                        .load(firstChapter.getPicture())
                        .into(holder.storyImageView);
            } else {
                holder.storyImageView.setImageResource(R.drawable.placeholder);
            }
        } else {
            holder.storyImageView.setImageResource(R.drawable.placeholder);
        }
//favourite button functionality hidden if in favourites activity
        boolean favouriteStatus=favouriteStatus(story.getId());
        setFavouriteStatus(holder.star, favouriteStatus);
        if (context instanceof FavouritesActivity) {
            holder.star.setVisibility(View.GONE);
        } else {
            holder.star.setVisibility(View.VISIBLE);

            //star click functionality
            holder.star.setOnClickListener(v -> {
                boolean newStatus = !favouriteStatus(story.getId());
                saveFavourite(story.getId(), newStatus);
                setFavouriteStatus(holder.star, newStatus);
            });
        }

        // set click listener for the story item
        holder.itemView.setOnClickListener(v -> {
            // Intent to navigate to StoryPage
            Intent intent = new Intent(context, StoryPage.class);
            intent.putExtra("storyId", story.getId());
            context.startActivity(intent);
        });
        // click listener for the voice icons
        holder.voice1Icon.setOnClickListener(v -> {
            navigateToStoryPage(story, "voice_1");
        });

        holder.voice2Icon.setOnClickListener(v -> {
            navigateToStoryPage(story, "voice_2");
        });

        // click listener for the card, default female
        holder.itemView.setOnClickListener(v -> {
            navigateToStoryPage(story, "voice_1");
        });

    }

    @Override
    public int getItemCount() {
        return stories.size();
    }
    private void navigateToStoryPage(Story story, String selectedVoice) {
        SharedPreferences prefs = context.getSharedPreferences("Stats", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // track total listened stories
        int totalStories = prefs.getInt("total_listened", 0);
        editor.putInt("total_listened", totalStories + 1);

        // track most recently listened story
        editor.putString("recent_story", story.getTitle());

        // rack last listened timestamp
        editor.putLong("last_listened_time", System.currentTimeMillis());

        // update most listened story
        String mostListened = prefs.getString("most_listened_story", "");
        int mostListenedCount = prefs.getInt("most_listened_count", 0);


        int currentCount = prefs.getInt("story_count_" + story.getId(), 0);
        currentCount++;
        editor.putInt("story_count_" + story.getId(), currentCount);
        // Update most listened
        if (currentCount > mostListenedCount) {
            editor.putString("most_listened_story", story.getTitle());
            editor.putInt("most_listened_count", currentCount);
        }

        editor.apply();
        Intent intent = new Intent(context, StoryPage.class);
        intent.putExtra("storyId", story.getId()); // pass storyId to StoryPage
        intent.putExtra("selectedVoice", selectedVoice); // pass selectedVoice to StoryPage
        context.startActivity(intent);
    }
    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView storyImageView, voice1Icon,voice2Icon,star;
        TextView titleTextView;

        public StoryViewHolder(View itemView) {
            super(itemView);
            // init views from story_card.xml
            storyImageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.textView3);
            voice1Icon=itemView.findViewById(R.id.voice_1);
            voice2Icon=itemView.findViewById(R.id.voice_2);
            star=itemView.findViewById(R.id.star);
        }
    }
    private boolean favouriteStatus(String storyId){
        SharedPreferences prefs = context.getSharedPreferences("Favourites", Context.MODE_PRIVATE);
        Set<String> favouriteStories=prefs.getStringSet("favourite_stories", new HashSet<>());
        return favouriteStories.contains(storyId);
    }
    private void setFavouriteStatus(ImageView star, boolean favouriteStatus){
        if(favouriteStatus){
            star.setImageResource(R.drawable.favourite);
        }else{
            star.setImageResource(R.drawable.not_favourite);
        }
    }
    private void saveFavourite(String storyId,boolean isFavourite){
        SharedPreferences prefs = context.getSharedPreferences("Favourites",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= prefs.edit();
        Set<String> favouriteStories= prefs.getStringSet("favourite_stories", new HashSet<>());
        Set<String> newFaves= new HashSet<>(favouriteStories);
        if(isFavourite){
            newFaves.add(storyId);
        }else{
            newFaves.remove(storyId);
        }
        editor.putStringSet("favourite_stories", newFaves);
        editor.apply();
    }
}
