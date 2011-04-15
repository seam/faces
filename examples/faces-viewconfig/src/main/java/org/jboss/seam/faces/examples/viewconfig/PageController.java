package org.jboss.seam.faces.examples.viewconfig;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.seam.faces.examples.viewconfig.model.Current;
import org.jboss.seam.faces.examples.viewconfig.model.Item;
import org.jboss.seam.faces.examples.viewconfig.model.ItemDao;

/**
 *
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

    @Produces @Current
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Item> getItems() {
        return items;
    }
    
}