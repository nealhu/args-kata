package args.after;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Args parses CLI args.
 */
public class Args {
  private Map<Character, ArgumentMarshaler> marshalers;
  private ListIterator<String> currentArgument;

  /**
   * Args Constructor.
   *
   * @param schema Schema of the args, see project README for more details
   * @param args   CLI args to parse
   * @throws ArgsException if there is a problem parsing the args
   */
  public Args(String schema, String[] args) throws ArgsException {
    marshalers = new HashMap<Character, ArgumentMarshaler>();
    parseSchema(schema);
    parseArgumentStrings(Arrays.asList(args));
  }

  private void parseSchema(String schema) throws ArgsException {
    for (String element : schema.split(",")) {
      if (element.length() > 0) {
        parseSchemaElement(element.trim());
      }
    }
  }

  private void parseSchemaElement(String element) throws ArgsException {
    char elementId = element.charAt(0);
    String elementTail = element.substring(1);
    validateSchemaElementId(elementId);
    if (elementTail.length() == 0) {
      marshalers.put(elementId, new BooleanArgumentMarshaler());
    } else if (elementTail.equals("*")) {
      marshalers.put(elementId, new StringArgumentMarshaler());
    } else if (elementTail.equals("#")) {
      marshalers.put(elementId, new IntegerArgumentMarshaler());
    } else if (elementTail.equals("##")) {
      marshalers.put(elementId, new DoubleArgumentMarshaler());
    } else {
      throw new ArgsException(ArgsErrorCode.INVALID_ARGUMENT_FORMAT, elementId, elementTail);
    }
  }

  private void validateSchemaElementId(char elementId) throws ArgsException {
    if (!Character.isLetter(elementId)) {
      throw new ArgsException(ArgsErrorCode.INVALID_ARGUMENT_NAME, elementId, null);
    }
  }

  private void parseArgumentStrings(List<String> argsList) throws ArgsException {
    if (argsList.size() == 0) {
      return;
    }
    currentArgument = argsList.listIterator();
    while (currentArgument.hasNext()) {
      String argString = currentArgument.next();
      if (!argString.startsWith("-")) {
        throw new ArgsException(ArgsErrorCode.INVALID_ARGUMENT_FORMAT, argString);
      }
      parseArgumentCharacters(argString.substring(1));
    }
  }

  private void parseArgumentCharacters(String argChars) throws ArgsException {
    for (int i = 0; i < argChars.length(); i++) {
      parseArgumentCharacter(argChars.charAt(i));
    }
  }

  private void parseArgumentCharacter(char argChar) throws ArgsException {
    ArgumentMarshaler m = marshalers.get(argChar);
    if (m == null) {
      throw new ArgsException(ArgsErrorCode.UNEXPECTED_ARGUMENT, argChar, null);
    }
    try {
      m.set(currentArgument);
    } catch (ArgsException e) {
      e.setErrorArgumentId(argChar);
      throw e;
    }
  }

  public boolean getBoolean(char arg) {
    return BooleanArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public String getString(char arg) {
    return StringArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public int getInt(char arg) {
    return IntegerArgumentMarshaler.getValue(marshalers.get(arg));
  }

  public double getDouble(char arg) {
    return DoubleArgumentMarshaler.getValue(marshalers.get(arg));
  }
}
