class BooleanArgumentMarshaler {
  constructor() {
    this.booleanValue = false;
  }

  set() {
    this.booleanValue = true;
  }

  static getValue(marshaler) {
    if (marshaler instanceof BooleanArgumentMarshaler) {
      return marshaler.booleanValue;
    }
    return false;
  }
}

module.exports = {
  BooleanArgumentMarshaler,
};
