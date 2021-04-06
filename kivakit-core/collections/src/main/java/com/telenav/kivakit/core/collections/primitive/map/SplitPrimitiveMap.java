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

package com.telenav.kivakit.core.collections.primitive.map;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMap;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

@UmlClassDiagram(diagram = DiagramPrimitiveMap.class)
public abstract class SplitPrimitiveMap extends PrimitiveMap
{
    protected SplitPrimitiveMap(final String name)
    {
        super(name);
    }

    protected SplitPrimitiveMap()
    {
    }

    @Override
    protected final void copyEntries(final PrimitiveMap that, final ProgressReporter reporter)
    {
        unsupported();
    }

    @Override
    protected final PrimitiveMap newMap()
    {
        return unsupported();
    }
}

