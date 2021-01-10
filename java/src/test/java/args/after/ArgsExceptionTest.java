package args.after;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Unit test for ArgsException.
 */
public class ArgsExceptionTest {
  @Test
  public void errorMessage_codeOk() throws Exception {
    ArgsException sut = new ArgsException(ArgsErrorCode.OK);
    String msg = sut.errorMessage();

    assertEquals(msg, "TILT: Should not get here.");
  }

  @Test
  public void errorMessage_codeUnexpectedArgument() throws Exception {
    ArgsException sut = new ArgsException(ArgsErrorCode.UNEXPECTED_ARGUMENT);
    sut.setErrorArgumentId('x');
    String msg = sut.errorMessage();

    assertEquals(msg, "Argument -x unexpected.");
  }

  @Test
  public void errorMessage_codeMissingString() throws Exception {
    ArgsException sut = new ArgsException(ArgsErrorCode.MISSING_STRING);
    sut.setErrorArgumentId('x');
    String msg = sut.errorMessage();

    assertEquals(msg, "Could not find string parameter for -x.");
  }

  @Test
  public void errorMessage_codeInvalidInteger() throws Exception {
    ArgsException sut = new ArgsException(ArgsErrorCode.INVALID_INTEGER);
    sut.setErrorArgumentId('x');
    sut.setErrorParameter("bad");
    String msg = sut.errorMessage();

    assertEquals(msg, "Argument -x expects an integer but was 'bad'.");
  }


  @Test
  public void errorMessage_codeMissingInteger() throws Exception {
    ArgsException sut = new ArgsException(ArgsErrorCode.MISSING_INTEGER);
    sut.setErrorArgumentId('x');
    String msg = sut.errorMessage();

    assertEquals(msg, "Could not find integer parameter for -x.");
  }

  @Test
  public void errorMessage_codeInvalidDouble() throws Exception {
    ArgsException sut = new ArgsException(ArgsErrorCode.INVALID_DOUBLE);
    sut.setErrorArgumentId('x');
    sut.setErrorParameter("bad");
    String msg = sut.errorMessage();

    assertEquals(msg, "Argument -x expects a double but was 'bad'.");
  }

  @Test
  public void errorMessage_codeMissingDouble() throws Exception {
    ArgsException sut = new ArgsException(ArgsErrorCode.MISSING_DOUBLE);
    sut.setErrorArgumentId('x');
    String msg = sut.errorMessage();

    assertEquals(msg, "Could not find double parameter for -x.");
  }

  @Test
  public void errorMessage_codeInvalidArgumentName() throws Exception {
    ArgsException sut = new ArgsException(ArgsErrorCode.INVALID_ARGUMENT_NAME);
    sut.setErrorArgumentId('$');
    String msg = sut.errorMessage();

    assertEquals(msg, "'$' is not a valid argument name.");
  }

  @Test
  public void errorMessage_codeInvalidArgumentFormat() throws Exception {
    ArgsException sut = new ArgsException(ArgsErrorCode.INVALID_ARGUMENT_FORMAT);
    sut.setErrorParameter("#$");
    String msg = sut.errorMessage();

    assertEquals(msg, "'#$' is not a valid argument format.");
  }
}
