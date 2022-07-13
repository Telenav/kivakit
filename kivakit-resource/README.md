[//]: # (start-user-text)

<a href="https://www.kivakit.org">
<img src="https://telenav.github.io/telenav-assets/images/icons/web-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/web-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://twitter.com/openkivakit">
<img src="https://telenav.github.io/telenav-assets/images/logos/twitter/twitter-32.png" srcset="https://telenav.github.io/telenav-assets/images/logos/twitter/twitter-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://kivakit.zulipchat.com">
<img src="https://telenav.github.io/telenav-assets/images/logos/zulip/zulip-32.png" srcset="https://telenav.github.io/telenav-assets/images/logos/zulip/zulip-32-2x.png 2x"/>
</a>

[//]: # (end-user-text)

# kivakit-resource &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/water-64.png" srcset="https://telenav.github.io/telenav-assets/images/icons/water-64-2x.png 2x"/>

This module contains abstractions for accessing the filesystem and arbitrary resources.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Readable Resources**](#readable-resources)  
[**Kinds of Resources**](#kinds-of-resources)  
[**Writable Resources**](#writable-resources)  
[**Files**](#files)  
[**Launching Jar Resources**](#launching-jar-resources)  
[**Other Resources**](#other-resources)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-resource</artifactId>
        <version>1.6.1</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module pulls together everything that can stream together under a single abstraction,
providing an easy way to work with different kinds of resources without being tied to any
of the details.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Readable Resources <a name="readable-resources"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/wand-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/wand-32-2x.png 2x"/>

Different resource classes are constructed in different ways, but each implements the *Resource*
interface. The most important methods are shown here in a simplified form:

    public interface Resource extends ... 
    { 
        void copyTo( WritableResource destination); 
        boolean exists();
        boolean isReadable();
        Time lastModified();
        InputStream openForReading();
        ResourcePath path();
        ResourceReader reader();
        Bytes size();
    }

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Kinds of Resources <a name="kinds-of-resources"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

The class *StringResource* is about as simple as they come, but it can be read just like it was a
*File* or any other resource:

    Resource resource = new StringResource("this is a test");
    String [contents = resource.reader().string();

In the same way, a *ZipArchive* contains *ZipEntry* resources, so they can be read in a similar
way through *reader().string()*.

    ZipArchive zip = new ZipArchive(new File("kivakit.zip"));
    ZipEntry entry = zip.entry("test.txt");
    String contents = entry.reader().string();

This method can accept a *ZipEntry*, a *StringResource*, a *File,* an *HttpGetResource* or any
other resource and it works the same:

    public String contents(Resource resource)
    { 
        return resource.reader().string(); 
    }

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Writable Resources <a name="writable-resources"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/pencil-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/pencil-32-2x.png 2x"/>

The *WritableResource* interface extends the *Resource* interface to add output capabilities:

    public interface WritableResource extends Resource 
    { 
        boolean isWritable();
        OutputStream openForWriting();
        ResourceWriter writer();
    }

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Files <a name="files"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/folder-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/folder-32-2x.png 2x"/>

The *File* object is a *WritableResource* that adds a number of file-specific methods, including:

    public class File implements WritableResource, FileSystemObject, ... 
    { 
        boolean chmod( PosixFilePermission... permissions);
        boolean delete();
        Folder parent();
        void renameTo(File destination);
    }

Besides being abstracted readable and writable resources, files are also special because they are
integrated with KivaKit FileSystem API. This API uses JavaServices to allow *FileSystemService*
implementations to be plugged in through a service provider interface (SPI).

When you construct a *File*, the *FileSystemServiceRegistry* asks each *FileSystemService* provider
it found through the Java *ServiceLoader* if it can accept the path you used when constructing the
object. If a provider accepts the file, it will then be installed into the *File* object, to provide
all the required filesystem features.

For example, if you construct a *File* like this:

    File file = new File("/home/jonathanl/kivakit.txt");

The service registry will find that the *LocalFileSystemService* accepts this path, and you will be
accessing a local file.

If you construct a *File* like this:

    File file = new File("hdfs://home/jonathanl/kivakit.txt");

You will be accessing a file on HDFS through the *HdfsFileSystemService* provider.

What makes all this so powerful is that you can accept a *File* object as a parameter and not be
tied to any particular filesystem, and you can accept a *Resource* as a parameter and have an even
wider scope of abstraction than that. It makes sense to get the most abstraction that you can. Use
*Resource* if it's sufficient and *File* if you need file-specific functionality. Using the standard JDK
objects like *java.io.File* will tie the code to a particular resource implementation, making it
less flexible.

| File System | Provider |
|---|---|
| Local Disk | *LocalFileSystemService* |
| HDFS | *HdfsFileSystemService* |
| S3 | *S3FileSystemService* |

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Launching Jar Resources <a name = "launching-jar-resources"></a>

The *JarLauncher* class can be used to launch executable JAR resources from a local or remote
location. Basic usage looks like this:

    var jar = new HttpNetworkLocation(...);

    var process = listenTo(new JarLauncher())
        .source(jar)
        .arguments("-verbose=true")
        .processType(CHILD)
        .redirectTo(FILE)
        .run();

    Processes.waitFor(process);

Output in this case is redirected to a file in the ~/.kivakit/[kivakit-version]/temporary/launcher folder.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Other Resources <a name="other-resources"></a>

A wide variety of other resources are available and new ones are easy to write. A few examples:

*PackageResource* - Accesses resources in a package, as designated by a class in the package

    PackageResource.packageResource(MyClass.class, "images/duke.png");

<br/>

*HttpGetResource* - An HTTP GET REST resource

    new HttpGetResource(location, constraints);

<br/>

*FtpResource* - An FTP resource

    new FtpResource(location, constraints);

<br/>

*InputStreamResource* - Wraps any JDK InputStream as a resource

    new InputStreamResource(in);

<br/>

*OutputStreamResource* - Wraps any JDK OutputStream as a resource

    new OutputStreamResource(out);

<br/>

*StringResource* - A readable resource from a String

    new StringResource("this is a resource")

[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

[*File System - File System Services*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/diagram-file-system-service.svg)  
[*File System - Files*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/diagram-file-system-file.svg)  
[*File System - Folders*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/diagram-file-system-folder.svg)  
[*Resources*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/diagram-resource.svg)  
[*Resources - Archives*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/diagram-resource-archive.svg)  
[*Resources - Built-In Resource Types*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/diagram-resource-type.svg)  
[*Resources - Compression*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/diagram-resource-compression.svg)  
[*Resources - Jar Launcher*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/diagram-jar-launcher.svg)  
[*Resources - Paths*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/diagram-resource-path.svg)  
[*Resources - Services*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/diagram-resource-service.svg)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-24-2x.png 2x"/>

[*com.telenav.kivakit.filesystem*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.filesystem.svg)  
[*com.telenav.kivakit.filesystem.loader*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.filesystem.loader.svg)  
[*com.telenav.kivakit.filesystem.local*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.filesystem.local.svg)  
[*com.telenav.kivakit.filesystem.spi*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.filesystem.spi.svg)  
[*com.telenav.kivakit.launcher*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.launcher.svg)  
[*com.telenav.kivakit.properties*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.properties.svg)  
[*com.telenav.kivakit.resource*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.resource.svg)  
[*com.telenav.kivakit.resource.compression*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.resource.compression.svg)  
[*com.telenav.kivakit.resource.compression.archive*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.resource.compression.archive.svg)  
[*com.telenav.kivakit.resource.compression.codecs*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.resource.compression.codecs.svg)  
[*com.telenav.kivakit.resource.lexakai*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.resource.lexakai.svg)  
[*com.telenav.kivakit.resource.packages*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.resource.packages.svg)  
[*com.telenav.kivakit.resource.reading*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.resource.reading.svg)  
[*com.telenav.kivakit.resource.resources*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.resource.resources.svg)  
[*com.telenav.kivakit.resource.serialization*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.resource.serialization.svg)  
[*com.telenav.kivakit.resource.spi*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.resource.spi.svg)  
[*com.telenav.kivakit.resource.writing*](https://www.kivakit.org/1.6.1/lexakai/kivakit/kivakit-resource/documentation/diagrams/com.telenav.kivakit.resource.writing.svg)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

Javadoc coverage for this project is 86.8%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-90-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-90-96-2x.png 2x"/>


The following significant classes are undocumented:  

- com.telenav.kivakit.resource  
- com.telenav.kivakit.resource.serialization

| Class | Documentation Sections |
|---|---|
| [*ArchivedFields*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////////////.html) |  |  
| [*BaseReadableResource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////.html) |  |  
| [*BaseResourceList*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////.html) |  |  
| [*BaseWritableResource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////.html) |  |  
| [*Codec*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////.html) |  |  
| [*Compressor*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////.html) |  |  
| [*CopyMode*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////.html) |  |  
| [*Decompressor*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////.html) |  |  
| [*Deletable*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////.html) |  |  
| [*DiagramFileSystem*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////////////.html) |  |  
| [*DiagramFileSystemFile*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////////////////.html) |  |  
| [*DiagramFileSystemFolder*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////////////////.html) |  |  
| [*DiagramFileSystemService*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////////.html) |  |  
| [*DiagramJarLauncher*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////.html) |  |  
| [*DiagramResource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////////.html) |  |  
| [*DiagramResourceArchive*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////////.html) |  |  
| [*DiagramResourceCompression*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////////////.html) |  |  
| [*DiagramResourcePath*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////////////.html) |  |  
| [*DiagramResourceService*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////////.html) |  |  
| [*DiagramResourceType*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////////////.html) |  |  
| [*Disk*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////.html) |  |  
| [*DiskService*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////.html) |  |  
| [*Extension*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////.html) | Matching |  
| | Checks |  
| | Properties |  
| | Functional Methods |  
| [*FieldArchive*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////////.html) |  |  
| [*FieldArchive.ObjectField*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////////////////////.html) |  |  
| [*File*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////.html) | Conversion Methods |  
| | Path Methods |  
| | Checks |  
| | Operations |  
| | Functional Methods |  
| [*File.Converter*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////.html) |  |  
| [*File.Resolver*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////.html) |  |  
| [*FileCache*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////.html) |  |  
| [*FileList*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////.html) |  |  
| [*FileList.Converter*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////.html) |  |  
| [*FileName*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////.html) | Matching |  
| | Functional |  
| | Checks |  
| [*FilePath*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////.html) | Parsing |  
| | Factories |  
| [*FilePath.Converter*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////.html) |  |  
| [*FileService*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////.html) |  |  
| [*FileSystemObject*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////.html) |  |  
| [*FileSystemObjectService*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////////////////.html) |  |  
| [*FileSystemService*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////////.html) |  |  
| [*FileSystemServiceLoader*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////////.html) |  |  
| [*Folder*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////.html) | Hierarchy |  
| | Contents |  
| | Conversions |  
| | Checks |  
| | Factory Methods |  
| | Locations |  
| | Properties |  
| | Operations |  
| [*Folder.Converter*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////.html) |  |  
| [*Folder.Resolver*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////.html) |  |  
| [*Folder.Traversal*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////.html) |  |  
| [*Folder.Type*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////.html) |  |  
| [*FolderChangeWatcher*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////////.html) |  |  
| [*FolderList*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////.html) |  |  
| [*FolderList.Converter*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////.html) |  |  
| [*FolderPruner*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////.html) | Expiration Criteria |  
| [*FolderService*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////.html) |  |  
| [*GzipCodec*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////.html) |  |  
| [*InputResource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////////.html) |  |  
| [*JarLauncher*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////.html) | Options |  
| | Example |  
| [*JarLauncher.ProcessType*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////////.html) |  |  
| [*JarLauncher.RedirectTo*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////.html) |  |  
| [*KivaKitArchivedField*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////////////////.html) |  |  
| [*LineReader*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////.html) |  |  
| [*LineSource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////.html) |  |  
| [*LocalDisk*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////.html) |  |  
| [*LocalFile*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////.html) |  |  
| [*LocalFileSystemService*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////////.html) |  |  
| [*LocalFolder*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////.html) |  |  
| [*NullCodec*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////.html) |  |  
| [*NullResource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////.html) |  |  
| [*ObjectMetadata*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////.html) |  |  
| [*ObjectReader*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////.html) |  |  
| [*ObjectSerializer*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////////.html) |  |  
| [*ObjectSerializers*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////////////////.html) |  |  
| [*ObjectWriter*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////.html) |  |  
| [*OutputResource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////.html) |  |  
| [*Package*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////.html) | Hierarchy |  
| | Resources |  
| [*Package.Resolver*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////////////.html) |  |  
| [*PackagePath*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////.html) | Parsing |  
| | Factories |  
| | Examples |  
| [*PackageResource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////.html) |  |  
| [*PackageResource.Resolver*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////////////////////.html) |  |  
| [*PackageTrait*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////////.html) |  |  
| [*Packaged*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////.html) |  |  
| [*PropertyMap*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////.html) | Conversions |  
| | Creating and Loading Property Maps |  
| | Adding to Property Maps |  
| | Saving Property Maps |  
| [*Readable*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////.html) |  |  
| [*ReadableResource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////.html) |  |  
| [*Renamable*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////.html) |  |  
| [*Resource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////.html) | Checks |  
| | Properties |  
| | Operations |  
| | Examples |  
| [*Resource.Can*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////.html) |  |  
| [*Resource.Converter*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////.html) |  |  
| [*ResourceFolder*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////.html) | Hierarchy |  
| | Contents |  
| | Conversions |  
| | Checks |  
| | Operations |  
| [*ResourceFolder.Converter*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////.html) |  |  
| [*ResourceFolderIdentifier*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////.html) |  |  
| [*ResourceFolderResolver*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////.html) |  |  
| [*ResourceFolderResolverServiceLoader*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////////////////////////.html) |  |  
| [*ResourceGlob*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////.html) | Creating Globs |  
| | Logical Operations |  
| | Patterns |  
| | Examples |  
| [*ResourceIdentifier*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////.html) | Resource Resolvers |  
| [*ResourceList*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////.html) |  |  
| [*ResourcePath*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////.html) | Parsing |  
| | Factories |  
| [*ResourcePath.Converter*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////.html) |  |  
| [*ResourcePathed*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////.html) |  |  
| [*ResourceReader*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////.html) |  |  
| [*ResourceResolver*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////.html) |  |  
| [*ResourceResolverServiceLoader*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////////////////////.html) |  |  
| [*ResourceSection*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////////////////.html) |  |  
| [*ResourceWriter*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////.html) | Writers |  
| | Saving |  
| [*Resourceful*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////.html) |  |  
| [*SerializableObject*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////////.html) |  |  
| [*StringOutputResource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////////.html) |  |  
| [*StringResource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////.html) |  |  
| [*UriIdentified*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource///////////////////////////////////////////.html) |  |  
| [*Writable*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////.html) |  |  
| [*WritableResource*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////.html) |  |  
| [*ZipArchive*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource////////////////////////////////////////////////////////////.html) | Adding Files |  
| | Loading |  
| | Saving |  
| [*ZipArchive.Mode*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////////////////////.html) |  |  
| [*ZipCodec*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource/////////////////////////////////////////////////////////.html) |  |  
| [*ZipEntry*](https://www.kivakit.org/1.6.1/javadoc/kivakit/kivakit.resource//////////////////////////////////////////////////////////.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
