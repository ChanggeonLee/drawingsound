package com.a7f.drawingsound.data;

import com.a7f.drawingsound.model.Sheet;

import java.util.ArrayList;

public class SheetsData {
    private ArrayList<Sheet> items;

    public SheetsData(){
        items = new ArrayList<>();
    }

    public void setItems(Sheet sheet){
            items.add(sheet);
    }

    public ArrayList<Sheet> getItems() {
        return items;
    }
}
