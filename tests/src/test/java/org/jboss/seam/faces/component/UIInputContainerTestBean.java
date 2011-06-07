package org.jboss.seam.faces.component;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.jboss.seam.international.status.Messages;

@Named("inputContainerTestBean")
@RequestScoped
public class UIInputContainerTestBean {

    @Inject
    private Messages messages;

    private String name;
    private Integer age;

    private String foo = "foo";

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void submitTest() {
        messages.info("The test succeeded with {0} of {1} years old", name, age);
    }

}
