////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.resource.compression.archive;

import com.telenav.kivakit.kernel.language.reflection.property.filters.field.AllFields;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * A filter that matches all fields annotated with {@link KivaKitArchivedField}
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
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
