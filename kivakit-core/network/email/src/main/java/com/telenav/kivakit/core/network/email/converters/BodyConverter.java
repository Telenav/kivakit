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

import com.telenav.kivakit.core.kernel.data.conversion.string.collection.BaseCollectionConverter;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.network.email.EmailBody;
import com.telenav.kivakit.core.network.email.HtmlEmailBody;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Converts to and from {@link EmailBody} models.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
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
