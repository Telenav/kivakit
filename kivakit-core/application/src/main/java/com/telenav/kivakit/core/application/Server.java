package com.telenav.kivakit.core.application;

import com.telenav.kivakit.core.application.project.lexakai.diagrams.DiagramApplication;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * Subclass of {@link Application} for use by servers.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramApplication.class)
public abstract class Server extends Application
{
    protected Server(final Project project)
    {
        super(project);
    }
}
