package

        com.telenav.kivakit.core.serialization.core;

import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.collections.map.ObjectMap;
import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.core.kernel.language.values.version.VersionedObject;

import java.util.List;
import java.util.Map;

/**
 * Unversioned primitive read and write methods. Prefer the automatically versioned {@link SerializationSession#read()}
 * and {@link SerializationSession#write(VersionedObject)} methods whenever possible.
 *
 * @author jonathanl (shibo)
 * @see SerializationSession
 */
public interface SerializationSessionReadWrite
{
    boolean readBoolean();

    byte readByte();

    char readChar();

    double readDouble();

    float readFloat();

    int readInt();

    <Element> ObjectList<Element> readList();

    <Element> ObjectList<Element> readList(final Class<Element> type);

    long readLong();

    <Key, Value> ObjectMap<Key, Value> readMap();

    <Type> Type readObject(final Class<Type> type);

    short readShort();

    String readString();

    Version readVersion();

    void writeBoolean(boolean value);

    void writeByte(byte value);

    void writeChar(char value);

    void writeDouble(double value);

    void writeFloat(float value);

    void writeInt(int value);

    <Element> void writeList(final List<Element> list);

    <Element> void writeList(final List<Element> list, final Class<Element> type);

    void writeLong(long value);

    <Key, Value> void writeMap(final Map<Key, Value> map);

    void writeObject(final Object object);

    void writeShort(short value);

    void writeString(String value);

    void writeVersion(final Version version);
}
