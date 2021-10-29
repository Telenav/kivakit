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

package com.telenav.kivakit.security.authentication.passwords;

import com.telenav.kivakit.security.project.lexakai.diagrams.DiagramSecurity;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Base64;

/**
 * A message-digested password created with the {@link Base64} encoder in *java.util*. This is not at all secure, but
 * can make it difficult to memorize a password on seeing it briefly.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSecurity.class)
@LexakaiJavadoc(complete = true)
public class DigestPassword extends PlainTextPassword
{
    public DigestPassword(String password)
    {
        super(Base64.getEncoder().encodeToString(password.getBytes()));
    }
}
