package android.example.com.magicproject_v1;

import android.example.com.magicproject_v1.classes.Mana;
import android.example.com.magicproject_v1.enums.ManaType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ManaTest {

    @Test
    public void manaCreateMana_isCorrect(){
        Mana redMana = Mana.RedMana(1);
        Mana greenMana = Mana.GreenMana(3);
        Mana blueMana = Mana.BlueMana(2);
        Mana whiteMana = Mana.WhiteMana(4);
        Mana blackMana = Mana.BlackMana(1);
        Mana colorlessMana = Mana.ColorlessMana(6);
        // --- RED MANA ---
        assertEquals(0, redMana.getBlack());
        assertEquals(0, redMana.getBlue());
        assertEquals(0, redMana.getWhite());
        assertEquals(0, redMana.getGreen());
        assertEquals(0, redMana.getColorless());
        assertEquals(1, redMana.getRed());
        // --- GREEN MANA ---
        assertEquals(0, greenMana.getBlack());
        assertEquals(0, greenMana.getBlue());
        assertEquals(0, greenMana.getWhite());
        assertEquals(3, greenMana.getGreen());
        assertEquals(0, greenMana.getColorless());
        assertEquals(0, greenMana.getRed());
        // --- BLUE MANA ---
        assertEquals(0, blueMana.getBlack());
        assertEquals(2, blueMana.getBlue());
        assertEquals(0, blueMana.getWhite());
        assertEquals(0, blueMana.getGreen());
        assertEquals(0, blueMana.getColorless());
        assertEquals(0, blueMana.getRed());
        // --- WHITE MANA ---
        assertEquals(0, whiteMana.getBlack());
        assertEquals(0, whiteMana.getBlue());
        assertEquals(4, whiteMana.getWhite());
        assertEquals(0, whiteMana.getGreen());
        assertEquals(0, whiteMana.getColorless());
        assertEquals(0, whiteMana.getRed());
        // --- BLACK MANA ---
        assertEquals(1, blackMana.getBlack());
        assertEquals(0, blackMana.getBlue());
        assertEquals(0, blackMana.getWhite());
        assertEquals(0, blackMana.getGreen());
        assertEquals(0, blackMana.getColorless());
        assertEquals(0, blackMana.getRed());
        // --- COLORLESS MANA ---
        assertEquals(0, colorlessMana.getBlack());
        assertEquals(0, colorlessMana.getBlue());
        assertEquals(0, colorlessMana.getWhite());
        assertEquals(0, colorlessMana.getGreen());
        assertEquals(6, colorlessMana.getColorless());
        assertEquals(0, colorlessMana.getRed());
    }

    @Test
    public void manaMultipleIntegerValuesConstructor_isCorrect() {
        Mana mana = new Mana(0, 2, 1, 0, 2, 5);

        assertEquals(2, mana.getBlack());
        assertEquals(1, mana.getBlue());
        assertEquals(0, mana.getWhite());
        assertEquals(2, mana.getGreen());
        assertEquals(5, mana.getColorless());
        assertEquals(0, mana.getRed());
    }

    @Test
    public void manaCloneConstructor_isCorrect() {
        Mana mana = new Mana(0, 0, 1, 0, 0, 5);
        Mana manaClone = new Mana(mana);

        assertEquals(0, mana.compareTo(manaClone));
    }

    @Test(expected = NullPointerException.class)
    public void manaCloneConstructor_doesNotAcceptNullValues() {
        Mana mana = null;
        new Mana(mana);
    }

    @Test
    public void manaAdd_isCorrect() {
        Mana mana1 = new Mana(0, 0, 1, 0, 0, 5);
        Mana mana2 = new Mana(3, 2, 1, 1, 4, 0);

        mana1.add(mana2);

        assertEquals(4, mana1.getBlack());
        assertEquals(2, mana1.getBlue());
        assertEquals(1, mana1.getWhite());
        assertEquals(2, mana1.getGreen());
        assertEquals(5, mana1.getColorless());
        assertEquals(3, mana1.getRed());
    }

    @Test
    public void manaGet_isCorrect() {
        Mana mana = new Mana(1, 2, 3, 4, 5, 6);

        assertEquals(1, mana.get(ManaType.RED));
        assertEquals(2, mana.get(ManaType.GREEN));
        assertEquals(3, mana.get(ManaType.BLUE));
        assertEquals(4, mana.get(ManaType.WHITE));
        assertEquals(5, mana.get(ManaType.BLACK));
        assertEquals(6, mana.get(ManaType.COLORLESS));
    }

    @Test
    public void manaSet_isCorrect() {
        Mana mana = new Mana(1, 2, 3, 4, 5, 6);

        mana.set(ManaType.BLACK, 2);
        mana.set(ManaType.BLUE, 2);
        mana.set(ManaType.RED, 5);

        assertEquals(5, mana.getRed());
        assertEquals(2, mana.getGreen());
        assertEquals(2, mana.getBlue());
        assertEquals(4, mana.getWhite());
        assertEquals(2, mana.getBlack());
        assertEquals(6, mana.getColorless());
    }

    @Test
    public void manaContains_isCorrect() {
        Mana mana = new Mana(1, 0, 0, 4, 5, 6);

        assertTrue(mana.contains(ManaType.BLACK));
        assertTrue(mana.contains(ManaType.RED));
        assertTrue(mana.contains(ManaType.WHITE));
        assertTrue(mana.contains(ManaType.COLORLESS));
        assertFalse(mana.contains(ManaType.GREEN));
        assertFalse(mana.contains(ManaType.BLUE));
    }

    @Test
    public void manaConvertedManaCost_isCorrect() {
        Mana mana = new Mana(1, 0, 0, 4, 5, 6);

        assertEquals(16, mana.convertedManaCost());
    }

}
