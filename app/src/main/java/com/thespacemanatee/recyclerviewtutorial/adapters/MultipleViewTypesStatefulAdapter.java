package com.thespacemanatee.recyclerviewtutorial.adapters;

import static com.thespacemanatee.recyclerviewtutorial.models.BaseItem.VIEW_TYPE_POKEMON;
import static com.thespacemanatee.recyclerviewtutorial.models.BaseItem.VIEW_TYPE_SECTION_TITLE;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.thespacemanatee.recyclerviewtutorial.databinding.ItemPokemonBinding;
import com.thespacemanatee.recyclerviewtutorial.databinding.ItemSectionTitleBinding;
import com.thespacemanatee.recyclerviewtutorial.models.BaseItem;
import com.thespacemanatee.recyclerviewtutorial.models.SectionItem;
import com.thespacemanatee.recyclerviewtutorial.models.StatefulPokemon;

public class MultipleViewTypesStatefulAdapter extends ListAdapter<BaseItem, RecyclerView.ViewHolder> {
    private final OnDeleteListener listener;

    public MultipleViewTypesStatefulAdapter(OnDeleteListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    public static final DiffUtil.ItemCallback<BaseItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<BaseItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull BaseItem oldItem, @NonNull BaseItem newItem) {
            // Pokemon properties may have changed if reloaded from the DB, but ID is fixed
            if (oldItem instanceof StatefulPokemon && newItem instanceof StatefulPokemon) {
                return ((StatefulPokemon) oldItem).getId() == ((StatefulPokemon) newItem).getId();
            }
            return oldItem.equals(newItem);
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull BaseItem oldItem, @NonNull BaseItem newItem) {
            // NOTE: if you use equals, your object must properly override Object#equals()
            // Incorrectly returning false here will result in too many animations.
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SECTION_TITLE) {
            ItemSectionTitleBinding binding = ItemSectionTitleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new SectionViewHolder(binding);
        }
        if (viewType == VIEW_TYPE_POKEMON) {
            ItemPokemonBinding binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new CharaViewHolder(binding);
        }
        throw new IllegalArgumentException("Unknown view type: " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        // Bind item to view based on item view type, which we defined in the respective classes
        if (getCurrentList().get(position).getViewType() == VIEW_TYPE_SECTION_TITLE) {
            ((SectionViewHolder) viewHolder).bind((SectionItem) getCurrentList().get(position));
        } else if (getCurrentList().get(position).getViewType() == VIEW_TYPE_POKEMON) {
            ((CharaViewHolder) viewHolder).bind((StatefulPokemon) getCurrentList().get(position));
        }
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return getCurrentList().get(position).getViewType();
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {
        private final ItemSectionTitleBinding binding;

        SectionViewHolder(ItemSectionTitleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SectionItem item) {
            binding.sectionTitleTv.setText(item.getTitle());
            binding.deleteIv.setOnClickListener(view -> {
                listener.deleteItem(item);
            });
        }
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

    public interface OnDeleteListener {
        void deleteItem(BaseItem item);
    }
}
