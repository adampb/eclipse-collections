/*
 * Copyright (c) 2015 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.set.mutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.function.primitive.BooleanFunction;
import org.eclipse.collections.api.block.function.primitive.ByteFunction;
import org.eclipse.collections.api.block.function.primitive.CharFunction;
import org.eclipse.collections.api.block.function.primitive.DoubleFunction;
import org.eclipse.collections.api.block.function.primitive.FloatFunction;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.api.block.function.primitive.ShortFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.api.ordered.OrderedIterable;
import org.eclipse.collections.api.partition.set.PartitionMutableSet;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.ParallelUnsortedSetIterable;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.api.set.UnsortedSetIterable;
import org.eclipse.collections.api.set.primitive.MutableBooleanSet;
import org.eclipse.collections.api.set.primitive.MutableByteSet;
import org.eclipse.collections.api.set.primitive.MutableCharSet;
import org.eclipse.collections.api.set.primitive.MutableDoubleSet;
import org.eclipse.collections.api.set.primitive.MutableFloatSet;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.api.set.primitive.MutableLongSet;
import org.eclipse.collections.api.set.primitive.MutableShortSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.collection.mutable.AbstractSynchronizedMutableCollection;
import org.eclipse.collections.impl.collection.mutable.SynchronizedCollectionSerializationProxy;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.lazy.parallel.set.SynchronizedParallelUnsortedSetIterable;

/**
 * A synchronized view of a {@link MutableSet}. It is imperative that the user manually synchronize on the collection when iterating over it using the
 * standard JDK iterator or JDK 5 for loop, as per {@link Collections#synchronizedCollection(Collection)}.
 *
 * @see MutableSet#asSynchronized()
 */
@ThreadSafe
public class SynchronizedMutableSet<T>
        extends AbstractSynchronizedMutableCollection<T>
        implements MutableSet<T>, Serializable
{
    private static final long serialVersionUID = 2L;

    SynchronizedMutableSet(MutableSet<T> set)
    {
        super(set);
    }

    SynchronizedMutableSet(MutableSet<T> set, Object newLock)
    {
        super(set, newLock);
    }

    /**
     * This method will take a MutableSet and wrap it directly in a SynchronizedMutableSet.  It will
     * take any other non-GS-collection and first adapt it will a SetAdapter, and then return a
     * SynchronizedMutableSet that wraps the adapter.
     */
    public static <E, S extends Set<E>> SynchronizedMutableSet<E> of(S set)
    {
        return new SynchronizedMutableSet<E>(SetAdapter.adapt(set));
    }

    /**
     * This method will take a MutableSet and wrap it directly in a SynchronizedMutableSet.  It will
     * take any other non-GS-collection and first adapt it will a SetAdapter, and then return a
     * SynchronizedMutableSet that wraps the adapter.  Additionally, a developer specifies which lock to use
     * with the collection.
     */
    public static <E, S extends Set<E>> SynchronizedMutableSet<E> of(S set, Object lock)
    {
        return new SynchronizedMutableSet<E>(SetAdapter.adapt(set), lock);
    }

    @GuardedBy("getLock()")
    private MutableSet<T> getMutableSet()
    {
        return (MutableSet<T>) this.getDelegate();
    }

    public MutableSet<T> with(T element)
    {
        this.add(element);
        return this;
    }

    public MutableSet<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public MutableSet<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    public MutableSet<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    public MutableSet<T> newEmpty()
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().newEmpty().asSynchronized();
        }
    }

    @Override
    public MutableSet<T> clone()
    {
        synchronized (this.getLock())
        {
            return SynchronizedMutableSet.of(this.getMutableSet().clone());
        }
    }

    protected Object writeReplace()
    {
        return new SynchronizedCollectionSerializationProxy<T>(this.getMutableSet());
    }

    public MutableSet<T> tap(Procedure<? super T> procedure)
    {
        synchronized (this.getLock())
        {
            this.forEach(procedure);
            return this;
        }
    }

    public MutableSet<T> select(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().select(predicate);
        }
    }

    public <P> MutableSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().selectWith(predicate, parameter);
        }
    }

    public MutableSet<T> reject(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().reject(predicate);
        }
    }

    public <P> MutableSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().rejectWith(predicate, parameter);
        }
    }

    public PartitionMutableSet<T> partition(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().partition(predicate);
        }
    }

    public <P> PartitionMutableSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().partitionWith(predicate, parameter);
        }
    }

    public MutableBooleanSet collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().collectBoolean(booleanFunction);
        }
    }

    public MutableByteSet collectByte(ByteFunction<? super T> byteFunction)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().collectByte(byteFunction);
        }
    }

    public MutableCharSet collectChar(CharFunction<? super T> charFunction)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().collectChar(charFunction);
        }
    }

    public MutableDoubleSet collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().collectDouble(doubleFunction);
        }
    }

    public MutableFloatSet collectFloat(FloatFunction<? super T> floatFunction)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().collectFloat(floatFunction);
        }
    }

    public MutableIntSet collectInt(IntFunction<? super T> intFunction)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().collectInt(intFunction);
        }
    }

    public MutableLongSet collectLong(LongFunction<? super T> longFunction)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().collectLong(longFunction);
        }
    }

    public MutableShortSet collectShort(ShortFunction<? super T> shortFunction)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().collectShort(shortFunction);
        }
    }

    public <S> MutableSet<S> selectInstancesOf(Class<S> clazz)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().selectInstancesOf(clazz);
        }
    }

    public <V> MutableSet<V> collect(Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().collect(function);
        }
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    public MutableSet<Pair<T, Integer>> zipWithIndex()
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().zipWithIndex();
        }
    }

    public <P, V> MutableSet<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().collectWith(function, parameter);
        }
    }

    public <V> MutableSet<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().collectIf(predicate, function);
        }
    }

    public <V> MutableSet<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().flatCollect(function);
        }
    }

    public <V> MutableSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().groupBy(function);
        }
    }

    public <V> MutableSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().groupByEach(function);
        }
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    public <S> MutableSet<Pair<T, S>> zip(Iterable<S> that)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().zip(that);
        }
    }

    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().unionInto(set, targetSet);
        }
    }

    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().intersectInto(set, targetSet);
        }
    }

    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().differenceInto(subtrahendSet, targetSet);
        }
    }

    public <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().symmetricDifferenceInto(set, targetSet);
        }
    }

    public boolean isSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().isSubsetOf(candidateSuperset);
        }
    }

    public boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().isProperSubsetOf(candidateSuperset);
        }
    }

    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().cartesianProduct(set);
        }
    }

    public MutableSet<T> union(SetIterable<? extends T> set)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().union(set);
        }
    }

    public MutableSet<T> intersect(SetIterable<? extends T> set)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().intersect(set);
        }
    }

    public MutableSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().difference(subtrahendSet);
        }
    }

    public MutableSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().symmetricDifference(setB);
        }
    }

    public MutableSet<UnsortedSetIterable<T>> powerSet()
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().powerSet();
        }
    }

    public ParallelUnsortedSetIterable<T> asParallel(ExecutorService executorService, int batchSize)
    {
        return new SynchronizedParallelUnsortedSetIterable<T>(this.getMutableSet().asParallel(executorService, batchSize), this.getLock());
    }

    public MutableSet<T> asUnmodifiable()
    {
        synchronized (this.getLock())
        {
            return UnmodifiableMutableSet.of(this);
        }
    }

    public MutableSet<T> asSynchronized()
    {
        return this;
    }

    public ImmutableSet<T> toImmutable()
    {
        synchronized (this.getLock())
        {
            return Sets.immutable.withAll(this.getMutableSet());
        }
    }
}
