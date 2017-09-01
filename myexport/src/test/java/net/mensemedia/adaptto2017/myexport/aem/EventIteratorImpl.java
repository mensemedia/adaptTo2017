package net.mensemedia.adaptto2017.myexport.aem;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.LongStream;

public class EventIteratorImpl implements EventIterator {
    private final Collection<Event> events;
    private final Iterator<Event> iterator;
    private long currpos = 0;

    public EventIteratorImpl(final Collection<Event> events) {
        this.events = events;
        iterator = events.iterator();
    }

    @Override
    public Event nextEvent() {
        currpos++;
        return iterator.next();
    }

    @Override
    public void skip(final long skipNum) {
        LongStream
                .range(0, skipNum)
                .filter(x -> hasNext())
                .forEach(x -> next());
    }

    @Override
    public long getSize() {
        return events.size();
    }

    @Override
    public long getPosition() {
        return currpos;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object next() {
        return nextEvent();
    }
}
