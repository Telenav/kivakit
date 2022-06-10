# kivakit-core-kernel language.primitives &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/bits-40.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.language.primitives*

### Index

[**Summary**](#summary)  
[**Arrays**](#arrays)  
[**Booleans**](#booleans)  
[**Doubles**](#doubles)  
[**Ints**](#ints)  
[**Longs**](#longs)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

This package contains various utility methods for working with Java primitive and array types.

### Arrays <a name="arrays"></a>

* *reverse(int[])*
* *reverse(long[])*

### Booleans <a name="booleans"></a>

* *boolean isFalse(String)*
* *boolean isTrue(String)*

### Doubles <a name="doubles"></a>

* *double INVALID*
* *double fastParse(String)*
* *String format(double)*
* *String format(double, int places)*
* *double inRange(double value, double minimum, double maximum)*
* *boolean isBetween(double value, double minimum, double maximum)*
* *double rounded(double)*

### Ints <a name="ints"></a>

* *int INVALID*
* *BitCount bitsToRepresent(int)*
* *int digits(int)*
* *int forHighLow(int high, int low)*
* *int high(int)*
* *int low(int)*
* *int inRange(int value, int minimum, int maximum)*
* *int isBetween(int value, int minimum, int maximum)*
* *boolean isPrime(int)*
* *int parse(String)*
* *int parse(String, int invalidValue)*
* *int parseNaturalNumber(String)*
* *int powerOfTen(int)*
* *int rounded(int)*
* *int signExtend(int, int bits)*
* *String toHex(int)*
* *String toHex(int, int minimumLength)*

### Longs <a name="longs"></a>

* *long INVALID*
* *BitCount bitsToRepresent(long)*
* *long forHighLow(int high, int low)*
* *int high(long)*
* *int low(long)*
* *long inRange(long value, long minimum, long maximum)*
* *long parse(String)*
* *long parse(String, long invalidValue)*
* *long parseHex(String)*
* *long parseNaturalNumber(String)*
* *boolean searchWords(long value, int bits, int searchFor)*

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
