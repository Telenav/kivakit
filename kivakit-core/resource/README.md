# kivakit-core-resource &nbsp;&nbsp;![](../../documentation/images/water-32.png)

This module contains abstractions for accessing the filesystem and arbitrary resources.

![](documentation/images/horizontal-line.png)

### Index

[**Summary**](#summary)  
[**Readable Resources**](#readable-resources)  
[**Kinds of Resources**](#kinds-of-resources)  
[**Writable Resources**](#writable-resources)  
[**Files**](#files)  
[**Launching Jar Resources**](#launching-jar-resources)  
[**Other Resources**](#other-resources)  
[**Dependencies**](#dependencies)  
[**Class Diagrams**](#class-diagrams)  
[**Package Diagrams**](#package-diagrams)  
[**Javadoc**](#javadoc)

![](documentation/images/horizontal-line.png)

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module pulls together everything that can stream together under a single abstraction,  
providing an easy way to process different kinds of resources without being tied to any  
of the details.

### Readable Resources <a name="readable-resources"></a> &nbsp; &nbsp; ![](../../documentation/images/wand-40.png)

Different resource classes are constructed in different ways, but each implements the _Resource_  
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

### Kinds of Resources <a name="kinds-of-resources"></a> &nbsp; &nbsp; ![](../../documentation/images/diagram-48.png)

The class _StringResource_ is about as simple as they come, but it can be read just like it was a  
_File_ or any other resource:

    Resource resource = new StringResource("this is a test");
    String [contents = resource.reader().string();

In the same way, a _ZipArchive_ contains _ZipEntry_ resources, so they can be read in a similar  
way through _reader().string()_.

    ZipArchive zip = new ZipArchive(new File("kivakit.zip"));
    ZipEntry entry = zip.entry("test.txt");
    String contents = entry.reader().string();

This method can accept a _ZipEntry_, a _StringResource_, a _File,_ an _HttpGetResource_ or any  
other resource and it works the same:

    public String contents(Resource resource)
    { 
        return resource.reader().string(); 
    }

### Writable Resources <a name="writable-resources"></a> &nbsp; &nbsp; ![](../../documentation/images/pencil-32.png)

The _WritableResource_ interface extends the _Resource_ interface to add output capabilities:

    public interface WritableResource extends Resource 
    { 
        boolean isWritable();
        OutputStream openForWriting();
        ResourceWriter writer();
    }

### Files <a name="files"></a> &nbsp; &nbsp; ![](../../documentation/images/folder-32.png)

The _File_ object is a _WritableResource_ that adds a number of file-specific methods, including:

    public class File implements WritableResource, FileSystemObject, ... 
    { 
        boolean chmod(final PosixFilePermission... permissions);
        boolean delete();
        Folder parent();
        void renameTo(File destination);
    }

Besides being abstracted readable and writable resources, files are also special because they are  
integrated with KivaKit FileSystem API. This API uses JavaServices to allow _FileSystemService_  
implementations to be plugged in through a service provider interface (SPI).

When you construct a _File_, the _FileSystemServiceRegistry_ asks each _FileSystemService_ provider  
it found through the Java _ServiceLoader_ if it can accept the path you used when constructing the  
object. If a provider accepts the file, it will then be installed into the _File_ object, to provide  
all the required filesystem features.

For example, if you construct a _File_ like this:

    File file = new File("/home/jonathanl/kivakit.txt");

The service registry will find that the _LocalFileSystemService_ accepts this path, and you will be  
accessing a local file.

If you construct a _File_ like this:

    File file = new File("hdfs://home/jonathanl/kivakit.txt");

You will be accessing a file on HDFS through the _HdfsFileSystemService_ provider.

What makes all this so powerful is that you can accept a _File_ object as a parameter and not be  
tied to any particular filesystem, and you can accept a _Resource_ as a parameter and have an even  
wider scope of abstraction than that. It makes sense to get the most abstraction that you can. Use  
_Resource_ if it's sufficient and _File_ if you need file-specific functionality. Using the standard JDK  
objects like java.io.File will tie the code to a particular resource implementation, making it far  
less flexible.

| File System | Provider |
|---|---|
| Local Disk | LocalFileSystemService |
| HDFS | HdfsFileSystemService |
| S3 | S3FileSystemService |

### Launching Jar Resources <a name = "launching-jar-resources"></a>

The *JarLauncher* class can be used to launch executable jar resources from a local or remote  
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

    PackageResource.packageResource(MyClass.class, new FilePath("images/duke.png"));

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

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp;  ![](documentation/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-resource</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp;![](documentation/images/diagram-48.png)

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

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp;![](documentation/images/box-40.png)

[*com.telenav.kivakit.core.filesystem*](documentation/diagrams/com.telenav.kivakit.core.filesystem.svg)  
[*com.telenav.kivakit.core.filesystem.loader*](documentation/diagrams/com.telenav.kivakit.core.filesystem.loader.svg)  
[*com.telenav.kivakit.core.filesystem.local*](documentation/diagrams/com.telenav.kivakit.core.filesystem.local.svg)  
[*com.telenav.kivakit.core.filesystem.spi*](documentation/diagrams/com.telenav.kivakit.core.filesystem.spi.svg)  
[*com.telenav.kivakit.core.resource*](documentation/diagrams/com.telenav.kivakit.core.resource.svg)  
[*com.telenav.kivakit.core.resource.compression*](documentation/diagrams/com.telenav.kivakit.core.resource.compression.svg)  
[*
com.telenav.kivakit.core.resource.compression.archive*](documentation/diagrams/com.telenav.kivakit.core.resource.compression.archive.svg)  
[*com.telenav.kivakit.core.resource.compression.codecs*](documentation/diagrams/com.telenav.kivakit.core.resource.compression.codecs.svg)  
[*com.telenav.kivakit.core.resource.path*](documentation/diagrams/com.telenav.kivakit.core.resource.path.svg)  
[*com.telenav.kivakit.core.resource.project*](documentation/diagrams/com.telenav.kivakit.core.resource.project.svg)  
[*com.telenav.kivakit.core.resource.reading*](documentation/diagrams/com.telenav.kivakit.core.resource.reading.svg)  
[*
com.telenav.kivakit.core.resource.resources.jar.launcher*](documentation/diagrams/com.telenav.kivakit.core.resource.resources.jar.launcher.svg)  
[*com.telenav.kivakit.core.resource.resources.other*](documentation/diagrams/com.telenav.kivakit.core.resource.resources.other.svg)  
[*com.telenav.kivakit.core.resource.resources.packaged*](documentation/diagrams/com.telenav.kivakit.core.resource.resources.packaged.svg)  
[*com.telenav.kivakit.core.resource.resources.streamed*](documentation/diagrams/com.telenav.kivakit.core.resource.resources.streamed.svg)  
[*com.telenav.kivakit.core.resource.resources.string*](documentation/diagrams/com.telenav.kivakit.core.resource.resources.string.svg)  
[*com.telenav.kivakit.core.resource.spi*](documentation/diagrams/com.telenav.kivakit.core.resource.spi.svg)  
[*com.telenav.kivakit.core.resource.store*](documentation/diagrams/com.telenav.kivakit.core.resource.store.svg)  
[*com.telenav.kivakit.core.resource.writing*](documentation/diagrams/com.telenav.kivakit.core.resource.writing.svg)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp;![](documentation/images/books-40.png)

| Class | Documentation Sections |
|---|---|
| [*
ArchivedFields*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/ArchivedFields.html) |  |  
| [*
BaseObjectReader*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/reading/BaseObjectReader.html) |  |  
| [*
BaseReadableResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/reading/BaseReadableResource.html) |  |  
| [*
BaseWritableResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/writing/BaseWritableResource.html) |  |  
| [*
BaseWriter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/writing/BaseWriter.html) |  |  
| [*
BinaryObjectStore*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/store/BinaryObjectStore.html) |  |  
| [*
BitArrayResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/other/BitArrayResource.html) |  |  
| [*
Cache*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/Cache.html) |  |  
| [*
Codec*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/Codec.html) |  |  
| [*
Compressor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/Compressor.html) |  |  
| [*
CopyMode*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/CopyMode.html) |  |  
| [*
CoreResourceKryoTypes*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/project/CoreResourceKryoTypes.html) |  |  
| [*
CoreResourceProject*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/project/CoreResourceProject.html) |  |  
| [*
Decompressor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/Decompressor.html) |  |  
| [*
Disk*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/Disk.html) |  |  
| [*
DiskService*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/spi/DiskService.html) |  |  
| [*
Extension*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/path/Extension.html) |  |  
| [*
FieldArchive*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/FieldArchive.html) |  |  
| [*
FieldArchive.ObjectField*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/FieldArchive.ObjectField.html) |  |  
| [*
File*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/File.html) |  |  
| [*
File.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/File.Converter.html) |  |  
| [*
File.Factory*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/File.Factory.html) |  |  
| [*
FileList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/FileList.html) |  |  
| [*
FileList.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/FileList.Converter.html) |  |  
| [*
FileName*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/path/FileName.html) |  |  
| [*
FilePath*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/path/FilePath.html) | Parsing |  
| | Factories |  
| [*
FilePath.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/path/FilePath.Converter.html) |  |  
| [*
FileService*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/spi/FileService.html) |  |  
| [*
FileSystemObject*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/FileSystemObject.html) |  |  
| [*
FileSystemObjectService*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/spi/FileSystemObjectService.html) |  |  
| [*
FileSystemService*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/spi/FileSystemService.html) |  |  
| [*
FileSystemServiceLoader*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/loader/FileSystemServiceLoader.html) |  |  
| [*
Folder*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/Folder.html) |  |  
| [*
Folder.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/Folder.Converter.html) |  |  
| [*
Folder.Traversal*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/Folder.Traversal.html) |  |  
| [*
Folder.Type*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/Folder.Type.html) |  |  
| [*
FolderChangeWatcher*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/FolderChangeWatcher.html) |  |  
| [*
FolderList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/FolderList.html) |  |  
| [*
FolderList.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/FolderList.Converter.html) |  |  
| [*
FolderPruner*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/FolderPruner.html) |  |  
| [*
FolderService*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/spi/FolderService.html) |  |  
| [*
GzipCodec*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/codecs/GzipCodec.html) |  |  
| [*
InputResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/streamed/InputResource.html) |  |  
| [*
JarLauncher*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/jar/launcher/JarLauncher.html) |  |  
| [*
JarLauncher.ProcessType*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/jar/launcher/JarLauncher.ProcessType.html) |  |  
| [*
JarLauncher.RedirectTo*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/jar/launcher/JarLauncher.RedirectTo.html) |  |  
| [*
LineReader*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/reading/LineReader.html) |  |  
| [*
LineSource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/reading/LineSource.html) |  |  
| [*
LocalDisk*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/local/LocalDisk.html) |  |  
| [*
LocalFile*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/local/LocalFile.html) |  |  
| [*
LocalFileSystemService*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/local/LocalFileSystemService.html) |  |  
| [*
LocalFolder*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/filesystem/local/LocalFolder.html) |  |  
| [*
NullCodec*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/codecs/NullCodec.html) |  |  
| [*
NullResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/other/NullResource.html) |  |  
| [*
OutputResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/streamed/OutputResource.html) |  |  
| [*
Package*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/packaged/Package.html) |  |  
| [*
PackageResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/packaged/PackageResource.html) |  |  
| [*
PackageResource.Factory*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/packaged/PackageResource.Factory.html) |  |  
| [*
Packaged*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/packaged/Packaged.html) |  |  
| [*
PropertyMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/other/PropertyMap.html) |  |  
| [*
ReadableResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/ReadableResource.html) |  |  
| [*
Resource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/Resource.html) |  |  
| [*
Resource.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/Resource.Converter.html) |  |  
| [*
ResourceFactoryService*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/spi/ResourceFactoryService.html) |  |  
| [*
ResourceFactoryServiceRegistry*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/spi/ResourceFactoryServiceRegistry.html) |  |  
| [*
ResourceIdentifier*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/ResourceIdentifier.html) |  |  
| [*
ResourceList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/ResourceList.html) |  |  
| [*
ResourceList.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/ResourceList.Converter.html) |  |  
| [*
ResourcePath*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/ResourcePath.html) | Parsing |  
| | Factories |  
| [*
ResourcePath.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/ResourcePath.Converter.html) |  |  
| [*
ResourcePathed*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/path/ResourcePathed.html) |  |  
| [*
ResourceReader*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/reading/ResourceReader.html) |  |  
| [*
ResourceSection*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/other/ResourceSection.html) |  |  
| [*
ResourceWriter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/writing/ResourceWriter.html) |  |  
| [*
Resourced*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/Resourced.html) |  |  
| [*
StringResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/resources/string/StringResource.html) |  |  
| [*
KivaArchivedField*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/KivaArchivedField.html) |  |  
| [*
WritableResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/WritableResource.html) |  |  
| [*
ZipArchive*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/ZipArchive.html) |  |  
| [*
ZipArchive.Mode*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/ZipArchive.Mode.html) |  |  
| [*
ZipCodec*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/codecs/ZipCodec.html) |  |  
| [*
ZipEntry*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.resource/com/telenav/kivakit/core/resource/compression/archive/ZipEntry.html) |  |  

[//]: # (start-user-text)


[//]: # (end-user-text)

<br/>

![](documentation/images/horizontal-line.png)

<sub>Copyright 2011-2021 [Telenav](http://telenav.com), Inc. Licensed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by Lexakai on 2021.04.01</sub>    
<sub>UML diagrams courtesy of PlantUML (http://plantuml.com)</sub>

