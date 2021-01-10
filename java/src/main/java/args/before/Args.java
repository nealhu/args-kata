package args.before;

import java.util.HashMap;
import java.util.Map;

/**
 * Args parses CLI args.
 */
public class Args {
  private String schema;
  private String[] args;
  private Map<Character, Boolean> booleanArgs;
  private Map<Character, Integer> intArgs;
  private Map<Character, String> stringArgs;
  private Map<Character, Double> doubleArgs;
  private int currentArgument;

  /**
   * Args Constructor.
   *
   * @param schema Schema of the args, see project README for more details
   * @param args   CLI args to parse
   * @throws ArgsException if there is a problem parsing the args
   */
  public Args(String schema, String[] args) throws ArgsException {
    this.schema = schema;
    this.args = args;
    this.booleanArgs = new HashMap<>();
    this.intArgs = new HashMap<>();
    this.stringArgs = new HashMap<>();
    this.doubleArgs = new HashMap<>();
    this.currentArgument = 0;
    parse();
  }

  private void parse() throws ArgsException {
    if (schema.length() == 0) {
      return;
    }
    parseSchema();
    if (args.length == 0) {
      return;
    }
    parseArguments();
  }

  private void parseSchema() throws ArgsException {
    for (String element : schema.split(",")) {
      parseSchemaElement(element.trim());
    }
  }

  private void parseSchemaElement(String element) throws ArgsException {
    char elementId = element.charAt(0);
    String elementTail = element.substring(1);
    validateSchemaElementId(elementId);
    if (isBooleanSchemaElement(elementTail)) {
      parseBooleanSchemaElement(elementId);
    } else if (isStringSchemaElement(elementTail)) {
      parseStringSchemaElement(elementId);
    } else if (isIntegerSchemaElement(elementTail)) {
      parseIntegerSchemaElement(elementId);
    } else if (isDoubleSchemaElement(elementTail)) {
      parseDoubleSchemaElement(elementId);
    } else {
      throw createArgsError(String.format("'%s' is not a valid argument format.", elementTail),
          ArgsErrorCode.INVALID_ARGUMENT_FORMAT);
    }
  }

  private void validateSchemaElementId(char elementId) throws ArgsException {
    if (!Character.isLetter(elementId)) {
      throw createArgsError(String.format("'%c' is not a valid argument name.", elementId),
          ArgsErrorCode.INVALID_ARGUMENT_NAME);
    }
  }

  private ArgsException createArgsError(String msg, ArgsErrorCode code) {
    ArgsException err = new ArgsException(msg);
    err.setErrorCode(code);
    return err;
  }

  private void parseBooleanSchemaElement(char elementId) {
    booleanArgs.put(elementId, false);
  }

  private void parseIntegerSchemaElement(char elementId) {
    intArgs.put(elementId, 0);
  }

  private void parseStringSchemaElement(char elementId) {
    stringArgs.put(elementId, "");
  }

  private void parseDoubleSchemaElement(char elementId) {
    doubleArgs.put(elementId, 0.0);
  }

  private boolean isStringSchemaElement(String elementTail) {
    return elementTail.equals("*");
  }

  private boolean isBooleanSchemaElement(String elementTail) {
    return elementTail.length() == 0;
  }

  private boolean isIntegerSchemaElement(String elementTail) {
    return elementTail.equals("#");
  }

  private boolean isDoubleSchemaElement(String elementTail) {
    return elementTail.equals("##");
  }

  private boolean parseArguments() throws ArgsException {
    for (currentArgument = 0; currentArgument < args.length; currentArgument++) {
      String arg = args[currentArgument];
      parseArgument(arg);
    }
    return true;
  }

  private void parseArgument(String arg) throws ArgsException {
    if (!arg.startsWith("-")) {
      throw createArgsError(String.format("'%s' is not a valid argument format.", arg),
          ArgsErrorCode.INVALID_ARGUMENT_FORMAT);
    }
    parseElements(arg);
  }

  private void parseElements(String arg) throws ArgsException {
    for (int i = 1; i < arg.length(); i++) {
      setArgument(arg.charAt(i));
    }
  }

  private boolean setArgument(char argChar) throws ArgsException {
    if (isBooleanArg(argChar)) {
      setBooleanArg(argChar, true);
    } else if (isStringArg(argChar)) {
      setStringArg(argChar);
    } else if (isIntArg(argChar)) {
      setIntArg(argChar);
    } else if (isDoubleArg(argChar)) {
      setDoubleArg(argChar);
    } else {
      throw this.createArgsError(String.format("Argument -%c unexpected.", argChar),
          ArgsErrorCode.UNEXPECTED_ARGUMENT);
    }
    return true;
  }

  private boolean isIntArg(char argChar) {
    return intArgs.containsKey(argChar);
  }

  private void setIntArg(char argChar) throws ArgsException {
    currentArgument++;
    String parameter = null;
    try {
      parameter = args[currentArgument];
      intArgs.put(argChar, Integer.parseInt(parameter));
    } catch (ArrayIndexOutOfBoundsException e) {
      throw createArgsError(String.format("Could not find integer parameter for -%c.", argChar),
          ArgsErrorCode.MISSING_INTEGER);
    } catch (NumberFormatException e) {
      throw createArgsError(
          String.format("Argument -%c expects an integer but was '%s'.", argChar, parameter),
          ArgsErrorCode.INVALID_INTEGER);
    }
  }

  private void setStringArg(char argChar) throws ArgsException {
    currentArgument++;
    try {
      stringArgs.put(argChar, args[currentArgument]);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw createArgsError(String.format("Could not find string parameter for -%c.", argChar),
          ArgsErrorCode.MISSING_STRING);
    }
  }

  private boolean isStringArg(char argChar) {
    return stringArgs.containsKey(argChar);
  }

  private void setBooleanArg(char argChar, boolean value) {
    booleanArgs.put(argChar, value);
  }

  private boolean isBooleanArg(char argChar) {
    return booleanArgs.containsKey(argChar);
  }

  private void setDoubleArg(char argChar) throws ArgsException {
    currentArgument++;
    String parameter = null;
    try {
      parameter = args[currentArgument];
      doubleArgs.put(argChar, Double.parseDouble(parameter));
    } catch (ArrayIndexOutOfBoundsException e) {
      throw createArgsError(String.format("Could not find double parameter for -%c.", argChar),
          ArgsErrorCode.MISSING_DOUBLE);
    } catch (NumberFormatException e) {
      throw createArgsError(
          String.format("Argument -%c expects a double but was '%s'.", argChar, parameter),
          ArgsErrorCode.INVALID_DOUBLE);
    }
  }

  private boolean isDoubleArg(char argChar) {
    return doubleArgs.containsKey(argChar);
  }

  public String getString(char arg) {
    return stringArgs.get(arg);
  }

  public int getInt(char arg) {
    return intArgs.get(arg);
  }

  public boolean getBoolean(char arg) {
    return booleanArgs.get(arg);
  }

  public double getDouble(char arg) {
    return doubleArgs.get(arg);
  }
}
