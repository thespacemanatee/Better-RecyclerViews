## Recycler View Tutorial

### Demo

https://user-images.githubusercontent.com/6837599/162474055-bbbe7c1a-dbc3-4003-995d-c0012b042844.mp4

---

### Prerequisites (you may skip)

- RecyclerViews
    - This tutorial assumes that you have a basic understanding of how `RecyclerView` works. There
      are a couple of great online resources already covering the basics of RecyclerViews, such
      as [this](https://medium.com/androiddevelopers/getting-to-know-recyclerview-ea14f8514e6).
- Context
    - `Context` is slightly complicated but in this tutorial, we just need to have a simple
      understanding that
        - `Context` object is the current state of your application
          (such as your view trees) and gives you access to resources associated with your
          application (such as string and drawable resource IDs)
        - All objects that extend the Android `View` class have an Activity `Context` object
          associated with it because intuitively, these views are located somewhere in the view
          hierarchy/tree of the current Activity and should have a `Context` of where it is at in
          this hierarchy

---

### Common Mistakes

1. Passing `Context` into `Adapter`
    1. This is unnecessary because you have access to `Context` in the parent `ViewGroup` which
       extends `View`.
2. Not informing your `RecyclerView` whenever your dataset has changed - this will leave you
   wondering why your `RecyclerView` hasn't updated!
3. Using `notifyDataSetChanged()` to inform your `RecyclerView` of any changes to your dataset
    1. This is bad for performance because it forces the entire `RecyclerView` to re-render - it
       does not know which item has been modified/deleted etc.
4. Not storing the state of your `ViewHolders` somewhere
    1. This leads to weird behaviour such as CheckBoxes checking and unchecking themselves for no
       reason (explained later)

We will discover how to prevent mistakes 2,3, and 4 below!

---

### Project Structure

We just need to focus on these 2 files for each example:

```
- *ExampleFragment
- *Adapter
```

The database of pokemons is hardcoded in `/data/PokemonDatabase.java`. You may plug in your desired
data source for your `RecyclerView`.

---

### Notes

- At each example, try out the respective example in the app to get a better idea of what I'm
  talking about.
- Please read my inline code comments!

---

### RecyclerView Basics

#### Single Responsibility Principle

In the `RecyclerView.Adapter` class, you have to override 3 methods. Each method should only do one
thing.

- `onCreateViewHolder()`
    - You should only write code that inflates the `ViewHolder`, nothing more

```java
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    @Override
    public CharaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPokemonBinding binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CharaViewHolder(binding);
    }
}

```

- `onBindViewHolder()`
    - You should only write code to bind your item data to your `ViewHolders` (such as `Pokemon`
      data), nothing more

```java
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public void onBindViewHolder(@NonNull CharaViewHolder viewHolder, int position) {
        // bind() method is defined in CharaViewHolder class
        viewHolder.bind(pokemons.get(position), position);
    }
}
```

- `getItemCount()`
    - You should only return the size of your data source, nothing more

```java
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public int getItemCount() {
        return pokemons.size();
    }
}
```

- Your `ViewHolder` class should should expose a `bind()` method, which you should call
  in `onBindViewHolder()`
  to bind your `Pokemon` data to your `ViewHolder`. Provide your logic for binding this data to
  your `View` inside this function only (you can have utility private functions in this class of
  course).

```java
class CharaViewHolder extends RecyclerView.ViewHolder {
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
```

---

### Example 1 - BadStatelessExample

This is the most common type of `RecylerView` written by budding Android Developers. There are alot
of problems we can fix here.

#### Problems

1. We have a stateless `Pokemon` class that does not store any state about itself
    1. How do we keep track of this `Pokemon`'s CheckBox selected state?
2. We are calling `notifyDataSetChanged()`, see [here](#common-mistakes) for why it is bad.
3. There are no animations when adding and deleting items, making for a bad user experience.

#### Demo

https://user-images.githubusercontent.com/6837599/162566414-a5480684-5e62-4505-adf6-ca2f62562760.mp4

---

### Example 2 - BadStatefulExample

This example is better than the previous, but we can certainly do better.

#### What's Fixed?

1. We have a stateful `StatefulPokemon` class that extends `Pokemon` and simply stores an `id`
   and `isSelected` for CheckBox state

```java
class CharaViewHolder extends RecyclerView.ViewHolder {
    public void bind(StatefulPokemon item) {
        // Set check box state stored in item instance
        binding.checkBox.setChecked(item.getIsSelected());
        binding.checkBox.setOnCheckedChangeListener((view, checked) -> {
            if (view.isPressed()) {
                // Persist selected state in the instance
                item.setIsSelected(checked);
            }
        });

    }
}
```

#### Problems

1. We are still calling `notifyDataSetChanged()`, see [here](#common-mistakes) for why it is bad.
2. Still no animations 😔

#### Demo

https://user-images.githubusercontent.com/6837599/162566488-59940fd8-394b-4467-a419-ec13800a953e.mp4

---

### Example 3 - GoodStatefulExample

This example is typically all you need to write good `RecyclerViews` everywhere! Read on to find out
some other cool tricks you can do with `RecyclerViews`!

#### What's Fixed?

1. We are no longer calling `notifyDataSetChanged()`! We are using `DiffUtil` and `AsyncListDiffer`
   instead, and we call `adapter.submitList()` in the `GoodStatefulExampleFragment` whenever we
   add/remove an item from the list, or when we want to replace the entire list altogether.

```java
public class GoodStatefulAdapter extends RecyclerView.Adapter<GoodStatefulAdapter.CharaViewHolder> {
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

    private final AsyncListDiffer<StatefulPokemon> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);
}
```

```java
public class GoodStatefulExampleFragment extends Fragment {
    private void initView() {
        binding.fab.setOnClickListener(v -> {
            // Just add a random new pokemon
            Pair<String, Integer> pokemonData = pokemons.get(new Random().nextInt(pokemons.size()));
            adapterDataSource.add(new StatefulPokemon(pokemonData.first, pokemonData.second, false));
            // Submit the modified list, that's it! Note that the list must be a new instance!
            goodStatefulAdapter.submitList(new ArrayList<>(adapterDataSource));
            // No longer need to call notifyDataSetChanged()!
        });
    }
}
```

---

### What is DiffUtil?

`DiffUtil` is a utility class that helps us in a process known as "diffing", which in this case is a
process of comparing two different lists to see exactly what has changed. `DiffUtil` uses the Eugene
W. Myers's difference algorithm to calculate the minimal number of updates to convert one list into
another [(source: Google)](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/DiffUtil)
. To use the `DiffUtil` callback, we need to pass it into an instance of `AsyncListDiffer`, which
will be bound to
`this` instance of `RecyclerView` and listen for updates, and notify the `RecyclerView` to update
its state with smooth animations.

Why is this so amazing? Well, for one, we no longer have to call any `notify*()` functions to notify
our
`RecyclerView` when something has changed. By calling `adapter.submitList()`, `AsyncListDiffer`
stores a reference to this list, and if there are any existing lists stored, it will diff the 2
lists and update the `RecyclerView`
to reflect these changes, with a cool animation!

#### Caveats

- Always pass in a NEW list! What this means is that never pass the same list into `submitList()`,
  even if the contents have changed.
    - This is because `AsyncListDiffer` does a shallow reference equality check between the new list
      and old list before doing any diffing, and if the reference is the same, it won't even care
      that the contents have changed. The simplest way is to write `new ArrayList<>(oldList)`.

---

### Example 4 - EasyModeStatefulExample

In example 3, we introduced quite a bit of boilerplate code (even though we removed some in the
process). To make the process less error-prone and eliminate some more boilerplate code, we can use
`ListAdapter`, another helper class provided by Google. This class is provided in
the `androidx.recyclerview:recyclerview` package by default.

```java
public class EasyModeStatefulAdapter extends ListAdapter<StatefulPokemon, EasyModeStatefulAdapter.CharaViewHolder> {
    public EasyModeStatefulAdapter(OnDeleteListener listener) {
        // Pass DIFF_CALLBACK into super class ListAdapter's constructor
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<StatefulPokemon> DIFF_CALLBACK = new DiffUtil.ItemCallback<StatefulPokemon>() {
        @Override
        public boolean areItemsTheSame(@NonNull StatefulPokemon oldPokemon, @NonNull StatefulPokemon newPokemon) {
            return oldPokemon.getId() == newPokemon.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull StatefulPokemon oldPokemon, @NonNull StatefulPokemon newPokemon) {
            return oldPokemon.equals(newPokemon);
        }
    };

    @Override
    public void onBindViewHolder(@NonNull CharaViewHolder viewHolder, int position) {
        // You now have access to list with getCurrentList() which is a method of the super class
        viewHolder.bind(getCurrentList().get(position));
    }
}
```

#### Demo

https://user-images.githubusercontent.com/6837599/162566397-49920f39-3e99-4dc5-828c-83dbdcde2f4d.mp4

---

### Example 5 - MultipleViewTypesStatefulExample

This example demonstrates how you can render different types of `ViewHolders` in your `RecyclerView`
.

#### Why would you want to render different types of ViewHolders?

Imagine you want to have different sections in your list. You want to have section titles between
those sections, and show different items in each section. You can either achieve this
with [ConcatAdapter](https://developer.android.com/reference/androidx/recyclerview/widget/ConcatAdapter)
or by overriding `getItemViewType(int position)` in your `RecyclerView.Adapter`. This example uses
the second method which is enough for simple requirements.

To make our code cleaner, I have extended `Pokemon` and `SectionItem` classes with the `BaseItem`
abstract class, which has a single abstract method `getItemViewType()` with return type `int`.
Within each subclass, I override this method to return a constant `int`. This `int` can be an
arbitrary value, and you will handle the logic of parsing this value in the adapter with your own
custom logic.

Also create a new class that extends `RecyclerView.ViewHolder` for each type of layout you want to
show. In this example, we created `SectionViewHolder` as our second type of `ViewHolder`.

```java
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
```

#### Override getItemViewType()

```java

public class MultipleViewTypesStatefulAdapter extends ListAdapter<BaseItem, RecyclerView.ViewHolder> {
    @Override
    public int getItemViewType(int position) {
        // Remember to override this method to tell the recycler view what a particular view holder's
        // type is. We defined a getItemViewType() method in our data classes which returns an integer.
        return getCurrentList().get(position).getViewType();
    }
}
```

#### Modify onCreateViewHolder()

```java
public class MultipleViewTypesStatefulAdapter extends ListAdapter<BaseItem, RecyclerView.ViewHolder> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Check the viewTypes that we returned from our overridden getItemViewType() method and inflate the correct layout
        // based on that value
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
}
```

#### Modify onBindViewHolder()

```java
public class MultipleViewTypesStatefulAdapter extends ListAdapter<BaseItem, RecyclerView.ViewHolder> {
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        // We can get the viewType of the current ViewHolder at this position and cast it to the correct class
        // accordingly and call that class' bind method.
        if (getCurrentList().get(position).getViewType() == VIEW_TYPE_SECTION_TITLE) {
            ((SectionViewHolder) viewHolder).bind((SectionItem) getCurrentList().get(position));
        } else if (getCurrentList().get(position).getViewType() == VIEW_TYPE_POKEMON) {
            ((CharaViewHolder) viewHolder).bind((StatefulPokemon) getCurrentList().get(position));
        }
    }
}
```

#### Demo

https://user-images.githubusercontent.com/6837599/162566544-be5f20ff-fa75-4d67-a206-3d711e0b1830.mp4

### Conclusion

If you read this far, you're officially equipped with the knowledge to write performant and
stylish `RecyclerViews` which are readable and maintainable by other developers! There is so much
more to discover with `RecyclerViews` and I hope that after reading this tutorial, `RecyclerViews`
don't seem that scary anymore, and can actually be fun to write 😀

### Acknowledgements

- SUTD 50.001 professors from which I took the base code from (Android Lesson 4)
- [Chee Kit - 50.001 TA](https://www.linkedin.com/in/chee-kit/)
