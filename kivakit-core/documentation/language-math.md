# kivakit-core-kernel language.math &nbsp; ![](../../../documentation/images/math-40.png)

![](../documentation/images/horizontal-line.png)

### Package &nbsp; ![](../../../documentation/images/box-32.png)

*com.telenav.kivakit.core.kernel.language.math*

![](../documentation/images/horizontal-line.png)

### Summary

This package contains broadly applicable math utilities.

### Constants

The constant *LARGEST_INT* is the largest prime int value (a Mersenne prime).

### Prime Allocation Sizes

The *Primes* class provides the method *allocationSize(long)* which provides prime values    
for the allocation of data structures like hash maps. Prime allocation size help to prevent  
hash collisions with certain hashing algorithms. See [Hash Table](https://en.wikipedia.org/wiki/Hash_table) on Wikipedia.

### "Prime Powers of Two"

The *powerOfTwoGreaterThan(int)* provides the *next prime* greater than next power of two  
that is greater than the given argument.

<br/>

![](../documentation/images/horizontal-line.png)
