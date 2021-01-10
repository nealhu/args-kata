package args.after;

/**
 * ArgsException represents errors when parsing CLI arg schema and args.
 */
public class ArgsException extends Exception {
  private static final long serialVersionUID = 6731998926285681391L;

  private ArgsErrorCode errorCode = ArgsErrorCode.OK;
  private char errorArgumentId = '\0';
  private String errorParameter = "";

  /**
   * ArgsException Constructor.
   *
   * @param code error code
   *
   */
  public ArgsException(ArgsErrorCode code) {
    this.errorCode = code;
  }

  /**
   * ArgsException Constructor.
   *
   * @param code           error code
   * @param errorParameter parameter in error
   */
  public ArgsException(ArgsErrorCode code, String errorParameter) {
    this.errorCode = code;
    this.errorParameter = errorParameter;
  }

  /**
   * ArgsException Constructor.
   *
   * @param code            error code
   * @param errorArgumentId ID of the argument in error
   * @param errorParameter  parameter in error
   */
  public ArgsException(ArgsErrorCode code, char errorArgumentId, String errorParameter) {
    this.errorCode = code;
    this.errorArgumentId = errorArgumentId;
    this.errorParameter = errorParameter;
  }

  /**
   * Get error message.
   *
   * @return error message
   */
  public String errorMessage() {
    switch (this.errorCode) {
      case UNEXPECTED_ARGUMENT:
        return String.format("Argument -%c unexpected.", this.errorArgumentId);
      case MISSING_STRING:
        return String.format("Could not find string parameter for -%c.", this.errorArgumentId);
      case INVALID_INTEGER:
        return String.format("Argument -%c expects an integer but was '%s'.", this.errorArgumentId,
            this.errorParameter);
      case MISSING_INTEGER:
        return String.format("Could not find integer parameter for -%c.", this.errorArgumentId);
      case INVALID_DOUBLE:
        return String.format("Argument -%c expects a double but was '%s'.", this.errorArgumentId,
            this.errorParameter);
      case MISSING_DOUBLE:
        return String.format("Could not find double parameter for -%c.", this.errorArgumentId);
      case INVALID_ARGUMENT_NAME:
        return String.format("'%c' is not a valid argument name.", this.errorArgumentId);
      case INVALID_ARGUMENT_FORMAT:
        return String.format("'%s' is not a valid argument format.", this.errorParameter);
      default:
        return "TILT: Should not get here.";
    }
  }

  public ArgsErrorCode getErrorCode() {
    return this.errorCode;
  }

  public void setErrorArgumentId(char errorArgumentId) {
    this.errorArgumentId = errorArgumentId;
  }

  public void setErrorParameter(String errorParameter) {
    this.errorParameter = errorParameter;
  }
}
