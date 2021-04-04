////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.objects.reference.virtual;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageObjectReference;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * The kind of reference to use to refer to objects that exceed the {@link VirtualReferenceTracker}'s maximum memory
 * size
 */
@UmlClassDiagram(diagram = DiagramLanguageObjectReference.class)
public enum VirtualReferenceType
{
    /** Soft reference */
    SOFT,

    /** Weak reference */
    WEAK,

    /** Don't keep a reference at all */
    NONE
}
