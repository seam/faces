package org.jboss.seam.faces.examples.viewconfig.model;

import javax.faces.component.UIComponent;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.jboss.seam.faces.conversion.Converter;

/**
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@FacesConverter("itemConverter")
public class ItemConverter extends Converter<Item> {
    @Inject
    private ItemDao itemDao;

    @Override
    public Item toObject(UIComponent uic, String string) {
        Integer id = Integer.parseInt(string);
        return itemDao.find(id);
    }

    @Override
    public String toString(UIComponent uic, Item t) {
        return t.getId().toString();
    }


}
