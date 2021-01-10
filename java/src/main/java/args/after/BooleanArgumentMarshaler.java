package args.after;

import java.util.Iterator;

/**
 * BooleanArgumentMarshaler parses boolean arguments.
 */
public class BooleanArgumentMarshaler implements ArgumentMarshaler {
  private boolean booleanValue = false;

  /**
   * Read and parse the next parameter from an iterator.
   *
   * @param currentArgument an iterator to read parameter from
   *
   */
  public void set(Iterator<String> currentArgument) {
    booleanValue = true;
  }

  /**
   * Get values stored in a BooleanArgumentMarshaler.
   *
   * @param am A BooleanArgumentMarshaler
   * @return value in marshaler
   */
  public static boolean getValue(ArgumentMarshaler am) {
    if (am == null) {
      return false;
    }
    if (!(am instanceof BooleanArgumentMarshaler)) {
      return false;
    }
    return ((BooleanArgumentMarshaler) am).booleanValue;
  }
}
