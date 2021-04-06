////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.codecs;

import com.telenav.kivakit.data.compression.Codec;
import com.telenav.kivakit.data.compression.codecs.huffman.list.HuffmanStringListCodec;

/**
 * A codec that compresses lists of strings. The value null represents the end of a list.
 *
 * @author jonathanl (shibo)
 * @see Codec
 * @see HuffmanStringListCodec
 */
public interface StringListCodec extends StringCodec
{
}
