import deque.ArrayDeque;
import deque.Deque;
import deque.LinkedListDeque;
import org.junit.jupiter.api.Test;

public class TestIteration {

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


}
