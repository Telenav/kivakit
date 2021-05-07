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

# kivakit-core collections &nbsp;&nbsp; <img src="https://www.kivakit.org/images/set-32.png" srcset="https://www.kivakit.org/images/set-32-2x.png 2x"/>

This module provides collections, iteration support, stacks, collection observation and bit I/O.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Batcher**](#batcher)  
[**Iterables and Iterators**](#iterables-and-iterators)  
[**Maps**](#maps)  
[**Sets**](#sets)  
[**Stack**](#stack)  
[**Collection Watching**](#collection-watching)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-collections</artifactId>
        <version>0.9.5-alpha-SNAPSHOT</version>
    </dependency>


<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Batcher <a name = "batcher"></a>

The Batcher class combines an ArrayBlockingQueue with an ExecutorService to create a batch-processor which
aggregates elements in batches and processes them with a set of worker threads. A batcher is created like this, passing in the maximum
number of batches in the queue and the size of each batch:

    Batcher<Record> batcher = new Batcher<>("RecordBatcher", Maximum._16, Count.16_384)
    {
        protected void onBatch(final Batch batch)
        {
            for (var record : batch)
            {
                [...]
            }
        }
    };

The batcher lifecycle then looks like this:

    batcher.start(Count._8);

        [...]

    batcher.adder().add(record);

        [...]

    batcher.close();

### Iterables and Iterators <a name = "iterables-and-iterators"></a>

Several implementations of the Java *Iterable* and *Iterator* interfaces are available:

- *DeduplicatingIterable* / *DeduplicatingIterator* - Wraps an *Iterable* or *Iterator* an iterates through
  all elements, keeping a set of elements that have been seen. Elements that have already been seen
  are skipped.
- *FilteredIterable* / *FilteredIterator* - Wraps an *Iterable* or *Iterator*, returning only elements that
  match the given Matcher.
- *CompoundIterator* - Combines a sequence of iterators into a single iterator
- *EmptyIterator* - An *Iterator* with no elements
- *SingletonIterator* - An *Iterator* with a single element

### Maps <a name = "maps"></a>

The available implementations of the *Map* interface in *kivakit-core-collections* extend the abstract base class,
*BaseMap*, from *kivakit-core-kernel*:

- *CacheMap* - A most-recently-used (MRU) map which caches elements up to a maximum capacity
- *CaseFoldingStringMap* - A *StringMap* implementation which is case-independent
- *LinkedMap* - A map that uses LinkedHashMap as its implementation
- *ReferenceCountMap* - A map that counts the number of references to its key type
- *TwoWayMap* - A map that can also map from value back to key
- *MultiMap* - A map from a single key to a list of values
- *MultiSet* - A map from a single key to a set of values

### Sets <a name = "sets"></a>

The *set* package provides several useful implementations of *BaseSet* as well as a set differencer that
determines what changes are required to turn one set into another.

- *CompoundSet* - A set of sets that logically combines the sets without creating a new set
- *ConcurrentHashSet* - A hash set implementation that is thread-safe
- *IdentitySet* - A set based on reference and not the *hashCode()* / *equals()* contract
- *LogicalSet* - An operation applied to two sets that logically combines the sets without actually
  combining the sets. Implementations include *Intersection*, *Subset*, *Union* and *Without*
- *SetDifferencer* - Determines what changes are required to turn one set into another

### Stack <a name = "stack"></a>

The *Stack* class is a subclass of *ObjectList* which adds push() and pop() methods.

### Collection Watching <a name = "collection-watching"></a>

The *watcher* package contains a *BaseCollectionChangeWatcher* for observing changes to collections of
objects (for example, a set of *File* objects). This base is extended by *PeriodicCollectionChangeWatcher*
which observes a given collection at a set *Frequency*. When the collection of objects changes, one or
more methods in *CollectionChangeListener* is called with information about what changed.

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*Batch Processing*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-batch-processing.svg)  
[*Collection Watching*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-watcher.svg)  
[*Iteration and Iterables*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-iteration.svg)  
[*Maps*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-map.svg)  
[*Sets*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-set.svg)  
[*Stacks*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-stack.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.core.collections.batcher*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.batcher.svg)  
[*com.telenav.kivakit.core.collections.iteration.iterables*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.iteration.iterables.svg)  
[*com.telenav.kivakit.core.collections.iteration.iterators*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.iteration.iterators.svg)  
[*com.telenav.kivakit.core.collections.map*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.map.svg)  
[*com.telenav.kivakit.core.collections.project*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.project.svg)  
[*com.telenav.kivakit.core.collections.set*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.set.svg)  
[*com.telenav.kivakit.core.collections.set.logical*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.set.logical.svg)  
[*com.telenav.kivakit.core.collections.set.logical.operations*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.set.logical.operations.svg)  
[*com.telenav.kivakit.core.collections.stack*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.stack.svg)  
[*com.telenav.kivakit.core.collections.watcher*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.watcher.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 80.3%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-80-96.png" srcset="https://www.kivakit.org/images/meter-80-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*BaseCollectionChangeWatcher*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/BaseCollectionChangeWatcher.html) |  |  
| [*Batcher*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.html) | Example |  
| | Adding Elements |  
| | Processing Elements |  
| [*Batcher.Batch*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.Batch.html) |  |  
| [*Batcher.BatchAdder*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.BatchAdder.html) |  |  
| [*CacheMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/CacheMap.html) |  |  
| [*CaseFoldingStringMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/CaseFoldingStringMap.html) |  |  
| [*CollectionChangeListener*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/CollectionChangeListener.html) |  |  
| [*CollectionChangeWatcher*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/CollectionChangeWatcher.html) |  |  
| [*CompoundIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/CompoundIterator.html) |  |  
| [*CompoundSet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/CompoundSet.html) |  |  
| [*ConcurrentHashSet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/ConcurrentHashSet.html) |  |  
| [*CoreCollectionsProject*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/project/CoreCollectionsProject.html) |  |  
| [*CoreCollectionsUnitTest*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/project/CoreCollectionsUnitTest.html) |  |  
| [*DeduplicatingIterable*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterables/DeduplicatingIterable.html) |  |  
| [*DeduplicatingIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/DeduplicatingIterator.html) |  |  
| [*EmptyIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/EmptyIterator.html) |  |  
| [*FilteredIterable*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterables/FilteredIterable.html) |  |  
| [*FilteredIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/FilteredIterator.html) |  |  
| [*IdentitySet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/IdentitySet.html) |  |  
| [*Intersection*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Intersection.html) |  |  
| [*LinkedMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/LinkedMap.html) |  |  
| [*LogicalSet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/LogicalSet.html) |  |  
| [*MultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/MultiMap.html) |  |  
| [*MultiSet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/MultiSet.html) |  |  
| [*PeriodicCollectionChangeWatcher*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/PeriodicCollectionChangeWatcher.html) |  |  
| [*ReferenceCountMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/ReferenceCountMap.html) |  |  
| [*SetDifferencer*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/SetDifferencer.html) |  |  
| [*SingletonIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/SingletonIterator.html) |  |  
| [*Stack*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/stack/Stack.html) |  |  
| [*Subset*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Subset.html) |  |  
| [*TwoWayMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/TwoWayMap.html) |  |  
| [*Union*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Union.html) |  |  
| [*Without*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Without.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai). UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

