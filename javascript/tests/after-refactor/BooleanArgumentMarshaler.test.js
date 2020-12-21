const sinon = require('sinon');
const { BooleanArgumentMarshaler } = require('../../after-refactor/BooleanArgumentMarshaler');

describe('#BooleanArgumentMarshaler', () => {
  describe('#set', () => {
    describe('Given an iterator', () => {
      it('Should set its value to true but should not advance the iterator', () => {
        const mockIter = {
          next: sinon.stub(),
        };
        mockIter.next.returns({ done: false, value: '-b' });
        const sut = new BooleanArgumentMarshaler();

        sut.set(mockIter);
        const value = BooleanArgumentMarshaler.getValue(sut);

        expect(mockIter.next.notCalled).toBe(true);
        expect(value).toBe(true);
      });
    });
  });

  describe('#getValue', () => {
    describe('Given a marshaler without calling set', () => {
      it('Should return the default value: false', () => {
        const marshaler = new BooleanArgumentMarshaler();

        const value = BooleanArgumentMarshaler.getValue(marshaler);

        expect(value).toBe(false);
      });
    });
    describe('Given a non-marshaler object', () => {
      it('Should return the default value: false', () => {
        const value = BooleanArgumentMarshaler.getValue(undefined);

        expect(value).toBe(false);
      });
    });
  });
});
