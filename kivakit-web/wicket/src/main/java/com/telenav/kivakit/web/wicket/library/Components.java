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

package com.telenav.kivakit.web.wicket.library;

import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;

import java.util.function.Consumer;

/**
 * Utility methods useful for Apache Wicket {@link Component}s.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class Components
{
    public static void update(final Component component,
                              final Frequency frequency,
                              final Consumer<AjaxRequestTarget> afterUpdate)
    {
        component.setOutputMarkupId(true);
        component.setOutputMarkupPlaceholderTag(true);
        component.add(new AjaxSelfUpdatingTimerBehavior(frequency.cycleLength().asJavaDuration())
        {
            @Override
            protected void onPostProcessTarget(final AjaxRequestTarget target)
            {
                super.onPostProcessTarget(target);
                afterUpdate.accept(target);
            }
        });
    }
}
