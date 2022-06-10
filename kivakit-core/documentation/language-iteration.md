# kivakit-core-kernel language.iteration &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/footprints-40.png)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.language.iteration*

### Index

[**Summary**](#summary)  
[**Implementing Iterators**](#implementing-iterators)  
[**Implementing Iterables**](#implementing-iterables)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

The *BaseIterator* and *BaseIterable* classes are a convenient way to fulfill the *Iterator* and *Iterable* contracts  
with a minimum of effort. In addition to creating subclasses of these base classes, it may be convenient to  
create iterators and iterables as anonymous subclasses. In some cases, *Iterables.iterable(Factory<Next<T>>)*   
and *Iterator.iterators(Supplier<T>)* can be used to create iterables with only a lambda function.

### Implementing Iterators <a name="implementing-iterators"></a>

The base class *BaseIterator* makes it easy to implement an iterator with just one method. A simple example:

    public class IntegerIterator<Element> extends BaseIterator<Element>
    {
        private Integer[] values;

        private int index;

        public IntegerIterator(Integer[] values)
        {
            this.values = values;
        }

        @Override
        protected Element onNext()
        {
            return index < values.length ? values[index++] : null;
        }
    }

Iteration continues until null is returned, when the index reaches the end of the array. It is also possible  
to implement this with a closure (the only trick is that index has to be effectively final within the closure):

    private int index;

    Iterators.iterator(() -> index < values.length ? values[index++] : null);

### Implementing Iterables <a name="implementing-iterables"></a>

*Iterable*s can be implemented by extending *BaseIterator*. A good example of this is *FilteredIterable*, which  
wraps an *Iterable* and applies a *Matcher* as an element filter. It looks like this:

    public class FilteredIterable<Element> extends BaseIterable<Element>
    {
        private Matcher<Element> filter;
    
        private Iterable<Element> iterable;
    
        public FilteredIterable Iterable<Element> iterable, Matcher<Element> filter)
        {
            this.iterable = iterable;
            this.filter = filter;
        }
    
        @Override
        protected Next<Element> newNext()
        {
            return new Next<>()
            {
                private final Iterator<Element> iterator = iterable.iterator();
    
                @Override
                public Element onNext()
                {
                    while (iterator.hasNext())
                    {
                        final var next = iterator.next();
                        if (filter.matches(next))
                        {
                            return next;
                        }
                    }
                    return null;
                }
            };
        }
    } 

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
