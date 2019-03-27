package pl.compprog;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Class used to test whether sudoku board was properly generated.
 */
public class SudokuBoardTest {

    private final int sizeOfSudoku = 9;

    /**
     * Tests whether each value of sudoku board is valid according to sudoku
     * rules
     */
    @Test
    public void testValidnessOfSudoku() {
        SudokuBoard sudokuBoard = new SudokuBoard();
        sudokuBoard.fillBoard();
        for (int i = 0; i < sizeOfSudoku; i++)
            for (int j = 0; j < sizeOfSudoku; j++) {
                assertTrue(sudokuBoard.canBePlaced(i, j,
                        sudokuBoard.getValueAt(i, j)));
            }
    }

    /**
     * Tests whether two subsequent calls of fillBoard generates different
     * digits layout on the board
     */
    @Test
    public void testRandomnessOfSudoku() {
        int counterSame = 0;
        SudokuBoard sudokuBoard1 = new SudokuBoard();
        SudokuBoard sudokuBoard2 = new SudokuBoard();
        sudokuBoard1.fillBoard();
        sudokuBoard2.fillBoard();
        assertFalse(sudokuBoard1.equals(sudokuBoard2));
        assertFalse(sudokuBoard2.equals(sudokuBoard1));
    }
}