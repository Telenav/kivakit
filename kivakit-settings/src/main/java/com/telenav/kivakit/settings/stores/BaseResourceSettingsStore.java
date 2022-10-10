package com.telenav.kivakit.settings.stores;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.core.language.object.KivaKitConverted;
import com.telenav.kivakit.conversion.core.language.object.ObjectPopulator;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.serialization.ObjectReader;
import com.telenav.kivakit.resource.serialization.ObjectSerializerRegistry;
import com.telenav.kivakit.settings.BaseSettingsStore;
import com.telenav.kivakit.settings.SettingsObject;
import com.telenav.kivakit.settings.SettingsStore;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.OBJECT_INSTANCE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.OBJECT_TYPE;

/**
 * <b>Service Provider API</b>
 *
 * <p>
 * A {@link SettingsStore} that loads settings objects from <i>.properties</i> and <i>.json</i> resources.
 * </p>
 *
 * <p>
 * This class is the base class for {@link ResourceFolderSettingsStore}, which loads settings information from
 * <i>.properties</i> files in folders and packages. Settings objects are loaded when they are requested, and each
 * <i>.properties</i> resource describes <i>just one settings object</i>. This means that a settings folder or package
 * will often have more than one <i>.properties</i> resource in it, such as:
 * </p>
 *
 * <pre>
 * settings
 *  |── DatabaseSettings.properties
 *  ├── WebSettings.properties
 *  └── HdfsSettings.properties</pre>
 *
 * <p><b>Properties File Format</b></p>
 *
 * <p>
 * Properties files are of this general form:
 * </p>
 *
 * <p><i>ServerSettings.properties</i></p>
 *
 * <pre>
 * class=com.telenav.navigation.my.application.ServerSettings
 * instance=SERVER1
 * port=aws.amazon.com:7001</pre>
 *
 * <p>
 * Here, the "class" key designates the class of the settings object to instantiate (note any the nested class has to be
 * indicated with '$' rather than '.'). The object that is created from this class is populated with the property values
 * by using {@link ObjectPopulator}, which automatically converts each property value into an object using the converter
 * framework. To do this, properties in the settings object are tagged with {@link KivaKitConverted} indicating which
 * converter the {@link ObjectPopulator} should use to convert a string value in the properties file to the
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
 *    {@literal @}KivaKitConverted(Port.Converter.class)
 *     public void port( Port port)
 *     {
 *         this.port = port;
 *     }
 *
 *    [...]
 * }</pre>
 *
 * <p>
 * The properties file key "instance" designates which instance of settings object the <i>.properties</i> file refers to
 * (in the event that more than one settings object of the same type needs to be registered at the same time).
 * </p>
 *
 * <p><b>JSON File Format</b></p>
 *
 * <p>
 * JSON files are of this general form:
 * </p>
 *
 * <p><i>ClientSettings.json</i></p>
 *
 * <pre>
 * {
 *     "class" : "com.telenav.navigation.my.application.ClientSettings"
 *     "instance" : "CLIENT1",
 *     "port" : "7001",
 *     "timeout" : "6 seconds"
 * }</pre>
 *
 * <p>
 * The object specified by the "class" property is used to instantiate the settings object. Deserialization of the JSON
 * is performed by a required {@link ObjectReader} object retrieved from the global object {@link Registry}. The
 * "instance" variable is used to distinguish between multiple instances of the same type of settings object.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see SettingsStore
 * @see ResourceFolderSettingsStore
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseResourceSettingsStore extends BaseSettingsStore implements
        SettingsStore,
        RegistryTrait
{
    /**
     * Loads a settings object from the given resource
     *
     * @param resource The resource to read
     * @return The {@link SettingsObject}
     */
    protected SettingsObject read(Resource resource)
    {
        var reader = require(ObjectSerializerRegistry.class, ObjectSerializerRegistry::new)
                .serializer(resource.extension());
        if (reader != null)
        {
            var object = reader.readObject(resource, OBJECT_TYPE, OBJECT_INSTANCE);
            if (object != null)
            {
                return new SettingsObject(object);
            }
            else
            {
                problem("Unable to read settings object from: $", resource);
            }
        }
        return null;
    }
}
