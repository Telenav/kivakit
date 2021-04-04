# kivakit-data compression &nbsp;&nbsp;![](../../documentation/images/compress-52.png)

This module contains packages for compressing and decompressing data.

![](documentation/images/horizontal-line.png)

### Index

[**Summary**](#summary)  
[**Codecs**](#codecs)  
[**Character**](#character)  
[**Dependencies**](#dependencies)  
[**Class Diagrams**](#class-diagrams)  
[**Package Diagrams**](#package-diagrams)  
[**Javadoc**](#javadoc)

![](documentation/images/horizontal-line.png)

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module contains a definition of a compressor / decompressor, otherwise known as a *Codec*.   
The design of this codec mini-framework is centered around direct access to byte buffers (*ByteList*s)  
for optimal efficiency. For other purposes, existing stream-oriented compressors already exist,  
including the support for ZIP format in the JDK. This module also contains an implementation of  
a fast (table-driven) [*Huffman*](https://en.wikipedia.org/wiki/Huffman_coding) codec.

### Codecs <a name = "codecs"></a>

The *Codec* interface is generic to any compression type and any symbol type and looks like this:

    public interface Codec<Symbol>
    {
        boolean canEncode(Symbol symbol)
        ByteList encode(ByteList output, SymbolProducer<Symbol> producer)
        void decode(ByteList input, SymbolConsumer<Symbol> consumer)
    }

The *encode()* method performs huffman coding of the symbols produced by the given *SymbolProducer*,  
and writes the compressed representation to the given *ByteList*. The *decode()* method takes a *ByteList*,  
and decodes the compressed data, calling a *SymbolConsumer* with each decoded symbol.

### Character <a name = "character"></a>, String and Tag Codecs

The *CharacterCodec*, *StringCodec* and *StringListCodec* interfaces provide a definition of compression  
by character, string and string list, respectively. The same underlying *Huffman* coder is used to implement  
all three.

[//]: # (end-user-text)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp;  ![](documentation/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-data-compression</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp;![](documentation/images/diagram-48.png)

None

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp;![](documentation/images/box-40.png)

[*com.telenav.kivakit.data.compression*](documentation/diagrams/com.telenav.kivakit.data.compression.svg)  
[*com.telenav.kivakit.data.compression.codecs*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.svg)  
[*com.telenav.kivakit.data.compression.codecs.huffman*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.svg)  
[*
com.telenav.kivakit.data.compression.codecs.huffman.character*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.character.svg)  
[*
com.telenav.kivakit.data.compression.codecs.huffman.list*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.list.svg)  
[*
com.telenav.kivakit.data.compression.codecs.huffman.string*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.string.svg)  
[*
com.telenav.kivakit.data.compression.codecs.huffman.tree*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.tree.svg)  
[*com.telenav.kivakit.data.compression.project*](documentation/diagrams/com.telenav.kivakit.data.compression.project.svg)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp;![](documentation/images/books-40.png)

| Class | Documentation Sections |
|---|---|
| [*
CharacterCodec*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/CharacterCodec.html) |  |  
| [*
CharacterFrequencies*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/character/CharacterFrequencies.html) |  |  
| [*
Code*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/tree/Code.html) |  |  
| [*
Codec*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/Codec.html) |  |  
| [*
CodedSymbol*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/tree/CodedSymbol.html) |  |  
| [*
DataCompressionKryoTypes*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/project/DataCompressionKryoTypes.html) |  |  
| [*
DataCompressionProject*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/project/DataCompressionProject.html) |  |  
| [*
DataCompressionUnitTest*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/project/DataCompressionUnitTest.html) |  |  
| [*
FastHuffmanDecoder*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/FastHuffmanDecoder.html) |  |  
| [*
FastHuffmanDecoder.Table*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/FastHuffmanDecoder.Table.html) |  |  
| [*
FastHuffmanDecoder.Table.Entry*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/FastHuffmanDecoder.Table.Entry.html) |  |  
| [*
HuffmanCharacterCodec*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/character/HuffmanCharacterCodec.html) |  |  
| [*
HuffmanCharacterCodec.Converter*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/character/HuffmanCharacterCodec.Converter.html) |  |  
| [*
HuffmanCodec*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/HuffmanCodec.html) |  |  
| [*
HuffmanStringCodec*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/string/HuffmanStringCodec.html) |  |  
| [*
HuffmanStringCodec.Converter*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/string/HuffmanStringCodec.Converter.html) |  |  
| [*
HuffmanStringListCodec*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/list/HuffmanStringListCodec.html) |  |  
| [*
Leaf*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/tree/Leaf.html) |  |  
| [*
Node*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/tree/Node.html) |  |  
| [*
StringCodec*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/StringCodec.html) |  |  
| [*
StringFrequencies*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/string/StringFrequencies.html) |  |  
| [*
StringListCodec*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/StringListCodec.html) |  |  
| [*
SymbolConsumer*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/SymbolConsumer.html) |  |  
| [*
SymbolConsumer.Directive*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/SymbolConsumer.Directive.html) |  |  
| [*
SymbolProducer*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/SymbolProducer.html) |  |  
| [*
Symbols*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/tree/Symbols.html) |  |  
| [*
Tree*](http://telenav-tdk.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.utilities.compression/com/telenav/tdk/data/compression/codecs/huffman/tree/Tree.html) |  |  

[//]: # (start-user-text)


[//]: # (end-user-text)

<br/>

![](documentation/images/horizontal-line.png)

<sub>This documentation was generated by Lexakai on 2021.04.01</sub>    
<sub>UML diagrams courtesy of PlantUML (http://plantuml.com), distributed unmodified under Apache License, Version 2.0.</sub>

