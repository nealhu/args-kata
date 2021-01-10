package args.after;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * StringArgumentMarshaler parses string arguments.
 */
public class StringArgumentMarshaler implements ArgumentMarshaler {
  private String stringValue = "";

  /**
   * Read and parse the next parameter from an iterator.
   *
   * @param currentArgument an iterator to read parameter from
   *
   * @throws ArgsException if the iterator has no more parameters
   */
  public void set(Iterator<String> currentArgument) throws ArgsException {
    try {
      stringValue = currentArgument.next();
    } catch (NoSuchElementException e) {
      throw new ArgsException(ArgsErrorCode.MISSING_STRING);
    }
  }

  /**
   * Get values stored in a StringArgumentMarshaler.
   *
   * @param am A StringArgumentMarshaler
   * @return value in marshaler
   */
  public static String getValue(ArgumentMarshaler am) {
    if (am == null) {
      return "";
    }
    if (!(am instanceof StringArgumentMarshaler)) {
      return "";
    }
    return ((StringArgumentMarshaler) am).stringValue;
  }
}
