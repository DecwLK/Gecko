import org.junit.jupiter.api.Test;

public class TestClass {
    @Test
    public void test() {
        System.out.println("Hello world!");
    }

    @Test
    public void failingTest() {
        throw new RuntimeException("Test failed!");
    }
}
