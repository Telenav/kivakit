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

package com.telenav.kivakit.kernel.language.objects.reference.virtual.types;

import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageObjectReference;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

@UmlClassDiagram(diagram = DiagramLanguageObjectReference.class)
@UmlExcludeSuperTypes({ Source.class, Named.class })
public class SoftReferencedValue<T> extends SoftReference<T> implements Source<T>, Named
{
    private final String name;

    public SoftReferencedValue(String name, T value, ReferenceQueue<T> queue)
    {
        super(value, queue);
        this.name = name;
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public T get()
    {
        return super.get();
    }

    @Override
    public String name()
    {
        return name;
    }
}
