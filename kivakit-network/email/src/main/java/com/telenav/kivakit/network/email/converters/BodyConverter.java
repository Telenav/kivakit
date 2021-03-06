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

import com.telenav.kivakit.conversion.BaseTwoWayConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.network.email.EmailBody;
import com.telenav.kivakit.network.email.HtmlEmailBody;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Converts to and from {@link EmailBody} models.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class BodyConverter extends BaseTwoWayConverter<StringList, EmailBody>
{
    public BodyConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected EmailBody onConvert(final StringList columns)
    {
        var mimeType = columns.get(0);
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
    protected StringList onUnconvert(final EmailBody emailBody)
    {
        var list = new StringList(Maximum._2);
        list.add(emailBody.mimeType());
        list.add(emailBody.text());
        return list;
    }
}
