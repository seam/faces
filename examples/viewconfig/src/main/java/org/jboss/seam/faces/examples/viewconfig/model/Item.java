package org.jboss.seam.faces.examples.viewconfig.model;

/**
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class Item {
    private Integer id;
    private String title;
    private String owner;

    public Item(Integer id, String title, String owner) {
        this.id = id;
        this.title = title;
        this.owner = owner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
