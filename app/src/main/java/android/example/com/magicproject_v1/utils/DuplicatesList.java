package android.example.com.magicproject_v1.utils;

import android.example.com.magicproject_v1.classes.Card;

import java.util.ArrayList;
import java.util.List;

public class DuplicatesList {

    private List<Card> list;
    private List<Integer> duplicates;

    public DuplicatesList() {
        this.list = new ArrayList<>();
        this.duplicates = new ArrayList<>();
    }

    public List<Card> getList() {
        return list;
    }

    public void setList(List<Card> list) {
        this.list = list;
    }

    public List<Integer> getDuplicates() {
        return duplicates;
    }

    public void setDuplicates(List<Integer> duplicates) {
        this.duplicates = duplicates;
    }
}
