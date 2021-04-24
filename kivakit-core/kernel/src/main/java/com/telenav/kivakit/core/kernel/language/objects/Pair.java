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

package com.telenav.kivakit.core.kernel.language.objects;

import com.telenav.kivakit.core.kernel.language.iteration.BaseIterator;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

@UmlClassDiagram(diagram = DiagramLanguageObject.class)
public class Pair<T> implements Iterable<T>
{
    private final T a;

    private final T b;

    public Pair(final T a, final T b)
    {
        this.a = a;
        this.b = b;
    }

    public T a()
    {
        return a;
    }

    public T b()
    {
        return b;
    }

    public boolean isIdentity()
    {
        return a.equals(b);
    }

    @Override
    public Iterator<T> iterator()
    {
        return new BaseIterator<>()
        {
            int index;

            @Override
            protected T onNext()
            {
                switch (index++)
                {
                    case 0:
                        return a;

                    case 1:
                        return b;
                }
                return null;
            }
        };
    }
}
