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

package com.telenav.kivakit.kernel.language.values.count;

import com.telenav.kivakit.kernel.interfaces.numeric.Maximizable;
import com.telenav.kivakit.kernel.interfaces.numeric.Minimizable;
import com.telenav.kivakit.kernel.interfaces.numeric.Ranged;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class Range<T extends Minimizable<T> & Maximizable<T>> implements Ranged<T>
{
    private final T minimum;

    private final T maximum;

    public Range(T minimum, T maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public T constrainTo(T value)
    {
        return maximum.minimum(minimum.maximum(value));
    }

    @Override
    public boolean contains(T value)
    {
        return false;
    }

    @Override
    public T maximum()
    {
        return maximum;
    }

    @Override
    public T minimum()
    {
        return minimum;
    }
}
