package com.thespacemanatee.recyclerviewtutorial.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.thespacemanatee.recyclerviewtutorial.databinding.ItemPokemonBinding;

import java.util.ArrayList;

public class CharaAdapter extends RecyclerView.Adapter<CharaAdapter.CharaViewHolder> {

    ArrayList<Pokemon> pokemons;

    public CharaAdapter(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    @NonNull
    @Override
    public CharaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemPokemonBinding binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CharaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CharaViewHolder viewHolder, int i) {
        viewHolder.bind(pokemons.get(i), i);
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
                notifyDataSetChanged();
            });
        }
    }
}
