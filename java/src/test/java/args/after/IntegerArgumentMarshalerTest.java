package args.after;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.testng.annotations.Test;

/**
 * Unit test for IntegerArgumentMarshaler.
 */
public class IntegerArgumentMarshalerTest {
  @Test
  @SuppressWarnings(value = "unchecked")
  public void set_validInput() throws Exception {
    Iterator<String> mockIterator = (Iterator<String>) mock(Iterator.class);
    when(mockIterator.next()).thenReturn("10");
    when(mockIterator.hasNext()).thenReturn(true);
    IntegerArgumentMarshaler sut = new IntegerArgumentMarshaler();

    sut.set(mockIterator);
    int value = IntegerArgumentMarshaler.getValue(sut);

    assertEquals(value, 10, "Should return 10 after the marshaler reads from the iterator");
    verify(mockIterator, times(1).description("Should only read one element from the iterator"))
        .next();
  }

  @Test
  @SuppressWarnings(value = "unchecked")
  public void set_invalidInput() throws Exception {
    Iterator<String> mockIterator = (Iterator<String>) mock(Iterator.class);
    when(mockIterator.next()).thenReturn("bad");
    when(mockIterator.hasNext()).thenReturn(false);
    IntegerArgumentMarshaler sut = new IntegerArgumentMarshaler();

    testThrowArgsException(sut, mockIterator, ArgsErrorCode.INVALID_INTEGER);
    verify(mockIterator, times(1).description("Should only read one element from the iterator"))
        .next();
  }

  private void testThrowArgsException(IntegerArgumentMarshaler marshaler, Iterator<String> input,
      ArgsErrorCode expectedCode) {
    try {
      marshaler.set(input);
    } catch (ArgsException e) {
      assertEquals(e.getErrorCode(), expectedCode);
      return;
    }
    fail(String.format("Expecting exception with code [%s], but got none", expectedCode.name()));
  }

  @Test
  @SuppressWarnings(value = "unchecked")
  public void set_noMoreInput() throws Exception {
    Iterator<String> mockIterator = (Iterator<String>) mock(Iterator.class);
    when(mockIterator.next()).thenThrow(new NoSuchElementException());
    when(mockIterator.hasNext()).thenReturn(true);
    IntegerArgumentMarshaler sut = new IntegerArgumentMarshaler();

    testThrowArgsException(sut, mockIterator, ArgsErrorCode.MISSING_INTEGER);
    verify(mockIterator, times(1).description("Should only read one element from the iterator"))
        .next();
  }

  @Test
  public void getValue_beforeCallingSet() throws Exception {
    IntegerArgumentMarshaler sut = new IntegerArgumentMarshaler();

    int value = IntegerArgumentMarshaler.getValue(sut);

    assertEquals(value, 0, "Should return 0 before the marshaler takes any input");
  }

  @Test
  public void getValue_nullInput() throws Exception {
    int value = IntegerArgumentMarshaler.getValue(null);

    assertEquals(value, 0, "Should return 0 given no marshaler");
  }

  @Test
  public void getValue_invalidMarshaler() throws Exception {
    ArgumentMarshaler unknownMarshaler = new ArgumentMarshaler() {
      @Override
      public void set(Iterator<String> currentArgument) throws ArgsException {
        currentArgument.hasNext();
      }
    };
    int value = IntegerArgumentMarshaler.getValue(unknownMarshaler);

    assertEquals(value, 0, "Should return 0 given unknown marshaler");
  }
}
