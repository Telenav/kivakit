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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.BaseTwoWayConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.network.email.EmailAttachment;

import java.util.Base64;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Converter to and from {@link EmailAttachment} objects
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class AttachmentConverter extends BaseTwoWayConverter<StringList, EmailAttachment>
{
    public AttachmentConverter(Listener listener)
    {
        super(listener, StringList.class, EmailAttachment.class);
    }

    @Override
    protected EmailAttachment onConvert(StringList value)
    {
        if (value.size() == 3)
        {
            var attachment = new EmailAttachment();
            attachment.mimeType(value.get(0));
            attachment.filename(value.get(1));
            attachment.data(Base64.getEncoder().encode(value.get(2).getBytes()));
            return attachment;
        }
        return null;
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    protected StringList onUnconvert(EmailAttachment emailAttachment)
    {
        var list = new StringList();
        list.add(emailAttachment.mimeType());
        list.add(emailAttachment.filename());
        list.add(new String(Base64.getDecoder().decode(emailAttachment.data())));
        return list;
    }
}
