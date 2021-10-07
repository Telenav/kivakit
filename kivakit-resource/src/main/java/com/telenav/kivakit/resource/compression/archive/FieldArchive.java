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

package com.telenav.kivakit.resource.compression.archive;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.kernel.interfaces.io.Closeable;
import com.telenav.kivakit.kernel.interfaces.naming.NamedObject;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.reflection.property.NamingConvention;
import com.telenav.kivakit.kernel.language.reflection.property.Property;
import com.telenav.kivakit.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.values.version.VersionedObject;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceArchive;
import com.telenav.kivakit.serialization.core.SerializationSession;
import com.telenav.kivakit.serialization.core.SerializationSessionFactory;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A field archive serializes data into zip file entries in a {@link ZipArchive}. The constructor for this class takes a
 * {@link Resource}, which is used to construct the zip archive, and a {@link SerializationSessionFactory}, which is
 * used to create {@link SerializationSession}s to save and load fields to and from the archive. {@link FieldArchive}
 * only serializes fields that are explicitly labeled with the {@link KivaKitArchivedField} annotation.
 * <p>
 * When the fields of an object are saved with {@link #saveFieldsOf(NamedObject, Version)}, the object's name via {@link
 * NamedObject#objectName()} is used as a prefix for each field that is saved. For example, if an object named
 * "paintbrush" has a field named "width", the entry would be saved as "paintbrush.width" in the archive.
 * <p>
 * When an entry is read with {@link #load(NamedObject, String)}, the object's name and the field name will be used
 * again to reconstruct the archive entry name. The methods {@link #loadFieldOf(NamedObject, String)} and {@link
 * #loadFieldsOf(NamedObject...)} both load object(s) from the archive, but they also set the loaded value into the
 * property with the given field name.
 * <p>
 * <b>Saving</b>
 * <ul>
 *     <li>{@link #save(String, VersionedObject)} - Saves the versioned object in the named archive entry</li>
 *     <li>{@link #saveFieldsOf(NamedObject, Version)} - Saves all object fields using the given version</li>
 *     <li>{@link #version(Version)} - Sets the version of data in this archive</li>
 * </ul>
 * <p>
 * <b>Loading</b>
 * <ul>
 *      <li>{@link #load(NamedObject, String)} - Loads the object with the given object name and field name</li>
 *      <li>{@link #loadFieldOf(NamedObject, String)} - Loads the named field into the given object</li>
 *      <li>{@link #loadFieldsOf(NamedObject...)} - Loads all the fields of the given object</li>
 *      <li>{@link #version()} - The version of data in this archive</li>
 * </ul>
 * <p>
 * When objects have been saved or loaded from the archive, {@link #close()} ensures that output (if any) is flushed and
 * streams are closed.
 *
 * @author jonathanl (shibo)
 * @see SerializationSession
 * @see Resource
 * @see VersionedObject
 * @see Version
 * @see ZipArchive
 * @see Repeater
 */
@UmlClassDiagram(diagram = DiagramResourceArchive.class)
@UmlRelation(label = "reads annotations", referent = KivaKitArchivedField.class)
@UmlRelation(label = "reads and writes", referent = NamedObject.class)
@LexakaiJavadoc(complete = true)
public class FieldArchive extends BaseRepeater implements Closeable
{
    /**
     * A particular field of an object
     */
    @LexakaiJavadoc(complete = true)
    private class ObjectField
    {
        private final Object object;

        private final Property field;

        public ObjectField(final Object object, final Property field)
        {
            this.object = object;
            this.field = field;
        }

        public String name()
        {
            return field.name();
        }

        @Override
        public String toString()
        {
            return field.toString();
        }

        boolean saveObject(final SerializationSession session, final String entryName)
        {
            final var outer = FieldArchive.this;
            final var value = field.get(object);
            if (value != null)
            {
                zip().save(session, entryName, new VersionedObject<>(outer.version, value));
                return true;
            }
            return false;
        }
    }

    /** The zip archive storing the fields */
    @KivaKitIncludeProperty
    @UmlAggregation(label = "writes to")
    private ZipArchive zip;

    /** The version of data in this archive */
    @KivaKitIncludeProperty
    private Version version;

    /** The zip file */
    private final File file;

    /** The progress reporter to update as the field archive is read */
    private final ProgressReporter reporter;

    /** The mode for accessing the zip file */
    private final ZipArchive.Mode mode;

    /** The session for this field archive */
    private final Lazy<SerializationSession> session;

    /**
     * @param file A field archive resource
     * @param mode The mode of access to this archive
     */
    public FieldArchive(final File file,
                        final SerializationSessionFactory sessionFactory,
                        final ProgressReporter reporter,
                        final ZipArchive.Mode mode)
    {
        this.file = file;
        this.reporter = reporter;
        this.mode = mode;
        session = Lazy.of(() -> sessionFactory.session(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    {
        if (zip != null)
        {
            zip.close();
        }
    }

    public File file()
    {
        return file;
    }

    /**
     * Loads a versioned object from the zip entry named "[object-name].[field-name]"
     */
    public <T> VersionedObject<T> load(final NamedObject object, final String fieldName)
    {
        return zip().load(session(), entryName(object, fieldName));
    }

    /**
     * Loads the field named "[object-name].[field-name]" and sets the field of the object to that value.
     *
     * @param object The object whose field should be loaded
     * @param fieldName The field to load
     * @return The value of the field after attempting to load
     */
    @SuppressWarnings({ "ConstantConditions", "unchecked" })
    public synchronized <T> T loadFieldOf(final NamedObject object, final String fieldName)
    {
        // Get the field
        final Type<?> type = Type.of(object);
        final var field = type.field(CaseFormat.hyphenatedToCamel(fieldName));
        ensure(field != null, "Cannot find field '$' in $", fieldName, type);

        // and to load object with object scoped name "[object-name].[field-name]"
        final var versionedObject = load(object, fieldName);
        Object value = null;
        if (versionedObject != null)
        {
            value = versionedObject.get();
        }

        // If we loaded the object
        if (value != null)
        {
            try
            {
                // set the field to that value
                final var message = field.setter().set(object, value);
                if (message.status().failed())
                {
                    transmit(message);
                }

                // and return it.
                return (T) value;
            }
            catch (final Exception e)
            {
                throw new RuntimeException("Unable to set field " + entryName(object, fieldName), e);
            }
        }

        return null;
    }

    /**
     * Loads the fields of the given object(s) from this archive
     *
     * @param objects The object whose fields should be loaded
     * @return True if all fields were loaded
     */
    @SuppressWarnings({ "UnusedReturnValue", "ConstantConditions" })
    public synchronized boolean loadFieldsOf(final NamedObject... objects)
    {
        ensure(objects != null);
        ensure(objects.length > 0);

        var success = true;

        // Go through the objects,
        for (final var object : objects)
        {
            // and for each archived field
            final Type<?> type = Type.of(object);
            for (final var field : type.properties(new ArchivedFields(NamingConvention.KIVAKIT)).sorted())
            {
                // if it is not lazy,
                if (!field.getter().annotation(KivaKitArchivedField.class).lazy())
                {
                    // then load the field into the object.
                    if (loadFieldOf(object, field.name()) == null)
                    {
                        success = false;
                    }
                }
            }
        }

        return success;
    }

    public ZipArchive.Mode mode()
    {
        return mode;
    }

    public ProgressReporter reporter()
    {
        return reporter;
    }

    /**
     * Saves the given versioned object to the entry with the given name
     */
    public synchronized <T> void save(final String fieldName, final VersionedObject<T> object)
    {
        try (final var session = session())
        {
            zip().save(session, CaseFormat.camelCaseToHyphenated(fieldName), object);
        }
    }

    /**
     * Saves the fields of the given object to this archive with the given version
     */
    @SuppressWarnings("ConstantConditions")
    public synchronized void saveFieldsOf(final NamedObject object, final Version version)
    {
        ensure(object != null);

        this.version = version;

        try (final var session = session())
        {
            for (final var field : Type.of(object).properties(new ArchivedFields(NamingConvention.KIVAKIT)).sorted())
            {
                try
                {
                    if (new ObjectField(object, field).saveObject(session, entryName(object, field.name())))
                    {
                        trace("Saved field $", field.name());
                    }
                }
                catch (final Exception e)
                {
                    warning(e, "Unable to save field $", field.name());
                }
            }
        }
    }

    public SerializationSession session()
    {
        return session.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * Saves the given archive version
     */
    public void version(final Version version)
    {
        save("version", new VersionedObject<>(version));
    }

    /**
     * @return The version of data in this archive
     */
    @KivaKitIncludeProperty
    public Version version()
    {
        if (version == null)
        {
            final var version = zip().load(session(), "version");
            if (version != null)
            {
                this.version = (Version) version.get();
            }
        }
        return version;
    }

    /**
     * @return The field archive's underlying zip archive for special operations on the archive
     */
    @KivaKitIncludeProperty
    public ZipArchive zip()
    {
        if (zip == null)
        {
            zip = ZipArchive.open(this, file, reporter, mode);
        }
        return zip;
    }

    private String entryName(final NamedObject object, final String fieldName)
    {
        return object.objectName() + "." + CaseFormat.camelCaseToHyphenated(fieldName);
    }
}
