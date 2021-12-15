![](https://www.kivakit.org/images/horizontal-line.png)

## Version 1.2.0 (2021.12.14) "plutonium goldfish"

### added

 * **Settings**
   * Added backwards-compatible support for JSON settings
   * Added SettingsStore SPI, changed names of some classes
     * Settings -> MemorySettingsStore
     * FolderSettings -> FolderSettingsStore
     * PackageSettings -> PackageSettingsStore
   * Added SettingsTrait
 * **Zookeeper**
   * ZookeeperSettingsStore
   * ZookeeperConnection
   * ZookeeperChangeListener
 * **Clustering**
   * MicroserviceCluster
   * MicroserviceClusterMember
   * Microservice.allowedLambdaRequests()
   * Microservice.onJoin()
   * Microservice.onLeave()
 * **Serialization**
   * Added GsonFactorySource, DefaultGsonFactory
 * **Mixins**
   * Added mapping from mixin back to the owning object
 * **New Members**
   * Strings.isOneOf(String, String...), Strings.doubleQuoted(String)
   * Version.asDouble()
   * Host.dnsName()
   * PropertyMap.asBoolean(String key)
   * Bytes.bytes(long[]), Bytes.bytes(int[]), Bytes.bytes(int[])
   * LanguageTrait.isTrueOr(boolean, String message, Object... arguments)
   * LanguageTrait.isNonNullOr(Object, String message, Object... arguments)
   * Path.copy(), StringPath.copy()
   * WaitState.TERMINATED
   * SwitchParser.durationSwitchParser(...)
   * Registry.unregister(), RegistryTrait.unregister()
   * JettyMicroservletRequest.hasBody()
 * **XML**
   * Added StaxReader, StaxPath
 * **AWS Lambda**
   * Added LambdaRequestHandler

### improved

 * Host address resolution
 * Added check for classes that extend BaseRepeater and also have a RepeaterMixin
 * Merged jars build and scripts
 * Broken listener chain diagnostics
 * Documentation

### fixed

 * Fixed status code check in BaseHttpResource
 * Fixed query parameter parsing bug in HttpNetworkLocation
 * Fixed bug in JettyMicroservletFilter so POSTed body is only read if it exists
 * Fixes to S3 filesystem

### security

 * Upgraded Jetty to 9.4.44

![](https://www.kivakit.org/images/horizontal-line.png)

## Version 1.1.3 (2021.11.14) "plutonium seal"

### added

 * Added LanguageTrait for common language-related extensions
 * Added Strings.doubleQuoted(String)
 * Added Folder.hasChanged() using an MD5 hash stored in Java Preferences
 * Added BaseList.bracketed() method
 * Added TryTrait.tryFinallyThrow()
 * Added MessageException, created when OperationMessage.asException() is called
 * Added SwitchParser.durationSwitchParser()
 * Added WakeState.TERMINATED
 * Documentation improvements

### changes

 * TryTrait.tryCatch() now returns a boolean
 * Version class no longer requires a minor version

### fixed

 * Application should listen to projects
 * BaseHttpResource doesn't check HTTP status correctly
 * HttpNetworkLocation query parameter handling fails when there are no parameters

![](https://www.kivakit.org/images/horizontal-line.png)

## Version 1.1.1 (2021.11.05) "plutonium panda"

### added 

* [Add .proto output files to kivakit-microservice GRPC service](https://github.com/Telenav/kivakit/issues/89)

### fixed

 * [Division microservice example is broken #87](https://github.com/Telenav/kivakit/issues/87)
 * [Broken listener chain due to inheritance from mixin and base component #86](https://github.com/Telenav/kivakit/issues/86)
 * [Documentation links are broken in kivakit 1.1.0 #88](https://github.com/Telenav/kivakit/issues/88)

![](https://www.kivakit.org/images/horizontal-line.png)

## Version 1.1.0 (2021.08.27) "plutonium snake"

### added

* New mini-frameworks  
  * *kivakit-microservices* - Jetty, REST, GRPC, Swagger microservices  
  * *kivakit-metrics* - Metrics collection  
  * *kivakit-metrics-prometheus* - Prometheus metrics provider
  * *kivakit-filesystems-java* - Java FileSystem provider
  * *kivakit-data-formats-xml* - StaxReader to improve use of java.xml.stream API

### updated

* Refactoring to improve naming and packaging
* Added and improved documentation
* Static methods returning broadcasting components now take a Listener as the first argument.
  This prevents callers from accidentally forgetting to *listenTo()* the component.
* Maven pom files now inherit from a "superpom" 
* Divided Component interface into traits:
    * RegistryTrait
    * SettingsTrait
    * ResourceTrait
    * TryTrait

### fixed

* Fixes to build and deployment scripts
* Many small bug fixes

![](https://www.kivakit.org/images/horizontal-line.png)

## Version 1.0.3 (2021.08.27) "puffy mouse"

Initial release of KivaKit.

![](https://www.kivakit.org/images/horizontal-line.png)

