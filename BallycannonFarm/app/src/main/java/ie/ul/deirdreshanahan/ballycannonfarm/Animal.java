package ie.ul.deirdreshanahan.ballycannonfarm;

import java.util.HashMap;
import java.util.Random;

public class Animal {

    private String mName;
    private int mImageResourceId;
    private float mRating;
    private Random random = new Random();

    public static final HashMap<String, Integer> sAnimalImageMap;
    static {
        sAnimalImageMap = new HashMap<>();
        sAnimalImageMap.put("Jessica 1826", R.drawable.cow4);
        sAnimalImageMap.put("Lotto  1377", R.drawable.cow1);
        sAnimalImageMap.put("Nancy 853", R.drawable.cow3);
        sAnimalImageMap.put("Danny  1021", R.drawable.cow3);

    }

    public Animal() {
        mName = getRandomAnimalName();
        mImageResourceId = sAnimalImageMap.get(mName);
        mRating = 0.0f;
    }

    private String getRandomAnimalName() {
        Object[] animals = Animal.sAnimalImageMap.keySet().toArray();
        return (String)animals[random.nextInt(animals.length)];
    }

    public String getName() {
        return mName;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
        mRating = rating;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }
}
