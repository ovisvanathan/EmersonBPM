package com.emerson.bpm.dsl;

import com.emerson.bpm.dsl.*;

import nz.org.take.rt.*;


/**
 * Class generated by the take compiler.
 * @version Wed Jun 16 13:15:35 IST 2021
 */
@SuppressWarnings("unchecked")
class KBFragement_greater_than_11 {
    /**
     * Method generated for query greater_than[in,in]
     * @param nz.org.take.compiler.reference.Slot@409bf450
     * @param nz.org.take.compiler.reference.Slot@38d8f54a
     * @return an iterator
     * code generated using velocity template Comparison_11.vm
    */
    static ResourceIterator<greater_than> greater_than_11(final double slot1,
        final double slot2, final DerivationController _derivation) {
        _derivation.log(">", DerivationController.COMPARISON);

        if (slot1 > slot2) {
            greater_than result = new greater_than();
            result.slot1 = slot1;
            result.slot2 = slot2;

            return new SingletonIterator<greater_than>(result);
        }

        return EmptyIterator.DEFAULT;
    }
}
