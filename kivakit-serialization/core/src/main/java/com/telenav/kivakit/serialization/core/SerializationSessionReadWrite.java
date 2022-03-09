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

package com.telenav.kivakit.serialization.core;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.core.version.VersionedObject;

import java.util.List;
import java.util.Map;

/**
 * Un-versioned primitive read and write methods. Prefer the automatically versioned {@link SerializationSession#read()}
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

    <Element> ObjectList<Element> readList(Class<Element> type);

    long readLong();

    <Key, Value> ObjectMap<Key, Value> readMap();

    <Type> Type readObject(Class<Type> type);

    short readShort();

    String readString();

    Version readVersion();

    void writeBoolean(boolean value);

    void writeByte(byte value);

    void writeChar(char value);

    void writeDouble(double value);

    void writeFloat(float value);

    void writeInt(int value);

    <Element> void writeList(List<Element> list);

    <Element> void writeList(List<Element> list, Class<Element> type);

    void writeLong(long value);

    <Key, Value> void writeMap(Map<Key, Value> map);

    void writeObject(Object object);

    void writeShort(short value);

    void writeString(String value);

    void writeVersion(Version version);
}
