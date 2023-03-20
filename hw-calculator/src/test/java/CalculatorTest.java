import org.junit.jupiter.api.*;

public class CalculatorTest {
    //ARRANGE
    static Calculator c;

    @BeforeAll
    public static void initVariable() {
        c = new Calculator();
    }

    @Test
    public void add_addNumbers_returns3(){
        int num1 = 1;
        int num2 = 2;
        int expectedResult = num1 + num2;

        int actualResult = c.add(1,2);

        Assertions.assertEquals(expectedResult, actualResult);
    }


    @Test
    public void subtract_subtractNumbers_returns5(){
        int num1 = 13;
        int num2 = 8;
        int expectedResult = num1 - num2;

        int actualResult = c.subtract(13,8);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void multiply_multiplyNumbers_returns18(){
        int num1 = 6;
        int num2 = 3;
        int expectedResult = num1 * num2;

        int actualResult = c.multiply(6,3);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void divide_divideNumbers_returns10(){
        int num1 = 20;
        int num2 = 2;
        int expectedResult = num1 / num2;

        int actualResult = c.divide(20,2);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void divide_divisionByZero_throwsException(){
        int num1 = 20;
        int num2 = 0;

        try {
            c.divide(num1, num2);
        } catch (ArithmeticException e){
            Assertions.assertEquals("/ by zero", e.getMessage());
        }
    }

    @Test
    public void exceptionThrownTest_exceptionThrown_exception() {

        // ACT + ASSERT 1 (Obsahuje assert, zda byla vyhozena výjimka očekávaného typu)
        Exception exception = Assertions.assertThrows(Exception.class, () -> c.exceptionThrown());


        String expectedMessage = "Ocekavana vyjimka";
        String actualMessage = exception.getMessage();

        // ASSERT 2
        Assertions.assertEquals(expectedMessage, actualMessage);
    }


}
