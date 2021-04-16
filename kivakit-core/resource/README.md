# kivakit-core resource &nbsp;&nbsp;![](https://kivakit.org/images/water-32.png)

This module contains abstractions for accessing the filesystem and arbitrary resources.

![](https://kivakit.org/images/horizontal-line.png)

### Index

[**Summary**](#summary)  
[**Readable Resources**](#readable-resources)  
[**Kinds of Resources**](#kinds-of-resources)  
[**Writable Resources**](#writable-resources)  
[**Files**](#files)  
[**Launching Jar Resources**](#launching-jar-resources)  
[**Other Resources**](#other-resources)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

![](https://kivakit.org/images/horizontal-line.png)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; ![](https://kivakit.org/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId></artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

![](https://kivakit.org/images/short-horizontal-line.png)

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module pulls together everything that can stream together under a single abstraction,  
providing an easy way to work with different kinds of resources without being tied to any  
of the details.

### Readable Resources <a name="readable-resources"></a> &nbsp; &nbsp; ![](https://kivakit.org/images/wand-40.png)

Different resource classes are constructed in different ways, but each implements the *Resource*  
interface. The most important methods are shown here in a simplified form:

    public interface Resource extends ... 
    { 
        void copyTo(final WritableResource destination); 
        boolean exists();
        boolean isReadable();
        Time lastModified();
        InputStream openForReading();
        ResourcePath path();
        ResourceReader reader();
        Bytes size();
    }

### Kinds of Resources <a name="kinds-of-resources"></a> &nbsp; &nbsp; ![](https://kivakit.org/images/diagram-48.png)

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

### Writable Resources <a name="writable-resources"></a> &nbsp; &nbsp; ![](https://kivakit.org/images/pencil-32.png)

The *WritableResource* interface extends the *Resource* interface to add output capabilities:

    public interface WritableResource extends Resource 
    { 
        boolean isWritable();
        OutputStream openForWriting();
        ResourceWriter writer();
    }

### Files <a name="files"></a> &nbsp; &nbsp; ![](https://kivakit.org/images/folder-32.png)

The *File* object is a *WritableResource* that adds a number of file-specific methods, including:

    public class File implements WritableResource, FileSystemObject, ... 
    { 
        boolean chmod(final PosixFilePermission... permissions);
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

![](https://kivakit.org/images/short-horizontal-line.png)

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; ![](https://kivakit.org/images/diagram-48.png)

[*File System - File System Services*](documentation/diagrams/diagram-file-system-service.svg)  
[*File System - Files*](documentation/diagrams/diagram-file-system-file.svg)  
[*File System - Folders*](documentation/diagrams/diagram-file-system-folder.svg)  
[*Resources*](documentation/diagrams/diagram-resource.svg)  
[*Resources - Archives*](documentation/diagrams/diagram-resource-archive.svg)  
[*Resources - Built-In Resource Types*](documentation/diagrams/diagram-resource-type.svg)  
[*Resources - Compression*](documentation/diagrams/diagram-resource-compression.svg)  
[*Resources - Paths*](documentation/diagrams/diagram-resource-path.svg)  
[*Resources - Services*](documentation/diagrams/diagram-resource-service.svg)  
[*diagram-jar-launcher*](documentation/diagrams/diagram-jar-launcher.svg)  

![](https://kivakit.org/images/short-horizontal-line.png)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; ![](https://kivakit.org/images/box-40.png)

[*com.telenav.kivakit.core.filesystem*](documentation/diagrams/com.telenav.kivakit.core.filesystem.svg)  
[*com.telenav.kivakit.core.filesystem.loader*](documentation/diagrams/com.telenav.kivakit.core.filesystem.loader.svg)  
[*com.telenav.kivakit.core.filesystem.local*](documentation/diagrams/com.telenav.kivakit.core.filesystem.local.svg)  
[*com.telenav.kivakit.core.filesystem.spi*](documentation/diagrams/com.telenav.kivakit.core.filesystem.spi.svg)  
[*com.telenav.kivakit.core.resource*](documentation/diagrams/com.telenav.kivakit.core.resource.svg)  
[*com.telenav.kivakit.core.resource.compression*](documentation/diagrams/com.telenav.kivakit.core.resource.compression.svg)  
[*com.telenav.kivakit.core.resource.compression.archive*](documentation/diagrams/com.telenav.kivakit.core.resource.compression.archive.svg)  
[*com.telenav.kivakit.core.resource.compression.codecs*](documentation/diagrams/com.telenav.kivakit.core.resource.compression.codecs.svg)  
[*com.telenav.kivakit.core.resource.path*](documentation/diagrams/com.telenav.kivakit.core.resource.path.svg)  
[*com.telenav.kivakit.core.resource.project*](documentation/diagrams/com.telenav.kivakit.core.resource.project.svg)  
[*com.telenav.kivakit.core.resource.reading*](documentation/diagrams/com.telenav.kivakit.core.resource.reading.svg)  
[*com.telenav.kivakit.core.resource.resources.jar.launcher*](documentation/diagrams/com.telenav.kivakit.core.resource.resources.jar.launcher.svg)  
[*com.telenav.kivakit.core.resource.resources.other*](documentation/diagrams/com.telenav.kivakit.core.resource.resources.other.svg)  
[*com.telenav.kivakit.core.resource.resources.packaged*](documentation/diagrams/com.telenav.kivakit.core.resource.resources.packaged.svg)  
[*com.telenav.kivakit.core.resource.resources.streamed*](documentation/diagrams/com.telenav.kivakit.core.resource.resources.streamed.svg)  
[*com.telenav.kivakit.core.resource.resources.string*](documentation/diagrams/com.telenav.kivakit.core.resource.resources.string.svg)  
[*com.telenav.kivakit.core.resource.spi*](documentation/diagrams/com.telenav.kivakit.core.resource.spi.svg)  
[*com.telenav.kivakit.core.resource.writing*](documentation/diagrams/com.telenav.kivakit.core.resource.writing.svg)  

![](https://kivakit.org/images/short-horizontal-line.png)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; ![](https://kivakit.org/images/books-40.png)

Javadoc coverage for this project is 87.8%.  
  
&nbsp; &nbsp;  ![](https://kivakit.org/images/meter-90-12.png)



| Class | Documentation Sections |
|---|---|
| [*ArchivedFields*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/ArchivedFields.html) |  |  
| [*BaseReadableResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/reading/BaseReadableResource.html) |  |  
| [*BaseWritableResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/writing/BaseWritableResource.html) |  |  
| [*BitArrayResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/other/BitArrayResource.html) |  |  
| [*Codec*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/Codec.html) |  |  
| [*Compressor*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/Compressor.html) |  |  
| [*CopyMode*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/CopyMode.html) |  |  
| [*CoreResourceKryoTypes*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/project/CoreResourceKryoTypes.html) |  |  
| [*CoreResourceProject*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/project/CoreResourceProject.html) |  |  
| [*Decompressor*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/Decompressor.html) |  |  
| [*Disk*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/Disk.html) |  |  
| [*DiskService*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/spi/DiskService.html) |  |  
| [*Extension*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/path/Extension.html) | Matching |  
| | Checks |  
| | Properties |  
| | Functional Methods |  
| [*FieldArchive*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/FieldArchive.html) |  |  
| [*FieldArchive.ObjectField*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/FieldArchive.ObjectField.html) |  |  
| [*File*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/File.html) | Conversion Methods |  
| | Path Methods |  
| | Checks |  
| | Operations |  
| | Functional Methods |  
| [*File.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/File.Converter.html) |  |  
| [*File.Resolver*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/File.Resolver.html) |  |  
| [*FileCache*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/FileCache.html) |  |  
| [*FileList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/FileList.html) |  |  
| [*FileList.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/FileList.Converter.html) |  |  
| [*FileName*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/path/FileName.html) | Matching |  
| | Functional |  
| | Checks |  
| [*FilePath*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/path/FilePath.html) | Parsing |  
| | Factories |  
| [*FilePath.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/path/FilePath.Converter.html) |  |  
| [*FileService*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/spi/FileService.html) |  |  
| [*FileSystemObject*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/FileSystemObject.html) |  |  
| [*FileSystemObjectService*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/spi/FileSystemObjectService.html) |  |  
| [*FileSystemService*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/spi/FileSystemService.html) |  |  
| [*FileSystemServiceLoader*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/loader/FileSystemServiceLoader.html) |  |  
| [*Folder*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/Folder.html) | Hierarchy |  
| | Contents |  
| | Conversions |  
| | Checks |  
| | Factory Methods |  
| | Locations |  
| | Properties |  
| | Operations |  
| [*Folder.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/Folder.Converter.html) |  |  
| [*Folder.Resolver*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/Folder.Resolver.html) |  |  
| [*Folder.Traversal*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/Folder.Traversal.html) |  |  
| [*Folder.Type*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/Folder.Type.html) |  |  
| [*FolderChangeWatcher*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/FolderChangeWatcher.html) |  |  
| [*FolderList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/FolderList.html) |  |  
| [*FolderList.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/FolderList.Converter.html) |  |  
| [*FolderPruner*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/FolderPruner.html) | Expiration Criteria |  
| [*FolderService*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/spi/FolderService.html) |  |  
| [*GzipCodec*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/codecs/GzipCodec.html) |  |  
| [*InputResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/streamed/InputResource.html) |  |  
| [*JarLauncher*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/jar/launcher/JarLauncher.html) | Options |  
| | Example |  
| [*JarLauncher.ProcessType*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/jar/launcher/JarLauncher.ProcessType.html) |  |  
| [*JarLauncher.RedirectTo*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/jar/launcher/JarLauncher.RedirectTo.html) |  |  
| [*KivaKitArchivedField*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/KivaKitArchivedField.html) |  |  
| [*LineReader*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/reading/LineReader.html) |  |  
| [*LineSource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/reading/LineSource.html) |  |  
| [*LocalDisk*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/local/LocalDisk.html) |  |  
| [*LocalFile*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/local/LocalFile.html) |  |  
| [*LocalFileSystemService*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/local/LocalFileSystemService.html) |  |  
| [*LocalFolder*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/filesystem/local/LocalFolder.html) |  |  
| [*NullCodec*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/codecs/NullCodec.html) |  |  
| [*NullResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/other/NullResource.html) |  |  
| [*OutputResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/streamed/OutputResource.html) |  |  
| [*Package*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/packaged/Package.html) | Hierarchy |  
| | Resources |  
| [*Package.Resolver*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/packaged/Package.Resolver.html) |  |  
| [*PackageResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/packaged/PackageResource.html) |  |  
| [*PackageResource.Resolver*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/packaged/PackageResource.Resolver.html) |  |  
| [*Packaged*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/packaged/Packaged.html) |  |  
| [*PropertyMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/other/PropertyMap.html) | Conversions |  
| | Creating and Loading Property Maps |  
| | Adding to Property Maps |  
| | Saving Property Maps |  
| [*ReadableResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/ReadableResource.html) |  |  
| [*Resource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/Resource.html) | Checks |  
| | Properties |  
| | Operations |  
| | Examples |  
| [*Resource.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/Resource.Converter.html) |  |  
| [*ResourceFolder*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/ResourceFolder.html) |  |  
| [*ResourceFolder.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/ResourceFolder.Converter.html) |  |  
| [*ResourceFolderIdentifier*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/ResourceFolderIdentifier.html) |  |  
| [*ResourceFolderResolver*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/spi/ResourceFolderResolver.html) |  |  
| [*ResourceFolderResolverServiceLoader*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/spi/ResourceFolderResolverServiceLoader.html) |  |  
| [*ResourceIdentifier*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/ResourceIdentifier.html) |  |  
| [*ResourceList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/ResourceList.html) |  |  
| [*ResourceList.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/ResourceList.Converter.html) |  |  
| [*ResourcePath*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/ResourcePath.html) | Parsing |  
| | Factories |  
| [*ResourcePath.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/ResourcePath.Converter.html) |  |  
| [*ResourcePathed*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/path/ResourcePathed.html) |  |  
| [*ResourceReader*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/reading/ResourceReader.html) |  |  
| [*ResourceResolver*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/spi/ResourceResolver.html) |  |  
| [*ResourceResolverServiceLoader*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/spi/ResourceResolverServiceLoader.html) |  |  
| [*ResourceSection*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/other/ResourceSection.html) |  |  
| [*ResourceWriter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/writing/ResourceWriter.html) | Writers |  
| | Saving |  
| [*Resourceful*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/Resourceful.html) |  |  
| [*StringResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/resources/string/StringResource.html) |  |  
| [*WritableResource*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/WritableResource.html) |  |  
| [*ZipArchive*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/ZipArchive.html) | Adding Files |  
| | Loading |  
| | Saving |  
| [*ZipArchive.Mode*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/ZipArchive.Mode.html) |  |  
| [*ZipCodec*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/codecs/ZipCodec.html) |  |  
| [*ZipEntry*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/ZipEntry.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<br/>

![](https://kivakit.org/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.15. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

