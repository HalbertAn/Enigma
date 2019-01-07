package enigma;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static enigma.EnigmaException.*;

/** Enigma simulator.
 * @author alberthan
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine enigma = readConfig();
        insertrotors = new String[enigma.numRotors()];
        while (_input.hasNext()) {
            int i = 0;
            String firstline = _input.nextLine();
            String[] settings = firstline.split(" ");
            if (firstline.length() == 0) {
                _output.println();
                continue;
            }
            if (!settings[0].equals("*")) {
                throw error("Wrong input");
            }
            if (settings[0].equals("*")) {
                for (; i < enigma.numRotors(); i++) {
                    insertrotors[i] = settings[i + 1];
                }
                enigma.insertRotors(insertrotors);
                Rotor[] a = enigma.listrotors();
                if (!a[0].reflecting()) {
                    throw new EnigmaException(
                            "Reflector in the wrong place");
                }
                for (int k = 0; k < enigma.numRotors() - 2; k++) {
                    for (int j = k + 1; j < enigma.numRotors() - 1; j++) {
                        if (a[k].name() == a[j].name()) {
                            throw new EnigmaException(
                                    "Duplicate rotor names");
                        }
                    }
                }
                enigma.resetreallength();
                rotorsettings = settings[enigma.getReallength() + 1];
                setUp(enigma, rotorsettings);
                if (settings.length > enigma.numRotors() + 2) {
                    for (int k = enigma.numRotors()
                            + 2; k < settings.length; k++) {
                        plugboardsettings += " " + settings[k];
                    }
                    enigma.setPlugboard(
                            new Permutation(plugboardsettings, _alphabet));
                }
                if (_input.hasNext("\\*")) {
                    System.exit(0);
                }
                if (!_input.hasNext("\\*")) {
                    while (_input.hasNextLine() && !_input.hasNext("\\*")) {
                        inputmessage = inputmessage
                                + _input.nextLine().toUpperCase();
                        inputmessage = inputmessage.replaceAll("\\s*", "");
                        convertedmessage = enigma.convert(inputmessage);
                        printMessageLine(convertedmessage);
                        inputmessage = "";
                    }
                }
            }
        }
    }


    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            range = _config.next();
            if (range.charAt(1) == '-') {
                _alphabet = new CharacterRange(
                        range.charAt(0), range.charAt(2));
            } else {
                _alphabet = new SetAlphabet(range);
            }
            int numrotors = _config.nextInt();
            int numpawls = _config.nextInt();
            if (numrotors <= numpawls) {
                throw error("Wrong amounts of rotors and pawls");
            }
            while (_config.hasNext()) {
                allrotors.add(readRotor());
            }
            return new Machine(_alphabet, numrotors, numpawls, allrotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        } catch (IndexOutOfBoundsException excp) {
            throw error("Invalid alphabet");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            rotorname = _config.next().toUpperCase();
            tynotch = _config.next();
            while (_config.hasNext("\\(+.+")) {
                cycle += " " + _config.next();
            }
            curpermutation = new Permutation(cycle, _alphabet);
            cycle = "";
            if (tynotch.charAt(0) == 'M') {
                return new MovingRotor(
                        rotorname, curpermutation, tynotch.substring(1));
            }
            if (tynotch.charAt(0) == 'N') {
                return new FixedRotor(rotorname, curpermutation);
            }
            if (tynotch.charAt(0) == 'R') {
                return new Reflector(
                        rotorname, curpermutation);
            } else {
                throw error("Missing rotor information");
            }

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        outputlength = msg.length();
        int a = msg.length();
        if (outputlength > 5) {
            int end = 5;
            while (outputlength > 5) {
                _output.print(msg.substring(end - 5, end));
                _output.print(" ");
                end += 5;
                outputlength -= 5;
            }
            _output.println(msg.substring(a - outputlength, a));
        } else {
            _output.print(msg);
            _output.println();
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** all the rotors. */
    private ArrayList<Rotor> allrotors = new ArrayList<>();

    /** the range of the alphabet of the rotors. */
    private String range;

    /** rotortype_notch. */
    private String tynotch;

    /** new permutation. */
    private Permutation curpermutation;

    /** the initial cycle. */
    private String cycle = "";

    /** the output length. */
    private int outputlength;

    /** rotor names. */
    private String rotorname;

    /** insertion rotor. */
    private String[] insertrotors;

    /** Rotor settings. */
    private String rotorsettings;

    /** Plugboard settings. */
    private String plugboardsettings = "";

    /** input message. */
    private String inputmessage = "";

    /** converted message. */
    private String convertedmessage;

}
