## Recycler View Tutorial

### Demo

https://user-images.githubusercontent.com/6837599/162474055-bbbe7c1a-dbc3-4003-995d-c0012b042844.mp4

### Prerequisites (you may skip)
- RecyclerViews
  - This tutorial assumes that you have a basic understanding of how `RecyclerView` works. There are a couple of great online resources already covering the basics of RecyclerViews, such as [this](https://medium.com/androiddevelopers/getting-to-know-recyclerview-ea14f8514e6).
- Context
  - `Context` is slightly complicated but in this tutorial, we just need to have a simple understanding that
    - `Context` object is the current state of your application
    (such as your view trees) and gives you access to resources associated with your application (such as string and drawable resource IDs)
    - All objects that extend the Android `View` class have an Activity `Context` object associated with it because intuitively, these views are located
    somewhere in the view hierarchy/tree of the current Activity and should have a `Context` of where it is at in this hierarchy

### Common Mistakes

1. Passing `Context` into `Adapter`
   1. This is unnecessary because you have access to `Context` in the parent `ViewGroup` which extends `View`.
2. Not informing your `RecyclerView` whenever your dataset has changed - this will leave you wondering why your `RecyclerView` hasn't updated!
3. Using `notifyDataSetChanged()` to inform your `RecyclerView` of any changes to your dataset
   1. This is bad for performance because it forces the entire `RecyclerView` to re-render - it does not know which item has been modified/deleted etc.
4. Not storing the state of your `ViewHolders` somewhere
   1. This leads to weird behaviour such as CheckBoxes checking themselves for no reason (explained later)

We will discover how to prevent mistakes 2,3, and 4 below!

### Project Structure

We just need to focus on these 2 files for each example:
```
- *ExampleFragment
- *Adapter
```

The database of pokemons is hardcoded in `data/PokemonDatabase.java`. You may plug in your desired
data source for your `RecyclerView`.

### Notes

- At each example, try out the respective example in the app to get a better idea of what I'm talking about.
- Please read my inline code comments!

### Single Responsibility Principle

- In the `RecyclerView.Adapter` class, you have to override 3 methods
  - onCreateViewHolder()
    - You should only write code that inflates the `ViewHolder`, nothing more
    - ```java
        @NonNull
        @Override
        public CharaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemPokemonBinding binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new CharaViewHolder(binding);
        }
      ```
  - onBindViewHolder()
      - You should only write code to bind your item data to your `ViewHolders` (such as `Pokemon` data), nothing more
      - ```java
          @Override
          public void onBindViewHolder(@NonNull CharaViewHolder viewHolder, int position) {
              viewHolder.bind(pokemons.get(position), position);
          }
        ```
  - getItemCount()
      - You should only return the size of your data source, nothing more
      - ```java
          @Override
          public int getItemCount() {
              return pokemons.size();
          }
        ```
- Your `ViewHolder` class should should expose a `bind()` method, which you should call in `onBindViewHolder()`
to bind your `Pokemon` data to your `ViewHolder`. Provide your logic for binding this data to your `View` inside this
function only.

### Example 1 - BadStatelessExample

#### Problems

1. We have a stateless `Pokemon` class that does not store any state about itself
   1. How do we keep track of this `Pokemon`'s CheckBox selected state?
2. We are calling `notifyDataSetChanged()`, see [here](#common-mistakes) for why it is bad.

### Example 2 - BadStatefulExample

#### What's Fixed?

1. We have a stateful `StatefulPokemon` class that extends `Pokemon` and simply stores an `id` and `isSelected` for CheckBox state

#### Problems

1. We are still calling `notifyDataSetChanged()`, see [here](#common-mistakes) for why it is bad.

### Example 3 - BadStatefulExample

#### What's Fixed?

1. We are no longer calling `notifyDataSetChanged()`! We are using `DiffUtil` instead.

### What is DiffUtil?

`DiffUtil` is a utility class that helps us in a process known as "diffing", which in this case is a process of comparing
two different lists to see exactly what has changed. `DiffUtil` uses the Eugene W. Myers's difference algorithm
to calculate the minimal number of updates to convert one list into another [(source: Google)](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/DiffUtil).
To use the `DiffUtil` callback, we need to pass it into an instance of `AsyncListDiffer`, which will be bound to
`this` instance of `RecyclerView` and listen for updates, and notify the `RecyclerView` to update its state with smooth animations.

Why is this so amazing? Well, for one, we no longer have to call any `notify*()` functions to notify our
`RecyclerView` when something has changed. By calling `adapter.submitList()`, `AsyncListDiffer` stores a reference
to this list, and if there are any existing lists stored, it will diff the 2 lists and update the `RecyclerView`
to reflect these changes, with a cool animation!

#### Caveats
- Always pass in a NEW list! What this means is that never pass the same list into `submitList()`, even if the contents have changed.
  - This is because `AsyncListDiffer` does a shallow reference equality check between the new list and old list before doing any diffing,
  and if the reference is the same, it won't even care that the contents have changed. The simplest way is to write `new ArrayList<>(oldList)`.
