# kivakit-core-kernel language.patterns &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/pattern-48.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.language.patterns*

### Index

[**Summary**](#summary)  
[**Pattern Composition**](#pattern-composition)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

This package provides classes that facilitate breaking down complex regular expressions into components  
that are easier to understand and maintain.

### Pattern Composition <a name="pattern-composition"></a>

The *Pattern* class has static methods and constants that comprise a set of reusable matchers, and a fluent  
API for producing *Pattern*s. In the following example, a pre-existing *Pattern* that matches floating-point  
numbers is used to construct a regular expression that matches geographic rectangles of the form:

    [bottomLeftLatitude],[bottomLeftLongitude]:[topRightLatitude],[topRightLongitude]

This regular expression is combined with a pattern matching commas and one matching colons to produce  
the composite pattern:

    public static Group<Rectangle> parser( Listener listener)
    {
        return Pattern(Pattern.FLOATING_POINT_NUMBER
                        .then(Pattern.COMMA)
                        .then(Pattern.FLOATING_POINT_NUMBER)
                        .then(Pattern.COLON)
                        .then(Pattern.FLOATING_POINT_NUMBER)
                        .then(Pattern.COMMA)
                        .then(Pattern.FLOATING_POINT_NUMBER))
                .group(new Rectangle.Converter(listener));
    }

Notice that this approach to regular expression composition is fully object-oriented and that the *Group*  
pattern produces a *Rectangle* object using the given rectangle converter on the captured group.

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
