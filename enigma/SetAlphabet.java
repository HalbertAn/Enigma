package enigma;

import static enigma.EnigmaException.*;



/** An class SetAlphabet that takes in a sting and generate a Alphabet.
 * @author albertwrhan
 */
class SetAlphabet extends Alphabet {
    /** An alphabet consisting a string input.
     * * @param input which is a string.
     * */
    SetAlphabet(String input) {
        result = input.toCharArray();
    }

    @Override
    int size() {
        return result.length;
    }

    @Override
    boolean contains(char ch) {
        for (int i = 0; i < result.length; i++) {
            if (Character.toString(ch).equals(result[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    char toChar(int index) {
        if (index > result.length - 1) {
            throw error("character index out of range");
        }
        return result[index];
    }

    @Override
    int toInt(char ch) {
        for (int i = 0; i < result.length; i++) {
            if (ch == result[i]) {
                return i;
            }
        }
        throw error("character out of range");
    }

    /** Range of characters in this Alphabet. */
    private char [] result;

}
