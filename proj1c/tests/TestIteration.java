import deque.Deque;
import deque.LinkedListDeque;
import org.junit.jupiter.api.Test;

public class TestIteration {

    @Test
    public void iterationTestLinkedListDeque(){
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(9);
        lld1.addLast(63);
        for(int i:lld1){
            System.out.println(i);
        }
    }

    @Test
    public void iterationTestArrayDeque(){
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(9);
        lld1.addFirst(63);
        for(int i:lld1){
            System.out.println(i);
        }
    }
}
