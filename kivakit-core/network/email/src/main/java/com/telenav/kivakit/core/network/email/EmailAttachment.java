////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.email;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitExcludeProperty;
import com.telenav.kivakit.core.network.email.project.lexakai.diagrams.DiagramEmail;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

@UmlClassDiagram(diagram = DiagramEmail.class)
public class EmailAttachment
{
    private byte[] data;

    private String mimeType;

    private String filename;

    @KivaKitExcludeProperty
    public byte[] data()
    {
        return data;
    }

    public EmailAttachment data(final byte[] data)
    {
        this.data = data;
        return this;
    }

    public String filename()
    {
        return filename;
    }

    public EmailAttachment filename(final String filename)
    {
        this.filename = filename;
        return this;
    }

    public String mimeType()
    {
        return mimeType;
    }

    public EmailAttachment mimeType(final String mimeType)
    {
        this.mimeType = mimeType;
        return this;
    }

    MimeBodyPart bodyPart()
    {
        try
        {
            final var part = new MimeBodyPart();
            part.setDataHandler(new DataHandler(new ByteArrayDataSource(data, mimeType)));
            part.setFileName(filename);
            return part;
        }
        catch (final MessagingException e)
        {
            throw new IllegalStateException("Cannot create body part for attachment " + this, e);
        }
    }
}
