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
 * Unit test for StringArgumentMarshaler.
 */
public class StringArgumentMarshalerTest {
  @Test
  @SuppressWarnings(value = "unchecked")
  public void set_validInput() throws Exception {
    Iterator<String> mockIterator = (Iterator<String>) mock(Iterator.class);
    when(mockIterator.next()).thenReturn("something");
    when(mockIterator.hasNext()).thenReturn(true);
    StringArgumentMarshaler sut = new StringArgumentMarshaler();

    sut.set(mockIterator);
    String value = StringArgumentMarshaler.getValue(sut);

    assertEquals(value, "something",
        "Should return 'something' after the marshaler reads from the iterator");
    verify(mockIterator, times(1).description("Should only read one element from the iterator"))
        .next();
  }

  private void testThrowArgsException(StringArgumentMarshaler marshaler, Iterator<String> input,
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
    StringArgumentMarshaler sut = new StringArgumentMarshaler();

    testThrowArgsException(sut, mockIterator, ArgsErrorCode.MISSING_STRING);
    verify(mockIterator, times(1).description("Should only read one element from the iterator"))
        .next();
  }

  @Test
  public void getValue_beforeCallingSet() throws Exception {
    StringArgumentMarshaler sut = new StringArgumentMarshaler();

    String value = StringArgumentMarshaler.getValue(sut);

    assertEquals(value, "", "Should return \"\" before the marshaler takes any input");
  }

  @Test
  public void getValue_nullInput() throws Exception {
    String value = StringArgumentMarshaler.getValue(null);

    assertEquals(value, "", "Should return \"\" given no marshaler");
  }

  @Test
  public void getValue_invalidMarshaler() throws Exception {
    ArgumentMarshaler unknownMarshaler = new ArgumentMarshaler() {
      @Override
      public void set(Iterator<String> currentArgument) throws ArgsException {
        currentArgument.hasNext();
      }
    };
    String value = StringArgumentMarshaler.getValue(unknownMarshaler);

    assertEquals(value, "", "Should return \"\" given unknown marshaler");
  }
}
