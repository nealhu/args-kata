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
 * Unit test for DoubleArgumentMarshaler.
 */
public class DoubleArgumentMarshalerTest {
  @Test
  @SuppressWarnings(value = "unchecked")
  public void set_validInput() throws Exception {
    Iterator<String> mockIterator = (Iterator<String>) mock(Iterator.class);
    when(mockIterator.next()).thenReturn("1.1");
    when(mockIterator.hasNext()).thenReturn(true);
    DoubleArgumentMarshaler sut = new DoubleArgumentMarshaler();

    sut.set(mockIterator);
    double value = DoubleArgumentMarshaler.getValue(sut);

    assertEquals(value, 1.1, "Should return 1.1 after the marshaler reads from the iterator");
    verify(mockIterator, times(1).description("Should only read one element from the iterator"))
        .next();
  }

  @Test
  @SuppressWarnings(value = "unchecked")
  public void set_invalidInput() throws Exception {
    Iterator<String> mockIterator = (Iterator<String>) mock(Iterator.class);
    when(mockIterator.next()).thenReturn("bad");
    when(mockIterator.hasNext()).thenReturn(false);
    DoubleArgumentMarshaler sut = new DoubleArgumentMarshaler();

    testThrowArgsException(sut, mockIterator, ArgsErrorCode.INVALID_DOUBLE);
    verify(mockIterator, times(1).description("Should only read one element from the iterator"))
        .next();
  }

  private void testThrowArgsException(DoubleArgumentMarshaler marshaler, Iterator<String> input,
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
    DoubleArgumentMarshaler sut = new DoubleArgumentMarshaler();

    testThrowArgsException(sut, mockIterator, ArgsErrorCode.MISSING_DOUBLE);
    verify(mockIterator, times(1).description("Should only read one element from the iterator"))
        .next();
  }

  @Test
  public void getValue_beforeCallingSet() throws Exception {
    DoubleArgumentMarshaler sut = new DoubleArgumentMarshaler();

    double value = DoubleArgumentMarshaler.getValue(sut);

    assertEquals(value, 0, "Should return 0 before the marshaler takes any input");
  }

  @Test
  public void getValue_nullInput() throws Exception {
    double value = DoubleArgumentMarshaler.getValue(null);

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
    double value = DoubleArgumentMarshaler.getValue(unknownMarshaler);

    assertEquals(value, 0, "Should return 0 given unknown marshaler");
  }
}
