package org.jboss.seam.faces.theme;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.jboss.solder.logging.Logger;

/**
 * Selects the current user's theme
 */
@SessionScoped
@Named("themeSelector")
public class ThemeSelector implements Serializable {

    private static final long serialVersionUID = 3920407140011388341L;
    private transient final Logger log = Logger.getLogger(ThemeSelector.class);
    private String theme;

    @Inject
    private ThemeConfig themeConfig;

    @Inject
    private Event<ThemeChangedEvent> event;

    private boolean cookieEnabled;
    private int cookieMaxAge;
    private String cookiePath;
    private String cookieName;
    private String[] availableThemes;

    @PostConstruct
    public void initDefaultTheme() {
        initParams();
        String themeName = getCookieValueIfEnabled();
        if (isThemeAvailable(themeName)) {
            setTheme(themeName);
        } else {
            theme = availableThemes[0];
        }
    }

    public void selectTheme(String themeName) {
        removeFromAllContexts("theme");
        setTheme(themeName);
        setCookieValueIfEnabled(themeName);
        raiseThemeEvent(themeName);
    }

    public void select(ValueChangeEvent event) {
        selectTheme((String) event.getNewValue());
    }

    public void select() {
        if (theme == null) {
            throw new IllegalStateException("no themes defined");
        }
        selectTheme(theme);
    }

    public List<SelectItem> getThemes() {
        List<SelectItem> selectItems = new ArrayList<SelectItem>(availableThemes.length);
        for (String name : availableThemes) {
            selectItems.add(new SelectItem(name, getLocalizedThemeName(name)));
        }
        return selectItems;
    }

    @Produces
    @RequestScoped
    @Named("theme")
    @Theme
    public Map getThemeMap() {
        return createMap();
    }

    /**
     * Get the name of the current theme
     */
    public String getTheme() {
        return theme;
    }

    public void setTheme(String themeName) {
        this.theme = themeName;
    }

    private void initParams() {
        cookieEnabled = themeConfig.isCookieEnabled();
        cookieMaxAge = themeConfig.getCookieMaxAge();
        cookiePath = themeConfig.getCookiePath();
        cookieName = themeConfig.getCookieName();
        availableThemes = themeConfig.getAvailableThemes();
        if (availableThemes == null || availableThemes.length == 0) {
            throw new IllegalStateException("no themes defined");
        }
    }

    private void setCookieValueIfEnabled(String value) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (cookieEnabled && ctx != null) {
            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            Cookie cookie = new Cookie(cookieName, value);
            cookie.setMaxAge(cookieMaxAge);
            cookie.setPath(cookiePath);
            response.addCookie(cookie);
        }
    }

    private String getCookieValueIfEnabled() {
        return cookieEnabled ? getCookieValue() : null;
    }

    private Cookie getCookie() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            return (Cookie) ctx.getExternalContext().getRequestCookieMap().get(themeConfig.getCookieName());
        } else {
            return null;
        }
    }

    private String getCookieValue() {
        Cookie cookie = getCookie();
        return cookie == null ? null : cookie.getValue();
    }

    private void raiseThemeEvent(String themeName) {
        event.fire(new ThemeChangedEvent(themeName));
    }

    private boolean isThemeAvailable(String themeName) {
        return (themeName != null && Arrays.asList(availableThemes).contains(themeName));
    }

    private void removeFromAllContexts(String nameTheme) {
        // TODO : this method should scan all CDI scopes
        // but don't know how to achieve it
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getRequestMap().remove(nameTheme);
        externalContext.getSessionMap().remove(nameTheme);
        externalContext.getApplicationMap().remove(nameTheme);
    }

    protected Map<String, String> createMap() {
        final ResourceBundle bundle = getThemeResourceBundle();
        if (bundle == null) {
            return null;
        }
        return new AbstractMap<String, String>() {
            @Override
            public String get(Object key) {
                if (key instanceof String) {
                    String resourceKey = (String) key;
                    String resource;
                    try {
                        resource = bundle.getString(resourceKey);
                    } catch (MissingResourceException mre) {
                        return resourceKey;
                    }
                    if (resource == null) {
                        return resourceKey;
                    } else {
                        return resource;
                    }
                } else {
                    return null;
                }
            }

            @Override
            public Set<Entry<String, String>> entrySet() {
                Set<Entry<String, String>> entrySet = new HashSet<Entry<String, String>>();
                Enumeration<String> keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    final String key = keys.nextElement();
                    entrySet.add(new Entry<String, String>() {

                        public String getKey() {
                            return key;
                        }

                        public String getValue() {
                            return get(key);
                        }

                        public String setValue(String arg0) {
                            throw new UnsupportedOperationException("not implemented");
                        }
                    });
                }
                return entrySet;
            }
        };
    }

    /**
     * Get the localized name of the named theme, by looking for
     * org.jboss.seam.theme.&lt;name&gt; in the JSF messageBundle
     * bundle
     */
    public String getLocalizedThemeName(String name) {

        String key = "org.jboss.seam.theme." + name;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String messageBundle = facesContext.getApplication().getMessageBundle();
        if (messageBundle != null) {
            Locale locale = facesContext.getViewRoot().getLocale();
            try {
                ResourceBundle resourceBundle = ResourceBundle.getBundle(messageBundle, locale);
                return resourceBundle.getString(key);
            } catch (MissingResourceException e) {
                log.debugf(e, "Error loading resourceBundle {0} or loading key {1}", messageBundle, key);
            }
        }
        return key;
    }

    /**
     * Get the resource bundle for the theme
     */
    public ResourceBundle getThemeResourceBundle() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(
                    theme,
                    FacesContext.getCurrentInstance().getViewRoot().getLocale(),
                    Thread.currentThread().getContextClassLoader()
            );
            log.trace("loaded resource bundle: " + theme);
            return bundle;

        } catch (MissingResourceException mre) {
            log.debug("resource bundle missing: " + theme);
            return new ResourceBundle() {

                @Override
                public Enumeration<String> getKeys() {
                    return new IteratorEnumeration(Collections.EMPTY_LIST.iterator());
                }

                @Override
                protected Object handleGetObject(String key) {
                    return null;
                }
            };
        }
    }

    private static class IteratorEnumeration implements Enumeration {
        private Iterator iterator;

        public IteratorEnumeration(Iterator iterator) {
            this.iterator = iterator;
        }

        public boolean hasMoreElements() {
            return iterator.hasNext();
        }

        public Object nextElement() {
            return iterator.next();
        }
    }
}