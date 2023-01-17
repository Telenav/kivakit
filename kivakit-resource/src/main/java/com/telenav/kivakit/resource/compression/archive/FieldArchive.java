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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.language.reflection.property.IncludeProperty;
import com.telenav.kivakit.core.language.reflection.property.Property;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.core.version.VersionedObject;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.interfaces.io.Closeable;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceArchive;
import com.telenav.kivakit.resource.serialization.ObjectReader;
import com.telenav.kivakit.resource.serialization.ObjectWriter;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.language.reflection.Type.type;
import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static com.telenav.kivakit.core.string.CaseFormat.camelCaseToHyphenated;
import static com.telenav.kivakit.core.string.CaseFormat.hyphenatedToCamel;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.zipArchive;

/**
 * <p>
 * A field archive serializes data into zip file entries in a {@link ZipArchive}. The constructor for this class takes a
 * {@link Resource}, which is used to construct the zip archive. {@link FieldArchive} only serializes fields that are
 * explicitly labeled with the {@link ArchivedField} annotation.
 * </p>
 *
 * <p>
 * When the fields of an object are saved with {@link #saveFieldsOf(ObjectWriter, NamedObject, Version)}, the object's
 * name via {@link NamedObject#objectName()} is used as a prefix for each field that is saved. For example, if an object
 * named "paintbrush" has a field named "width", the entry would be saved as "paintbrush.width" in the archive.
 * </p>
 *
 * <p>
 * When an entry is read with {@link #load(ObjectReader, NamedObject, String)}, the object's name and the field name
 * will be used again to reconstruct the archive entry name. The methods
 * {@link #loadFieldOf(ObjectReader, NamedObject, String)} and {@link #loadFieldsOf(ObjectReader, NamedObject...)} both
 * load object(s) from the archive, but they also set the loaded value into the property with the given field name.
 * </p>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #file()}</li>
 *     <li>{@link #mode()}</li>
 *     <li>{@link #progressReporter()}</li>
 *     <li>{@link #version()}</li>
 *     <li>{@link #version(Version)}</li>
 *     <li>{@link #zip()}</li>
 * </ul>
 *
 * <p><b>Loading</b></p>
 *
 * <ul>
 *      <li>{@link #load(ObjectReader reader, NamedObject, String)} - Loads the object with the given object name and field name</li>
 *      <li>{@link #loadFieldOf(ObjectReader, NamedObject, String)} - Loads the named field into the given object</li>
 *      <li>{@link #loadFieldsOf(ObjectReader, NamedObject...)} - Loads all the fields of the given object</li>
 *      <li>{@link #loadVersion(ObjectReader)} - The version of data in this archive</li>
 * </ul>
 *
 * <p><b>Saving</b></p>
 *
 * <ul>
 *     <li>{@link #save(ObjectWriter, String, VersionedObject)} - Saves the versioned object in the named archive entry</li>
 *     <li>{@link #saveFieldsOf(ObjectWriter, NamedObject, Version)} - Saves all object fields using the given version</li>
 *     <li>{@link #saveVersion(ObjectWriter, Version)} - Sets the version of data in this archive</li>
 * </ul>
 *
 * <p>
 * When objects have been saved or loaded from the archive, {@link #close()} ensures that output (if any) is flushed and
 * streams are closed.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Resource
 * @see VersionedObject
 * @see Version
 * @see ZipArchive
 * @see Repeater
 */
