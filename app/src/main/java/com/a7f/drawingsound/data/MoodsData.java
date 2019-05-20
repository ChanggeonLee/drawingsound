package com.a7f.drawingsound.data;

import com.a7f.drawingsound.R;
import com.a7f.drawingsound.model.Mood;

import java.util.ArrayList;

public class MoodsData {

    ArrayList<Mood> items = new ArrayList<>();

    public ArrayList<Mood> getItems() {

        Mood movie1 = new Mood("https://i.ytimg.com/vi/5-mWvUR7_P0/maxresdefault.jpg",
                "action");

        Mood movie2 = new Mood("https://t1.daumcdn.net/cfile/tistory/0138F14A517F77713A",
                "action");

        Mood movie3 = new Mood("https://i.ytimg.com/vi/5-mWvUR7_P0/maxresdefault.jpg",
                "action");

        items.add(movie1);
        items.add(movie2);
        items.add(movie3);

        return items;
    }
}
