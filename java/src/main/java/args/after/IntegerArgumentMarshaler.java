package args.after;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * IntegerArgumentMarshaler parses integer arguments.
 */
public class IntegerArgumentMarshaler implements ArgumentMarshaler {
  private int intValue = 0;

  /**
   * Read and parse the next parameter from an iterator.
   *
   * @param currentArgument an iterator to read parameter from
   *
   * @throws ArgsException if the iterator has no more parameters or the next parameter is not
   *                       parsable as int
   */
  public void set(Iterator<String> currentArgument) throws ArgsException {
    String parameter = null;
    try {
      parameter = currentArgument.next();
      intValue = Integer.parseInt(parameter);
    } catch (NoSuchElementException e) {
      throw new ArgsException(ArgsErrorCode.MISSING_INTEGER);
    } catch (NumberFormatException e) {
      throw new ArgsException(ArgsErrorCode.INVALID_INTEGER, parameter);
    }
  }

  /**
   * Get values stored in a IntegerArgumentMarshaler.
   *
   * @param am A IntegerArgumentMarshaler
   * @return value in marshaler
   */
  public static int getValue(ArgumentMarshaler am) {
    if (am == null) {
      return 0;
    }
    if (!(am instanceof IntegerArgumentMarshaler)) {
      return 0;
    }
    return ((IntegerArgumentMarshaler) am).intValue;
  }
}
