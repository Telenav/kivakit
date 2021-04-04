# kivakit-core-kernel - language.locales &nbsp; ![](../../../documentation/images/world-40.png)

![](../documentation/images/horizontal-line.png)

### Package &nbsp; ![](../../../documentation/images/box-32.png)

*com.telenav.kivakit.core.kernel.language.locales*

![](../documentation/images/horizontal-line.png)

### Summary

This package contains helpful classes for localizing applications based on language and country.

### ISO Codes

The International Standards Organization (ISO) provides convenient coding schemes for languages  
and countries. The classes *LanguageIsoCode* and *CountryIsoCode* provide this information in a  
convenient and typesafe fashion.

### Locales

A *Locale* is a *LanguageIsoCode* with an optional *CountryIsoCode*. A KivaKit *Locale* can be turned into  
a *java.util.Locale* with *asJavaLocale()*. A locale can also be converted to a *StringPath* of the form   
"locales/[language]/[country]?" with *path()*. This path is used to load localized resources in relevant  
classes like *Application*, *Package* and *PropertyMap*.

<br/>

![](../documentation/images/horizontal-line.png)
