package com.a7f.drawingsound.data;

import com.a7f.drawingsound.R;
import com.a7f.drawingsound.model.Mood;

import java.util.ArrayList;

public class MoodsData {

    ArrayList<Mood> items = new ArrayList<>();

    public ArrayList<Mood> getItems() {

        Mood mood1 = new Mood("https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/brightness.jpeg?alt=media&token=39180ee2-0073-4dc7-840e-f635bafb92b3",
                "밝음");

        Mood mood2 = new Mood("https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/calmness.png?alt=media&token=c76726b8-81c2-42e4-8752-194cb18f770a",
                "잔잔함");

        Mood mood3 = new Mood("https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/darkness.png?alt=media&token=615c451d-92c3-4de6-be61-cc5340e53a10",
                "어두움");

        items.add(mood1);
        items.add(mood2);
        items.add(mood3);

        return items;
    }
}
