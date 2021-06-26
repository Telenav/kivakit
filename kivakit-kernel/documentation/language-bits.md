# kivakit-core-kernel language.bits &nbsp; ![](../../../documentation/images/bits-40.png)

![](../documentation/images/horizontal-line.png)

### Package &nbsp; ![](../../../documentation/images/box-32.png)

*com.telenav.kivakit.core.kernel.language.bits*

### Index

[**Summary**](#summary)  
[**BitDiagram and BitField**](#bitdiagram-and-bitfield)  
[**Bit I/O**](#bit-io)  
[**Huffman Coding**](#huffman-coding)

![](../documentation/images/horizontal-line.png)

### Summary <a name="summary"></a>

This package includes functionality that helps to manipulate bits in a more object-oriented way, so  
you can get bits into and out of primitive values while avoiding shifting, masking and other operations.

### BitDiagram and BitField <a name="bitdiagram-and-bitfield"></a>

The _BitDiagram_ class allows you to create a textual map of the bits you want to access in a primitive  
type like a _long_ or an _int_. Each character in the diagram can be repeated to indicate related bits,  
then from the diagram you can create a _BitField_ object which has all the logic to shift and mask to  
get your bits out. Unused bits can be signaled with '?' for clarity and spaces are ignored, so you can  
break bits down into bytes for legibility:

    private static BitDiagram TRAFFIC_IDENTIFIER = new BitDiagram("????SSSS VVVVvvvv ZZZZLLLL ?RCDTTTT");

    // Bit fields from the bit diagram private static BitField CODING_SYSTEM = DIAGRAM.field('S'); 
    private static BitField MAJOR_VERSION = DIAGRAM.field('V'); 
    private static BitField MINOR_VERSION = DIAGRAM.field('v'); 
    private static BitField ROAD_TYPE = DIAGRAM.field('T'); 
    private static BitField LANE_COUNT = DIAGRAM.field('L'); 
    private static BitField RAMP = DIAGRAM.field('R'); 
    private static BitField CONNECTOR = DIAGRAM.field('C'); 
    private static BitField DIRECTION = DIAGRAM.field('D'); 
    private static BitField ZOOM_LEVEL = DIAGRAM.field('Z');

Once you have a _BitField_, you can extract or set bits from a primitive like this:

    // Extract the coding system bits as an integer and ramp as a boolean
    int codingSystem = CODING_SYSTEM.extractInt(identifier); 
    boolean isRamp = RAMP.extractBoolean(identifier);

    // Set the major and minor version fields 
    identifier = MAJOR_VERSION.set(identifier, 8); 
    identifier = MINOR_VERSION.set(identifier, 0);

### Bit I/O <a name="bit-io"></a>

The _BitArray_ class provides easy access to bits:

    BitArray bits = new BitArray(); 
    int index = 15; 
    bits.set(index, true); 
    boolean is = bits.get(index);

It also provides a streaming model for accessing _BitArrays_:

    // Read a boolean and a 5 bit integer 
    BitReader in = bits.reader();  
    boolean is = in.readBit();  
    int monkeys = in.readBits(BitCount._5);

    // Write a boolean and a 5 bit integer  
    BitWriter out = bits.writer(); 
    out.write(is); 
    out.write(monkeys, BitCount._5);

Finally, if you have an _InputStream_ or _OutputStream_, you can do bitwise I/O by wrapping the stream  
in _BitInputStream_ or _BitOutputStream_, respectively.

### Huffman Coding <a name="huffman-coding"></a>

KivaKit implements Huffman Coding ([https://en.wikipedia.org/wiki/Huffman_coding](https://en.wikipedia.org/wiki/Huffman_coding)) in
order  
to compress tag values in the Graph API (kivakit-graph). The basic API works like this:

    // Count the number of symbols (for example, words)
    CountMap<String> symbols = new CountMap<>(); 

        [...]

    // Create the codec to encode the symbols 
    Codec codec = Codec.forCountMap(counts);

    // Encode and decode 
    Code code = codec.encode(Symbol<String>("highway")); 
    Symbol<String> symbol = codec.decode(code);
    LOGGER.information("The frequency of $ is $", symbol, symbol.frequency);

<br/>

![](../documentation/images/horizontal-line.png)

