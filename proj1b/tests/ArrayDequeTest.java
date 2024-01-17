import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {

     @Test
     @DisplayName("ArrayDeque has no fields besides backing array and primitives")
     void noNonTrivialFields() {
         List<Field> badFields = Reflection.getFields(ArrayDeque.class)
                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                 .toList();

         assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
     }

    @Test
    /** In this test, we have three different assert statements that verify that addFirst works correctly. */
    public void addFirstTestBasic() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addFirst("back"); // after this call we expect: ["back"]
        lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        lld1.addFirst("apple");
        lld1.addFirst("orange");
        lld1.addFirst("banana");
        lld1.addFirst("cherry");
        lld1.addFirst("blueberry");
        lld1.addFirst("blackberry");
//        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

    }

    @Test
    /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
     *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
    public void addLastTestBasic() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        lld1.addLast("apple");
        lld1.addLast("orange");
        lld1.addLast("banana");
        lld1.addLast("cherry");
        lld1.addLast("blueberry");
        lld1.addLast("blackberry");

//        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
    }

    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]
        lld1.addFirst(-1);
        lld1.addLast(11);
        lld1.addLast(22);
        lld1.addFirst(88);
        lld1.addLast(-99);

//        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
    }

    @Test
    /**Check get works*/
    public void testGet(){
        Deque<String> lld1 = new ArrayDeque<>();
        assertThat(lld1.get(-1)).isEqualTo(null);

        lld1.addFirst("orange");
        assertThat(lld1.get(3)).isEqualTo("orange");
        assertThat(lld1.get(10)).isEqualTo(null);
        lld1.addLast("banana");
        assertThat(lld1.get(4)).isEqualTo("banana");
        lld1.addFirst("apple");
        assertThat(lld1.get(2)).isEqualTo("apple");
        assertThat(lld1.get(1)).isNull();
        lld1.addFirst("apple2");
        lld1.addFirst("apple5");
        lld1.addFirst("apple33");
        assertThat(lld1.get(7)).isEqualTo("apple33");
        lld1.addLast("banana5");
        lld1.addFirst("apple9");
        lld1.addFirst("berry");
        String _1 = lld1.get(0);
        String _2 = lld1.get(9);
        assertThat(lld1.get(0)).isEqualTo("apple9");
        assertThat(lld1.get(9)).isNull();
    }

}
