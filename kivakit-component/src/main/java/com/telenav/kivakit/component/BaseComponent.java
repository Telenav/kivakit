package com.telenav.kivakit.component;

import com.telenav.kivakit.component.project.lexakai.diagrams.DiagramComponent;
import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.function.Consumer;

/**
 * Base class for KivaKit components. Provides easy access to object registration and lookup (see {@link Registry}) as
 * well as settings registration and lookup (see {@link Settings}). For details, see {@link Component}.
 *
 * @author jonathanl (shibo)
 * @see Component
 * @see Registry
 * @see Settings
 */
@UmlClassDiagram(diagram = DiagramComponent.class)
public class BaseComponent extends BaseRepeater implements Component
{
}
