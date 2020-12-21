const sinon = require('sinon');
const { ErrorCode } = require('../../after-refactor/ArgsError');
const { DoubleArgumentMarshaler } = require('../../after-refactor/DoubleArgumentMarshaler');

describe('#DoubleArgumentMarshaler', () => {
  describe('#set', () => {
    describe('Given a valid iterator', () => {
      it('Should read value from the iterator', () => {
        const mockIter = {
          next: sinon.stub(),
        };
        mockIter.next.returns({ done: false, value: '1.1' });
        const sut = new DoubleArgumentMarshaler();

        sut.set(mockIter);
        const value = DoubleArgumentMarshaler.getValue(sut);

        expect(mockIter.next.calledOnce).toBe(true);
        expect(value).toBe(1.1);
      });
    });

    describe('Given a iterator with bad float format', () => {
      it('Should throw an error', () => {
        const mockIter = {
          next: sinon.stub(),
        };
        mockIter.next.returns({ done: false, value: 'bad' });
        const sut = new DoubleArgumentMarshaler();

        let err;
        try {
          sut.set(mockIter);
        } catch (e) {
          err = e;
        }

        expect(mockIter.next.calledOnce).toBe(true);
        expect(err.getErrorCode()).toBe(ErrorCode.INVALID_DOUBLE);
      });
    });

    describe('Given a iterator with no more value', () => {
      it('Should throw an error', () => {
        const mockIter = {
          next: sinon.stub(),
        };
        mockIter.next.returns({ done: true });
        const sut = new DoubleArgumentMarshaler();

        let err;
        try {
          sut.set(mockIter);
        } catch (e) {
          err = e;
        }

        expect(mockIter.next.calledOnce).toBe(true);
        expect(err.getErrorCode()).toBe(ErrorCode.MISSING_DOUBLE);
      });
    });
  });

  describe('#getValue', () => {
    describe('Given a marshaler without calling set', () => {
      it('Should return the default value: 0', () => {
        const marshaler = new DoubleArgumentMarshaler();

        const value = DoubleArgumentMarshaler.getValue(marshaler);

        expect(value).toBe(0);
      });
    });
    describe('Given a non-marshaler object', () => {
      it('Should return the default value: 0', () => {
        const value = DoubleArgumentMarshaler.getValue(undefined);

        expect(value).toBe(0);
      });
    });
  });
});
