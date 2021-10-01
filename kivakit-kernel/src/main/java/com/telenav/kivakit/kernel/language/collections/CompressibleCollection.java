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

package com.telenav.kivakit.kernel.language.collections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telenav.kivakit.kernel.interfaces.messaging.Receiver;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramExampleBaseList;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Interface for collection objects that can be compressed. The {@link #compress(Method)} method attempts to compress
 * the object using the given {@link Method}, returning the actual method used. The {@link #compressionMethod()} method
 * returns the type of compression that has been used to compress an object. If this value is not {@link Method#NONE}
 * then the object is compressed and {@link #isCompressed()} will return true.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public interface CompressibleCollection
{
    /**
     * Compress all {@link CompressibleCollection} fields recursively on the given object
     *
     * @param root The object to compress
     * @return Size of the object after compressing
     */
    static Bytes compressReachableObjects
    (
            final Listener listener,
            final Object root,
            final Method method,
            final Receiver receiver
    )
    {
        return JavaVirtualMachine.local().traceSizeChange(listener, "compress", root, Bytes.megabytes(1), () ->
        {
            // Go through all reachable sub-objects
            final var values = Type.of(root).reachableObjectsImplementing(root, CompressibleCollection.class);
            final Map<Object, Boolean> compressed = new IdentityHashMap<>();
            for (final var value : values)
            {
                // and if we haven't already compressed it and it can be compressed,
                if (compressed.get(value) != Boolean.TRUE)
                {
                    // compress it using the given method,
                    final var compressible = (CompressibleCollection) value;
                    final var methodUsed = compressible.compress(method);
                    assert methodUsed != null;
                    if (methodUsed != Method.NONE)
                    {
                        // and if it did compress, notify the client,
                        receiver.receive(new CompressionEvent(compressible, methodUsed));
                    }

                    // then mark it as compressed so we don't try it again.
                    compressed.put(value, Boolean.TRUE);
                }
            }
        });
    }

    enum Method
    {
        /**
         * No compression method can be applied to the collection
         */
        NONE,

        /**
         * Resize the collection to its minimal size but retain its mutability. For example, an map might reduce its
         * capacity by resizing to an optimal number of buckets or slots, while an array might trim off excess elements
         * that aren't occupied.
         */
        RESIZE,

        /**
         * Reduce this collection's memory footprint by changing its internal data representation to something that's
         * less space consuming but immutable. For example, a mutable hash map has to have free buckets to store new
         * data in, but a frozen map can use exactly the space required and no more by sorting its entries by key in an
         * array and finding keys by binary search.
         */
        FREEZE,

        /**
         * Return value from {@link #compress(Method)} when a collection performs more than one kind of compression,
         * like resizing arrays and freezing maps.
         */
        MIXED
    }

    /**
     * A compression event when compressing objects with {@link #compressReachableObjects(Listener, Object, Method,
     * Receiver)}
     */
    class CompressionEvent implements Transmittable
    {
        /** The object that was compressed */
        final CompressibleCollection object;

        /** The method of compression */
        Method method;

        public CompressionEvent(final CompressibleCollection object, final Method method)
        {
            this.object = object;
            this.method = method;
        }

        public void method(final Method method)
        {
            this.method = method;
        }

        public CompressibleCollection object()
        {
            return object;
        }
    }

    /**
     * Compress this collection using the given method, if possible. The collection can decide to use a different
     * method, in which case it returns the method and {@link #compressionMethod()} will return it in the future.
     *
     * @return The method used to compress the collection
     */
    Method compress(Method method);

    /**
     * @return Any compression method that has been applied to this collection. For subclasses of PrimitiveCollection,
     * this will normally be the value that was returned from {@link #compress(Method)}.
     */
    Method compressionMethod();

    /**
     * @return True if this collection has been compressed by some method
     */
    @JsonIgnore
    default boolean isCompressed()
    {
        return compressionMethod() != null && compressionMethod() != Method.NONE;
    }
}
