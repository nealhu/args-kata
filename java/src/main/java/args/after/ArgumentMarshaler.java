package args.after;

import java.util.Iterator;

/**
 * Interface for marshalers that read and parse arguments.
 */
public interface ArgumentMarshaler {
  void set(Iterator<String> currentArgument) throws ArgsException;
}
