package args.before;

/**
 * ArgsException represents errors when parsing CLI arg schema and args.
 */
public class ArgsException extends Exception {
  private static final long serialVersionUID = 4589646632983754452L;
  private ArgsErrorCode errorCode;

  public ArgsException(String errorMessage) {
    super(errorMessage);
  }

  public String errorMessage() {
    return this.getMessage();
  }

  public void setErrorCode(ArgsErrorCode code) {
    this.errorCode = code;
  }

  public ArgsErrorCode getErrorCode() {
    return this.errorCode;
  }
}
