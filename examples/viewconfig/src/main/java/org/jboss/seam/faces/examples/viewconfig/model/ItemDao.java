/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
