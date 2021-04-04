package com.telenav.kivakit.core.kernel.interfaces.model;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceModel;

/**
 * Listens to a model for changes in its internal state.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceModel.class)
public interface ModelListener<Model>
{
    /**
     * Called when the model changes
     *
     * @param model The model
     */
    void modelChanged(Model model);
}
