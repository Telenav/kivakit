package com.telenav.kivakit.configuration.settings.stores.resource;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.settings.BaseSettingsStore;
import com.telenav.kivakit.configuration.settings.SettingsObject;
import com.telenav.kivakit.configuration.settings.SettingsStore;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.kernel.language.reflection.populator.ObjectPopulator;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * <b>Service Provider API</b>
 *
 * <p>
 * A {@link SettingsStore} that reads <i>.properties</i> resources.
 * </p>
 *
 * <p>
 * This class is the base class for {@link FolderSettingsStore} and {@link PackageSettingsStore}, which load settings
 * information from <i>.properties</i> files in folders and packages, respectively. Settings objects are loaded when
 * they are requested, and each <i>.properties</i> resource describes <i>just one settings object</i>. This means that a
 * settings folder or package will often have more than one <i>.properties</i> resource in it, such as:
 * </p>
 *
 * <pre>
 * settings
 *  |── DatabaseSettings.properties
 *  ├── WebSettings.properties
 *  └── HdfsSettings.properties
 * </pre>
 *
 * <p><b>Properties File Format</b></p>
 *
 * <p>
 * Properties files are of this general form:
 * </p>
 *
 * <p><i>Server1.properties</i></p>
 *
 * <pre>
 * class=com.telenav.navigation.my.application.Server$Settings
 * instance=SERVER1
 * port=aws.amazon.com:7001
 * </pre>
 *
 * <p>
 * Here, the "class" key designates a class to instantiate (note that the nested class has to be indicated with '$'
 * rather than '.' here). The object that is created from this class is populated with the property values by using
 * {@link ObjectPopulator}, which automatically converts each property value into an object using the converter
 * framework. To do this, properties in the settings object are tagged with {@link KivaKitPropertyConverter} indicating
 * which converter the {@link ObjectPopulator} should use to convert a string value in the properties file to the
 * corresponding object. For example, in this case the property converter for the settings class above is Port.Converter
 * and the port property is converted to a Port object:
 * </p>
 *
 * <p><i>Server.Settings Class</i></p>
 *
 * <pre>
 * public class Settings
 * {
 *     private Port port;
 *
 *    {@literal @}KivaKitPropertyConverter(Port.Converter.class)
 *     public void port( Port port)
 *     {
 *         this.port = port;
 *     }
 *
 *    [...]
 * }
 * </pre>
 *
 * <p>
 * The properties file key "instance" designates which instance of settings object the <i>.properties</i> file refers to
 * (in the event that more than one settings object of the same type needs to be registered at the same time).
 * </p>
 *
 * @author jonathanl (shibo)
 * @see SettingsStore
 * @see FolderSettingsStore
 * @see PackageSettingsStore
 */
public abstract class BaseResourceSettingsStore extends BaseSettingsStore implements SettingsStore
{
    /**
     * Loads a settings object from the given properties resource.
     *
     * @return A settings object loaded from the given properties resource
     */
    @UmlExcludeMember
    protected SettingsObject loadFromProperties(Resource resource)
    {
        // Load the given properties
        trace("Loading settings from $", resource);
        var properties = PropertyMap.load(this, resource);
        try
        {
            // then get the settings class to instantiate,
            var typeName = properties.get("class");
            ensureNotNull(typeName, "Missing class property in $", resource);
            var type = Class.forName(typeName);
            ensureNotNull(type, "Unable to load class $ specified in $", type, resource);
            trace("Settings class: $", type.getSimpleName());

            // and the instance identifier (if any),
            var instance = properties.get("instance");
            var identifier = instance != null ? InstanceIdentifier.instanceIdentifier(instance) : InstanceIdentifier.SINGLETON;
            trace("Settings identifier: $", identifier);

            // then create the settings object and populate it using the converter framework
            var settings = properties.asObject(this, type);
            if (settings != null)
            {
                trace("Loaded settings: $", settings);

                // and return the settings set entry for the fully loaded settings object
                return new SettingsObject(type, identifier, settings);
            }
            else
            {
                return fail("Unable to load settings object from $", resource);
            }
        }
        catch (Exception e)
        {
            return fail(e, "Unable to load properties from $", resource);
        }
    }
}
