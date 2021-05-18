////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.reflection.access.field;

import com.telenav.kivakit.kernel.language.reflection.Field;
import com.telenav.kivakit.kernel.language.reflection.access.Getter;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;

@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class FieldGetter implements Getter
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private final transient java.lang.reflect.Field field;

    public FieldGetter(final java.lang.reflect.Field field)
    {
        this.field = field;
    }

    @Override
    public <T extends Annotation> T annotation(final Class<T> annotationType)
    {
        return field.getAnnotation(annotationType);
    }

    @Override
    public Object get(final Object object)
    {
        try
        {
            if (Field.accessible(field))
            {
                return field.get(object);
            }
            LOGGER.warning("Cannot get ${debug}", this);
        }
        catch (final Exception e)
        {
            LOGGER.warning(e, "Cannot get ${debug}", this);
        }
        return null;
    }

    @Override
    public String name()
    {
        return field.getName();
    }

    @Override
    public String toString()
    {
        return "[FieldGetter name = " + field.getName() + ", type = " + type() + "]";
    }

    @Override
    public Class<?> type()
    {
        return field.getType();
    }
}
