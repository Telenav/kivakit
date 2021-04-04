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
