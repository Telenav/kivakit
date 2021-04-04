////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.compression.archive;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.field.AllFields;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * A filter that matches all fields annotated with {@link KivaKitArchivedField}
 */
class ArchivedFields extends AllFields
{
    public ArchivedFields()
    {
        super(Collections.emptySet());
    }

    @Override
    public boolean includeField(final Field field)
    {
        return field.getAnnotation(KivaKitArchivedField.class) != null;
    }
}
