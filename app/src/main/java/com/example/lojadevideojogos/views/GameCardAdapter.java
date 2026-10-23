package com.example.lojadevideojogos.views;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.lojadevideojogos.databinding.ItemGameCardBinding;
import com.example.lojadevideojogos.model.Game;
import java.util.List;

public class GameCardAdapter extends RecyclerView.Adapter<GameCardAdapter.GameViewHolder> {

    private List<Game> gameList;
    private OnGameClickListener listener;

    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    public GameCardAdapter(List<Game> gameList, OnGameClickListener listener) {
        this.gameList = gameList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGameCardBinding binding = ItemGameCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GameViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.bind(game, listener);
    }

    @Override
    public int getItemCount() {
        return gameList == null ? 0 : gameList.size();
    }

    public void updateList(List<Game> newList) {
        this.gameList = newList;
        notifyDataSetChanged();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        private final ItemGameCardBinding binding;

        public GameViewHolder(ItemGameCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Game game, OnGameClickListener listener) {
            binding.tvTitle.setText(game.getTitle());
            binding.tvPrice.setText(String.format("%.2f €", game.getPrice()));

            Glide.with(binding.getRoot().getContext())
                    .load(game.getImageUrl())
                    .into(binding.ivCover);

            if (game.getStock() <= 0) {
                binding.tvStock.setVisibility(View.VISIBLE);
                binding.tvPrice.setTextColor(binding.getRoot().getContext().getResources().getColor(android.R.color.darker_gray));
            } else {
                binding.tvStock.setVisibility(View.GONE);
            }

            if (game.isOnSale()) {
                binding.tvPromo.setVisibility(View.VISIBLE);
            } else {
                binding.tvPromo.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(v -> listener.onGameClick(game));
        }
    }
}