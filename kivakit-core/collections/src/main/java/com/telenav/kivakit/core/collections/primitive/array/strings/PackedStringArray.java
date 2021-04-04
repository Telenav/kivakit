////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.strings;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.map.CacheMap;
import com.telenav.kivakit.core.collections.primitive.array.PrimitiveArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitByteArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitCharArray;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArray;
import com.telenav.kivakit.core.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.language.values.count.BitCount;
import com.telenav.kivakit.core.kernel.language.values.count.Estimate;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An store of strings that can be added to that is packed according to information content, using 8 or 16 bit storage
 * for ASCII and Unicode characters, respectively. Strings are added with {@link #add(String)}, which returns an
 * identifier that can be used to retrieve the string again with {@link #get(int)} or {@link #safeGet(int)}.
 * <p>
 * In addition to reducing space by storing ASCII strings as bytes, this store performs string pooling. Adding a string
 * that has been added in the past N (65,536 by default) additions will return the identifier of the previously added
 * string instead of adding a new string.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArray.class)
public class PackedStringArray extends PrimitiveArray
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    private static final int TYPE_MASK = (int) BitCount.bitCount(Type.UNICODE.ordinal()).mask();

    private static final int TYPE_SHIFT = 32 - 1 - BitCount.bitCount(Type.UNICODE.ordinal()).asInt();

    private enum Type
    {
        ASCII,
        UNICODE;

        public static Type forIdentifier(final int identifier)
        {
            switch (identifier)
            {
                case 0:
                    return ASCII;

                case 1:
                    return UNICODE;

                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    /**
     * All simple ASCII (7-bit character) strings laid out end-to-end (with no terminators)
     */
    private SplitByteArray asciiCharacters;

    /**
     * Insert index to the asciiCharacters array
     */
    private int asciiCharacterIndex = 1;

    /**
     * All character strings in this string array laid out end-to-end
     */
    private SplitCharArray unicodeCharacters;

    /**
     * Insert index to the characters array
     */
    private int characterIndex = 1;

    /**
     * The size of this array
     */
    private int size;

    /**
     * Maximum length of strings stored in this array
     */
    private int maximumStringLength = 64;

    /**
     * String pooling map used while loading to avoid duplicating frequently occurring strings. This effectively
     * compresses the input ala LZW.
     */
    @JavaVirtualMachine.KivaKitExcludeFromSizeOf
    private transient CacheMap<String, Integer> pool = new CacheMap<>(Maximum._65536);

    public PackedStringArray(final String objectName)
    {
        super(objectName);
    }

    protected PackedStringArray()
    {
    }

    /**
     * @param string The string to add
     * @return An identifier for the string
     */
    public int add(String string)
    {
        assert isInitialized();
        assert string != null;

        if (string.length() > maximumStringLength)
        {
            DEBUG.warning("'$' exceeds maximum string length of $", string, maximumStringLength);
        }
        string = AsciiArt.clip(string, maximumStringLength);

        // Look in pool for an already-stored index
        final var pooledIndex = pool.get(string);
        if (pooledIndex != null)
        {
            return pooledIndex;
        }

        final var type = type(string);
        final int arrayIndex;
        switch (type)
        {
            case ASCII:
                arrayIndex = asciiCharacterIndex;
                for (var i = 0; i < string.length(); i++)
                {
                    asciiCharacters.set(asciiCharacterIndex++, (byte) string.charAt(i));
                }
                asciiCharacters.set(asciiCharacterIndex++, (byte) 0);
                break;

            case UNICODE:
                arrayIndex = characterIndex;
                for (var i = 0; i < string.length(); i++)
                {
                    unicodeCharacters.set(characterIndex++, string.charAt(i));
                }
                unicodeCharacters.set(characterIndex++, (char) 0);
                break;

            default:
                throw new IllegalStateException();
        }
        size++;
        final var index = index(type, arrayIndex);
        pool.put(string, index);
        return index;
    }

    /**
     * @return The string for the given identifier (returned by add)
     */
    public String get(final int identifier)
    {
        final var string = safeGet(identifier);
        if (string == null)
        {
            throw new IllegalStateException();
        }
        return string;
    }

    public void maximumStringLength(final int maximumStringLength)
    {
        this.maximumStringLength = maximumStringLength;
    }

    @Override
    public Method onCompress(final Method method)
    {
        pool = null;

        asciiCharacters.compress(method);
        unicodeCharacters.compress(method);

        return Method.RESIZE;
    }

    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);
        asciiCharacters = kryo.readObject(input, SplitByteArray.class);
        unicodeCharacters = kryo.readObject(input, SplitCharArray.class);
        size = kryo.readObject(input, int.class);
    }

    /**
     * @return The string for the given identifier (returned by add), or null if the identifier was invalid
     */
    public String safeGet(final int identifier)
    {
        final var type = type(identifier);
        final var start = index(identifier);
        switch (type)
        {
            case ASCII:
                return string(asciiCharacters, start);

            case UNICODE:
                return string(unicodeCharacters, start);

            default:
                return null;
        }
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);
        kryo.writeObject(output, asciiCharacters);
        kryo.writeObject(output, unicodeCharacters);
        kryo.writeObject(output, size);
    }

    @Override
    protected void onInitialize()
    {
        super.onInitialize();
        asciiCharacters = new SplitByteArray(objectName() + ".ascii");
        asciiCharacters.initialSize(Estimate._65536).initialize();

        unicodeCharacters = new SplitCharArray(objectName() + ".unicode");
        unicodeCharacters.initialSize(1024).initialize();
    }

    private int index(final int index)
    {
        return index & ~(TYPE_MASK << TYPE_SHIFT);
    }

    private int index(final Type type, final int index)
    {
        return (type.ordinal() << TYPE_SHIFT) | index;
    }

    private String string(final SplitCharArray array, final int index)
    {
        final var builder = new StringBuilder();
        var i = index;
        while (true)
        {
            final var next = array.get(i++);
            if (next == 0)
            {
                break;
            }
            builder.append(next);
        }
        return builder.toString();
    }

    private String string(final SplitByteArray array, final int index)
    {
        final var builder = new StringBuilder();
        var i = index;
        while (true)
        {
            final var next = (char) array.get(i++);
            if (next == 0)
            {
                break;
            }
            builder.append(next);
        }
        return builder.toString();
    }

    private Type type(final int index)
    {
        return Type.forIdentifier(index >>> TYPE_SHIFT);
    }

    private Type type(final String value)
    {
        if (Strings.isAscii(value))
        {
            return Type.ASCII;
        }
        return Type.UNICODE;
    }
}
