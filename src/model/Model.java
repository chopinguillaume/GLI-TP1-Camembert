package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume on 16/09/16.
 */
public class Model implements IModel {

    private String name;
    private List<Item> items;

    public Model(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    @Override
    public int getTotalAmount() {
        int total = 0;
        for(Item item : items){
            total += item.getAmount();
        }
        return total;
    }
}
