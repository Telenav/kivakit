////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.email;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.network.email.project.lexakai.diagrams.DiagramEmail;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

@UmlClassDiagram(diagram = DiagramEmail.class)
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
