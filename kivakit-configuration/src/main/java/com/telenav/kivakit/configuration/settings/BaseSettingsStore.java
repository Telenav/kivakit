package com.telenav.kivakit.configuration.settings;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

public abstract class BaseSettingsStore extends BaseRepeater implements SettingsStore
{
    public void install()
    {

    }

    /**
     * <b>Service Provider API</b>
     *
     * <p>
     * Loads a settings object from the given properties resource.
     *
     * @return A settings object loaded from the given properties resource
     */
    @UmlExcludeMember
    protected SettingsObject loadFromProperties(Resource resource)
    {
        // Load the given properties
        trace("Loading configuration from $", resource);
        var properties = PropertyMap.load(this, resource);
        try
        {
            // then get the configuration class to instantiate,
            var configurationClassName = properties.get("class");
            ensureNotNull(configurationClassName, "Missing class property in $", resource);
            var configurationClass = Class.forName(configurationClassName);
            ensureNotNull(configurationClass, "Unable to load class $ specified in $", configurationClass, resource);
            trace("Configuration class: $", configurationClass.getSimpleName());

            // and the name of which identifier of the class to configure (if any)
            var configurationInstance = properties.get("instance");
            var identifier = configurationInstance != null ? InstanceIdentifier.instanceIdentifier(configurationInstance) : InstanceIdentifier.SINGLETON;
            trace("Configuration identifier: $", identifier);

            // then create the configuration object and populate it using the converter framework
            var configuration = properties.asObject(this, configurationClass);
            if (configuration != null)
            {
                trace("Loaded configuration: $", configuration);

                // and return the configuration set entry for the fully loaded configuration object
                return new SettingsObject(new SettingsObject.Identifier(configurationClass, identifier), configuration);
            }
            else
            {
                return fail("Unable to load configuration object from $", resource);
            }
        }
        catch (Exception e)
        {
            return fail(e, "Unable to load properties from $", resource);
        }
    }
}
