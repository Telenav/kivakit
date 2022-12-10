# KivaKit Change Log

-----------------------------------------------------------------------------------------------------------------------

## Version 1.9.0 (2022.12.08) "happy dracula"

### Security

 - Update version of commons-net to 3.9.0 

### Features

 - Added support for per-project and per-application build and project metadata 

-----------------------------------------------------------------------------------------------------------------------

## Version 1.8.9 (2022.12.07) "happy sneakers"

### Security

 - Update version of commons-net to 3.8.0 

### Features

 - Added global listener facility

### Bug Fixes

 - Fix name of java filesystem provider, resolving test failure
 - Warn instead of throwing exception when adding shutdown hook during shutdown

-----------------------------------------------------------------------------------------------------------------------

## Version 1.8.8 (2022.12.01) "happy dolphin"

### Bug Fixes

 - Ensure that settings objects are always registered in the global registry

-----------------------------------------------------------------------------------------------------------------------

## Version 1.8.7 (2022.11.30) "happy hippo"

### Messaging

 - Added `KIVAKIT_NO_LISTENER_ERROR` flag (default is `false`) to throw `NoListenerError` for broken listener chains

-----------------------------------------------------------------------------------------------------------------------

## Version 1.8.6 (2022.11.29) "happy duckling"

### Build

 - Removed modularity
 - Replaced module resource handling with ClassGraph
 - Fix javadoc output
 - Add CodeFlowers generation

-----------------------------------------------------------------------------------------------------------------------

## Version 1.8.5 (2022.11.13) "happy rabbit"

### Build

 - Rewrote all pom files
 - Updated build and release documentation
 - Added and updated scripts
 - Removed use of `cactus` in the build and release process
 - Removed `cactus` repositories from the workspace
 - Removed use of super-poms and removed `telenav-superpom` from the workspace
 - Removed `kivakit-filesystem` from the workspace

-----------------------------------------------------------------------------------------------------------------------

## Version 1.8.0 (2022.10.05) "yellow sneakers"

### Major Review

Version 1.8 of KivaKit marks a major review and code stability release
in preparation for the 2.0 version, which will freeze the KivaKit API
and limit incompatible changes as much as possible. Numerous changes were
made in this version, but were limited primarily to name changes and fixes
for code that never worked correctly. Code annotated with @ApiQuality
expresses the evaluation.

```
@ApiQuality(stability = API_FURTHER_EVALUATION_NEEDED,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE,
            reviews = 1,
            reviewers = "shibo")
```

Much testing remains to be done, as indicated by @ApiQuality annotations
with testing marked as TESTING_INSUFFICIENT or TESTING_NONE.

The primary remaining code instability is the handling of OpenAPI / Swagger 
documentation in the kivakit-microservice project. This will be resolved
for the final 2.0 release, but will likely involve significant changes.

-----------------------------------------------------------------------------------------------------------------------

## Version 1.7.0 (2022.09.07) "yellow gorilla"

### Build

 - Major rework of the build system using Cactus
 - Rewrite of setup, development and release scripts
 - Added Zulip notifications of github operations, including builds

### Bugs

 - Invalid response JSON when response is null #141
 - KivaKit build fails due to Hadoop shading bug #125
 - Exception in kivakit merged gRPC #110
 - Failure gets dropped on floor during app initialization #108
 - NPE in CommandLineParser.exit() #107
 - Response is not a valid json #41

### Documentation

 - Moved all `Diagram*` lexakai annotations into `internal.lexakai` packages

### Core

 - Factored out `Joinable` interface
 - Added narrow log formatting (default) with `KIVAKIT_LOG_FORMATTER=wide` for wide logging format

### Network

 - Added `HttpMethod` enum
 - Added `PROGRAMMATIC` `asString(Format)` method to `Port`

### Web

 - Added cross-origin request (CORS) support to `JettyServer`

### Filesystems

 - Added `module-info.java` generation

### Microservice

 - Added cross-origin request (CORS) support
 - Switched to network `HttpMethod` enum
 - Renamed `Microservlet*` REST classes to `Rest*`
 - `MicroservletRequestHandler.prepareResponse()` -> `onPrepareResponse()`
 - `MicroservletResponse.prepareResponse()` -> `onPrepareResponse()`
 - `MicroservletResponse.endResponse()` -> `onEndResponse()`
 - Improvements to problem/status reporting
 - Fixed invalid JSON bracketing problem
 - Added `HttpStatus` to `MicroservletError`
 - Factored out `RestRequestCycle` interface
 - Factored out `RestRequestThread`
 - Return 200 when reporting errors rather than 500

