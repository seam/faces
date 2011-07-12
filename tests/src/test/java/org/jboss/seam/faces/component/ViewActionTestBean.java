package org.jboss.seam.faces.component;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * @author <a href="http://community.jboss.org/people/bleathem)">Brian Leathem</a>
 */
@RequestScoped
@Named
public class ViewActionTestBean {
    private String data;

    public String gotoResult() {
        return "/result";
    }

    public void loadData() {
        data = "Data Loaded";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
