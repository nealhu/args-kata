const { ArgsError } = require('./ArgsError');

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

class Args {
  constructor(schema, args) {
    this.schema = schema;
    this.args = args;
    this.booleanArgs = new Map();
    this.intArgs = new Map();
    this.stringArgs = new Map();
    this.doubleArgs = new Map();
    this.currentArgument = 0;
    this.parse();
  }

  parse() {
    if (this.schema.length === 0 && this.args.length === 0) {
      return;
    }
    this.parseSchema();
    this.parseArguments();
  }

  parseSchema() {
    this.schema.split(',')
      .filter((element) => element.length > 0)
      .forEach((element) => {
        const trimmedElement = element.trim();
        this.parseSchemaElement(trimmedElement);
      });
    return true;
  }

  parseSchemaElement(element) {
    const elementID = element[0];

    const elementTail = element.substring(1);

    this.validateSchemaElementID(elementID);

    if (this.isBooleanSchemaElement(elementTail)) {
      this.parseBooleanSchemaElement(elementID);
    } else if (this.isStringSchemaElement(elementTail)) {
      this.parseStringSchemaElement(elementID);
    } else if (this.isIntegerSchemaElement(elementTail)) {
      this.parseIntegerSchemaElement(elementID);
    } else if (this.isDoubleSchemaElement(elementTail)) {
      this.parseDoubleSchemaElement(elementID);
    } else {
      throw this.createArgsError(`'${elementTail}' is not a valid argument format.`, ErrorCode.INVALID_ARGUMENT_FORMAT);
    }
  }

  validateSchemaElementID(elementID) {
    if (elementID.length !== 1 || !elementID.match(/[a-z]/i)) {
      throw this.createArgsError(`'${elementID}' is not a valid argument name.`, ErrorCode.INVALID_ARGUMENT_NAME);
    }
  }

  parseBooleanSchemaElement(elementID) {
    this.booleanArgs.set(elementID, false);
  }

  parseIntegerSchemaElement(elementID) {
    this.intArgs.set(elementID, 0);
  }

  parseStringSchemaElement(elementID) {
    this.stringArgs.set(elementID, '');
  }

  parseDoubleSchemaElement(elementID) {
    this.doubleArgs.set(elementID, 0);
  }

  isStringSchemaElement(elementTail) {
    return elementTail === '*';
  }

  isBooleanSchemaElement(elementTail) {
    return elementTail.length === 0;
  }

  isIntegerSchemaElement(elementTail) {
    return elementTail === '#';
  }

  isDoubleSchemaElement(elementTail) {
    return elementTail === '##';
  }

  parseArguments() {
    for (this.currentArgument = 0;
      this.currentArgument < this.args.length;
      this.currentArgument += 1) {
      const arg = this.args[this.currentArgument];
      this.parseArgument(arg);
    }
    return true;
  }

  parseArgument(arg) {
    if (!arg.startsWith('-')) {
      throw this.createArgsError(`'${arg}' is not a valid argument format.`, ErrorCode.INVALID_ARGUMENT_FORMAT);
    }
    this.parseElements(arg);
  }

  parseElements(arg) {
    for (let i = 1; i < arg.length; i += 1) {
      this.setArgument(arg[i]);
    }
  }

  setArgument(argChar) {
    if (this.isBooleanArg(argChar)) {
      this.setBooleanArg(argChar, true);
    } else if (this.isStringArg(argChar)) {
      this.setStringArg(argChar);
    } else if (this.isIntArg(argChar)) {
      this.setIntArg(argChar);
    } else if (this.isDoubleArg(argChar)) {
      this.setDoubleArg(argChar);
    } else {
      throw this.createArgsError(`Argument -${argChar} unexpected.`, ErrorCode.UNEXPECTED_ARGUMENT);
    }

    return true;
  }

  isIntArg(argChar) {
    return this.intArgs.has(argChar);
  }

  setIntArg(argChar) {
    this.currentArgument += 1;
    if (this.currentArgument >= this.args.length) {
      throw this.createArgsError(
        `Could not find integer parameter for -${argChar}.`,
        ErrorCode.MISSING_INTEGER,
      );
    }
    const parameter = this.args[this.currentArgument];
    const value = parseInt(parameter, 10);
    if (Number.isNaN(value)) {
      throw this.createArgsError(
        `Argument -${argChar} expects an integer but was '${parameter}'.`,
        ErrorCode.INVALID_INTEGER,
      );
    }
    this.intArgs.set(argChar, value);
  }

  createArgsError(msg, code) {
    const err = new ArgsError(msg);
    err.setErrorCode(code);
    return err;
  }

  setStringArg(argChar) {
    this.currentArgument += 1;
    if (this.currentArgument >= this.args.length) {
      throw this.createArgsError(
        `Could not find string parameter for -${argChar}.`,
        ErrorCode.MISSING_STRING,
      );
    }
    this.stringArgs.set(argChar, this.args[this.currentArgument]);
  }

  isStringArg(argChar) {
    return this.stringArgs.has(argChar);
  }

  setBooleanArg(argChar, value) {
    this.booleanArgs.set(argChar, value);
  }

  isBooleanArg(argChar) {
    return this.booleanArgs.has(argChar);
  }

  setDoubleArg(argChar) {
    this.currentArgument += 1;
    if (this.currentArgument >= this.args.length) {
      throw this.createArgsError(
        `Could not find double parameter for -${argChar}.`,
        ErrorCode.MISSING_DOUBLE,
      );
    }
    const parameter = this.args[this.currentArgument];
    const value = parseFloat(parameter, 10);
    if (Number.isNaN(value)) {
      throw this.createArgsError(
        `Argument -${argChar} expects a double but was '${parameter}'.`,
        ErrorCode.INVALID_DOUBLE,
      );
    }
    this.doubleArgs.set(argChar, value);
  }

  isDoubleArg(argChar) {
    return this.doubleArgs.has(argChar);
  }

  getString(arg) {
    return this.stringArgs.get(arg);
  }

  getInt(arg) {
    return this.intArgs.get(arg);
  }

  getBoolean(arg) {
    return this.booleanArgs.get(arg);
  }

  getDouble(arg) {
    return this.doubleArgs.get(arg);
  }
}

module.exports = {
  Args,
  ErrorCode,
  ArgsError,
};
