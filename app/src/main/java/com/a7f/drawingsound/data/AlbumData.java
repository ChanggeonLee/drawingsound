package com.a7f.drawingsound.data;

import android.util.Log;

import com.a7f.drawingsound.model.Album;

import java.util.ArrayList;

public class AlbumData {

    ArrayList<Album> items = new ArrayList<>();

    public AlbumData(){
        items.add(new Album(
                "풍선_동방신기",
                "풍선",
                "동방신기",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/balloon.png?alt=media&token=1298649c-2923-4217-b6c6-c2292fc1a74c"));

        items.add(new Album(
                "만약에_태연",
                "만약에",
                "태연",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/if.png?alt=media&token=eb3f5095-7d1d-4058-86f8-9f6f32ad3094"));

        items.add(new Album(
                "아기상어_핑크퐁",
                "상어가족",
                "핑크퐁",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/shark_family.png?alt=media&token=bf2e9738-3bf4-4d90-a6cb-1f73224f72f4"));

        items.add(new Album(
                "소주한잔_임창정",
                "소주한잔",
                "임창정",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/soju.png?alt=media&token=dfb04dd1-4b6e-45e2-b967-7852a6030199"));

        items.add(new Album(
                "아메리카노_10cm",
                "아메리카노",
                "10cm",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/americano%20.png?alt=media&token=b60d32d4-d50f-4971-aa70-b62763fb3686"));

        items.add(new Album(
                "쿵따리샤바라_클론",
                "쿵따리샤바라",
                "클론",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/Kung_Tari_Shabara.jpeg?alt=media&token=47f1fef7-d515-4e56-9a54-bd9a48979885"));

        items.add(new Album(
                "내여자라니까_이승기",
                "내여자라니까",
                "이승기",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/my_girl.png?alt=media&token=c52c41ca-785a-4bb8-965a-413f45387e5f"));

        items.add(new Album(
                "아름다운세상_박학기",
                "아름다운 세상",
                "박학기",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/beautiful_world.png?alt=media&token=12e0bb63-267d-43da-a0e7-219769547df4"));

        items.add(new Album(
                "캔디_HOT",
                "캔디",
                "HOT",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/candy.png?alt=media&token=1fecb6c4-0a4f-4fdb-9545-0fd839cc1f58"));

        items.add(new Album(
                "Letitgo_IdinaMenzel",
                "Let it go",
                "IdinaMenzel",
                "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/let_it_go.png?alt=media&token=b8aacff5-58f2-4446-ba41-6159dcfeb97e"));
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
