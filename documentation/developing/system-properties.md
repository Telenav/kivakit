# KivaKit - System Properties   <img src="https://www.kivakit.org/images/gears-32.png" srcset="https://www.kivakit.org/images/gears-32-2x.png 2x"/>

### Summary

One or more system properties can be defined in Java by passing switch(es) of the form **\-D***key*=*value* to the JVM.

The following properties affect the function of KivaKit in useful ways.

| System Property | Description |
|-----------------|-------------|
| **KIVAKIT_AGENT_JAR** | Location of KivaKit instrumentation agent JAR file |
| **KIVAKIT_DEBUG** | Turns on/off debug traces for packages and classes matching a series of one or more patterns. See [kivakit-core debugging](../../../../kivakit/kivakit-core/kernel/documentation/messaging-debugging.md) and Javadoc in Debug.java
| **KIVAKIT_HDFS_CONFIGURATION_FOLDER** | Location of configuration folder for HDFS filesystem service |
| **KIVAKIT_KEEP_TEMPORARY_FILES** | Stops temporary files created by KivaKit from being removed on exit. |
| **KIVAKIT_LOG** | A comma-separated list of logs to send log entries to along with their configurations. See [kivakit-core logging](../../../../kivakit/kivakit-core/kernel/documentation/logging.md) |
| **KIVAKIT_LOG_ALLOCATIONS** | Set to true to log allocations of primitive collections and other KivaKit objects |
| **KIVAKIT_LOG_ALLOCATIONS_MINIMUM_SIZE** | The minimum allocation size for debug tracing of kivakit-core primitive collections. Defaults to 64K. |
| **KIVAKIT_LOG_ALLOCATION_STACK_TRACES** | Includes a stack trace in allocation logging. This property is true by default. |
| **KIVAKIT_LOG_LEVEL** | Restricts the output to a entries of the given and higher severities. See [kivakit-core logging](../../../../kivakit/kivakit-core/kernel/documentation/logging.md) |
| **KIVAKIT_LOG_SERVER_PORT** | The port where a remote server has interactive log information. If this value is set, the client and serve must choose the same port to communicate. If this property isn't set, the default is port 7531. |
| **KIVAKIT_LOG_SYNCHRONOUS** | KivaKit logs are asynchronous by default for performance reasons. Sometimes when debugging it's important to be able to turn this off. By setting this property to "true", logging will be done in a blocking fashion instead. |
| **KIVAKIT_NETWORK_SERVICE_REGISTRY_PORT** | Host and port of network service registry |
| **KIVAKIT_SERIALIZATION_TRACE** | Turns on serialization tracing |
| **KIVAKIT_SIMPLIFIED_STACK_TRACES** | Set to true to enable stack trace simplification |
| **KIVAKIT_TEMPORARY_FOLDERS** | A path of folders to search for a place with room for a temporary folder |
