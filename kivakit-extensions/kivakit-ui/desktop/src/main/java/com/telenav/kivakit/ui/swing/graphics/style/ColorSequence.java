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

package com.telenav.kivakit.ui.swing.graphics.style;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

public class ColorSequence implements Iterator<Color>
{
    private static final List<Color> COLORS = new ArrayList<>();

    static
    {
        for (var hue = 0F; hue < 1.0; hue += 0.075f)
        {
            COLORS.add(Color.of(java.awt.Color.getHSBColor(hue, 1.0f, 1.0f)));
        }
    }

    private int index;

    @Override
    public boolean hasNext()
    {
        return true;
    }

    @Override
    public Color next()
    {
        if (index == COLORS.size())
        {
            index = 0;
        }
        return COLORS.get(index++);
    }

    @Override
    public void remove()
    {
        unsupported();
    }
}
