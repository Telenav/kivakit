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

package com.telenav.kivakit.kernel.language.objects.reference.virtual.types;

import com.telenav.kivakit.kernel.interfaces.value.Source;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageObjectReference;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

@UmlClassDiagram(diagram = DiagramLanguageObjectReference.class)
@UmlExcludeSuperTypes({ Source.class })
public class HardReferencedValue<T> implements Source<T>
{
    private final T value;

    public HardReferencedValue(final T value)
    {
        this.value = value;
    }

    @Override
    public T get()
    {
        return value;
    }
}
