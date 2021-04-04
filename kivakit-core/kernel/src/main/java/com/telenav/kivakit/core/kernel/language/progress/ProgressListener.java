package com.telenav.kivakit.core.kernel.language.progress;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageProgress;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramLanguageProgress.class)
public interface ProgressListener
{
    void at(Percent at);
}
