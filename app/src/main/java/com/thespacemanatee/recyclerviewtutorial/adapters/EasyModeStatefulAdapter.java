package com.thespacemanatee.recyclerviewtutorial.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.thespacemanatee.recyclerviewtutorial.databinding.ItemPokemonBinding;
import com.thespacemanatee.recyclerviewtutorial.models.StatefulPokemon;

public class EasyModeStatefulAdapter extends ListAdapter<StatefulPokemon, EasyModeStatefulAdapter.CharaViewHolder> {
    private final OnDeleteListener listener;

    public EasyModeStatefulAdapter(OnDeleteListener listener) {
        // Pass DIFF_CALLBACK into super class ListAdapter's constructor
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    public static final DiffUtil.ItemCallback<StatefulPokemon> DIFF_CALLBACK = new DiffUtil.ItemCallback<StatefulPokemon>() {
        @Override
        public boolean areItemsTheSame(@NonNull StatefulPokemon oldPokemon, @NonNull StatefulPokemon newPokemon) {
            // Pokemon properties may have changed if reloaded from the DB, but ID is fixed
            return oldPokemon.getId() == newPokemon.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull StatefulPokemon oldPokemon, @NonNull StatefulPokemon newPokemon) {
            // NOTE: if you use equals, your object must properly override Object#equals()
            // Incorrectly returning false here will result in too many animations.
            return oldPokemon.equals(newPokemon);
        }
    };

    @NonNull
    @Override
    public CharaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPokemonBinding binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CharaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CharaViewHolder viewHolder, int position) {
        // You now have access to list with getCurrentList() which is a method of the super class
        viewHolder.bind(getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    class CharaViewHolder extends RecyclerView.ViewHolder {
        private final ItemPokemonBinding binding;

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
                // DO NOT MODIFY MDIFFER LIST DIRECTLY!
                // mDiffer.getCurrentList().remove(item); <- no go
                // Use a listener instead and delete the item from the original data source in
                // EasyStatefulExampleFragment
                listener.deleteItem(item);
                // No longer need to call notifyDataSetChanged()!
            });
        }
    }
}
