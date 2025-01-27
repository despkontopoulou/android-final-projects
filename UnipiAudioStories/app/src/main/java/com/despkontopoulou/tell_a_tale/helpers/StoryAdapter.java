package com.despkontopoulou.tell_a_tale.helpers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.despkontopoulou.tell_a_tale.R;
import com.despkontopoulou.tell_a_tale.activities.StoryPage;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    private List<Story> stories;
    private Context context;

    public StoryAdapter(Context context, List<Story> stories) {
        this.context= context;
        this.stories = stories;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_card, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.StoryViewHolder holder, int position) {
        Story story = stories.get(position);

        // Set the story title
        holder.titleTextView.setText(story.getTitle());

        // Load the image of the first chapter (if available)
        if (story.getChapters() != null && !story.getChapters().isEmpty()) {
            Chapter firstChapter = story.getChapters().get(0);  // Get the first chapter
            if (firstChapter.getPicture() != null && !firstChapter.getPicture().isEmpty()) {
                Glide.with(context)
                        .load(firstChapter.getPicture())  // Load the first chapter's image
                        .into(holder.storyImageView);
            } else {
                // Optional: Set a placeholder if no image is found
                holder.storyImageView.setImageResource(R.drawable.placeholder);
            }
        } else {
            // Optional: Set a placeholder if the story has no chapters
            holder.storyImageView.setImageResource(R.drawable.placeholder);
        }

        // Set click listener for the story item
        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to navigate to StoryPage
            Intent intent = new Intent(context, StoryPage.class);

            // Pass the storyId to the StoryPage
            intent.putExtra("storyId", story.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView storyImageView;
        TextView titleTextView;

        public StoryViewHolder(View itemView) {
            super(itemView);
            // Initialize views from story_card.xml
            storyImageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.textView3);
        }
    }
}
