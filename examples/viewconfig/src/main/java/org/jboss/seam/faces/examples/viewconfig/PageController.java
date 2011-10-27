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
package org.jboss.seam.faces.examples.viewconfig;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.faces.examples.viewconfig.MyAppViewConfig.Pages;
import org.jboss.seam.faces.examples.viewconfig.model.Current;
import org.jboss.seam.faces.examples.viewconfig.model.Item;
import org.jboss.seam.faces.examples.viewconfig.model.ItemDao;

/**
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@ViewScoped
@Named
public class PageController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private ItemDao itemDao;
    private Item item;
    private List<Item> items;

    @PostConstruct
    void init() {
        items = itemDao.findAll();
    }

    @Produces
    @Current
    public Item getItem() {
        return item;
    }

    @MyViewAction(Pages.ITEM)
    public void loadEntry() {
    	System.out.println("loadEntry called");
    }
    
    @MyViewAction(Pages.ITEM)
    public void setItem(Item item) {
        this.item = item;
    }

    public List<Item> getItems() {
        return items;
    }

}
