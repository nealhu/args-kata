package args.after;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Iterator;
import org.testng.annotations.Test;

/**
 * Unit test for BooleanArgumentMarshaler.
 */
public class BooleanArgumentMarshalerTest {
  @Test
  @SuppressWarnings(value = "unchecked")
  public void set_validInput() throws Exception {
    Iterator<String> mockIterator = (Iterator<String>) mock(Iterator.class);
    when(mockIterator.next()).thenReturn("-b");
    when(mockIterator.hasNext()).thenReturn(true);
    BooleanArgumentMarshaler sut = new BooleanArgumentMarshaler();

    sut.set(mockIterator);
    boolean value = BooleanArgumentMarshaler.getValue(sut);

    assertEquals(value, true, "Should return true after the marshaler reads from the iterator");
    verify(mockIterator, never().description("Should not read from iterator")).next();
  }

  @Test
  public void getValue_beforeCallingSet() throws Exception {
    BooleanArgumentMarshaler sut = new BooleanArgumentMarshaler();

    boolean value = BooleanArgumentMarshaler.getValue(sut);

    assertEquals(value, false, "Should return false before the marshaler takes any input");
  }

  @Test
  public void getValue_nullInput() throws Exception {
    boolean value = BooleanArgumentMarshaler.getValue(null);

    assertEquals(value, false, "Should return false given no marshaler");
  }

  @Test
  public void getValue_invalidMarshaler() throws Exception {
    ArgumentMarshaler unknownMarshaler = new ArgumentMarshaler() {
      @Override
      public void set(Iterator<String> currentArgument) throws ArgsException {
        currentArgument.hasNext();
      }
    };
    boolean value = BooleanArgumentMarshaler.getValue(unknownMarshaler);

    assertEquals(value, false, "Should return false given unknown marshaler");
  }
}
