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

package com.telenav.kivakit.network.email.converters;

import com.telenav.kivakit.kernel.data.conversion.string.collection.BaseCollectionConverter;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.network.email.EmailAttachment;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.Base64;

/**
 * Converter to and from {@link EmailAttachment} objects
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class AttachmentConverter extends BaseCollectionConverter<EmailAttachment>
{
    public AttachmentConverter(Listener listener)
    {
        super(listener, ",");
    }

    @Override
    protected EmailAttachment onConvertToObject(StringList columns)
    {
        if (columns.size() == 3)
        {
            var attachment = new EmailAttachment();
            attachment.mimeType(columns.get(0));
            attachment.filename(columns.get(1));
            attachment.data(Base64.getEncoder().encode(columns.get(2).getBytes()));
            return attachment;
        }
        return null;
    }

    @Override
    protected StringList onConvertToStringList(EmailAttachment value)
    {
        var list = new StringList();
        list.add(value.mimeType());
        list.add(value.filename());
        list.add(new String(Base64.getDecoder().decode(value.data())));
        return list;
    }
}
