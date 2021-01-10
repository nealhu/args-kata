package args.after;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * DoubleArgumentMarshaler parses double arguments.
 */
public class DoubleArgumentMarshaler implements ArgumentMarshaler {
  private double doubleValue = 0;

  /**
   * Read and parse the next parameter from an iterator.
   *
   * @param currentArgument an iterator to read parameter from
   *
   * @throws ArgsException if the iterator has no more parameters or the next parameter is not
   *                       parsable as double
   */
  public void set(Iterator<String> currentArgument) throws ArgsException {
    String parameter = null;
    try {
      parameter = currentArgument.next();
      doubleValue = Double.parseDouble(parameter);
    } catch (NoSuchElementException e) {
      throw new ArgsException(ArgsErrorCode.MISSING_DOUBLE);
    } catch (NumberFormatException e) {
      throw new ArgsException(ArgsErrorCode.INVALID_DOUBLE, parameter);
    }
  }

  /**
   * Get values stored in a DoubleArgumentMarshaler.
   *
   * @param am A DoubleArgumentMarshaler
   * @return value in marshaler
   */
  public static double getValue(ArgumentMarshaler am) {
    if (am == null) {
      return 0;
    }
    if (!(am instanceof DoubleArgumentMarshaler)) {
      return 0;
    }
    return ((DoubleArgumentMarshaler) am).doubleValue;
  }
}
