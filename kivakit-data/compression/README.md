# kivakit-utilities compression &nbsp;&nbsp;![](../../documentation/images/compress-52.png)

This module contains packages for compressing and decompressing data.

![](documentation/images/horizontal-line.png)

### Index

[**Dependencies**](#dependencies)  
[**Summary**](#summary)  
[**Codecs**](#codecs)  
[**Character**](#character)  
[**Class Diagrams**](#class-diagrams)  
[**Package Diagrams**](#package-diagrams)  
[**Javadoc**](#javadoc)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; ![](documentation/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId></artifactId>
        <version><!-- ${project-version} --> 0.9.0-SNAPSHOT <!-- end --></version>
    </dependency>
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

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; ![](documentation/images/diagram-48.png)

None

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; ![](documentation/images/box-40.png)

[*com.telenav.kivakit.data.compression*](documentation/diagrams/com.telenav.kivakit.data.compression.svg)  
[*com.telenav.kivakit.data.compression.codecs*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.svg)  
[*com.telenav.kivakit.data.compression.codecs.huffman*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.svg)  
[*com.telenav.kivakit.data.compression.codecs.huffman.character*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.character.svg)  
[*com.telenav.kivakit.data.compression.codecs.huffman.list*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.list.svg)  
[*com.telenav.kivakit.data.compression.codecs.huffman.string*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.string.svg)  
[*com.telenav.kivakit.data.compression.codecs.huffman.tree*](documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.tree.svg)  
[*com.telenav.kivakit.data.compression.project*](documentation/diagrams/com.telenav.kivakit.data.compression.project.svg)  

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; ![](documentation/images/books-40.png)

Javadoc coverage for this project is 65.4%.  
  
&nbsp; &nbsp;  ![](documentation/images/meter-70-12.png)



| Class | Documentation Sections |
|---|---|
| [*CharacterCodec*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/CharacterCodec.html) |  |  
| [*CharacterFrequencies*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/character/CharacterFrequencies.html) |  |  
| [*Code*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/Code.html) |  |  
| [*Codec*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/Codec.html) |  |  
| [*CodedSymbol*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/CodedSymbol.html) |  |  
| [*DataCompressionKryoTypes*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/project/DataCompressionKryoTypes.html) |  |  
| [*DataCompressionProject*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/project/DataCompressionProject.html) |  |  
| [*DataCompressionUnitTest*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/project/DataCompressionUnitTest.html) |  |  
| [*FastHuffmanDecoder*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/FastHuffmanDecoder.html) |  |  
| [*FastHuffmanDecoder.Table*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/FastHuffmanDecoder.Table.html) |  |  
| [*FastHuffmanDecoder.Table.Entry*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/FastHuffmanDecoder.Table.Entry.html) |  |  
| [*HuffmanCharacterCodec*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/character/HuffmanCharacterCodec.html) |  |  
| [*HuffmanCharacterCodec.Converter*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/character/HuffmanCharacterCodec.Converter.html) |  |  
| [*HuffmanCodec*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/HuffmanCodec.html) |  |  
| [*HuffmanStringCodec*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/string/HuffmanStringCodec.html) |  |  
| [*HuffmanStringCodec.Converter*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/string/HuffmanStringCodec.Converter.html) |  |  
| [*HuffmanStringListCodec*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/list/HuffmanStringListCodec.html) |  |  
| [*Leaf*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/Leaf.html) |  |  
| [*Node*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/Node.html) |  |  
| [*StringCodec*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/StringCodec.html) |  |  
| [*StringFrequencies*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/string/StringFrequencies.html) |  |  
| [*StringListCodec*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/StringListCodec.html) |  |  
| [*SymbolConsumer*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/SymbolConsumer.html) |  |  
| [*SymbolConsumer.Directive*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/SymbolConsumer.Directive.html) |  |  
| [*SymbolProducer*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/SymbolProducer.html) |  |  
| [*Symbols*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/Symbols.html) |  |  
| [*Tree*](https://telenav.github.io/kivakit/javadoc/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/Tree.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<br/>

![](documentation/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.09. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

