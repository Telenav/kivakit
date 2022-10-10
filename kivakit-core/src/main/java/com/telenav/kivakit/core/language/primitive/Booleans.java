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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramPrimitive;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;

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
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Booleans
{
    /**
     * Returns true if the string represents falsity (case-independent), meaning any of:
     *
     * <ul>
     *     <li>disabled</li>
     *     <li>false</li>
     *     <li>no</li>
     *     <li>off</li>
     * </ul>
     */
    public static boolean isFalse(String string)
    {
        return "disabled".equalsIgnoreCase(string)
                || "false".equalsIgnoreCase(string)
                || "no".equalsIgnoreCase(string)
                || "off".equalsIgnoreCase(string);
    }

    /**
     * Returns true if the string represents truth (case-independent), meaning any of:
     *
     * <ul>
     *     <li>enabled</li>
     *     <li>true</li>
     *     <li>yes</li>
     *     <li>on</li>
     * </ul>
     */
    public static boolean isTrue(String string)
    {
        return "enabled".equalsIgnoreCase(string)
                || "true".equalsIgnoreCase(string)
                || "yes".equalsIgnoreCase(string)
                || "on".equalsIgnoreCase(string);
    }
}
