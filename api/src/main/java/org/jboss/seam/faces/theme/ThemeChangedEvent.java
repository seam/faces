package org.jboss.seam.faces.theme;

public class ThemeChangedEvent {
    private String themeName;

    public ThemeChangedEvent(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeName() {
        return themeName;
    }
}
