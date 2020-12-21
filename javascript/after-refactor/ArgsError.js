const ErrorCode = {
  OK: 'OK',
  INVALID_ARGUMENT_FORMAT: 'INVALID_ARGUMENT_FORMAT',
  UNEXPECTED_ARGUMENT: 'UNEXPECTED_ARGUMENT',
  INVALID_ARGUMENT_NAME: 'INVALID_ARGUMENT_NAME',
  MISSING_STRING: 'MISSING_STRING',
  MISSING_INTEGER: 'MISSING_INTEGER',
  INVALID_INTEGER: 'INVALID_INTEGER',
  MISSING_DOUBLE: 'MISSING_DOUBLE',
  INVALID_DOUBLE: 'INVALID_DOUBLE',
};

class ArgsError extends Error {
  constructor(errorCode, errorArgumentID, errorParameter) {
    super();
    this.errorCode = errorCode;
    this.errorArgumentID = errorArgumentID;
    this.errorParameter = errorParameter;
    Error.captureStackTrace(this, ArgsError);
  }

  errorMessage() {
    switch (this.errorCode) {
      case ErrorCode.OK:
        return 'TILT: Should not get here.';
      case ErrorCode.UNEXPECTED_ARGUMENT:
        return `Argument -${this.errorArgumentID} unexpected.`;
      case ErrorCode.MISSING_STRING:
        return `Could not find string parameter for -${this.errorArgumentID}.`;
      case ErrorCode.INVALID_INTEGER:
        return `Argument -${this.errorArgumentID} expects an integer but was '${this.errorParameter}'.`;
      case ErrorCode.MISSING_INTEGER:
        return `Could not find integer parameter for -${this.errorArgumentID}.`;
      case ErrorCode.INVALID_DOUBLE:
        return `Argument -${this.errorArgumentID} expects a double but was '${this.errorParameter}'.`;
      case ErrorCode.MISSING_DOUBLE:
        return `Could not find double parameter for -${this.errorArgumentID}.`;
      case ErrorCode.INVALID_ARGUMENT_NAME:
        return `'${this.errorArgumentID}' is not a valid argument name.`;
      case ErrorCode.INVALID_ARGUMENT_FORMAT:
        return `'${this.errorParameter}' is not a valid argument format.`;
      default:
        return '';
    }
  }

  setErrorCode(errorCode) {
    this.errorCode = errorCode;
  }

  getErrorCode() {
    return this.errorCode;
  }

  setErrorArgumentID(errorArgumentID) {
    this.errorArgumentID = errorArgumentID;
  }

  setErrorParameter(errorParameter) {
    this.errorParameter = errorParameter;
  }
}

module.exports = {
  ArgsError,
  ErrorCode,
};
