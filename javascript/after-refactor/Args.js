const { ArgsError, ErrorCode } = require('./ArgsError');
const { BooleanArgumentMarshaler } = require('./BooleanArgumentMarshaler');
const { IntegerArgumentMarshaler } = require('./IntegerArgumentMarshaler');
const { DoubleArgumentMarshaler } = require('./DoubleArgumentMarshaler');
const { StringArgumentMarshaler } = require('./StringArgumentMarshaler');

class Args {
  constructor(schema, args) {
    this.marshalers = new Map();
    this.currentArgument = undefined;
    this.parseSchema(schema);
    this.parseArgumentStrings(args);
  }

  parseSchema(schema) {
    schema.split(',')
      .filter((element) => element.length > 0)
      .forEach((element) => this.parseSchemaElement(element.trim()));
  }

  parseSchemaElement(element) {
    const elementID = element[0];
    const elementTail = element.substring(1);
    this.validateSchemaElementId(elementID);
    if (elementTail.length === 0) {
      this.marshalers.set(elementID, new BooleanArgumentMarshaler());
    } else if (elementTail === '*') {
      this.marshalers.set(elementID, new StringArgumentMarshaler());
    } else if (elementTail === '#') {
      this.marshalers.set(elementID, new IntegerArgumentMarshaler());
    } else if (elementTail === '##') {
      this.marshalers.set(elementID, new DoubleArgumentMarshaler());
    } else {
      throw new ArgsError(ErrorCode.INVALID_ARGUMENT_FORMAT, elementID, elementTail);
    }
  }

  validateSchemaElementId(elementID) {
    if (elementID.length !== 1 || !elementID.match(/[a-z]/i)) {
      throw new ArgsError(ErrorCode.INVALID_ARGUMENT_NAME, elementID);
    }
  }

  parseArgumentStrings(args) {
    if (args.length === 0) {
      return;
    }
    this.currentArgument = args[Symbol.iterator]();
    for (;;) {
      const { value: argString, done } = this.currentArgument.next();
      if (done) {
        break;
      }
      if (argString.startsWith('-')) {
        this.parseArgumentCharacters(argString.substring(1));
      } else {
        throw new ArgsError(ErrorCode.INVALID_ARGUMENT_FORMAT, undefined, argString);
      }
    }
  }

  parseArgumentCharacters(argChars) {
    for (let i = 0; i < argChars.length; i += 1) {
      this.parseArgumentCharacter(argChars[i]);
    }
  }

  parseArgumentCharacter(argChar) {
    const m = this.marshalers.get(argChar);
    if (!m) {
      throw new ArgsError(ErrorCode.UNEXPECTED_ARGUMENT, argChar);
    } else {
      try {
        m.set(this.currentArgument);
      } catch (e) {
        e.setErrorArgumentID(argChar);
        throw e;
      }
    }
  }

  getBoolean(arg) {
    return BooleanArgumentMarshaler.getValue(this.marshalers.get(arg));
  }

  getString(arg) {
    return StringArgumentMarshaler.getValue(this.marshalers.get(arg));
  }

  getInt(arg) {
    return IntegerArgumentMarshaler.getValue(this.marshalers.get(arg));
  }

  getDouble(arg) {
    return DoubleArgumentMarshaler.getValue(this.marshalers.get(arg));
  }
}

module.exports = {
  Args,
  ErrorCode,
  ArgsError,
};
