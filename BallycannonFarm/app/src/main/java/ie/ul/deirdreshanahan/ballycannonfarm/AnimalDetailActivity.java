package ie.ul.deirdreshanahan.ballycannonfarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.koushikdutta.ion.Ion;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AnimalDetailActivity extends AppCompatActivity {

    private DocumentReference mDocRef;
    private DocumentSnapshot mDocSnapshot;

    private TextView mQuoteTextView;
    private TextView mMovieTextView;
    private ImageView mPhotoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mQuoteTextView = findViewById(R.id.detail_tag);
        mMovieTextView = findViewById(R.id.detail_breed);
        mPhotoImageView = findViewById(R.id.detail_photo);
        String docId = getIntent().getStringExtra(Constants.EXTRA_DOC_ID);

        //mQuoteTextView.setText(docId);
        mDocRef = FirebaseFirestore.getInstance()
                .collection(Constants.COLLECTION_PATH).document(docId);
        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(Constants.TAG, "Listen failed");
                    return;
                }
                if (documentSnapshot.exists()){
                    mDocSnapshot = documentSnapshot;
                    mQuoteTextView.setText((String)documentSnapshot.get(Constants.KEY_ANIMAL_TAG));
                    mMovieTextView.setText((String)documentSnapshot.get(Constants.KEY_BREED));

                    // https://github.com/koush/ion
                    //Ion.with()
                    if (documentSnapshot.get(Constants.KEY_PHOTO) == Constants.EMPTY) {
                        Ion.with(mPhotoImageView).load((randomImageUrl()));
                    }
                   else {
                        Ion.with(mPhotoImageView).load((String)documentSnapshot.get(Constants.KEY_PHOTO));
                    }
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();

            }
        });
    }

    public String randomImageUrl() {
        String[] urls = new String[]{
                //"https://wallpaper.wiki/wp-content/uploads/2017/04/wallpaper.wiki-Cow-Background-Free-Download-PIC-WPB0011874.jpg",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/Alicia.jpeg?alt=media&token=dbf597b8-b2f6-48bf-b981-16f72ff925b8",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/Ballycannon%20Holstein.jpeg?alt=media&token=6500f5d4-efde-483d-bf01-ec848a446223",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/Blackie.jpeg?alt=media&token=47d0738a-03e0-4bfa-be99-d7a341327e89",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/Caroline.jpeg?alt=media&token=d26f1b9e-7b01-41ff-a19c-2907ed878d1b",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/Dude.jpg?alt=media&token=2eb9d720-7b23-4613-9ee8-c7f57af56fca",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/Josie1.jpeg?alt=media&token=a0b610c7-b30e-4c5b-9c26-2a45f7085eae",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/Mags.jpeg?alt=media&token=1265da40-0c3d-49a5-82f1-082cbab3258a",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/Kerry.jpeg?alt=media&token=b4a7be31-455e-4290-ba56-384ee1bbc2ba",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/Rosie.jpeg?alt=media&token=3a5e406f-ef08-4c61-94c5-1155e2104014",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/Tina.jpeg?alt=media&token=ab5be70a-c290-4e39-9fcd-ac72b460edfa",
                "https://firebasestorage.googleapis.com/v0/b/myherdnialldeirdre.appspot.com/o/ShuttleRita.jpg?alt=media&token=f0c2f6e7-bec6-4b2b-85d4-f1b352527c87",


        };
        return urls[(int) (Math.random() * urls.length)];
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.animal_dialog, null, false );
        builder.setView(view);
        builder.setTitle("Edit this Animal Details");
        final TextView tagEditText = view.findViewById(R.id.dialog_tag_edittext);
        final TextView breedEditText = view.findViewById(R.id.dialog_breed_edittext);
        final TextView nicknameEditText = view.findViewById(R.id.dialog_nickname_edittext);
        final TextView healthEditText = view.findViewById(R.id.dialog_health_edittext);

        tagEditText.setText((String)mDocSnapshot.get(Constants.KEY_ANIMAL_TAG));
        breedEditText.setText((String)mDocSnapshot.get(Constants.KEY_BREED));
        nicknameEditText.setText((String)mDocSnapshot.get(Constants.KEY_NAME));
        healthEditText.setText((String)mDocSnapshot.get(Constants.KEY_HEALTH));

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Map<String, Object> mq = new HashMap<>();

                mq.put(Constants.KEY_ANIMAL_TAG, tagEditText.getText().toString());
                mq.put(Constants.KEY_BREED, breedEditText.getText().toString());
                mq.put(Constants.KEY_NAME, nicknameEditText.getText().toString());
                mq.put(Constants.KEY_HEALTH, healthEditText.getText().toString());
                mq.put(Constants.KEY_CREATED, new Date());
                mDocRef.update(mq);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);


        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void composeEmail(String[] addresses, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.ACTION_ANSWER, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_delete:
                mDocRef.delete();
                finish();
                return true;
            case R.id.action_email:

                composeEmail(new String[]{"departmentofargriculture@gov.ie"},"Register new animal births",
                        "These are the new births:" +
                                "1111111" +
                                "2222222" +
                                "3333333 ");

                ///Toast.makeText(AnimalDetailActivity.this, "Sending email!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_take_photo:

                ///Toast.makeText(AnimalDetailActivity.this, "Taking photo!", Toast.LENGTH_SHORT).show();
                dispatchTakePictureIntent();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
