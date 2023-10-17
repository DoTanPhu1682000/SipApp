package com.utils.tuple;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Quint<First, Second, Third, Fourth, Fifth> {
    public final First first;
    public final Second second;
    public final Third third;
    public final Fourth fourth;
    public final Fifth fifth;

    /**
     * Constructor for a Quint.
     *
     * @param first  the first object in the Quint
     * @param second the second object in the Quint
     * @param third  the third object in the Quint
     * @param fourth the quad object in the Quint
     * @param fifth  the quad object in the Quint
     */
    public Quint(First first, Second second, Third third, Fourth fourth, Fifth fifth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
    }

    /**
     * Checks the four objects for equality by delegating to their respective
     *
     * @param o the {@link Quint} to which this one is to be checked for equality
     * @return true if the underlying objects of the Quint are both considered
     * equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Quint)) {
            return false;
        }
        Quint<?, ?, ?, ?, ?> p = (Quint<?, ?, ?, ?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second) && Objects.equals(p.third, third) && Objects.equals(p.fourth, fourth) && Objects.equals(p.fifth, fifth);
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Quint
     */
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode()) ^ (third == null ? 0 : third.hashCode()) ^ (fourth == null ? 0 : fourth.hashCode() ^ (fifth == null ? 0 : fifth.hashCode()));
    }

    @NonNull
    @Override
    public String toString() {
        return "Quint{" + String.valueOf(first) + " " + String.valueOf(second) + " " + String.valueOf(third) + " " + String.valueOf(fourth) + " " + String.valueOf(fifth) + "}";
    }

    /**
     * Convenience method for creating an appropriately typed Quint.
     *
     * @param a the first object in the Quint
     * @param b the second object in the Quint
     * @param c the third object in the Quint
     * @param d the quad object in the Quint
     * @param e the quad object in the Quint
     * @return a Quint that is templatized with the types of a and b
     */
    public static <A, B, C, D, E> Quint<A, B, C, D, E> create(A a, B b, C c, D d, E e) {
        return new Quint<A, B, C, D, E>(a, b, c, d, e);
    }
}
