package org.jboss.seam.faces.examples.viewconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

/**
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@Singleton
public class ItemDao {
    private Map<Integer, Item> itemMap;
    private int index;

    @PostConstruct
    void initializeList() {
        itemMap = new HashMap<Integer, Item>();
        itemMap.put(1, new Item(1, "Sock", "demo"));
        itemMap.put(2, new Item(2, "Pants", "demo"));
        itemMap.put(3, new Item(3, "Shirt", "admin"));
        itemMap.put(4, new Item(4, "Shoe", "admin"));
        index = 5;
    }

    public Item find(Integer id) {
        return itemMap.get(id);
    }

    public List<Item> findAll() {
        List<Item> list = new ArrayList<Item>();
        list.addAll(itemMap.values());
        return list;
    }

    public void create(Item item) {
        item.setId(index);
        itemMap.put(5, item);
        index++;
    }

    public void update(Item item) {
        itemMap.put(item.getId(), item);
    }

    public void remove(Item item) {
        itemMap.remove(item.getId());
    }
}
