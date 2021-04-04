# kivakit-core-kernel language.strings &nbsp; ![](../../../documentation/images/string-64.png)

![](../documentation/images/horizontal-line.png)

### Package &nbsp; ![](../../../documentation/images/box-32.png)

*com.telenav.kivakit.core.kernel.language.strings*

### Index

[**Summary**](#summary)  
[**Utility Methods**](#utility-methods)  
[**Formatting**](#formatting)  
[**Conversion**](#conversion)

![](../documentation/images/horizontal-line.png)

### Summary <a name="summary"></a>

This package contains useful utility methods and classes for working with strings and   
converting objects into strings.

### Utility Methods <a name="utility-methods"></a>

The classes immediately in *kernel.language.strings* are a set of simple utility methods  
performing the following categories of operations:

* *Align* - String alignment
* *AsciiArt* - Bulleted lists, text boxes, banners, etc.
* *CaseFormat* - Conversion between different case patterns like lowercase, camelCase and hyphenated-case
* *Comparison* - String comparison including Levenschtein distance
* *Escape* - String escaping for XML, SQL, and Javascript
* *Indent* - String indentation
* *Join* - Joining of string lists into strings with a separator
* *Normalize* - String normalization
* *PathStrings* - Parsing of paths represented as strings (prefer the *language.path* objects when possible)
* *Plural* - Simple pluralization rules (English only)
* *Split* - Splitting of strings on a separator
* *Strings* - Low-level string utility methods
* *StringTo* - Various string conversion methods
* *Strip* - Trimming of leading and trailing strings
* *Wrap* - String wrapping

### Formatting <a name="formatting"></a>

Two useful classes are provided to make it easy to format strings. The *IndentingStringBuilder* class  
has generally the same functionality as *StringBuilder*, but adds an indentation level which can be  
increased or decreased, allowing hierarchical indentation of strings. The *ObjectFormatter* class is  
used to automatically format objects as strings by reflecting on their properties:

    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

Any field or method with no parameters can be annotated with @KivaFormatProperty and that value  
will be included in the formatted string. This is intended to be used for debugging and tracing  
purposes.

### Conversion <a name="conversion"></a>

The *AsString* interface looks like this:

    public interface AsString
    {
        default String asString(final StringFormat format)
        {
            switch (format.identifier())
            {
                case StringFormat._DEBUGGER:
                case StringFormat._LOG:
                    return new ObjectFormatter(this).toString();
    
                default:
                    return toString();
            }
        }

        default String asString()
        {
            return asString(StringFormat.TEXT);
        }
    }

The default implementation of *asString()* functions much like *toString()*, producing a string  
representation of the object in the TEXT format. By default, this will call *toString()* as  
seen in the code above. However, the *asString(StringFormat)* overload can produce strings for  
different purposes (which is a shortcoming of *toString()*). The *StringFormat* class is a simple  
identifier specifying a purpose. For example, an object might wish to provide one format for  
programmatic use, another for user display and yet another for display in a web browser.   
Several common formats are pre-defined (and new ones can always be defined):

    public class StringFormat
    {
        public static StringFormat DEBUGGER         = new StringFormat(DEBUGGER_IDENTIFIER);
        public static StringFormat FILESYSTEM       = new StringFormat(FILESYSTEM_IDENTIFIER);
        public static StringFormat HTML             = new StringFormat(HTML_IDENTIFIER);
        public static StringFormat PROGRAMMATIC     = new StringFormat(PROGRAMMATIC_IDENTIFIER);
        public static StringFormat TEXT             = new StringFormat(TEXT_IDENTIFIER);
        public static StringFormat USER_MULTILINE   = new StringFormat(USER_MULTILINE_IDENTIFIER);
        public static StringFormat USER_SINGLE_LINE = new StringFormat(USER_SINGLE_LINE_IDENTIFIER);
        public static StringFormat USER_LABEL       = new StringFormat(USER_LABEL_IDENTIFIER);
        public static StringFormat LOG              = new StringFormat(LOG_IDENTIFIER);

            [...]
    }

<br/>

![](../documentation/images/horizontal-line.png)
