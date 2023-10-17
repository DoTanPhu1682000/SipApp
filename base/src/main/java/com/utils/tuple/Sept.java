package com.utils.tuple;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Sept<First, Second, Third, Fourth, Fifth, Sixth, Seventh> implements Serializable {
    public final First first;
    public final Second second;
    public final Third third;
    public final Fourth fourth;
    public final Fifth fifth;
    public final Sixth sixth;
    public final Seventh seventh;

    /**
     * Constructor for a Seventh.
     *
     * @param first  the first object in the Seventh
     * @param second the second object in the Seventh
     * @param third  the third object in the Seventh
     * @param fourth the quad object in the Seventh
     * @param fifth  the quad object in the Seventh
     * @param sixth  the quad object in the Seventh
     */
    public Sept(First first, Second second, Third third, Fourth fourth, Fifth fifth, Sixth sixth, Seventh seventh) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
        this.sixth = sixth;
        this.seventh = seventh;
    }

    /**
     * Checks the four objects for equality by delegating to their respective
     *
     * @param o the {@link Sept} to which this one is to be checked for equality
     * @return true if the underlying objects of the Seventh are both considered
     * equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sept)) {
            return false;
        }
        Sept<?, ?, ?, ?, ?, ?, ?> p = (Sept<?, ?, ?, ?, ?, ?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second) && Objects.equals(p.third, third) && Objects.equals(p.fourth, fourth) && Objects.equals(p.fifth, fifth) && Objects.equals(p.sixth, sixth) && Objects.equals(p.seventh, seventh);
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Seventh
     */
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode()) ^ (third == null ? 0 : third.hashCode()) ^ (fourth == null ? 0 : fourth.hashCode() ^ (fifth == null ? 0 : fifth.hashCode()) ^ (sixth == null ? 0 : sixth.hashCode()) ^ (seventh == null ? 0 : seventh.hashCode()));
    }

    @NonNull
    @Override
    public String toString() {
        return "Seventh{" + String.valueOf(first) + " " + String.valueOf(second) + " " + String.valueOf(third) + " " + String.valueOf(fourth) + " " + String.valueOf(fifth) + " " + String.valueOf(sixth) + " " + String.valueOf(seventh) + "}";
    }

    /**
     * Convenience method for creating an appropriately typed Seventh.
     *
     * @param a the first object in the Seventh
     * @param b the second object in the Seventh
     * @param c the third object in the Seventh
     * @param d the quad object in the Seventh
     * @param e the quad object in the Seventh
     * @param f the quad object in the Seventh
     * @param g the quad object in the Seventh
     * @return a Seventh that is templatized with the types of a and b
     */
    public static <A, B, C, D, E, F, G> Sept<A, B, C, D, E, F, G> create(A a, B b, C c, D d, E e, F f, G g) {
        return new Sept<A, B, C, D, E, F, G>(a, b, c, d, e, f, g);
    }
}
