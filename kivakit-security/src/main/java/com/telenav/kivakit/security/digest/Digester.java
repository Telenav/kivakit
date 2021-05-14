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

package com.telenav.kivakit.security.digest;

import com.telenav.kivakit.security.project.lexakai.diagrams.DiagramSecurityDigest;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Interface to message digesters. The {@link #digest(byte[])} and {@link #digest(String)} methods return a byte array
 * containing the message digest. The length of this array varies depending on the algorithm used.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSecurityDigest.class)
@LexakaiJavadoc(complete = true)
public interface Digester
{
    /**
     * @param value The value to create a digest of
     * @return The digest
     */
    byte[] digest(final byte[] value);

    /**
     * @return A message digest of the given string
     */
    default byte[] digest(final String value)
    {
        return digest(value.getBytes());
    }
}
