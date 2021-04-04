////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.collections.list;

import com.telenav.kivakit.core.kernel.language.collections.list.BaseList;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class BoundedListTest
{
    @Test
    public void testAdd()
    {
        // First make sure that we can add an element to a list with room.
        final BaseList<Integer> list = getPopulatedList(100, 99);
        list.add(11);

        // Now make sure we can't add anything to a full list.
        final int size = list.size();
        list.add(22);
        ensureEqual(size, list.size());

        // Now remove two entries from the list and try again.
        list.remove(1);
        list.add(33);
    }

    @Test
    public void testAddAll()
    {
        final BaseList<Integer> list = getPopulatedList(100, 75);
        final List<Integer> listToAdd = getPopulatedList(25, 25);

        // We should be able to add the list once.
        list.addAll(listToAdd);

        // We should not be able to add it again though.
        final int size = list.size();
        list.addAll(listToAdd);
        ensureEqual(size, list.size());
    }

    @Test
    public void testAddIndex()
    {
        // First make sure that we can add an element to a list with room and
        // then verify that the value was set.
        final BaseList<Integer> list = getPopulatedList(100, 99);
        list.add(25, 11);
        Assert.assertEquals(Integer.valueOf(11), list.get(25));

        // Now make sure we can't add anything to a full list.
        final int size = list.size();
        list.add(25, 22);
        ensureEqual(size, list.size());

        // Now remove two entries from the list and try again.
        list.remove(1);
        list.add(12, 33);
    }

    private BaseList<Integer> getPopulatedList(final int maximumSize, final int currentLevel)
    {
        final ObjectList<Integer> list = new ObjectList<>(Maximum.maximum(maximumSize))
        {
            @Override
            protected void onOutOfRoom()
            {
            }
        };
        for (var i = 0; i < currentLevel; i++)
        {
            list.add(i);
        }
        return list;
    }
}
