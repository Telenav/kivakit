# kivakit-core-kernel project &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/gears-40.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.project*

### Index

[**Summary**](#summary)  
[**Dependencies**](#dependencies)  
[**Initialization**](#initialization)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

Many KivaKit projects have a _Project_ subclass that defines important aspects of the project. These  
project definitions will always end with "Project" like *CoreKernelProject*, so they are easy to spot.  
Each such project subclasses _Project_ and overrides its abstract methods:

    public abstract class Project
    {
        public abstract Set<Project> dependencies();
        public abstract void onInitialize();
        public abstract void onRegister( Kryo kryo);
    }

### Dependencies <a name="dependencies"></a> &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/dependencies-40.png)

The *dependencies()* method returns the set of all projects that this project depends on.

Here we show a hierarchical tree of dependencies by visiting each dependency of the project  
*KivaGraphCore*:

    KivaGraphCore.get().visitDependencies((dependency, level)
        -> System.out.println(Strings.repeat(level, "  ") + dependency.name()));

This code produces the output:

    KivaGraphCore
      KivaGraphTraffic
        KivaMapGeography
          KivaMapMeasurements
            KivaCore
      KivaMapRegion
        KivaMapGeography
          KivaMapMeasurements
            KivaCore
        KivaUtilitiesNetworkCore
          KivaCore
      KivaMapUtilities
        KivaMapGeography
          KivaMapMeasurements
            KivaCore

### Initialization <a name="initialization"></a> &nbsp; &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/wand-40.png)

The *onInitialize()* method gives a project a chance to initialize when you call the '*initialize()*'  
method to begin using the *Project*.

You don't need to initialize a project's dependencies to use it, only the project itself.  
KivaKit will initialize all the dependencies for you.

_Before using a KivaKit project, you must call the '*initialize()*' method on the project._

    // Initializes everything you need to initialize to use the Graph API, including
    // KivaMapRegion, KivaMapGeography, KivaMapUtilities, KivaGraphTraffic, KivaCore, ...
    KivaGraphCore.get().initialize();

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
