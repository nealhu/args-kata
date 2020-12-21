const { ArgsError, ErrorCode } = require('./ArgsError');

class IntegerArgumentMarshaler {
  constructor() {
    this.intValue = 0;
  }

  set(argIterator) {
    const { value: parameter, done } = argIterator.next();
    if (done) {
      throw new ArgsError(ErrorCode.MISSING_INTEGER);
    }
    const value = parseInt(parameter, 10);
    if (Number.isNaN(value)) {
      throw new ArgsError(ErrorCode.INVALID_INTEGER, undefined, parameter);
    }
    this.intValue = value;
  }

  static getValue(marshaler) {
    if (marshaler instanceof IntegerArgumentMarshaler) {
      return marshaler.intValue;
    }
    return 0;
  }
}

module.exports = {
  IntegerArgumentMarshaler,
};
