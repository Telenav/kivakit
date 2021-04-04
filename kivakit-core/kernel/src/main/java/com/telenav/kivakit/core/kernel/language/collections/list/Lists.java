package com.telenav.kivakit.core.kernel.language.collections.list;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsList;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsList.class)
public class Lists
{
    @SafeVarargs
    public static <T> ArrayList<T> arrayList(final T... objects)
    {
        return new ArrayList<>(List.of(objects));
    }
}
