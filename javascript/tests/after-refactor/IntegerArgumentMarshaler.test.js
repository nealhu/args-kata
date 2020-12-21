const sinon = require('sinon');
const { ErrorCode } = require('../../after-refactor/ArgsError');
const { IntegerArgumentMarshaler } = require('../../after-refactor/IntegerArgumentMarshaler');

describe('#IntegerArgumentMarshaler', () => {
  describe('#set', () => {
    describe('Given a valid iterator', () => {
      it('Should read value from the iterator', () => {
        const mockIter = {
          next: sinon.stub(),
        };
        mockIter.next.returns({ done: false, value: '10' });
        const sut = new IntegerArgumentMarshaler();

        sut.set(mockIter);
        const value = IntegerArgumentMarshaler.getValue(sut);

        expect(mockIter.next.calledOnce).toBe(true);
        expect(value).toBe(10);
      });
    });

    describe('Given a iterator with bad int format', () => {
      it('Should throw an error', () => {
        const mockIter = {
          next: sinon.stub(),
        };
        mockIter.next.returns({ done: false, value: 'bad' });
        const sut = new IntegerArgumentMarshaler();

        let err;
        try {
          sut.set(mockIter);
        } catch (e) {
          err = e;
        }

        expect(mockIter.next.calledOnce).toBe(true);
        expect(err.getErrorCode()).toBe(ErrorCode.INVALID_INTEGER);
      });
    });

    describe('Given a iterator with no more value', () => {
      it('Should throw an error', () => {
        const mockIter = {
          next: sinon.stub(),
        };
        mockIter.next.returns({ done: true });
        const sut = new IntegerArgumentMarshaler();

        let err;
        try {
          sut.set(mockIter);
        } catch (e) {
          err = e;
        }

        expect(mockIter.next.calledOnce).toBe(true);
        expect(err.getErrorCode()).toBe(ErrorCode.MISSING_INTEGER);
      });
    });
  });

  describe('#getValue', () => {
    describe('Given a marshaler without calling set', () => {
      it('Should return the default value: 0', () => {
        const marshaler = new IntegerArgumentMarshaler();

        const value = IntegerArgumentMarshaler.getValue(marshaler);

        expect(value).toBe(0);
      });
    });
    describe('Given a non-marshaler object', () => {
      it('Should return the default value: 0', () => {
        const value = IntegerArgumentMarshaler.getValue(undefined);

        expect(value).toBe(0);
      });
    });
  });
});
