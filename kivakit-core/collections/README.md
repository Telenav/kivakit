# kivakit-core collections &nbsp;&nbsp; <img src="https://www.kivakit.org/images/set-40.png" srcset="https://www.kivakit.org/images/set-40-2x.png 2x"/>

This module provides collections, iteration support, primitive collections, stacks, collection observation and bit I/O.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Batcher**](#batcher)  
[**Iterables and Iterators**](#iterables-and-iterators)  
[**Maps**](#maps)  
[**Sets**](#sets)  
[**Stack**](#stack)  
[**Collection Watching**](#collection-watching)  
[**Primitive Collections**](#primitive-collections)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-collections</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>


<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*Batch Processing*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-batch-processing.svg)  
[*Collection Watching*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-watcher.svg)  
[*Iteration and Iterables*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-iteration.svg)  
[*Maps*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-map.svg)  
[*Primitive Arrays*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-primitive-array.svg)  
[*Primitive Bit I/O*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-primitive-array-bit-io.svg)  
[*Primitive Collections*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-primitive-collection.svg)  
[*Primitive Lists*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-primitive-list.svg)  
[*Primitive Maps*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-primitive-map.svg)  
[*Primitive Multi-Maps*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-primitive-multi-map.svg)  
[*Primitive Sets*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-primitive-set.svg)  
[*Sets*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-set.svg)  
[*Split Primitive Arrays*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-primitive-split-array.svg)  
[*Stacks*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-stack.svg)  
[*Two-Dimensional Arrays*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/diagram-primitive-array-array.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.core.collections.batcher*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.batcher.svg)  
[*com.telenav.kivakit.core.collections.iteration.iterables*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.iteration.iterables.svg)  
[*com.telenav.kivakit.core.collections.iteration.iterators*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.iteration.iterators.svg)  
[*com.telenav.kivakit.core.collections.map*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.map.svg)  
[*com.telenav.kivakit.core.collections.primitive*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.svg)  
[*com.telenav.kivakit.core.collections.primitive.array*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.arrays*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.arrays.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.bits*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.bits.io*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.io.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.bits.io.input*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.io.input.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.bits.io.output*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.io.output.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.packed*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.packed.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.scalars*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.scalars.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.strings*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.strings.svg)  
[*com.telenav.kivakit.core.collections.primitive.iteration*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.iteration.svg)  
[*com.telenav.kivakit.core.collections.primitive.list*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.list.svg)  
[*com.telenav.kivakit.core.collections.primitive.list.store*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.list.store.svg)  
[*com.telenav.kivakit.core.collections.primitive.map*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.multi*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.multi.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.multi.dynamic*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.multi.dynamic.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.multi.fixed*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.multi.fixed.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.objects*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.objects.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.scalars*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.scalars.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.split*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.split.svg)  
[*com.telenav.kivakit.core.collections.primitive.set*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.primitive.set.svg)  
[*com.telenav.kivakit.core.collections.project*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.project.svg)  
[*com.telenav.kivakit.core.collections.set*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.set.svg)  
[*com.telenav.kivakit.core.collections.set.logical*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.set.logical.svg)  
[*com.telenav.kivakit.core.collections.set.logical.operations*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.set.logical.operations.svg)  
[*com.telenav.kivakit.core.collections.stack*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.stack.svg)  
[*com.telenav.kivakit.core.collections.watcher*](https://www.kivakit.org/lexakai/kivakit/kivakit-core/collections/documentation/diagrams/com.telenav.kivakit.core.collections.watcher.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 70.4%.  
  
&nbsp; &nbsp;  ![](https://www.kivakit.org/images/meter-70-12.png)

The following significant classes are undocumented:  

- BaseBitReader  
- BaseBitWriter  
- BitArray  
- LongToIntMultiMap  
- LongToLongMultiMap  
- LongToObjectMap  
- PrimitiveMap

| Class | Documentation Sections |
|---|---|
| [*BaseBitReader*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/input/BaseBitReader.html) |  |  
| [*BaseBitWriter*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/output/BaseBitWriter.html) |  |  
| [*BaseCollectionChangeWatcher*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/BaseCollectionChangeWatcher.html) |  |  
| [*Batcher*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.html) | Example |  
| | Adding Elements |  
| | Processing Elements |  
| [*Batcher.Batch*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.Batch.html) |  |  
| [*Batcher.BatchAdder*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.BatchAdder.html) |  |  
| [*BigSplitPackedArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/BigSplitPackedArray.html) |  |  
| [*BitArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/BitArray.html) |  |  
| [*BitInput*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/input/BitInput.html) |  |  
| [*BitOutput*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/output/BitOutput.html) |  |  
| [*BitReader*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/BitReader.html) |  |  
| [*BitWriter*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/BitWriter.html) |  |  
| [*ByteArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/ByteArray.html) |  |  
| [*ByteArrayArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/arrays/ByteArrayArray.html) |  |  
| [*ByteCollection*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/ByteCollection.html) |  |  
| [*ByteIterable*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ByteIterable.html) |  |  
| [*ByteIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ByteIterator.html) |  |  
| [*ByteList*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/ByteList.html) |  |  
| [*CacheMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/CacheMap.html) |  |  
| [*CaseFoldingStringMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/CaseFoldingStringMap.html) |  |  
| [*CharArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/CharArray.html) |  |  
| [*CharArray.Converter*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/CharArray.Converter.html) |  |  
| [*CharCollection*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/CharCollection.html) |  |  
| [*CharIterable*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/CharIterable.html) |  |  
| [*CharIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/CharIterator.html) |  |  
| [*CharList*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/CharList.html) |  |  
| [*CollectionChangeListener*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/CollectionChangeListener.html) |  |  
| [*CollectionChangeWatcher*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/CollectionChangeWatcher.html) |  |  
| [*CompoundIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/CompoundIterator.html) |  |  
| [*CompoundSet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/CompoundSet.html) |  |  
| [*ConcurrentHashSet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/ConcurrentHashSet.html) |  |  
| [*CoreCollectionsKryoTypes*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/project/CoreCollectionsKryoTypes.html) |  |  
| [*CoreCollectionsProject*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/project/CoreCollectionsProject.html) |  |  
| [*CoreCollectionsUnitTest*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/project/CoreCollectionsUnitTest.html) |  |  
| [*DeduplicatingIterable*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterables/DeduplicatingIterable.html) |  |  
| [*DeduplicatingIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/DeduplicatingIterator.html) |  |  
| [*DefaultHashingStrategy*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/DefaultHashingStrategy.html) |  |  
| [*EmptyIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/EmptyIterator.html) |  |  
| [*FilteredIterable*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterables/FilteredIterable.html) |  |  
| [*FilteredIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/FilteredIterator.html) |  |  
| [*FixedSizeBitArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/FixedSizeBitArray.html) |  |  
| [*HashingStrategy*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/HashingStrategy.html) |  |  
| [*IdentitySet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/IdentitySet.html) |  |  
| [*IntArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/IntArray.html) |  |  
| [*IntArray.Converter*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/IntArray.Converter.html) |  |  
| [*IntArrayArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/arrays/IntArrayArray.html) |  |  
| [*IntCollection*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/IntCollection.html) |  |  
| [*IntIterable*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/IntIterable.html) |  |  
| [*IntIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/IntIterator.html) |  |  
| [*IntLinkedListStore*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/IntLinkedListStore.html) |  |  
| [*IntList*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/IntList.html) |  |  
| [*IntMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/IntMultiMap.html) |  |  
| [*IntToByteFixedMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToByteFixedMultiMap.html) |  |  
| [*IntToByteMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToByteMap.html) |  |  
| [*IntToByteMap.EntryVisitor*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToByteMap.EntryVisitor.html) |  |  
| [*IntToIntFixedMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToIntFixedMultiMap.html) |  |  
| [*IntToIntMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToIntMap.html) |  |  
| [*IntToIntMap.EntryVisitor*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToIntMap.EntryVisitor.html) |  |  
| [*IntToLongFixedMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToLongFixedMultiMap.html) |  |  
| [*IntToLongMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToLongMap.html) |  |  
| [*IntToLongMap.EntryVisitor*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToLongMap.EntryVisitor.html) |  |  
| [*IntToPackedArrayFixedMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToPackedArrayFixedMultiMap.html) |  |  
| [*Intersection*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Intersection.html) |  |  
| [*LinkedMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/LinkedMap.html) |  |  
| [*LogicalSet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/LogicalSet.html) |  |  
| [*LongArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/LongArray.html) |  |  
| [*LongArray.Converter*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/LongArray.Converter.html) |  |  
| [*LongArrayArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/arrays/LongArrayArray.html) |  |  
| [*LongCollection*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/LongCollection.html) |  |  
| [*LongIterable*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/LongIterable.html) |  |  
| [*LongIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/LongIterator.html) |  |  
| [*LongLinkedListStore*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/LongLinkedListStore.html) |  |  
| [*LongList*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/LongList.html) |  |  
| [*LongMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/LongMultiMap.html) |  |  
| [*LongSet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/LongSet.html) |  |  
| [*LongToByteFixedMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/LongToByteFixedMultiMap.html) |  |  
| [*LongToByteMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToByteMap.html) |  |  
| [*LongToByteMap.EntryVisitor*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToByteMap.EntryVisitor.html) |  |  
| [*LongToIntFixedMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/LongToIntFixedMultiMap.html) |  |  
| [*LongToIntMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToIntMap.html) |  |  
| [*LongToIntMap.EntryVisitor*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToIntMap.EntryVisitor.html) |  |  
| [*LongToIntMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/dynamic/LongToIntMultiMap.html) |  |  
| [*LongToLongFixedMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/LongToLongFixedMultiMap.html) |  |  
| [*LongToLongMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToLongMap.html) |  |  
| [*LongToLongMap.EntryVisitor*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToLongMap.EntryVisitor.html) |  |  
| [*LongToLongMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/dynamic/LongToLongMultiMap.html) |  |  
| [*LongToObjectMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/objects/LongToObjectMap.html) |  |  
| [*MultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/MultiMap.html) |  |  
| [*MultiSet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/MultiSet.html) |  |  
| [*PackedArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/PackedArray.html) |  |  
| [*PackedPrimitiveArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/PackedPrimitiveArray.html) |  |  
| [*PackedPrimitiveArray.OverflowHandling*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/PackedPrimitiveArray.OverflowHandling.html) |  |  
| [*PackedStringArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/strings/PackedStringArray.html) |  |  
| [*PackedStringArray.Type*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/strings/PackedStringArray.Type.html) |  |  
| [*PackedStringStore*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/PackedStringStore.html) |  |  
| [*PeriodicCollectionChangeWatcher*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/PeriodicCollectionChangeWatcher.html) |  |  
| [*PrimitiveArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/PrimitiveArray.html) |  |  
| [*PrimitiveArrayArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/PrimitiveArrayArray.html) |  |  
| [*PrimitiveCollection*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.html) |  |  
| [*PrimitiveCollection.AllocationStackTrace*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.AllocationStackTrace.html) |  |  
| [*PrimitiveCollection.CompressionRecord*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.CompressionRecord.html) |  |  
| [*PrimitiveCollection.IndexedToString*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.IndexedToString.html) |  |  
| [*PrimitiveIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/PrimitiveIterator.html) |  |  
| [*PrimitiveList*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/PrimitiveList.html) |  |  
| [*PrimitiveListStore*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/PrimitiveListStore.html) |  |  
| [*PrimitiveMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/PrimitiveMap.html) |  |  
| [*PrimitiveMap.MapToString*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/PrimitiveMap.MapToString.html) |  |  
| [*PrimitiveMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/PrimitiveMultiMap.html) |  |  
| [*PrimitiveMultiMap.MultiMapToString*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/PrimitiveMultiMap.MultiMapToString.html) |  |  
| [*PrimitiveScalarMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/PrimitiveScalarMap.html) |  |  
| [*PrimitiveScalarMultiMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/PrimitiveScalarMultiMap.html) |  |  
| [*PrimitiveSet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/PrimitiveSet.html) |  |  
| [*PrimitiveSet.SetToString*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/PrimitiveSet.SetToString.html) |  |  
| [*PrimitiveSplitArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/PrimitiveSplitArray.html) |  |  
| [*ReferenceCountMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/ReferenceCountMap.html) |  |  
| [*SetDifferencer*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/SetDifferencer.html) |  |  
| [*ShortArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/ShortArray.html) |  |  
| [*ShortArray.Converter*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/ShortArray.Converter.html) |  |  
| [*ShortCollection*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/ShortCollection.html) |  |  
| [*ShortIterable*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ShortIterable.html) |  |  
| [*ShortIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ShortIterator.html) |  |  
| [*ShortList*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/ShortList.html) |  |  
| [*SingletonIterator*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/SingletonIterator.html) |  |  
| [*SplitByteArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitByteArray.html) |  |  
| [*SplitCharArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitCharArray.html) |  |  
| [*SplitIntArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitIntArray.html) |  |  
| [*SplitIntToIntMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitIntToIntMap.html) |  |  
| [*SplitLongArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitLongArray.html) |  |  
| [*SplitLongSet*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/SplitLongSet.html) |  |  
| [*SplitLongToByteMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitLongToByteMap.html) |  |  
| [*SplitLongToIntMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitLongToIntMap.html) |  |  
| [*SplitLongToLongMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitLongToLongMap.html) |  |  
| [*SplitPackedArray*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/SplitPackedArray.html) |  |  
| [*SplitPrimitiveMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/SplitPrimitiveMap.html) |  |  
| [*Stack*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/stack/Stack.html) |  |  
| [*StringToIntMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/StringToIntMap.html) |  |  
| [*StringToObjectMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/StringToObjectMap.html) |  |  
| [*Subset*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Subset.html) |  |  
| [*TwoWayMap*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/map/TwoWayMap.html) |  |  
| [*Union*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Union.html) |  |  
| [*VariableReadSizeBitInput*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/input/VariableReadSizeBitInput.html) |  |  
| [*Without*](https://www.kivakit.org/javadoc/kivakit/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Without.html) |  |  

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

### Primitive Collections <a name = "primitive-collections"></a>

The *primitive* package contains an extensive set of classes for storing data in primitive data structures, including:

- Dynamic primitive arrays (1 and 2 dimensional)
- Primitive lists, maps, sets and multi-maps
- Bit arrays and bitwise I/O
- Bit-packed arrays
- *Split* versions of many data structures which virtualize several primitive data structures into a single
  large one. This can be beneficial in keeping object allocation and garbage collection under control.

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai). UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

