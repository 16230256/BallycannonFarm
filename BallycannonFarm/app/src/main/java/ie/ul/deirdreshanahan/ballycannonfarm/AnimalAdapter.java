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
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>{

    private List<DocumentSnapshot> mAnimalSnapshots = new ArrayList<>();
	
	//private List<Animal> mAnimals = new ArrayList<>();
    //private RecyclerView mRecyclerView;

    public AnimalAdapter(){
        CollectionReference animalRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PATH);

        //animalRef.orderBy(Constants.KEY_CREATED, Query.Direction.DESCENDING).limit(50)
        //        .addSnapshotListener(new EventListener<QuerySnapshot>() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        animalRef.whereEqualTo(Constants.KEY_USER_ID, uid)
                .orderBy(Constants.KEY_CREATED, Query.Direction.DESCENDING).limit(50)
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



   // @Override
    //public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        //super.onAttachedToRecyclerView(recyclerView);
        //mRecyclerView = recyclerView;
    //}



    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {


        //final Animal animal = mAnimals.get(position);
        //holder.mName.setText(animal.getName());
        //holder.mImageView.setImageResource(animal.getImageResourceId());
        //holder.mRatingBar.setRating(animal.getRating());



		DocumentSnapshot ds = mAnimalSnapshots.get(position);
        String tag = (String)ds.get(Constants.KEY_ANIMAL_TAG);
        String breed = (String)ds.get(Constants.KEY_BREED);
		//String photo = (String)ds.get(Constants.KEY_PHOTO);

        holder.mName.setText(tag + " " + breed);

        

        //holder.mName.setText(breed);

        // https://github.com/koush/ion
        //Ion.with()
        //if (ds.get(Constants.KEY_PHOTO) == Constants.EMPTY) {
            Ion.with(holder.mImageView).load((randomImageUrl()));
        //}
        //else {
            //Ion.with(holder.mImageView).load(photo);
        //}


    }

    public String randomImageUrl() {
        String[] urls = new String[]{
                // From Deirdre's Farm
                //"https://drive.google.com/drive/folders/1gsUj1fbnj3OYx33vqYzqyzrJSjPwrwBE", // Alicia
                //"https://drive.google.com/file/d/1MlXlxwn4FQ5O_t4PXqLgJTToSUPIrQQx/view",      // Ballycannon Holstein
                //"https://drive.google.com/open?id=1q_S1X5tzOY2n7FyKh4Ttism_qOOnrGdm",       // Blackie
                //"https://drive.google.com/open?id=1RWEnCGbA9NqNnfj09fk6ZIUs36MDE8Z5",       // Caroline
                //"https://drive.google.com/open?id=15CiPw09Y2mx3WtHxhraWWuTvS9TMOLoq",       // Dude
                //"https://drive.google.com/open?id=14V1GKOmRujLiktzwt5o9Q2jGimj-oWLL",       // Josie
                //"https://drive.google.com/open?id=1sriPKX37rEJKdyUZOmflrzJqDTcdXoDU",       // Mags
                //"https://drive.google.com/open?id=1SMY_qeQnNeS_9WQTt0YzUciK7ptj4w1W",       /// Mistake
                //"https://drive.google.com/open?id=1D54rqmil9qLtf_xYb75l-nGl2-J55NNp",       // Rosie
                //"https://drive.google.com/open?id=1lqxrSnN_Ixi8drREYSDV10iw25EncBtn",       // ShuttleRita
                //"https://drive.google.com/open?id=1CnmcA-YDQen65g9b5sNWpeyBlSeAGvHY",       // Tina
                //"https://drive.google.com/file/d/1lqxrSnN_Ixi8drREYSDV10iw25EncBtn/view",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/Alicia.jpeg?alt=media&token=dbf597b8-b2f6-48bf-b981-16f72ff925b8",
                //"https://wallpaper.wiki/wp-content/uploads/2017/04/wallpaper.wiki-Cow-Background-Free-Download-PIC-WPB0011874.jpg",
                //"https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Cow_female_black_white.jpg/220px-Cow_female_black_white.jpg",
                //"https://static1.squarespace.com/static/5670c8d31115e008c1d01b1d/t/583e53bbbe659429d12aa010/1480479725191/Ariana.jpg",
                //"https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/CH_cow_2_cropped.jpg/240px-CH_cow_2_cropped.jpg",
        };
        return urls[(int) (Math.random() * urls.length)];
    }
    @Override
    public int getItemCount() {
        //return mAnimals.size();
		return mAnimalSnapshots.size();
    }

    class AnimalViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mName;
        private SeekBar mSeekBar;

         public  AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.animal_pic);
            mName = itemView.findViewById(R.id.name);
            mSeekBar = itemView.findViewById(R.id.seekBar);

            //TODO: Delete this animal on long press
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteAnimal(getAdapterPosition());
                    return true;
                }
            });

            // Done together, update the rating for this animal
            //mSeekBar.setOnSeekBarChangeListener();
            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChangedValue = 0;

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChangedValue = progress;
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                public void onStopTrackingTouch(SeekBar seekBar) {

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
	
	public void addAnimal(){
        //mAnimals.add(0, new Animal());
        //notifyItemChanged(0);
        //notifyItemRangeChanged(0, mAnimals.size());
        //mRecyclerView.getLayoutManager().scrollToPosition(0);
    }

    private void deleteAnimal(int position){
        //mAnimals.remove(position);
        //notifyItemRemoved(position);
        //notifyItemRangeChanged(0, mAnimals.size());
    }
}

