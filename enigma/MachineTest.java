package enigma;

import org.junit.Test;

import java.util.ArrayList;

import static enigma.TestUtils.*;

import static org.junit.Assert.*;

public class MachineTest {
    /** create all the rotors. */
    private ArrayList<Rotor> allrotors = new ArrayList<Rotor>();
    /** create machine, */
    private Machine machine;
    /** create the inserting rotor. */
    private String[] insertingrotors = {"B", "BETA", "III", "IV", "I"};
    /** taking the example from the course website. */
    private Permutation _permI = new Permutation(
            "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
    private Permutation _permII = new Permutation(
            "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)", UPPER);
    private Permutation _permIII = new Permutation(
            "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", UPPER);
    private Permutation _permIV = new Permutation(
            "(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", UPPER);
    private Permutation _permV = new Permutation(
            "(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)", UPPER);
    private Permutation _permVI = new Permutation(
            "(AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)", UPPER);
    private Permutation _permVII = new Permutation(
            "(ANOUPFRIMBZTLWKSVEGCJYDHXQ)", UPPER);
    private Permutation _permVIII = new Permutation(
            "(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)", UPPER);
    private Permutation _permBeta = new Permutation(
            "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", UPPER);
    private Permutation _permGamma = new Permutation(
            "(AFNIRLBSQWVXGUZDKMTPCOYJHE)", UPPER);
    private Permutation _permReflectorB = new Permutation(
            "(AE) (BN) (CK) (DQ) (FU) (GY) "
                    + "(HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", UPPER);
    private Permutation _permReflectorC = new Permutation(
            "(AR) (BD) (CO) (EJ) (FN) (GT) "
                    + "(HK) (IV) (LM) (PW) (QZ) (SX) (UY)", UPPER);
    private MovingRotor _rotor1 = new MovingRotor(
            "I", _permI, "Q");
    private MovingRotor _rotor2 = new MovingRotor(
            "II", _permII, "E");
    private MovingRotor _rotor3 = new MovingRotor(
            "III", _permIII, "V");
    private MovingRotor _rotor4 = new MovingRotor(
            "IV", _permIV, "J");
    private MovingRotor _rotor5 = new MovingRotor(
            "V", _permV, "Z");
    private MovingRotor _rotor6 = new MovingRotor(
            "VI", _permVI, "ZM");
    private MovingRotor _rotor7 = new MovingRotor(
            "VII", _permVII, "ZM");
    private MovingRotor _rotor8 = new MovingRotor(
            "VIII", _permVIII, "ZM");
    private FixedRotor _fixedbeta = new FixedRotor(
            "BETA", _permBeta);
    private FixedRotor _fixedgamma = new FixedRotor(
            "GAMMA", _permGamma);
    private Reflector _reflectorb = new Reflector(
            "B", _permReflectorB);
    private Reflector _reflectorc = new Reflector(
            "C", _permReflectorC);

    @Test
    public void insertrotorsTest() {
        allrotors.add(_rotor1);
        allrotors.add(_rotor2);
        allrotors.add(_rotor3);
        allrotors.add(_rotor4);
        allrotors.add(_rotor5);
        allrotors.add(_rotor6);
        allrotors.add(_rotor7);
        allrotors.add(_rotor8);
        allrotors.add(_fixedbeta);
        allrotors.add(_fixedgamma);
        allrotors.add(_reflectorb);
        allrotors.add(_reflectorc);
        machine = new Machine(UPPER, 5, 3, allrotors);
        machine.insertRotors(insertingrotors);
        assertEquals(allrotors.get(10), machine.listrotors()[0]);
        assertEquals(allrotors.get(0), machine.listrotors()[4]);
        assertEquals(allrotors.get(2), machine.listrotors()[2]);
        assertEquals(allrotors.get(8), machine.listrotors()[1]);
    }

    @Test
    public void setrotorsTest() {
        allrotors.add(_rotor1);
        allrotors.add(_rotor2);
        allrotors.add(_rotor3);
        allrotors.add(_rotor4);
        allrotors.add(_rotor5);
        allrotors.add(_rotor6);
        allrotors.add(_rotor7);
        allrotors.add(_rotor8);
        allrotors.add(_fixedbeta);
        allrotors.add(_fixedgamma);
        allrotors.add(_reflectorb);
        allrotors.add(_reflectorc);
        machine = new Machine(UPPER, 5, 3, allrotors);
        machine.insertRotors(insertingrotors);
        machine.setRotors("AXLE");
        assertEquals(0, machine.listrotors()[1].setting());
        assertEquals(23, machine.listrotors()[2].setting());
        assertEquals(11, machine.listrotors()[3].setting());
        assertEquals(4, machine.listrotors()[4].setting());
    }

    @Test
    public void convertTest() {
        allrotors.add(_rotor1);
        allrotors.add(_rotor2);
        allrotors.add(_rotor3);
        allrotors.add(_rotor4);
        allrotors.add(_rotor5);
        allrotors.add(_rotor6);
        allrotors.add(_rotor7);
        allrotors.add(_rotor8);
        allrotors.add(_fixedbeta);
        allrotors.add(_fixedgamma);
        allrotors.add(_reflectorb);
        allrotors.add(_reflectorc);
        machine = new Machine(UPPER, 5, 3, allrotors);
        machine.insertRotors(insertingrotors);
        machine.setRotors("AXLE");
        machine.setPlugboard(new Permutation("(YF) (ZH)", UPPER));
        assertEquals(25, machine.convert(24));
        assertEquals("SDLK", machine.convert("YEGH"));
    }
}













