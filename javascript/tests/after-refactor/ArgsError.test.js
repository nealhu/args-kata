const { ArgsError, ErrorCode } = require('../../after-refactor/ArgsError');

describe('#ArgsError', () => {
  describe('#errorMessage()', () => {
    describe('Given code ok', () => {
      it('Should return desired error message', () => {
        const sut = new ArgsError(ErrorCode.OK);
        const msg = sut.errorMessage();
        expect(msg).toEqual('TILT: Should not get here.');
      });
    });
    describe('Given code unexpected argument', () => {
      it('Should return desired error message', () => {
        const sut = new ArgsError(ErrorCode.UNEXPECTED_ARGUMENT);
        sut.setErrorArgumentID('x');
        const msg = sut.errorMessage();
        expect(msg).toEqual('Argument -x unexpected.');
      });
    });
    describe('Given code missing string', () => {
      it('Should return desired error message', () => {
        const sut = new ArgsError(ErrorCode.MISSING_STRING);
        sut.setErrorArgumentID('x');
        const msg = sut.errorMessage();
        expect(msg).toEqual('Could not find string parameter for -x.');
      });
    });
    describe('Given code invalid integer', () => {
      it('Should return desired error message', () => {
        const sut = new ArgsError(ErrorCode.INVALID_INTEGER);
        sut.setErrorArgumentID('x');
        sut.setErrorParameter('bad');
        const msg = sut.errorMessage();
        expect(msg).toEqual('Argument -x expects an integer but was \'bad\'.');
      });
    });
    describe('Given code missing integer', () => {
      it('Should return desired error message', () => {
        const sut = new ArgsError(ErrorCode.MISSING_INTEGER);
        sut.setErrorArgumentID('x');
        const msg = sut.errorMessage();
        expect(msg).toEqual('Could not find integer parameter for -x.');
      });
    });
    describe('Given code invalid double', () => {
      it('Should return desired error message', () => {
        const sut = new ArgsError(ErrorCode.INVALID_DOUBLE);
        sut.setErrorArgumentID('x');
        sut.setErrorParameter('bad');
        const msg = sut.errorMessage();
        expect(msg).toEqual('Argument -x expects a double but was \'bad\'.');
      });
    });
    describe('Given code missing double', () => {
      it('Should return desired error message', () => {
        const sut = new ArgsError(ErrorCode.MISSING_DOUBLE);
        sut.setErrorArgumentID('x');
        const msg = sut.errorMessage();
        expect(msg).toEqual('Could not find double parameter for -x.');
      });
    });
    describe('Given code invalid argument name', () => {
      it('Should return desired error message', () => {
        const sut = new ArgsError(ErrorCode.INVALID_ARGUMENT_NAME);
        sut.setErrorArgumentID('$');
        const msg = sut.errorMessage();
        expect(msg).toEqual('\'$\' is not a valid argument name.');
      });
    });
    describe('Given code invalid argument format', () => {
      it('Should return desired error message', () => {
        const sut = new ArgsError(ErrorCode.INVALID_ARGUMENT_FORMAT);
        sut.setErrorParameter('#$');
        const msg = sut.errorMessage();
        expect(msg).toEqual('\'#$\' is not a valid argument format.');
      });
    });
    describe('Given no code', () => {
      it('Should return an empty error message', () => {
        const sut = new ArgsError();
        sut.setErrorCode(undefined);
        const msg = sut.errorMessage();
        expect(msg).toEqual('');
      });
    });
  });
});
