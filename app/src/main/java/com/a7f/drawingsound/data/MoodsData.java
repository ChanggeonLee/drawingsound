package com.a7f.drawingsound.data;

import com.a7f.drawingsound.R;
import com.a7f.drawingsound.model.Mood;

import java.util.ArrayList;

public class MoodsData {

    ArrayList<Mood> items = new ArrayList<>();

    public ArrayList<Mood> getItems() {

        Mood mood1 = new Mood("https://i.ytimg.com/vi/5-mWvUR7_P0/maxresdefault.jpg",
                "밝음");

        Mood mood2 = new Mood("https://t1.daumcdn.net/cfile/tistory/0138F14A517F77713A",
                "잔잔함");

        Mood mood3 = new Mood("https://i.ytimg.com/vi/5-mWvUR7_P0/maxresdefault.jpg",
                "어두움");

        items.add(mood1);
        items.add(mood2);
        items.add(mood3);

        return items;
    }
}
