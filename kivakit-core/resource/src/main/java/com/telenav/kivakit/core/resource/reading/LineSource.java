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

package com.telenav.kivakit.core.resource.reading;

import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Source of a {@link LineReader} and of lines of text.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public interface LineSource
{
    /**
     * @return The line reader
     */
    LineReader lineReader();

    /**
     * @return Lines in this line source
     */
    Iterable<String> lines();
}
