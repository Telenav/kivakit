# KivaKit - Maven Artifact Conventions &nbsp; <img src="https://telenav.github.io/telenav-assets/images/iconsdependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/iconsdependencies-32-2x.png 2x"/>

<img src="https://telenav.github.io/telenav-assets/images/iconshorizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/png/separators/horizontal-line-512-2x.png 2x"/>

The *README.md* markdown file for each KivaKit project includes the Maven dependency, so it can 
be easily cut and pasted into the *pom.xml* file of a project using KivaKit.

### Maven Dependencies

Maven dependencies on KivaKit modules have the following general format:

- The group identifier is *com.telenav.kivakit* in all cases
- Artifact identifiers start with *kivakit* and are separated by dashes

For example, this is the dependency for *kivakit-core-kernel*:

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-kernel</artifactId>
        <version>${kivakit.version}</version>
    </dependency>

### Module Names

Module names use the fully qualified group and artifact ids with dots substituted for any hyphens. 
For example, this *module-info.java* statement imports the *kivakit-core-kernel* project:

    requires com.telenav.kivakit.core.kernel;

<br/> 

<img src="https://telenav.github.io/telenav-assets/images/iconshorizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/png/separators/horizontal-line-512-2x.png 2x"/>
