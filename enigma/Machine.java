package enigma;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author alberthan
 */
class Machine {
    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _rotors = new Rotor[_numRotors];
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor x : _allRotors) {
                if (rotors[i].equals(x.name())) {
                    _rotors[i] = x;
                }
            }
        }
        for (int i = 0; i < _rotors.length - 1; i++) {
            if (_rotors[i] == null) {
                throw new EnigmaException("Bad rotor name");
            }
        }

    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        justsetting = setting;
        resetreallength();
        int L = getReallength() - 1;
        if (setting.length() < numPawls() || setting.length() > L) {
            throw new EnigmaException(
                    "wrong settings");
        }
        for (int i = 1; i < setting.length() + 1; i++) {
            char a = setting.charAt(i - 1);
            int b = _alphabet.toInt(a);
            _rotors[i]._setting = _alphabet.toInt(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int convertingoutput = c;
        int rotornumber = _rotors.length;
        int rm = getmovingrotors();
        if (rm != _pawls) {
            throw new EnigmaException("wrong pawl amounts");
        }
        boolean[] rrotates = new boolean[rotornumber];
        for (int i = 1; i < reallength; i++) {
            if (i >= numRotors() - numPawls() && _rotors[i].atNotch()) {
                if (_rotors[i - 1].rotates()) {
                    _rotors[i].advance();
                    rrotates[i] = true;
                    if (!rrotates[i - 1]) {
                        _rotors[i - 1].advance();
                    }
                }
            }
        }
        if (!rrotates[reallength - 1]) {
            _rotors[reallength - 1].advance();
        }
        if (_plugboard != null) {
            convertingoutput = _plugboard.permute(convertingoutput);
        }
        for (int i = reallength - 1; i >= 0; i--) {
            convertingoutput = _rotors[i].convertForward(convertingoutput);
        }
        for (int i = 1; i < reallength; i++) {
            convertingoutput = _rotors[i].convertBackward(convertingoutput);
        }
        if (_plugboard != null) {
            convertingoutput = _plugboard.permute(convertingoutput);
        }
        movingrotor = 0;
        return convertingoutput;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String convertoutput = "";
        char convert;
        for (int i = 0; i < msg.length(); i++) {
            convert = _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
            convertoutput += convert;
        }
        return convertoutput;

    }
    /**
     * @return
     * get the length of the rotor list. */
    int getReallength() {
        for (int k = 0; k < numRotors(); k++) {
            if (_rotors[k] != null) {
                reallength++;
            }
        }
        return reallength;
    }
    /** reset the reallength Lol. */
    void resetreallength() {
        reallength = 0;
    }
    /**
     * @return
     * geting moving rotors LoL. */
    int getmovingrotors() {
        for (int i = 1; i < reallength; i++) {
            if (_rotors[i].rotates()) {
                movingrotor++;
            }
        }
        return movingrotor;
    }

    /** return a listrotors. */
    Rotor[] listrotors() {
        return _rotors;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** the number of Rotors. */
    private int _numRotors;
    /** pawls. */
    private int _pawls;
    /** the collection of all Rotors. */
    private Collection<Rotor> _allRotors;
    /** plugboard setup. */
    private Permutation _plugboard;
    /** A list of Rotors. */
    private Rotor[] _rotors;
    /** output. */
    private String justsetting;
    /** result. */
    private int result;
    /** reallength. */
    private int reallength = 0;
    /** the number of moving rotors. */
    private int movingrotor;
}
