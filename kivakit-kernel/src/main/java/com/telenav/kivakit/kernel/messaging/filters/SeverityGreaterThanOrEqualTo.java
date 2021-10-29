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

package com.telenav.kivakit.kernel.messaging.filters;

import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.MessageFilter;
import com.telenav.kivakit.kernel.messaging.messages.Severity;

public class SeverityGreaterThanOrEqualTo implements MessageFilter
{
    private final Severity value;

    public SeverityGreaterThanOrEqualTo(Severity value)
    {
        this.value = value;
    }

    @Override
    public boolean accepts(Message value)
    {
        return value.severity().isGreaterThanOrEqualTo(this.value);
    }

    @Override
    public String toString()
    {
        return "severityGreaterThan(" + value + ")";
    }
}
