package com.thespacemanatee.recyclerviewtutorial.screens;

import static com.thespacemanatee.recyclerviewtutorial.data.PokemonDatabase.pokemons;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.thespacemanatee.recyclerviewtutorial.adapters.MultipleViewTypesStatefulAdapter;
import com.thespacemanatee.recyclerviewtutorial.adapters.OnDeleteListener;
import com.thespacemanatee.recyclerviewtutorial.databinding.FragmentPokemonListBinding;
import com.thespacemanatee.recyclerviewtutorial.models.BaseItem;
import com.thespacemanatee.recyclerviewtutorial.models.SectionItem;
import com.thespacemanatee.recyclerviewtutorial.models.StatefulPokemon;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MultipleViewTypesStatefulExampleFragment extends Fragment {
    private FragmentPokemonListBinding binding;

    private final ArrayList<BaseItem> adapterDataSource = new ArrayList<>();
    private MultipleViewTypesStatefulAdapter multipleViewTypesStatefulAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initRecyclerView();
    }

    // Make sure you clean up here to prevent memory leaks. This is only applicable to view binding
    // in fragments
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initView() {
        AtomicInteger i = new AtomicInteger();
        binding.fab.setOnClickListener(v -> {
            // Just add a random new pokemon or section
            if (i.get() % 2 == 0) {
                Pair<String, Integer> pokemonData = pokemons.get(new Random().nextInt(pokemons.size()));
                Snackbar.make(requireContext(), binding.getRoot(), "Added " + pokemonData.first, 500).show();
                adapterDataSource.add(new StatefulPokemon(pokemonData.first, pokemonData.second, false));
            } else {
                Snackbar.make(requireContext(), binding.getRoot(), "Added section " + i, 500).show();
                adapterDataSource.add(new SectionItem("Section " + i));
            }
            // Submit the modified list, that's it! Note that the list must be a new instance!
            multipleViewTypesStatefulAdapter.submitList(new ArrayList<>(adapterDataSource));
            // No longer need to call notifyDataSetChanged()!
            binding.charaRecyclerView.smoothScrollToPosition(adapterDataSource.size() - 1);
            handleAnimation();
            i.getAndIncrement();
        });
    }

    private void initRecyclerView() {
        OnDeleteListener listener = item -> {
            adapterDataSource.remove(item);
            // Always submit a NEW list because AsyncListDiffer does nothing if it receives the same
            // list instance
            multipleViewTypesStatefulAdapter.submitList(new ArrayList<>(adapterDataSource));
            handleAnimation();
        };
        multipleViewTypesStatefulAdapter = new MultipleViewTypesStatefulAdapter(listener);
        // Remember to set a layout manager or the recyclerview will not display anything!
        binding.charaRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.charaRecyclerView.setAdapter(multipleViewTypesStatefulAdapter);
    }

    private void handleAnimation() {
        if (adapterDataSource.isEmpty()) {
            binding.emptyAnim.setVisibility(View.VISIBLE);
            if (!binding.emptyAnim.isAnimating()) {
                binding.emptyAnim.playAnimation();
            }
        } else {
            binding.emptyAnim.setVisibility(View.GONE);
            binding.emptyAnim.pauseAnimation();
        }
    }
}
