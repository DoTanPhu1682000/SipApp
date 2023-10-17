package com.utils.tuple;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Sex<First, Second, Third, Fourth, Fifth, Sixth> {
    public final First first;
    public final Second second;
    public final Third third;
    public final Fourth fourth;
    public final Fifth fifth;
    public final Sixth sixth;

    /**
     * Constructor for a Sixth.
     *
     * @param first  the first object in the Sixth
     * @param second the second object in the Sixth
     * @param third  the third object in the Sixth
     * @param fourth the quad object in the Sixth
     * @param fifth  the quad object in the Sixth
     * @param sixth  the quad object in the Sixth
     */
    public Sex(First first, Second second, Third third, Fourth fourth, Fifth fifth, Sixth sixth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
        this.sixth = sixth;
    }

    /**
     * Checks the four objects for equality by delegating to their respective
     *
     * @param o the {@link Sex} to which this one is to be checked for equality
     * @return true if the underlying objects of the Sixth are both considered
     * equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sex)) {
            return false;
        }
        Sex<?, ?, ?, ?, ?, ?> p = (Sex<?, ?, ?, ?, ?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second) && Objects.equals(p.third, third) && Objects.equals(p.fourth, fourth) && Objects.equals(p.fifth, fifth) && Objects.equals(p.sixth, sixth);
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Sixth
     */
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode()) ^ (third == null ? 0 : third.hashCode()) ^ (fourth == null ? 0 : fourth.hashCode() ^ (fifth == null ? 0 : fifth.hashCode()) ^ (sixth == null ? 0 : sixth.hashCode()));
    }

    @NonNull
    @Override
    public String toString() {
        return "Sixth{" + String.valueOf(first) + " " + String.valueOf(second) + " " + String.valueOf(third) + " " + String.valueOf(fourth) + " " + String.valueOf(fifth) + " " + String.valueOf(sixth) + "}";
    }

    /**
     * Convenience method for creating an appropriately typed Sixth.
     *
     * @param a the first object in the Sixth
     * @param b the second object in the Sixth
     * @param c the third object in the Sixth
     * @param d the quad object in the Sixth
     * @param e the quad object in the Sixth
     * @param f the quad object in the Sixth
     * @return a Sixth that is templatized with the types of a and b
     */
    public static <A, B, C, D, E, F> Sex<A, B, C, D, E, F> create(A a, B b, C c, D d, E e, F f) {
        return new Sex<A, B, C, D, E, F>(a, b, c, d, e, f);
    }
}
