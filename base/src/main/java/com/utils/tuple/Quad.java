package com.utils.tuple;

import java.util.Objects;

public class Quad<First, Second, Third, Fourth> {
    public final First first;
    public final Second second;
    public final Third third;
    public final Fourth fourth;

    /**
     * Constructor for a Quad.
     *
     * @param first  the first object in the Quad
     * @param second the second object in the Quad
     * @param third  the third object in the Quad
     * @param fourth the quad object in the Quad
     */
    public Quad(First first, Second second, Third third, Fourth fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    /**
     * Checks the four objects for equality by delegating to their respective
     *
     * @param o the {@link Quad} to which this one is to be checked for equality
     * @return true if the underlying objects of the Quad are both considered
     * equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Quad)) {
            return false;
        }
        Quad<?, ?, ?, ?> p = (Quad<?, ?, ?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second) && Objects.equals(p.third, third) && Objects.equals(p.fourth, fourth);
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Quad
     */
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode()) ^ (third == null ? 0 : third.hashCode()) ^ (fourth == null ? 0 : fourth.hashCode());
    }

    @Override
    public String toString() {
        return "Quad{" + String.valueOf(first) + " " + String.valueOf(second) + " " + String.valueOf(third) + " " + String.valueOf(fourth) + "}";
    }

    /**
     * Convenience method for creating an appropriately typed Quad.
     *
     * @param a the first object in the Quad
     * @param b the second object in the Quad
     * @param c the third object in the Quad
     * @param d the quad object in the Quad
     * @return a Quad that is templatized with the types of a and b
     */
    public static <A, B, C, D> Quad<A, B, C, D> create(A a, B b, C c, D d) {
        return new Quad<A, B, C, D>(a, b, c, d);
    }
}
