package ie.ul.deirdreshanahan.ballycannonfarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;




public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final AnimalAdapter animalAdapter = new AnimalAdapter();
        recyclerView.setAdapter(animalAdapter);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.animal_dialog, null, false );
        builder.setView(view);
        builder.setTitle("Add an animal");
        final TextView tagEditText = view.findViewById(R.id.dialog_tag_edittext);
        final TextView breedEditText = view.findViewById(R.id.dialog_breed_edittext);
        final TextView nicknameEditText = view.findViewById(R.id.dialog_nickname_edittext);
        final TextView healthEditText = view.findViewById(R.id.dialog_health_edittext);
        final TextView photoEditText = view.findViewById(R.id.dialog_photo_edittext);


        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Map<String, Object> mq = new HashMap<>();

                mq.put(Constants.KEY_ANIMAL_TAG, tagEditText.getText().toString());
                mq.put(Constants.KEY_BREED, breedEditText.getText().toString());
                mq.put(Constants.KEY_NAME, nicknameEditText.getText().toString());
                mq.put(Constants.KEY_HEALTH, healthEditText.getText().toString());
                mq.put(Constants.KEY_PHOTO, photoEditText.getText().toString());
                mq.put(Constants.KEY_CREATED, new Date());

                mq.put(Constants.KEY_USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());


                FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PATH).add(mq);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);


        builder.create().show();
    }

    //new code here
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signout, menu);
        return true;
    }

    public void capturePhoto() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_signout:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;

            case R.id.action_email:

                composeEmail(new String[]{"departmentofargriculture@gov.ie"},"Register new animal births",
                        "These are the new births:" +
                                "1111111" +
                                "2222222" +
                                "3333333 ");

                Toast.makeText(MainActivity.this, "Sending email!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_take_photo:

                Toast.makeText(MainActivity.this, "Taking photo!", Toast.LENGTH_SHORT).show();
                dispatchTakePictureIntent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                //insertItem(true);
                return true;

            case R.id.action_remove:
                //mItems.remove(mCurrentItem);
                //mCurrentItem = new Item();
                //showCurrentItem();
                return true;


        }
        return super.onContextItemSelected(item);
    }
}
