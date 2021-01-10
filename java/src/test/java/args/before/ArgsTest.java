package args.before;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;


/**
 * Unit tests for Args before refactoring. You should not modify this class when completing the kata
 */
public class ArgsTest {
  @Test
  public void getMethods_validSchemaValidArgs() throws Exception {
    String schema = "i#,b,d##,s*";
    String[] args = {"-i", "10", "-b", "-d", "1.1", "-s", "something"};
    Args sut = new Args(schema, args);

    String strArg = sut.getString('s');
    assertEquals(strArg, "something",
        "should return the desired string value when asked about the string argument");
    int intArg = sut.getInt('i');
    assertEquals(intArg, 10,
        "should return the desired int value when asked about the int argument");
    boolean boolArg = sut.getBoolean('b');
    assertEquals(boolArg, true,
        "should return the desired bool value when asked about the bool argument");
    double doubleArg = sut.getDouble('d');
    assertEquals(doubleArg, 1.1,
        "should return the desired double value when asked about the double argument");
  }

  @Test
  public void getMethods_reorderedValidSchemaValidArgs() throws Exception {
    String schema = "i#,b,d##,s*";
    String[] args = {"-b", "-d", "1.1", "-s", "something", "-i", "10"};
    Args sut = new Args(schema, args);

    String strArg = sut.getString('s');
    assertEquals(strArg, "something",
        "should return the desired string value when asked about the string argument");
    int intArg = sut.getInt('i');
    assertEquals(intArg, 10,
        "should return the desired int value when asked about the int argument");
    boolean boolArg = sut.getBoolean('b');
    assertEquals(boolArg, true,
        "should return the desired bool value when asked about the bool argument");
    double doubleArg = sut.getDouble('d');
    assertEquals(doubleArg, 1.1,
        "should return the desired double value when asked about the double argument");
  }

  @Test
  public void getMethods_validSchemaNoArgs() throws Exception {
    String schema = "i#,b,d##,s*";
    String[] args = {};
    Args sut = new Args(schema, args);

    String strArg = sut.getString('s');
    assertEquals(strArg, "", "should return the default \"\" when asked about the string argument");
    int intArg = sut.getInt('i');
    assertEquals(intArg, 0, "should return the default 0 when asked about the int argument");
    boolean boolArg = sut.getBoolean('b');
    assertEquals(boolArg, false,
        "should return the default false when asked about the bool argument");
    double doubleArg = sut.getDouble('d');
    assertEquals(doubleArg, 0, "should return the default 0 when asked about the double argument");
  }

  private void testThrowArgsException(String schema, String[] args, String expectedMsg,
      ArgsErrorCode expectedCode) {
    try {
      new Args(schema, args);
    } catch (ArgsException e) {
      assertEquals(e.errorMessage(), expectedMsg);
      assertEquals(e.getErrorCode(), expectedCode);
      return;
    }
    fail(String.format("Expecting exception (%s) for schema [%s] and args [%s], but got none",
        expectedMsg, schema, String.join(",", args)));
  }

  @Test
  public void getMethods_missingDashInArgs() {
    String schema = "i#,b,d##,s*";
    String[] args = {"i", "10"};
    testThrowArgsException(schema, args, "'i' is not a valid argument format.",
        ArgsErrorCode.INVALID_ARGUMENT_FORMAT);
  }

  @Test
  public void getMethods_invalidIntArgValue() {
    String schema = "i#,b,d##,s*";
    String[] args = {"-i", "bad"};
    testThrowArgsException(schema, args, "Argument -i expects an integer but was 'bad'.",
        ArgsErrorCode.INVALID_INTEGER);
  }

  @Test
  public void getMethods_invalidDoubleArgValue() {
    String schema = "i#,b,d##,s*";
    String[] args = {"-d", "bad"};
    testThrowArgsException(schema, args, "Argument -d expects a double but was 'bad'.",
        ArgsErrorCode.INVALID_DOUBLE);
  }

  @Test
  public void getMethods_unexpectedArg() {
    String schema = "i#,b,d##,s*";
    String[] args = {"-n", "10"};
    testThrowArgsException(schema, args, "Argument -n unexpected.",
        ArgsErrorCode.UNEXPECTED_ARGUMENT);
  }

  @Test
  public void getMethods_missingIntArgValue() {
    String schema = "i#,b,d##,s*";
    String[] args = {"-i"};
    testThrowArgsException(schema, args, "Could not find integer parameter for -i.",
        ArgsErrorCode.MISSING_INTEGER);
  }

  @Test
  public void getMethods_missingStringArgValue() {
    String schema = "i#,b,d##,s*";
    String[] args = {"-s"};
    testThrowArgsException(schema, args, "Could not find string parameter for -s.",
        ArgsErrorCode.MISSING_STRING);
  }

  @Test
  public void getMethods_missingDoubleArgValue() {
    String schema = "i#,b,d##,s*";
    String[] args = {"-d"};
    testThrowArgsException(schema, args, "Could not find double parameter for -d.",
        ArgsErrorCode.MISSING_DOUBLE);
  }

  @Test
  public void getMethods_invalidArgName() {
    String schema = "$#";
    String[] args = {};
    testThrowArgsException(schema, args, "'$' is not a valid argument name.",
        ArgsErrorCode.INVALID_ARGUMENT_NAME);
  }

  @Test
  public void getMethods_invalidSchemaFormat() {
    String schema = "i#^";
    String[] args = {};
    testThrowArgsException(schema, args, "'#^' is not a valid argument format.",
        ArgsErrorCode.INVALID_ARGUMENT_FORMAT);
  }

  @Test
  public void getMethods_emptySchemaNoArgs() {
    String schema = "";
    String[] args = {};
    try {
      new Args(schema, args);
    } catch (Throwable e) {
      fail("Should not throw given empty schema and no arguments", e);
    }
  }
}
