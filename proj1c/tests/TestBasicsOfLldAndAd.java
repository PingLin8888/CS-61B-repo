import deque.ArrayDeque;
import deque.Deque;
import deque.LinkedListDeque;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class TestBasicsOfLldAndAd {

    @Test
    public void iterationTestLinkedListDeque(){
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(9);
        lld1.addLast(63);
        for (int i : lld1) {
            System.out.println(i);
        }
    }

    @Test
    public void iterationTestArrayDeque(){
        Deque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(9);
        lld1.addFirst(63);
        lld1.addFirst(9);
        lld1.addFirst(-3);
        lld1.addLast(8);
        lld1.addFirst(43);
        lld1.addFirst(-21);
        lld1.addLast(11);
        lld1.addFirst(75);
        lld1.addFirst(222);
        lld1.addFirst(-96);
        for (int i : lld1) {
            System.out.println(i);
        }
    }

    @Test
    public void testEqualDequesLld() {
        Deque<String> lld1 = new LinkedListDeque<>();
        Deque<String> lld2 = new LinkedListDeque<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        lld2.addLast("front");
        lld2.addLast("middle");
        lld2.addLast("back");

        assertThat(lld1).isEqualTo(lld2);
    }

    @Test
    public void testEqualDequeAd() {
        Deque<String> lld1 = new ArrayDeque<>();
        Deque<String> lld2 = new ArrayDeque<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        lld2.addLast("front");
        lld2.addLast("middle");
        lld2.addLast("back");

        assertThat(lld1).isEqualTo(lld2);
    }

    @Test
    public void testToStringDequesLld() {
        Deque<String> lld1 = new LinkedListDeque<>();
        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");
        assertThat(lld1.toString()).isEqualTo("{[front, middle, back]}");
    }

    @Test
    public void testToStringDequesAd() {
        Deque<String> lld1 = new ArrayDeque<>();
        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");
        assertThat(lld1.toString()).isEqualTo("{[front, middle, back]}");
    }
}
