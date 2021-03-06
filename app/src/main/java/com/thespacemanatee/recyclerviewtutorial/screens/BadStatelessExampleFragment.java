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
import com.thespacemanatee.recyclerviewtutorial.adapters.BadStatelessAdapter;
import com.thespacemanatee.recyclerviewtutorial.adapters.OnDeleteListener;
import com.thespacemanatee.recyclerviewtutorial.databinding.FragmentPokemonListBinding;
import com.thespacemanatee.recyclerviewtutorial.models.Pokemon;

import java.util.ArrayList;
import java.util.Random;

public class BadStatelessExampleFragment extends Fragment {
    private FragmentPokemonListBinding binding;

    private final ArrayList<Pokemon> adapterDataSource = new ArrayList<>();
    private BadStatelessAdapter badStatelessAdapter;

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
        binding.fab.setOnClickListener(v -> {
            // Just add a random new pokemon
            Pair<String, Integer> pokemonData = pokemons.get(new Random().nextInt(pokemons.size()));
            Snackbar.make(requireContext(), binding.getRoot(), "Added " + pokemonData.first, 500).show();
            adapterDataSource.add(new Pokemon(pokemonData.first, pokemonData.second));
            // This is bad
            badStatelessAdapter.notifyDataSetChanged();
            binding.charaRecyclerView.smoothScrollToPosition(adapterDataSource.size() - 1);
            handleAnimation();
        });
    }

    private void initRecyclerView() {
        OnDeleteListener listener = (item) -> handleAnimation();
        badStatelessAdapter = new BadStatelessAdapter(adapterDataSource, listener);
        // Remember to set a layout manager or the recyclerview will not display anything!
        binding.charaRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.charaRecyclerView.setAdapter(badStatelessAdapter);
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
