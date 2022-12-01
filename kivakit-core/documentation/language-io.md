# kivakit-core-kernel language.io &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/convert-32.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.language.io*

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary

This package provides helpful utilities for performing input and output operations.

### Input

* *LookAheadReader* - A Java *Reader* that provides a one character lookahead
* *LimitedInput* - An *InputStream* wrapper that signals end-of-input after a maximum  
  number of bytes
* *StringInput* - An *InputStream* that reads from a string
* *StringReader* - A Java *Reader* that reads from a string

### Output

* *ByteSizedOutput* - An *OutputStream* that implements *ByteSized*, providing the  
  number of bytes read

### Progress Reporting

The *ProgressiveInput* and *ProgressiveOutput* make calls to a *ProgressReporter* for  
each byte that is read or written

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
