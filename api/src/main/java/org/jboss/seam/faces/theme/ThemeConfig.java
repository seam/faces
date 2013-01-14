package org.jboss.seam.faces.theme;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ThemeConfig {

    private String[] availableThemes;
    public static final int DEFAULT_MAX_AGE = 31536000; // 1 year in seconds
    private boolean cookieEnabled = true;
    private int cookieMaxAge = DEFAULT_MAX_AGE;
    private String cookiePath = "/";
    private String cookieName = "org.jboss.seam.core.Theme";

    public boolean isCookieEnabled() {
        return cookieEnabled;
    }

    public void setCookieEnabled(boolean cookieEnabled) {
        this.cookieEnabled = cookieEnabled;
    }

    public int getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String[] getAvailableThemes() {
        return availableThemes;
    }

    public void setAvailableThemes(String[] availableThemes) {
        this.availableThemes = availableThemes;
    }
}