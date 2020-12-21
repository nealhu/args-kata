const sinon = require('sinon');
const { ErrorCode } = require('../../after-refactor/ArgsError');
const { StringArgumentMarshaler } = require('../../after-refactor/StringArgumentMarshaler');

describe('#StringArgumentMarshaler', () => {
  describe('#set', () => {
    describe('Given a valid iterator', () => {
      it('Should read value from the iterator', () => {
        const mockIter = {
          next: sinon.stub(),
        };
        mockIter.next.returns({ done: false, value: 'something' });
        const sut = new StringArgumentMarshaler();

        sut.set(mockIter);
        const value = StringArgumentMarshaler.getValue(sut);

        expect(mockIter.next.calledOnce).toBe(true);
        expect(value).toBe('something');
      });
    });

    describe('Given a iterator with no more value', () => {
      it('Should throw an error', () => {
        const mockIter = {
          next: sinon.stub(),
        };
        mockIter.next.returns({ done: true });
        const sut = new StringArgumentMarshaler();

        let err;
        try {
          sut.set(mockIter);
        } catch (e) {
          err = e;
        }

        expect(mockIter.next.calledOnce).toBe(true);
        expect(err.getErrorCode()).toBe(ErrorCode.MISSING_STRING);
      });
    });
  });

  describe('#getValue', () => {
    describe('Given a marshaler without calling set', () => {
      it('Should return the default value: ""', () => {
        const marshaler = new StringArgumentMarshaler();

        const value = StringArgumentMarshaler.getValue(marshaler);

        expect(value).toBe('');
      });
    });
    describe('Given a non-marshaler object', () => {
      it('Should return the default value: ""', () => {
        const value = StringArgumentMarshaler.getValue(undefined);

        expect(value).toBe('');
      });
    });
  });
});
