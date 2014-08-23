package com.hackathon4.mash;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericwood on 8/23/14.
 */
public class CategoryManager {

    private static final CategoryManager instance = new CategoryManager();

    public static CategoryManager getInstance() {
        return instance;
    }

    private List<Category> categories = new ArrayList<Category>();

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public List<Category> getCategories() {
        return new ArrayList<Category>(categories);
    }
}
