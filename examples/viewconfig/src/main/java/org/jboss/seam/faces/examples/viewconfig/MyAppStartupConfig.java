package org.jboss.seam.faces.examples.viewconfig;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.jboss.seam.faces.theme.ThemeConfig;

@Singleton
@Startup
public class MyAppStartupConfig {

    @Inject
    private ThemeConfig themeConfig;

    @PostConstruct
    public void onLoad() {
        themeConfig.setAvailableThemes(new String[]{"blue", "red", "black"});
    }
}
