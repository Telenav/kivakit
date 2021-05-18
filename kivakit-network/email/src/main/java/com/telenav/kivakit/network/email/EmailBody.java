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

package com.telenav.kivakit.network.email;

import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.network.email.project.lexakai.diagrams.DiagramEmail;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

/**
 * The body of an email in plain text.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramEmail.class)
@LexakaiJavadoc(complete = true)
public class EmailBody
{
    public static final String MIME_TYPE = "text/plain";

    final MimeBodyPart bodyPart = new MimeBodyPart();

    private final String text;

    public EmailBody(final String text)
    {
        this.text = text;
        try
        {
            bodyPart.setContent(text(), mimeType());
        }
        catch (final MessagingException e)
        {
            throw new Problem(e, "Can't create body").asException();
        }
    }

    @KivaKitIncludeProperty
    public String mimeType()
    {
        return MIME_TYPE;
    }

    @KivaKitIncludeProperty
    public String text()
    {
        return text;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