@SuppressWarnings({ "unused" })
@UmlClassDiagram(diagram = DiagramResourceArchive.class)
@UmlRelation(label = "reads annotations", referent = ArchivedField.class)
@UmlRelation(label = "reads and writes", referent = NamedObject.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class FieldArchive extends BaseRepeater implements Closeable
{
    /**
     * A particular field of an object
     */
    @TypeQuality(stability = STABLE_EXTENSIBLE,
                 testing = UNTESTED,
                 documentation = DOCUMENTED)
    private class ObjectField
    {
        /** The object */
        private final Object object;

        /** The property */
        private final Property property;

        public ObjectField(@NotNull Object object, @NotNull Property property)
        {
            this.object = object;
            this.property = property;
        }

        public String name()
        {
            return property.name();
        }

        @Override
        public String toString()
        {
            return property.toString();
        }

        boolean saveObject(@NotNull ObjectWriter writer, @NotNull String entryName)
        {
            var outer = FieldArchive.this;
            var value = property.get(object);
            if (value != null)
            {
                zip().save(writer, entryName, new SerializableObject<>(value, outer.version));
                return true;
            }
            return false;
        }
    }

    /** The zip archive storing the fields */
    @FormatProperty
    @UmlAggregation(label = "writes to")
    private ZipArchive zip;

    /** The version of data in this archive */
    @FormatProperty
    private Version version;

    /** The zip file */
    private final File file;

    /** The progress reporter to update as the field archive is read */
    private final ProgressReporter reporter;

    /** The mode for accessing the zip file */
    private final ZipArchive.AccessMode mode;

    /**
     * @param file A field archive resource
     * @param mode The mode of access to this archive
     */
    public FieldArchive(@NotNull File file,
                        @NotNull ProgressReporter reporter,
                        @NotNull ZipArchive.AccessMode mode)
    {
        this.file = file;
        this.reporter = reporter;
        this.mode = mode;
    }

    public FieldArchive(@NotNull File file,
                        @NotNull ZipArchive.AccessMode mode)
    {
        this(file, nullProgressReporter(), mode);
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
    public <T> VersionedObject<T> load(@NotNull ObjectReader reader,
                                       @NotNull NamedObject object,
                                       @NotNull String fieldName)
    {
        return zip().loadVersionedObject(reader, entryName(object, fieldName));
    }

    /**
     * Loads the field named "[object-name].[field-name]" and sets the field of the object to that value.
     *
     * @param object The object whose field should be loaded
     * @param fieldName The field to load
     * @return The value of the field after attempting to load
     */
    @SuppressWarnings({ "unchecked" })
    public synchronized <T> T loadFieldOf(@NotNull ObjectReader reader,
                                          @NotNull NamedObject object,
                                          @NotNull String fieldName)
    {
        // Get the field
        Type<?> type = type(object);
        var field = type.field(hyphenatedToCamel(fieldName));
        ensure(field != null, "Cannot find field '$' in $", fieldName, type);

        // and to load object with object scoped name "[object-name].[field-name]"
        var versionedObject = load(reader, object, fieldName);
        Object value = null;
        if (versionedObject != null)
        {
            value = versionedObject.object();
        }

        // If we loaded the object
        if (value != null)
        {
            try
            {
                // set the field to that value
                var message = field.setter().set(object, value);
                if (message != null && message.status().failed())
                {
                    transmit(message);
                }

                // and return it.
                return (T) value;
            }
            catch (Exception e)
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
    @SuppressWarnings({ "UnusedReturnValue" })
    public synchronized boolean loadFieldsOf(@NotNull ObjectReader reader,
                                             @NotNull NamedObject... objects)
    {
        ensure(objects != null);
        ensure(objects.length > 0);

        var success = true;

        // Go through the objects,
        for (var object : objects)
        {
            // and for each archived field
            Type<?> type = type(object);
            for (var field : type.properties(new ArchivedFields()).sorted())
            {
                // if it is not lazy,
                if (!field.getter().annotation(ArchivedField.class).lazy())
                {
                    // then load the field into the object.
                    if (loadFieldOf(reader, object, field.name()) == null)
                    {
                        success = false;
                    }
                }
            }
        }

        return success;
    }

    /**
     * Returns the version of data in this archive
     */
    public Version loadVersion(@NotNull ObjectReader reader)
    {
        if (version == null)
        {
            var version = zip().loadVersionedObject(reader, "version");
            if (version != null)
            {
                this.version = (Version) version.object();
            }
        }
        return version;
    }

    public ZipArchive.AccessMode mode()
    {
        return mode;
    }

    public ProgressReporter progressReporter()
    {
        return reporter;
    }

    /**
     * Saves the given versioned object to the entry with the given name
     */
    public synchronized <T> void save(@NotNull ObjectWriter writer,
                                      @NotNull String fieldName,
                                      @NotNull VersionedObject<T> object)
    {
        zip().save(writer, camelCaseToHyphenated(fieldName), object);
    }

    /**
     * Saves the fields of the given object to this archive with the given version
     */
    @SuppressWarnings("ConstantConditions")
    public synchronized void saveFieldsOf(@NotNull ObjectWriter writer,
                                          @NotNull NamedObject object,
                                          @NotNull Version version)
    {
        ensure(object != null);

        this.version = version;

        for (var field : type(object).properties(new ArchivedFields()).sorted())
        {
            try
            {
                if (new ObjectField(object, field).saveObject(writer, entryName(object, field.name())))
                {
                    trace("Saved field $", field.name());
                }
            }
            catch (Exception e)
            {
                warning(e, "Unable to save field $", field.name());
            }
        }
    }

    /**
     * Saves the given archive version
     */
    public void saveVersion(@NotNull ObjectWriter writer,
                            @NotNull Version version)
    {
        save(writer, "version", new SerializableObject<>(version, version));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public Version version()
    {
        return version;
    }

    public void version(@NotNull Version version)
    {
        this.version = version;
    }

    /**
     * Returns the field archive's underlying zip archive for special operations on the archive
     */
    @IncludeProperty
    public ZipArchive zip()
    {
        if (zip == null)
        {
            zip = zipArchive(this, file, mode);
        }
        return zip;
    }

    private String entryName(@NotNull NamedObject object,
                             @NotNull String fieldName)
    {
        return object.objectName() + "." + camelCaseToHyphenated(fieldName);
    }
}
