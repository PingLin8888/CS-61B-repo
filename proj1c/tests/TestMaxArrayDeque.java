import deque.Deque;
import deque.MaxArrayDeque;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static com.google.common.truth.Truth.assertThat;

public class TestMaxArrayDeque {
    @Test
    public void paraConstructorTest(){
        Deque<Integer> mad1 = new MaxArrayDeque<>();
        assertThat(mad1.size()).isEqualTo(0);
    }

    @Test
    public void intComparatorTest() {
        Comparator<Integer> c = new IntegerComparator();
        MaxArrayDeque<Integer> mad1 = new MaxArrayDeque<>(c);
        mad1.addFirst(88);
        mad1.addLast(22);
        mad1.addFirst(33);
        mad1.addFirst(74);
        mad1.addLast(90);
        assertThat(mad1.max()).isEqualTo(90);
    }

    @Test
    public void StringComparatorTest() {
        Comparator<String> c = new StringComparator();
        MaxArrayDeque<String> mad1 = new MaxArrayDeque<>(c);
        mad1.addFirst("x");
        mad1.addLast("aa");
        mad1.addFirst("dd");
        mad1.addFirst("bbb");
        mad1.addLast("ccc");
        assertThat(mad1.max()).isEqualTo("x");
    }

    private static class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }

    private static class StringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    
}
