package com.thespacemanatee.recyclerviewtutorial.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.thespacemanatee.recyclerviewtutorial.databinding.ItemPokemonBinding;
import com.thespacemanatee.recyclerviewtutorial.models.StatefulPokemon;

import java.util.ArrayList;

public class BadStatefulAdapter extends RecyclerView.Adapter<BadStatefulAdapter.CharaViewHolder> {

    private ArrayList<StatefulPokemon> pokemons;
    private final OnDeleteListener listener;

    public BadStatefulAdapter(ArrayList<StatefulPokemon> pokemons, OnDeleteListener listener) {
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
        viewHolder.bind(pokemons.get(position));
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

        public void bind(StatefulPokemon item) {
            binding.cardIv.setImageDrawable(
                    ResourcesCompat.getDrawable(binding.getRoot().getResources(), item.getResId(), null)
            );
            binding.cardTv.setText(item.getName());
            // Set check box state stored in item instance
            binding.checkBox.setChecked(item.getIsSelected());
            binding.checkBox.setOnCheckedChangeListener((view, checked) -> {
                if (view.isPressed()) {
                    // Persist selected state in the instance
                    item.setIsSelected(checked);
                }
            });
            binding.deleteIv.setOnClickListener(view -> {
                pokemons.remove(item);
                // Bad!
                notifyDataSetChanged();
                listener.deleteItem(item);
            });
        }
    }
}
