package com.thespacemanatee.recyclerviewtutorial.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.thespacemanatee.recyclerviewtutorial.databinding.ItemPokemonBinding;
import com.thespacemanatee.recyclerviewtutorial.models.Pokemon;
import com.thespacemanatee.recyclerviewtutorial.models.StatefulPokemon;

import java.util.ArrayList;

public class BadStatelessAdapter extends RecyclerView.Adapter<BadStatelessAdapter.CharaViewHolder> {

    private ArrayList<Pokemon> pokemons;
    private final OnDeleteListener listener;

    public BadStatelessAdapter(ArrayList<Pokemon> pokemons, OnDeleteListener listener) {
        this.pokemons = pokemons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CharaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPokemonBinding binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CharaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CharaViewHolder viewHolder, int position) {
        viewHolder.bind(pokemons.get(position), position);
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    class CharaViewHolder extends RecyclerView.ViewHolder {
        ItemPokemonBinding binding;

        CharaViewHolder(ItemPokemonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Pokemon item, int position) {
            binding.cardIv.setImageDrawable(
                    ResourcesCompat.getDrawable(binding.getRoot().getResources(), item.getResId(), null)
            );
            binding.cardTv.setText(item.getName());
            binding.deleteIv.setOnClickListener(view -> {
                pokemons.remove(position);
                // Bad!
                notifyDataSetChanged();
                listener.deleteItem(item);
            });
        }
    }
}
