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

package com.telenav.kivakit.core.language.primitive;

import com.telenav.kivakit.core.project.lexakai.DiagramPrimitive;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Utility methods for <i>boolean</i>> values.
 *
 * <ul>
 *     <li>{@link #isFalse(String)}</li>
 *     <li>{@link #isTrue(String)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitive.class)
@LexakaiJavadoc(complete = true)
public class Booleans
{
    public static boolean isFalse(String string)
    {
        return "false".equalsIgnoreCase(string) || "f".equalsIgnoreCase(string) || "no".equalsIgnoreCase(string)
                || "disabled".equalsIgnoreCase(string);
    }

    public static boolean isTrue(String string)
    {
        return "true".equalsIgnoreCase(string) || "t".equalsIgnoreCase(string) || "yes".equalsIgnoreCase(string)
                || "enabled".equalsIgnoreCase(string);
    }
}
