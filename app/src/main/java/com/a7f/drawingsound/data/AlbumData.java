package com.a7f.drawingsound.data;

import android.util.Log;

import com.a7f.drawingsound.model.Album;

import java.util.ArrayList;

public class AlbumData {

    ArrayList<Album> items = new ArrayList<>();

    public AlbumData(){
        items.add(new Album(
                "삐삐_IU",
                "삐삐",
                "IU",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/bbibbi.PNG?alt=media&token=18cceeb9-840d-4246-87ce-97a259e3bf50"));

        items.add(new Album(
                "200%_AKMU",
                "200%",
                "AKMU",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/200.PNG?alt=media&token=cd099f92-707e-4ae9-a432-7e0174d27ef4"));

        items.add(new Album(
                "GIVE_LOVE_AKMU",
                "give love",
                "AKMU",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/give_love.PNG?alt=media&token=8aaacd35-6d26-40a3-ba51-a8868c713f5d"));

        items.add(new Album(
                "방에_모기가_있어_10cm",
                "방에 모기가 있어",
                "10cm",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/mogi.PNG?alt=media&token=941ed88d-dfcf-4146-9e69-c6ad14e7380d"));

        items.add(new Album(
                "Palette_IU",
                "palette",
                "IU",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/palette.PNG?alt=media&token=e361a379-9de4-416f-befa-2f12c94ceca6"));

        items.add(new Album(
                "상어가족_핑크퐁",
                "상어가족",
                "핑크퐁",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/shark.PNG?alt=media&token=74ced995-caaf-4618-8aae-00640d92fb7d"));

        items.add(new Album(
                "밤편지_IU",
                "밤편지",
                "IU",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/through_the_night.PNG?alt=media&token=4a0cc9e8-883b-4f09-8277-b4f0e6c06843"));

        items.add(new Album(
                "여수_밤바다_버스커버스커",
                "여수 밤바다",
                "버스커 버스커",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/yeosu.PNG?alt=media&token=00d8cd9c-95d7-4e72-868a-fe564030b337"));

        items.add(new Album(
                "소녀_오혁",
                "소녀",
                "오혁",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/1988.png?alt=media&token=76e0554d-1905-4dc5-9fed-fe2055d860aa"));

        items.add(new Album(
                "스물셋_IU",
                "스물셋",
                "IU",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/23.png?alt=media&token=d7b0d006-3b65-4403-8797-b7c56b44339e"));
    }

    public ArrayList<Album> getItems() {
        return items;
    }

    public Album findItems(String key){
        int len=items.size();
        for(int i=0; i<len; i++) {
            String itemKey = items.get(i).getKey().trim();
            if (itemKey.equals(key.trim())) {
                return items.get(i);
            }
        }
        return null;
    }
}
