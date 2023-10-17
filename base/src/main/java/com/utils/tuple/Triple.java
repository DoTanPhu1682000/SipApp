package com.utils.tuple;

import java.util.Objects;

public class Triple<First, Second, Third> {
    public final First first;
    public final Second second;
    public final Third third;

    /**
     * Constructor for a Triple.
     *
     * @param first  the first object in the Triple
     * @param second the second object in the Triple
     * @param third  the third object in the Triple
     */
    public Triple(First first, Second second, Third third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Checks the three objects for equality by delegating to their respective
     *
     * @param o the {@link Triple} to which this one is to be checked for equality
     * @return true if the underlying objects of the Triple are both considered
     * equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Triple)) {
            return false;
        }
        Triple<?, ?, ?> p = (Triple<?, ?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second) && Objects.equals(p.third, third);
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Triple
     */
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode()) ^ (third == null ? 0 : third.hashCode());
    }

    @Override
    public String toString() {
        return "Triple{" + String.valueOf(first) + " " + String.valueOf(second) + " " + String.valueOf(third) + "}";
    }

    /**
     * Convenience method for creating an appropriately typed Triple.
     *
     * @param a the first object in the Triple
     * @param b the second object in the Triple
     * @param c the third object in the Triple
     * @return a Triple that is templatized with the types of a and b
     */
    public static <A, B, C> Triple<A, B, C> create(A a, B b, C c) {
        return new Triple<A, B, C>(a, b, c);
    }
}
