# kivakit-filesystems s3fs &nbsp;&nbsp; <img src="https://www.lexakai.org/images/disks-48.png" srcset="https://www.lexakai.org/images/disks-48-2x.png 2x"/>

Service provider for AWS S3 filesystems.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512@2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Example**](#example)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512@2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.lexakai.org/images/dependencies-32.png" srcset="https://www.lexakai.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit/kivakit-filesystems/s3fs/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-filesystems-s3fs</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>


<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128@2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module is a filesystem service provider, giving access to S3 filesystem data under the
[*kivakit-core-resource*](../../kivakit-core/resource/README.md)
mini-framework. This service provider will be discovered using the Java
service loader mechanism. All that is required to make use of HDFS paths with *File* and
*Folder* objects is to:

1. Include the dependency above in *pom.xml*
2. Import *kivakit-filesystems-s3fs* in *module-info.java*
3. Provide an instance of *S3Settings* with *Settings.register(settings, bucket)*,
   where *bucket* identifies the S3 bucket that is used in paths, such as hdfs://*bucket*/data.txt
4. Make use of HDFS paths in files and folders, for example:

### Example <a name = "example"></a>

For example:

    var settings = new S3Settings();

        [...]

    Settings.register(settings, new InstanceIdentifier("my-bucket"));

    var file = File.parse("hdfs://my-bucket/myfile.txt")

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128@2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.lexakai.org/images/diagram-32.png" srcset="https://www.lexakai.org/images/diagram-32-2x.png 2x"/>

[*S3 Service Provider*](https://www.kivakit.org/lexakai/kivakit/kivakit-filesystems/s3fs/documentation/diagrams/diagram-s3.svg)

<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128@2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.lexakai.org/images/box-32.png" srcset="https://www.lexakai.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.filesystems.s3fs*](https://www.kivakit.org/lexakai/kivakit/kivakit-filesystems/s3fs/documentation/diagrams/com.telenav.kivakit.filesystems.s3fs.svg)  
[*com.telenav.kivakit.filesystems.s3fs.project*](https://www.kivakit.org/lexakai/kivakit/kivakit-filesystems/s3fs/documentation/diagrams/com.telenav.kivakit.filesystems.s3fs.project.svg)

<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128@2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.lexakai.org/images/books-32.png" srcset="https://www.lexakai.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 80.0%.  
  
&nbsp; &nbsp;  ![](https://www.kivakit.org/images/meter-80-12.png)



| Class | Documentation Sections |
|---|---|
| [*FileSystemsS3FsProject*](https://www.kivakit.org/javadoc/kivakit/kivakit.filesystems.s3fs/com/telenav/kivakit/filesystems/s3fs/project/FileSystemsS3FsProject.html) |  |  
| [*S3File*](https://www.kivakit.org/javadoc/kivakit/kivakit.filesystems.s3fs/com/telenav/kivakit/filesystems/s3fs/S3File.html) |  |  
| [*S3FileSystemObject*](https://www.kivakit.org/javadoc/kivakit/kivakit.filesystems.s3fs/com/telenav/kivakit/filesystems/s3fs/S3FileSystemObject.html) |  |  
| [*S3FileSystemService*](https://www.kivakit.org/javadoc/kivakit/kivakit.filesystems.s3fs/com/telenav/kivakit/filesystems/s3fs/S3FileSystemService.html) |  |  
| [*S3Folder*](https://www.kivakit.org/javadoc/kivakit/kivakit.filesystems.s3fs/com/telenav/kivakit/filesystems/s3fs/S3Folder.html) |  |  
| [*S3Output*](https://www.kivakit.org/javadoc/kivakit/kivakit.filesystems.s3fs/com/telenav/kivakit/filesystems/s3fs/S3Output.html) |  |  
| [*S3Settings*](https://www.kivakit.org/javadoc/kivakit/kivakit.filesystems.s3fs/com/telenav/kivakit/filesystems/s3fs/S3Settings.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512@2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai). UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

