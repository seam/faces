package com.jboss.seam.faces.forge;

import javax.inject.Inject;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.builtin.MavenDependencyFacet;
import org.jboss.seam.forge.shell.ShellColor;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;
import org.jboss.seam.forge.shell.project.ProjectScoped;

@Alias("faces")
@RequiresFacet(MavenDependencyFacet.class)
public class FacesPlugin implements Plugin {
    private static final String FACES_ARTIFACT_ID = "seam-faces";
    private static final String FACES_GROUP_ID = "org.jboss.seam.faces";
    private static final String FACES_VERSION = "3.0.0.Final";
    
    @Inject
    @ProjectScoped Project project;
    
    @DefaultCommand
    public void exampleDefaultCommand(@Option String opt, PipeOut pipeOut) {
        pipeOut.println(ShellColor.BLUE, "Use the install command to add a Seam Faces dependency.");
    }
    
    @Command("install")
    public void installCommand(PipeOut pipeOut) {
        DependencyBuilder seamDependency = DependencyBuilder.create();
        seamDependency.setArtifactId(FACES_ARTIFACT_ID);
        seamDependency.setGroupId(FACES_GROUP_ID);
        seamDependency.setVersion(FACES_VERSION);
        DependencyFacet deps = project.getFacet(DependencyFacet.class);
        if (deps.hasDependency(seamDependency)) {
            pipeOut.println(ShellColor.RED, "Seam Faces dependency is already present.");
        } else {
            deps.addDependency(seamDependency);
            pipeOut.println(ShellColor.GREEN, "Seam Faces dependency has been installed.");
        }
    }
}