-----------------------------------------------------------------------------------------------------------------------

## Version 1.6.1 (2022.06.30) "purple diorama"

### Build

 - New cactus version with new maven plugin
 - Updated build scripts for new cactus version
 - Updated superpoms

### Documentation

 - Fixed image links and improved documentation

### Core

 - Fixed bugs in ShutdownHook

-----------------------------------------------------------------------------------------------------------------------

## Version 1.6.0 (2022.06.15) "purple toaster"

### Build

 - Renamed cactus-build -> cactus
 - Added cactus-maven-plugin with mojos to support new build process
   - Added new Maven "mojos" for Lexakai and CodeFlowers
   - Moved most script logic into mojos
   - Added mojos for consistency checks and git operations
   - Introduced project families to allow builds of subsets of projects
   - Added cactus-maven-model project
 - Removed old scripts and centralized in telenav-build/bin
 - Refactored scripts to support new build process
 - Restructured superpom hierarchy and documented
 - Rewrote documentation for new build process
 - Changed maven plugin to write project and build properties to target folder
 - Cleaned up build output to improve readability

### Documentation

 - Moved all graphic assets from individual projects into telenav-assets and fixed links
 - Wrote documentation for new build processes
 - Fixed javadoc warnings

### Core

 - Refactored time classes
 - Bug fixes for Hour, Duration, Day, DayOfWeek, HourOfWeek
 - Extracted BaseTime superclass and removed dependency on Count
 - Added additional tests for time
 - Fixed potential locking bug in StateWatcher
 - Fixed null value bug in VariableMap
 - Improvements and fixes to Range
 - Fixed timezone bug in Time.elapsedSince()
 - Fixed potential CacheMap expiration bug
 - Fixed bug in KivaKitThread.startSynchronously()
 - KivaKitThread.waitFor() now returns WakeState
 - Changed resolution of time classes to nanoseconds and removed PreciseDuration
 - Added or*() methods to Maybe monad, added tests 
 - Improved serialization initialization
 - Added SetDifferencerTest and SetOperationTest

### Resources

 - Improved use of ResourceFolder
 - Added ResourceGlob

### Testing 

 - Moved all test code out of src/main/java into new test-scoped projects
 - Added tests

## Version 1.5.0 (2022.03.30) "cobalt penguin"

### Application

 - Added StartUp class for startup options
 - Improved start-up messages
   - Only show information when QUIET startup option not present
   - Include correct version and build information for loaded Projects
   - Correct application version information

### Build

 - Removed telenav.third.party from build and published to Maven Central
 - Code inspection and clean up
 - Fix warnings
 
### Collections

 - Fixed problem with tombstone identifiers in primitive map tests
 - Added Iterables.iterable(T[])
 - Added ObjectSet.sorted(), which produces an ObjectList

### Deployments

 - Generalized deployment loading using ResourceFolder

### Documentation

 - Added documentation for 

### Interfaces

 - Added Matchable
 - Matcher.anything() -> Matcher.matchAll()

### Microservlets

 - Added support for Result to BaseMicroservletResponse, and endResponse() to MicroservletResponse
 - Added GsonFactory.ignoreClass() and ignoreField()

### Packages

 - Fixed package resource regressions
 - Improved PackageTrait
 - Renamed old PackagePath class in kivakit core to PackageReference (which is only for use in kivakit-core)
 - Created new PackagePath class in kivakit-resource as subclass of ResourcePath

### Resources

 - Reorganized package structure to clarify code
 - Added resource creation time
 - Added ResourceGlob

### Settings 

 - Generalized folder and package settings stores into ResourceFolderSettingsStore
 
-----------------------------------------------------------------------------------------------------------------------

## Version 1.4.2 (2022.03.24) "cobalt penguin"

This release makes merged jars part of the main build.

-----------------------------------------------------------------------------------------------------------------------

## Version 1.4.1 (2022.03.23) "cobalt piglet"

This release includes small changes and improvements to existing code.

### build

- Improved docker build scripts
- Made build scripts consistent across repositories

### core

- Rewrote Range and Count hierarchy to fix generics and extract:
    - BaseCount
    - Arithmetic
    - Castable
    - IntegerNumeric
- Added LoopBody, FilteredLoopBody for use in unit test update
- Added BaseList/ObjectList.shuffle() methods
- Moved iteration interfaces from kivakit-core to kivakit-interfaces
- Added Maybe.then() arities, fixed parameter binding mistake

### debugging

- Added TripWireTrait

