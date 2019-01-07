package enigma;
/** Class that represents a rotating rotor in the enigma machine.
 *  @author alberthan
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    /** permutation. */
    private Permutation _permutation;
    /** notches. */
    private String _notches;

    /** A moving rotor named NAME whose permutation
     * is given by PERM and whose notches is given by NOTCHES.*/
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _permutation = perm;
        _notches = notches;

    }
    @Override
    boolean rotates() {
        return true;
    }
    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            if (_permutation.alphabet().toInt(_notches.charAt(i)) == _setting) {
                return true;
            }
        }
        return false;
    }
    @Override
    void advance() {
        _setting = _permutation.wrap(_setting + 1);
    }
}
