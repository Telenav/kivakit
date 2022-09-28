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

package com.telenav.kivakit.core.string;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Interface to an object that can produce one or more different kinds of string representations. This can be useful
 * when the {@link Object#toString()} method is already being used or when other kinds of strings are needed for
 * specific purposes.
 * <p>
 * The {@link Format} enum defines different kinds of string representations which can be retrieved with
 * {@link #asString(Format)}. The object can override this method to provide multiple string representations for
 * specific purposes.
 *
 * @author jonathanl (shibo)
 */
@UmlRelation(label = "formats with", referent = StringFormattable.Format.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public interface AsString extends StringFormattable
{
    /**
     * @return A string representation of this object that is suitable for the given purpose
     */
    @Override
    default String asString(Format format)
    {
        return toString();
    }
}
