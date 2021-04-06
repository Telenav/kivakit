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

package com.telenav.kivakit.web.wicket.components.header;

import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.language.values.version.Version;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * @author jonathanl (shibo)
 */
public class HeaderPanel extends Panel
{
    /**
     * @param id Wicket identifier
     * @param version Project version
     * @param title Header title
     */
    public HeaderPanel(final String id, final Version version, final String title)
    {
        super(id);
        add(new Label("title", title));
        add(new Image("icon", new PackageResourceReference(getClass(), "kivakit-logo.png")));
        add(new Label("version", version + " / KivaKit " + KivaKit.get().version() + " " + KivaKit.get().build()));
    }
}
