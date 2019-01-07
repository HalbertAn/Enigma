package enigma;


/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author alberthan
 */
class Permutation {

    /** the cycles of the permutation. */
    private String _cycles;

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */

    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        int countleft = 0;
        int countright = 0;
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) == '(') {
                countleft++;
            }
            if (cycles.charAt(i) == ')') {
                countright++;
            }
        }
        if (countleft != countright) {
            throw new EnigmaException("missing parenthesis");
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles = '(' + cycle + ')';
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int iinput = wrap(p);
        char cinput = _alphabet.toChar(iinput);
        char coutput = permute(cinput);
        int ioutput = _alphabet.toInt(coutput);
        return ioutput;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int iinput = wrap(c);
        char cinput = _alphabet.toChar(iinput);
        char coutput = invert(cinput);
        int ioutput = _alphabet.toInt(coutput);
        return ioutput;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        char result = p;
        _cycles.replaceAll(" ", "");
        if (_cycles.equals("")) {
            return result;
        } else {
            for (int i = 0; i < _cycles.length(); i++) {
                if (_cycles.charAt(i) == p) {
                    if (_cycles.charAt(i + 1) == ')') {
                        for (int k = i; k > 0; k--) {
                            if (_cycles.charAt(k - 1) == '(') {
                                result = _cycles.charAt(k);
                                return result;
                            }
                        }
                    } else {
                        result = _cycles.charAt(i + 1);
                        break;
                    }
                }
            }

        }
        return result;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char result = c;
        _cycles.replaceAll(" ", "");
        if (_cycles.equals("")) {
            return result;
        } else {
            for (int i = _cycles.length() - 1; i > 0; i--) {
                if (_cycles.charAt(i) == c) {
                    if (_cycles.charAt(i - 1) == '(') {
                        for (int k = i; k < _cycles.length() - 1; k++) {
                            if (_cycles.charAt(k + 1) == ')') {
                                result = _cycles.charAt(k);
                                return result;
                            }
                        }
                    } else {
                        result = _cycles.charAt(i - 1);
                    }
                }
            }

        }
        return result;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _cycles.length(); i++) {
            if (_cycles.charAt(i + 1) == ')' && _cycles.charAt(i - 1) == '(') {
                return true;
            }
        }
        return false;
    }


    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

}
