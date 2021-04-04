////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.email.converters;

import com.telenav.kivakit.core.network.email.EmailAttachment;
import com.telenav.kivakit.core.kernel.data.conversion.string.collection.BaseCollectionConverter;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.messaging.Listener;

import java.util.Base64;

public class AttachmentConverter extends BaseCollectionConverter<EmailAttachment>
{
    public AttachmentConverter(final Listener listener)
    {
        super(listener, ",");
    }

    @Override
    protected EmailAttachment onConvertToObject(final StringList columns)
    {
        if (columns.size() == 3)
        {
            final var attachment = new EmailAttachment();
            attachment.mimeType(columns.get(0));
            attachment.filename(columns.get(1));
            attachment.data(Base64.getEncoder().encode(columns.get(2).getBytes()));
            return attachment;
        }
        return null;
    }

    @Override
    protected StringList onConvertToStringList(final EmailAttachment value)
    {
        final var list = new StringList();
        list.add(value.mimeType());
        list.add(value.filename());
        list.add(new String(Base64.getDecoder().decode(value.data())));
        return list;
    }
}
