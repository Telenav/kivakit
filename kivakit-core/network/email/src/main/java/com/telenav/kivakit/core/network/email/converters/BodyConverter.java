////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.email.converters;

import com.telenav.kivakit.core.network.email.EmailBody;
import com.telenav.kivakit.core.network.email.HtmlEmailBody;
import com.telenav.kivakit.core.kernel.data.conversion.string.collection.BaseCollectionConverter;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.messaging.Listener;

public class BodyConverter extends BaseCollectionConverter<EmailBody>
{
    public BodyConverter(final Listener listener)
    {
        super(listener, ",");
    }

    @Override
    protected EmailBody onConvertToObject(final StringList columns)
    {
        final var mimeType = columns.get(0);
        if (EmailBody.MIME_TYPE.equals(mimeType))
        {
            return new EmailBody(columns.get(1));
        }
        if (HtmlEmailBody.MIME_TYPE.equals(mimeType))
        {
            return new HtmlEmailBody(columns.get(1));
        }
        throw new IllegalStateException("Invalid mime type " + mimeType);
    }

    @Override
    protected StringList onConvertToStringList(final EmailBody value)
    {
        final var list = new StringList(Maximum._2);
        list.add(value.mimeType());
        list.add(value.text());
        return list;
    }
}
