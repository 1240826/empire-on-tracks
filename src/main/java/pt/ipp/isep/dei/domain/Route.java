package pt.ipp.isep.dei.domain;

import java.util.*;

/**
 * Represents a route as an ordered list of station–cargo assignments.
 * Now implemented as a generic container so it can hold any type of elements (e.g., String or StationCargo).
 *
 * @param <T> the type of elements in the route list
 */
public class Route<T> implements List<Station> {

    private final List<T> stationCargoList;

    /**
     * Constructs a new Route.
     *
     * @param stationCargoList list of elements; must not be null
     * @throws IllegalArgumentException if stationCargoList is null
     */
    public Route(List<T> stationCargoList) {
        if (stationCargoList == null) {
            throw new IllegalArgumentException("stationCargoList cannot be null");
        }
        this.stationCargoList = new ArrayList<>(stationCargoList);
    }

    /**
     * Returns an unmodifiable view of the route elements.
     *
     * @return list of elements (non-null)
     */
    public List<T> getStationCargoList() {
        return Collections.unmodifiableList(stationCargoList);
    }

    @Override
    public String toString() {
        return stationCargoList.toString();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Station> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Station station) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Station> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Station> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Station get(int index) {
        return null;
    }

    @Override
    public Station set(int index, Station element) {
        return null;
    }

    @Override
    public void add(int index, Station element) {

    }

    @Override
    public Station remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Station> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Station> listIterator(int index) {
        return null;
    }

    @Override
    public List<Station> subList(int fromIndex, int toIndex) {
        return List.of();
    }
}
