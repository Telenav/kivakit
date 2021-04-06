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
