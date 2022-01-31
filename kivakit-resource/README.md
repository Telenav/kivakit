[//]: # (start-user-text)

<a href="https://www.kivakit.org">
<img src="https://www.kivakit.org/images/web-32.png" srcset="https://www.kivakit.org/images/web-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://twitter.com/openkivakit">
<img src="https://www.kivakit.org/images/twitter-32.png" srcset="https://www.kivakit.org/images/twitter-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://kivakit.zulipchat.com">
<img src="https://www.kivakit.org/images/zulip-32.png" srcset="https://www.kivakit.org/images/zulip-32-2x.png 2x"/>
</a>

[//]: # (end-user-text)

# kivakit-resource &nbsp;&nbsp; <img src="https://www.kivakit.org/images/water-32.png" srcset="https://www.kivakit.org/images/water-32-2x.png 2x"/>

This module contains abstractions for accessing the filesystem and arbitrary resources.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Readable Resources**](#readable-resources)  
[**Kinds of Resources**](#kinds-of-resources)  
[**Writable Resources**](#writable-resources)  
[**Files**](#files)  
[**Launching Jar Resources**](#launching-jar-resources)  
[**Other Resources**](#other-resources)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-resource</artifactId>
        <version>1.3.1-SNAPSHOT</version>
    </dependency>

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module pulls together everything that can stream together under a single abstraction,
providing an easy way to work with different kinds of resources without being tied to any
of the details.

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Readable Resources <a name="readable-resources"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/wand-32.png" srcset="https://www.kivakit.org/images/wand-32-2x.png 2x"/>

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

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Kinds of Resources <a name="kinds-of-resources"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

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

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Writable Resources <a name="writable-resources"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/pencil-32.png" srcset="https://www.kivakit.org/images/pencil-32-2x.png 2x"/>

The *WritableResource* interface extends the *Resource* interface to add output capabilities:

    public interface WritableResource extends Resource 
    { 
        boolean isWritable();
        OutputStream openForWriting();
        ResourceWriter writer();
    }

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Files <a name="files"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/folder-32.png" srcset="https://www.kivakit.org/images/folder-32-2x.png 2x"/>

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

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

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

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

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

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*File System - File System Services*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/diagram-file-system-service.svg)  
[*File System - Files*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/diagram-file-system-file.svg)  
[*File System - Folders*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/diagram-file-system-folder.svg)  
[*Resources*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/diagram-resource.svg)  
[*Resources - Archives*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/diagram-resource-archive.svg)  
[*Resources - Built-In Resource Types*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/diagram-resource-type.svg)  
[*Resources - Compression*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/diagram-resource-compression.svg)  
[*Resources - Jar Launcher*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/diagram-jar-launcher.svg)  
[*Resources - Paths*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/diagram-resource-path.svg)  
[*Resources - Services*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/diagram-resource-service.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.filesystem*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.filesystem.svg)  
[*com.telenav.kivakit.filesystem.loader*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.filesystem.loader.svg)  
[*com.telenav.kivakit.filesystem.local*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.filesystem.local.svg)  
[*com.telenav.kivakit.filesystem.spi*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.filesystem.spi.svg)  
[*com.telenav.kivakit.resource*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.svg)  
[*com.telenav.kivakit.resource.compression*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.compression.svg)  
[*com.telenav.kivakit.resource.compression.archive*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.compression.archive.svg)  
[*com.telenav.kivakit.resource.compression.codecs*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.compression.codecs.svg)  
[*com.telenav.kivakit.resource.path*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.path.svg)  
[*com.telenav.kivakit.resource.project*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.project.svg)  
[*com.telenav.kivakit.resource.reading*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.reading.svg)  
[*com.telenav.kivakit.resource.resources.jar.launcher*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.resources.jar.launcher.svg)  
[*com.telenav.kivakit.resource.resources.other*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.resources.other.svg)  
[*com.telenav.kivakit.resource.resources.packaged*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.resources.packaged.svg)  
[*com.telenav.kivakit.resource.resources.streamed*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.resources.streamed.svg)  
[*com.telenav.kivakit.resource.resources.string*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.resources.string.svg)  
[*com.telenav.kivakit.resource.spi*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.spi.svg)  
[*com.telenav.kivakit.resource.writing*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-resource//documentation/diagrams/com.telenav.kivakit.resource.writing.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 86.6%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-90-96.png" srcset="https://www.kivakit.org/images/meter-90-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*ArchivedFields*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/archive/ArchivedFields.html) |  |  
| [*BaseReadableResource*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/reading/BaseReadableResource.html) |  |  
| [*BaseWritableResource*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/writing/BaseWritableResource.html) |  |  
| [*Codec*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/Codec.html) |  |  
| [*Compressor*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/Compressor.html) |  |  
| [*CopyMode*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/CopyMode.html) |  |  
| [*Decompressor*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/Decompressor.html) |  |  
| [*Disk*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/Disk.html) |  |  
| [*DiskService*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/spi/DiskService.html) |  |  
| [*Extension*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/path/Extension.html) | Matching |  
| | Checks |  
| | Properties |  
| | Functional Methods |  
| [*FieldArchive*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/archive/FieldArchive.html) |  |  
| [*FieldArchive.ObjectField*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/archive/FieldArchive.ObjectField.html) |  |  
| [*File*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/File.html) | Conversion Methods |  
| | Path Methods |  
| | Checks |  
| | Operations |  
| | Functional Methods |  
| [*File.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/File.Converter.html) |  |  
| [*File.Resolver*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/File.Resolver.html) |  |  
| [*FileCache*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/FileCache.html) |  |  
| [*FileGlobPattern*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/FileGlobPattern.html) |  |  
| [*FileList*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/FileList.html) |  |  
| [*FileList.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/FileList.Converter.html) |  |  
| [*FileName*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/path/FileName.html) | Matching |  
| | Functional |  
| | Checks |  
| [*FilePath*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/path/FilePath.html) | Parsing |  
| | Factories |  
| [*FilePath.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/path/FilePath.Converter.html) |  |  
| [*FileService*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/spi/FileService.html) |  |  
| [*FileSystemObject*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/FileSystemObject.html) |  |  
| [*FileSystemObjectService*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/spi/FileSystemObjectService.html) |  |  
| [*FileSystemService*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/spi/FileSystemService.html) |  |  
| [*FileSystemServiceLoader*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/loader/FileSystemServiceLoader.html) |  |  
| [*Folder*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/Folder.html) | Hierarchy |  
| | Contents |  
| | Conversions |  
| | Checks |  
| | Factory Methods |  
| | Locations |  
| | Properties |  
| | Operations |  
| [*Folder.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/Folder.Converter.html) |  |  
| [*Folder.Resolver*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/Folder.Resolver.html) |  |  
| [*Folder.Traversal*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/Folder.Traversal.html) |  |  
| [*Folder.Type*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/Folder.Type.html) |  |  
| [*FolderChangeWatcher*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/FolderChangeWatcher.html) |  |  
| [*FolderGlobPattern*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/FolderGlobPattern.html) |  |  
| [*FolderList*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/FolderList.html) |  |  
| [*FolderList.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/FolderList.Converter.html) |  |  
| [*FolderPruner*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/FolderPruner.html) | Expiration Criteria |  
| [*FolderService*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/spi/FolderService.html) |  |  
| [*GzipCodec*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/codecs/GzipCodec.html) |  |  
| [*InputResource*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/streamed/InputResource.html) |  |  
| [*JarLauncher*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/jar/launcher/JarLauncher.html) | Options |  
| | Example |  
| [*JarLauncher.ProcessType*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/jar/launcher/JarLauncher.ProcessType.html) |  |  
| [*JarLauncher.RedirectTo*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/jar/launcher/JarLauncher.RedirectTo.html) |  |  
| [*KivaKitArchivedField*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/archive/KivaKitArchivedField.html) |  |  
| [*LineReader*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/reading/LineReader.html) |  |  
| [*LineSource*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/reading/LineSource.html) |  |  
| [*LocalDisk*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/local/LocalDisk.html) |  |  
| [*LocalFile*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/local/LocalFile.html) |  |  
| [*LocalFileSystemService*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/local/LocalFileSystemService.html) |  |  
| [*LocalFolder*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/filesystem/local/LocalFolder.html) |  |  
| [*NullCodec*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/codecs/NullCodec.html) |  |  
| [*NullResource*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/other/NullResource.html) |  |  
| [*OutputResource*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/streamed/OutputResource.html) |  |  
| [*Package*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/packaged/Package.html) | Hierarchy |  
| | Resources |  
| [*Package.Resolver*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/packaged/Package.Resolver.html) |  |  
| [*PackageResource*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/packaged/PackageResource.html) |  |  
| [*PackageResource.Resolver*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/packaged/PackageResource.Resolver.html) |  |  
| [*Packaged*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/packaged/Packaged.html) |  |  
| [*PropertyMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/other/PropertyMap.html) | Conversions |  
| | Creating and Loading Property Maps |  
| | Adding to Property Maps |  
| | Saving Property Maps |  
| [*ReadableResource*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/ReadableResource.html) |  |  
| [*Resource*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/Resource.html) | Checks |  
| | Properties |  
| | Operations |  
| | Examples |  
| [*Resource.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/Resource.Converter.html) |  |  
| [*ResourceFolder*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/ResourceFolder.html) |  |  
| [*ResourceFolder.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/ResourceFolder.Converter.html) |  |  
| [*ResourceFolderIdentifier*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/ResourceFolderIdentifier.html) |  |  
| [*ResourceFolderResolver*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/spi/ResourceFolderResolver.html) |  |  
| [*ResourceFolderResolverServiceLoader*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/spi/ResourceFolderResolverServiceLoader.html) |  |  
| [*ResourceIdentifier*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/ResourceIdentifier.html) |  |  
| [*ResourceKryoTypes*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/project/ResourceKryoTypes.html) |  |  
| [*ResourceList*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/ResourceList.html) |  |  
| [*ResourceList.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/ResourceList.Converter.html) |  |  
| [*ResourcePath*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/ResourcePath.html) | Parsing |  
| | Factories |  
| [*ResourcePath.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/ResourcePath.Converter.html) |  |  
| [*ResourcePathed*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/path/ResourcePathed.html) |  |  
| [*ResourceProject*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/ResourceProject.html) |  |  
| [*ResourceReader*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/reading/ResourceReader.html) |  |  
| [*ResourceResolver*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/spi/ResourceResolver.html) |  |  
| [*ResourceResolverServiceLoader*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/spi/ResourceResolverServiceLoader.html) |  |  
| [*ResourceSection*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/other/ResourceSection.html) |  |  
| [*ResourceTrait*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/ResourceTrait.html) |  |  
| [*ResourceWriter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/writing/ResourceWriter.html) | Writers |  
| | Saving |  
| [*Resourceful*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/Resourceful.html) |  |  
| [*StringResource*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/resources/string/StringResource.html) |  |  
| [*UriIdentified*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/UriIdentified.html) |  |  
| [*WritableResource*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/WritableResource.html) |  |  
| [*ZipArchive*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/archive/ZipArchive.html) | Adding Files |  
| | Loading |  
| | Saving |  
| [*ZipArchive.Mode*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/archive/ZipArchive.Mode.html) |  |  
| [*ZipCodec*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/codecs/ZipCodec.html) |  |  
| [*ZipEntry*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc//kivakit.resource/com/telenav/kivakit/resource/compression/archive/ZipEntry.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>

