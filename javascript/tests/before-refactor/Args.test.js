const { Args, ErrorCode } = require('../../before-refactor/Args');

describe('#Args before refactoring', () => {
  describe('Given a valid schema "i#,b,d##,s*"', () => {
    const schema = 'i#,b,d##,s*';
    describe('Given a valid argument list "-i 10 -b -d 1.1 -s something"', () => {
      const args = ['-i', '10', '-b', '-d', '1.1', '-s', 'something'];
      const sut = new Args(schema, args);
      it('Should return the desired string value when asked about the string argument', () => {
        const strArg = sut.getString('s');
        expect(strArg).toEqual('something');
      });
      it('Should return the desired int value when asked about the int argument', () => {
        const intArg = sut.getInt('i');
        expect(intArg).toEqual(10);
      });
      it('Should return the desired double value when asked about the double argument', () => {
        const doubleArg = sut.getDouble('d');
        expect(doubleArg).toEqual(1.1);
      });
      it('Should return the desired boolean value when asked about the boolean argument', () => {
        const boolArg = sut.getBoolean('b');
        expect(boolArg).toEqual(true);
      });
    });

    describe('Order in the argument list should not matter', () => {
      const args = ['-i', '10', '-b', '-d', '1.1', '-s', 'something'];
      const sut = new Args(schema, args);
      const argsReorder = ['-b', '-d', '1.1', '-s', 'something', '-i', '10'];
      const sutReorder = new Args(schema, argsReorder);
      expect(sut.getBoolean('b')).toEqual(sutReorder.getBoolean('b'));
      expect(sut.getDouble('d')).toEqual(sutReorder.getDouble('d'));
      expect(sut.getString('s')).toEqual(sutReorder.getString('s'));
      expect(sut.getInt('i')).toEqual(sutReorder.getInt('i'));
    });

    describe('Given no argumenet', () => {
      const args = [];
      const sut = new Args(schema, args);
      it('Should default double argument to 0', () => {
        const doubleArg = sut.getDouble('d');
        expect(doubleArg).toEqual(0);
      });
      it('Should default int argument to 0', () => {
        const intArg = sut.getInt('i');
        expect(intArg).toEqual(0);
      });
      it('Should default boolean argument to false', () => {
        const boolArg = sut.getBoolean('b');
        expect(boolArg).toEqual(false);
      });
      it('Should default string argument to ""', () => {
        const strArg = sut.getString('s');
        expect(strArg).toEqual('');
      });
    });

    describe('Given an argument with no -', () => {
      it('Should throw an error', () => {
        const args = ['i', '10'];
        const sut = () => new Args(schema, args);
        let err;
        try {
          sut();
        } catch (e) {
          err = e;
        }
        const errMsg = err.errorMessage();
        const errCode = err.getErrorCode();
        expect(errMsg).toEqual('\'i\' is not a valid argument format.');
        expect(errCode).toEqual(ErrorCode.INVALID_ARGUMENT_FORMAT);
      });
    });

    it('Should throw error if an int argument has an invalid value', () => {
      const args = ['-i', 'bad'];
      const sut = () => new Args(schema, args);
      let err;
      try {
        sut();
      } catch (e) {
        err = e;
      }
      const errMsg = err.errorMessage();
      const errCode = err.getErrorCode();
      expect(errMsg).toEqual('Argument -i expects an integer but was \'bad\'.');
      expect(errCode).toEqual(ErrorCode.INVALID_INTEGER);
    });

    it('Should throw error if a double argument has an invalid value', () => {
      const args = ['-d', 'bad'];
      const sut = () => new Args(schema, args);
      let err;
      try {
        sut();
      } catch (e) {
        err = e;
      }
      const errMsg = err.errorMessage();
      const errCode = err.getErrorCode();
      expect(errMsg).toEqual('Argument -d expects a double but was \'bad\'.');
      expect(errCode).toEqual(ErrorCode.INVALID_DOUBLE);
    });

    it('Should throw error if asked about an arguement that does not exist', () => {
      const args = ['-n', '10'];
      const sut = () => new Args(schema, args);
      let err;
      try {
        sut();
      } catch (e) {
        err = e;
      }
      const errMsg = err.errorMessage();
      const errCode = err.getErrorCode();
      expect(errMsg).toEqual('Argument -n unexpected.');
      expect(errCode).toEqual(ErrorCode.UNEXPECTED_ARGUMENT);
    });

    it('Should throw error when an integer argument misses its value', () => {
      const args = ['-i'];
      const sut = () => new Args(schema, args);
      let err;
      try {
        sut();
      } catch (e) {
        err = e;
      }
      const errMsg = err.errorMessage();
      const errCode = err.getErrorCode();
      expect(errMsg).toEqual('Could not find integer parameter for -i.');
      expect(errCode).toEqual(ErrorCode.MISSING_INTEGER);
    });

    it('Should throw error when a string argument misses its value', () => {
      const args = ['-s'];
      const sut = () => new Args(schema, args);
      let err;
      try {
        sut();
      } catch (e) {
        err = e;
      }
      const errMsg = err.errorMessage();
      const errCode = err.getErrorCode();
      expect(errMsg).toEqual('Could not find string parameter for -s.');
      expect(errCode).toEqual(ErrorCode.MISSING_STRING);
    });

    it('Should throw error when a double argument misses its value', () => {
      const args = ['-d'];
      const sut = () => new Args(schema, args);
      let err;
      try {
        sut();
      } catch (e) {
        err = e;
      }
      const errMsg = err.errorMessage();
      const errCode = err.getErrorCode();
      expect(errMsg).toEqual('Could not find double parameter for -d.');
      expect(errCode).toEqual(ErrorCode.MISSING_DOUBLE);
    });
  });

  describe('Given a schema with invalid argument name', () => {
    it('Should throw an error', () => {
      const schema = '$#';
      const sut = () => new Args(schema, []);
      let err;
      try {
        sut();
      } catch (e) {
        err = e;
      }
      const errMsg = err.errorMessage();
      const errCode = err.getErrorCode();
      expect(errMsg).toEqual('\'$\' is not a valid argument name.');
      expect(errCode).toEqual(ErrorCode.INVALID_ARGUMENT_NAME);
    });
  });

  describe('Given a schema with invalid type syntax', () => {
    it('Should throw an error', () => {
      const schema = 'i#^';
      const sut = () => new Args(schema, []);
      let err;
      try {
        sut();
      } catch (e) {
        err = e;
      }
      const errMsg = err.errorMessage();
      const errCode = err.getErrorCode();
      expect(errMsg).toEqual('\'#^\' is not a valid argument format.');
      expect(errCode).toEqual(ErrorCode.INVALID_ARGUMENT_FORMAT);
    });
  });

  describe('Given an empty schema and an empty argument list', () => {
    it('Should not throw error', () => {
      const sut = () => new Args('', []);
      let err;
      try {
        sut();
      } catch (e) {
        err = e;
      }
      expect(err).toBeUndefined();
    });
  });
});
