class ArgsError extends Error {
  constructor(...params) {
    super(...params);
    Error.captureStackTrace(this, ArgsError);
  }

  errorMessage() {
    return this.message;
  }

  setErrorCode(code) {
    this.code = code;
  }

  getErrorCode() {
    return this.code;
  }
}

module.exports = {
  ArgsError,
};
