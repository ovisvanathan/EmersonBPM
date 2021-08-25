package com.emerson.bpm.dsl;

import nz.org.take.rt.*;


/**
 * Class generated by the take compiler.
 * @version Wed Jun 16 13:40:38 IST 2021
 */
@SuppressWarnings("unchecked")
class KBFragement_whichIsGreater_110 {
    /**
     * Method generated for query whichIsGreater[in,in,out]
     * @param slot1 input parameter generated from slot 0
     * @param slot2 input parameter generated from slot 1
     * @return an iterator for instances of whichIsGreater
    */
    public static ResultSet<whichIsGreater> whichIsGreater_110(
        final long slot1, final long slot2) {
        DerivationController _derivation = new DefaultDerivationController();
        ResultSet<whichIsGreater> _result = new ResultSet(KBFragement_whichIsGreater_110.whichIsGreater_110(
                    slot1, slot2, _derivation), _derivation);

        return _result;
    }

    /**
     * Method generated for query whichIsGreater[in,in,out]
     * @param slot1 input parameter generated from slot 0
     * @param slot2 input parameter generated from slot 1
     * @return an iterator for instances of whichIsGreater
    */
    static ResourceIterator<whichIsGreater> whichIsGreater_110(
        final long slot1, final long slot2,
        final DerivationController _derivation) {
        final int _derivationlevel = _derivation.getDepth();
        ResourceIterator<whichIsGreater> result = new IteratorChain<whichIsGreater>(2) {
                public Object getIteratorOrObject(int pos) {
                    switch (pos) {
                    // AE_PTC01  if greater_than[x,y] then whichIsGreater[x,y,x]
                    case 0:
                        return whichIsGreater_110_0(slot1, slot2,
                            _derivation.reset(_derivationlevel));

                    // AE_PTC02  if greater_than[y,x] then whichIsGreater[x,y,y]
                    case 1:
                        return whichIsGreater_110_1(slot1, slot2,
                            _derivation.reset(_derivationlevel));

                    default:
                        return EmptyIterator.DEFAULT;
                    } // switch(pos)
                } // getIterator()

                public String getRuleRef(int pos) {
                    switch (pos) {
                    // AE_PTC01  if greater_than[x,y] then whichIsGreater[x,y,x]
                    case 0:
                        return "AE_PTC01";

                    // AE_PTC02  if greater_than[y,x] then whichIsGreater[x,y,y]
                    case 1:
                        return "AE_PTC02";

                    default:
                        return "";
                    } // switch(pos)
                } // getRuleRef()
            };

        return result;
    }

    /**
     * Method generated for query whichIsGreater[in,in,out]
     * @param slot1 input parameter generated from slot 0
     * @param slot2 input parameter generated from slot 1
     * @return an iterator for instances of whichIsGreater
    */
    private static ResourceIterator<whichIsGreater> whichIsGreater_110_0(
        final long slot1, final long slot2,
        final DerivationController _derivation) {
        _derivation.log("AE_PTC01", DerivationController.RULE, slot1, slot2,
            DerivationController.NIL);

        // comparing constants in rule head with parameters
        if ((!Constants.x == slot1) || (!Constants.y == slot2)) {
            return EmptyIterator.DEFAULT;
        }

        // Variable bindings in rule:  if greater_than[x,y] then whichIsGreater[x,y,x]
        class bindingsInRule1 {
            // Property generated for term  "x"
            long p1;

            // Property generated for term  "y"
            long p2;
        }
        ;

        final bindingsInRule1 bindings = new bindingsInRule1();
        bindings.p1 = Constants.x;
        bindings.p2 = Constants.y;

        // code for prereq greater_than[x,y]
        final ResourceIterator<greater_than> iterator1 = KBFragement_greater_than_11.greater_than_11(Constants.x,
                Constants.y, _derivation.increaseDepth());

        // code for prereq whichIsGreater[x,y,x]
        final ResourceIterator<whichIsGreater> iterator2 = new NestedIterator<greater_than, whichIsGreater>(iterator1) {
                public ResourceIterator<whichIsGreater> getNextIterator(
                    greater_than object) {
                    bindings.p1 = (long) Constants.x;
                    bindings.p2 = (long) Constants.y;

                    return new SingletonIterator(new whichIsGreater(
                            bindings.p1, bindings.p2, bindings.p1));
                }
            };

        return iterator2;
    }

    /**
     * Method generated for query whichIsGreater[in,in,out]
     * @param slot1 input parameter generated from slot 0
     * @param slot2 input parameter generated from slot 1
     * @return an iterator for instances of whichIsGreater
    */
    private static ResourceIterator<whichIsGreater> whichIsGreater_110_1(
        final long slot1, final long slot2,
        final DerivationController _derivation) {
        _derivation.log("AE_PTC02", DerivationController.RULE, slot1, slot2,
            DerivationController.NIL);

        // comparing constants in rule head with parameters
        if ((!Constants.x == slot1) || (!Constants.y == slot2)) {
            return EmptyIterator.DEFAULT;
        }

        // Variable bindings in rule:  if greater_than[y,x] then whichIsGreater[x,y,y]
        class bindingsInRule2 {
            // Property generated for term  "x"
            long p1;

            // Property generated for term  "y"
            long p2;
        }
        ;

        final bindingsInRule2 bindings = new bindingsInRule2();
        bindings.p1 = Constants.x;
        bindings.p2 = Constants.y;

        // code for prereq greater_than[y,x]
        final ResourceIterator<greater_than> iterator1 = KBFragement_greater_than_11.greater_than_11(Constants.y,
                Constants.x, _derivation.increaseDepth());

        // code for prereq whichIsGreater[x,y,y]
        final ResourceIterator<whichIsGreater> iterator2 = new NestedIterator<greater_than, whichIsGreater>(iterator1) {
                public ResourceIterator<whichIsGreater> getNextIterator(
                    greater_than object) {
                    bindings.p2 = (long) Constants.y;
                    bindings.p1 = (long) Constants.x;

                    return new SingletonIterator(new whichIsGreater(
                            bindings.p1, bindings.p2, bindings.p2));
                }
            };

        return iterator2;
    }
}
