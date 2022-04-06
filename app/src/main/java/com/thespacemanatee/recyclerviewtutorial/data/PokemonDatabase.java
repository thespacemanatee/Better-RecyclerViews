package com.thespacemanatee.recyclerviewtutorial.data;

import android.util.Pair;

import com.thespacemanatee.recyclerviewtutorial.R;

import java.util.Arrays;
import java.util.List;

public class PokemonDatabase {
    // List of random pokemons
    public static List<Pair<String, Integer>> pokemons = Arrays.asList(
            new Pair<>("Bulbasaur", R.drawable.bulbasaur),
            new Pair<>("Eevee", R.drawable.eevee),
            new Pair<>("Gyrados", R.drawable.gyrados),
            new Pair<>("Pikachu", R.drawable.pikachu),
            new Pair<>("Psyduck", R.drawable.psyduck),
            new Pair<>("Snorlax", R.drawable.snorlax),
            new Pair<>("Spearow", R.drawable.spearow),
            new Pair<>("Squirtle", R.drawable.squirtle)
    );
}
