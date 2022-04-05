package com.thespacemanatee.recyclerviewtutorial;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.thespacemanatee.recyclerviewtutorial.adapter.CharaAdapter;
import com.thespacemanatee.recyclerviewtutorial.adapter.Pokemon;
import com.thespacemanatee.recyclerviewtutorial.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<Pokemon> adapterDataSource = new ArrayList<>();
    CharaAdapter charaAdapter = new CharaAdapter(adapterDataSource);

    // List of random pokemons
    List<Pair<String, Integer>> pokemons = Arrays.asList(
            new Pair<>("Bulbasaur", R.drawable.bulbasaur),
            new Pair<>("Eevee", R.drawable.eevee),
            new Pair<>("Gyrados", R.drawable.gyrados),
            new Pair<>("Pikachu", R.drawable.pikachu),
            new Pair<>("Psyduck", R.drawable.psyduck),
            new Pair<>("Snorlax", R.drawable.snorlax),
            new Pair<>("Spearow", R.drawable.spearow),
            new Pair<>("Squirtle", R.drawable.squirtle)
    );

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        initRecyclerView();

        binding.fab.setOnClickListener(view -> {
            // Just add a random new pokemon
            Pair<String, Integer> pokemonData = pokemons.get(new Random().nextInt(pokemons.size()));
            Snackbar.make(this, binding.getRoot(), "Added " + pokemonData.first, 500).show();
            adapterDataSource.add(new Pokemon(pokemonData.first, pokemonData.second));
            // This is bad
            charaAdapter.notifyDataSetChanged();
            binding.charaRecyclerView.smoothScrollToPosition(adapterDataSource.size() - 1);
        });
    }

    private void initRecyclerView() {
        // Remember to set a layout manager or the recyclerview will not display anything!
        binding.charaRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.charaRecyclerView.setAdapter(charaAdapter);
    }
}
