////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.email.converters;

import com.telenav.kivakit.core.network.email.EmailAttachment;
import com.telenav.kivakit.core.kernel.data.conversion.string.collection.BaseListConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;

public class AttachmentListConverter extends BaseListConverter<EmailAttachment>
{
    public AttachmentListConverter(final Listener listener)
    {
        super(listener, new AttachmentConverter(listener), ",");
    }
}
