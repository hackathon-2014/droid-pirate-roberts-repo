package com.hackathon4.mash;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericwood on 8/23/14.
 */
public class Category {

    public List<String> items = new ArrayList<String>();

    public void addItem(String item) {
        this.items.add(item);
    }

    public List<String> getItems() {
        return new ArrayList<String>(items);
    }
}
