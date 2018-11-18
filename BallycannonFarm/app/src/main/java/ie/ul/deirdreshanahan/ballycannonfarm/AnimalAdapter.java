package ie.ul.deirdreshanahan.ballycannonfarm;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>{

    private List<DocumentSnapshot> mAnimalSnapshots = new ArrayList<>();

    public AnimalAdapter(){
        CollectionReference animalRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PATH);

        animalRef.orderBy(Constants.KEY_CREATED, Query.Direction.DESCENDING).limit(50)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(Constants.TAG, "Listening failed!");
                    return;
                }
                mAnimalSnapshots = queryDocumentSnapshots.getDocuments();
                notifyDataSetChanged();
            }
        });
    }
	
	
    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.animal_item_view, parent, false);
        return new AnimalViewHolder(itemView);
    }

    private List<Animal> mAnimals = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public void addAnimal(){
        mAnimals.add(0, new Animal());
        notifyItemChanged(0);
        notifyItemRangeChanged(0, mAnimals.size());
        mRecyclerView.getLayoutManager().scrollToPosition(0);
    }

    private void deleteAnimal(int position){
        mAnimals.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, mAnimals.size());
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        final Animal animal = mAnimals.get(position);
        holder.mName.setText(animal.getName());
        holder.mImageView.setImageResource(animal.getImageResourceId());
        holder.mRatingBar.setRating(animal.getRating());
    }

    @Override
    public int getItemCount() {
        return mAnimals.size();
    }

    class AnimalViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mName;
        private RatingBar mRatingBar;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.animal_pic);
            mName = itemView.findViewById(R.id.name);
            mRatingBar = itemView.findViewById(R.id.rating_bar);

            //TODO: Delete this animal on long press
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteAnimal(getAdapterPosition());
                    return true;
                }
            });

            // Done together, update the rating for this animal
            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        // Update this animal's rating
                        Animal currentAnimal = mAnimals.get(getAdapterPosition());
                        currentAnimal.setRating(rating);
                    }

                }
            });
			
			itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot ds = mAnimalSnapshots.get(getAdapterPosition());

                    Context c = view.getContext();
                    Intent intent = new Intent(c, AnimalDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_DOC_ID, ds.getId());
                    c.startActivity(intent);
                }
            });
        }
    }
}