### testing

- Improved testing framework including UnitTest and RandomValue
- Improved documentation
- Created HourOfWeekTest and HourOfWeekSpanTest as examples
- Added test for Range

### security

- Upgraded to json-smart version 2.4.8 to address vulnerability

### time

- Added HourOfWeek and HourOfWeekSpan

-----------------------------------------------------------------------------------------------------------------------

## Version 1.4.0 (2022.03.14) "cobalt hamster"

This release was a major cleanup release and refactored and relocated code between the three repositories, _kivakit_, _kivakit-extensions_, and
_kivakit-stuff_, according to the principles described in the Medium article [*Open Source Repository Structure*](https://medium.com/@jonathanlocke/open-source-repository-structure-c1050d5840c6). See *restructuring* below for details.

### applications

* Applications now add project dependencies with addProject() instead of passing projects to the Application constructor

### collections

* CacheMap expiry time
* BaseList.replaceAll()
* Added BaseStringMap.asObject() methods
* Added LazyMap

### conversion

* Added converters
    * VariableMapConverter
    * ObjectConverter
    * BaseTwoWayConverter
* Added BaseStringMap.asObject() methods

### documentation

* improvements to documentation in key classes
* moved project.lexakai.* -> lexakai.*

### interfaces

* Added Presence
* Added LengthOfTime

### logging

* Removed remaining static LOGGER fields

### microservices

* Added -api-forwarding switch to enable easy backwards compatibility by forwarding requests to old API versions to child processes

### monads

* Added monads
    * Maybe
    * Result
* Added ResultTrait for convenience

### progress reporting

* Added ProgressiveIterable

### resources

* FilePath.last() now returns the last filename in the path, rather than the last element (which could be empty in the case where the path ends in a slash)

### restructuring

* Code was migrated between kivakit, kivakit-extensions and kivakit-stuff to ensure that only mature code is in kivakit and kivakit-extensions.
* _kivakit-kernel_ was broken into multiple projects to reduce dependencies and conceptual surface area.
    * kivakit-conversion
    * kivakit-core
    * kivakit-extraction
    * kivakit-interfaces
    * kivakit-mixins
    * kivakit-validation
* Code lacking maturity in _kivakit-extensions_ was moved:
    * kivakit-hdfs-filesystem (lacks test and has regressions)
    * kivakit-remote-log (presently broken)
    * kivakit-service (presently broken)
    * kivakit-primitive-collections (working, but only has one consumer)

### serialization

* The serialization abstraction was rewritten to provide cleaner structure and to support more future serialization providers. The ObjectReader, ObjectWriter, and ObjectSerializer interfaces give more control over this functionality than the prior idea of serialization sessions. In addition, the new SerializationSession abstraction works in a similar way to the previous code, with some simplifications and improvements. It is not anticipated that this code will need significant work like this again in the future, so it has been migrated into the *kivakit* repository.
* Created providers for serializing objects to:
    * .properties format
    * JSON
    * Kryo

### string

* Similarity -> StringSimilarity
* Message.format() -> Formatter.format()

### system

* Added SystemProperties
* Added Console.print*()
* Improved reflection code internals
* Move virtual reference code to kivakit-stuff

### traits

* Added traits:
    * JavaTrait
    * PackageTrait
    * RegistryTrait
    * ProjectTrait
    * SettingsTrait
    * LanguageTrait
        * TryTrait
        * ResultTrait

### values

* Moved *Matcher classes into methods in Matcher
* Added Primitives
* Added NameMixin
* AttributedMixin -> AttributesMixin

-----------------------------------------------------------------------------------------------------------------------

## Version 1.3.0 (2022.01.31) "mango beachball"

### todo

* Debug AWS Lambda request handling for 1.3.1 release

### security

* Upgraded protobuf to 3.18.2

### documentation

* Improved landing pages, documentation structure

### build

* Added docker build environment
* Added setup-repositories script

### dependencies

* Downgraded JUnit to 4.13.2

### microservices

* Microservice.onCreateWebApplication() -> onNewWebApplication()
* Fix problem where Zookeeper connection errors could cause early termination
* Add ready() signal after web server starts
* Fix MicroserviceCluster.join() so it won't attempt to join if there are no Zookeeper settings
* Add prepare() method so response can be prepared outside its constructor
* MicroservletRequestHandler.request() -> respond(), onRequest() -> onRespond()
* MicroservletRequestHandler.onRequestStatistics -> onRequestHandlingStatistics
* MicroservletRequestStatistics -> MicroservletRequestHandlingStatistics

### applications and settings

* Added code to handle exceptions thrown in onRun()
* Added Registry/RegistryTrait.unregisterAll()
* Fixed object registration bug in BaseSettingsStore, Settings
* Added SettingsStore.registerAllIn()
* Show contraction of folder when displaying command line

### messaging

* Added MessageAlarm API and EmailMessageAlarm

### utility

* Objects/LanguageTrait.notNullOr() -> ifNullDefault()
* Added LanguageTrait.ifNonNullApply(), isNonNullOr(), isFalseOr()

-----------------------------------------------------------------------------------------------------------------------

## Version 1.2.2 (2022.01.13) "mango duckling"

### security

* Upgraded log4j to 2.17.1
* Upgraded guava to 31.0.1-jre
* Upgraded commons-compress to 1.21
* Upgraded ow2.asm to 8.0.1
* Upgraded ow2.ow2 to 1.5.1

### documentation

* Microservices mini-framework diagram
* Javadoc improvements and fixes
* Documented TimeZones methods

### source control

* Added tagging to git-flow finish scripts

### microservices

* Fixed error serialization issue
* Fixed message capturing issues
* Added MicroserviceRestPath.version()
* Added MicroserviceRestService.mount(Version, String path, HttpMethod, Class<Request)
* Added support for Swagger tags
* Fixed handling of arrays in Swagger

### clustering

* Changed BaseSettingsStore.forceLoad() -> reload(), prevent reentrancy to reload()
* Excluded reentrancy of BaseSettingsStore.reload()
* Fixed issues in MicroserviceCluster tracking, allowing Zookeeper to go up or down without errors
* Changed return value of ReentrancyTracker.enter() to be true if the code can be entered (no reentrancy)
* Added MicroserviceCluster.leader() method
* Added Microservice.leader() convenience method
* Optimized reloads of member data in MicroserviceCluster
* Changed ZookeeperSettingsStore.readSettings() -> loadSettings()

### collections

* Added BaseSet.addAllMatching(Collection, Matcher)
* Added ObjectSet.matching(Matcher)
* Added Sets.matching(Set, Matcher)
* Improved BaseList constructor
* Added ObjectList.objectListFromArray(long[] objects)

### utility

* Changed Ints.isBetween() -> isBetweenExclusive(), isBetweenInclusive()
* Added Member.arrayElementType()
* Added validator parent chaining
* Added validate(Validatable, ValidationType) method
* Fixed issue with null Validatables and Validators
* Added LocalTime.hourOfWeek()
* Added TimeSpan.future(Duration)
* Added TimeSpan.past(Duration)
* Changed DayOfWeek.jodaTimeConstant() -> asJavaConstant()
* Added TimeZones.isUtc(ZoneId)
* Added TimeZones.isValidShortDisplayName(String)
* Added TimeZones.parseShortDisplayName(Listener, String)
* Added TimeZones.parseZoneId(Listener, String)
* Added TimeZones.parseZoneIdOrDisplayName(Listener, String)
* Changed TimeZones.displayName(ZoneId) -> shortDisplayName(ZoneId)

-----------------------------------------------------------------------------------------------------------------------

## Version 1.2.1 (2021.12.14) "plutonium goldfish"

### lambdas

* Added *MicroserviceLambdaService* with Lambda mounting

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

-----------------------------------------------------------------------------------------------------------------------

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

-----------------------------------------------------------------------------------------------------------------------

## Version 1.1.1 (2021.11.05) "plutonium panda"

### added

* [Add .proto output files to kivakit-microservice GRPC service](https://github.com/Telenav/kivakit/issues/89)

### fixed

* [Division microservice example is broken #87](https://github.com/Telenav/kivakit/issues/87)
* [Broken listener chain due to inheritance from mixin and base component #86](https://github.com/Telenav/kivakit/issues/86)
* [Documentation links are broken in kivakit 1.1.0 #88](https://github.com/Telenav/kivakit/issues/88)

-----------------------------------------------------------------------------------------------------------------------

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
* Static methods returning broadcasting components now take a Listener as the first argument. This prevents callers from accidentally forgetting to *listenTo()* the component.
* Maven pom files now inherit from a "superpom"
* Divided Component interface into traits:
    * RegistryTrait
    * SettingsTrait
    * ResourceTrait
    * TryTrait

### fixed

* Fixes to build and deployment scripts
* Many small bug fixes

-----------------------------------------------------------------------------------------------------------------------

## Version 1.0.3 (2021.08.27) "puffy mouse"

Initial release of KivaKit.
