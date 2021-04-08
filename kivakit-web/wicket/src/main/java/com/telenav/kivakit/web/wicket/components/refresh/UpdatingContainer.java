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

package com.telenav.kivakit.web.wicket.components.refresh;

import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.web.wicket.library.Components;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;

import java.util.function.Consumer;

/**
 * A {@link WebMarkupContainer} that refreshes itself at the given {@link Frequency}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class UpdatingContainer extends WebMarkupContainer
{
    public UpdatingContainer(final String id, final Frequency frequency)
    {
        this(id, frequency, target ->
        {
        });
    }

    public UpdatingContainer(final String id, final Frequency frequency, final Consumer<AjaxRequestTarget> target)
    {
        super(id);
        Components.update(this, frequency, target);
    }
}
