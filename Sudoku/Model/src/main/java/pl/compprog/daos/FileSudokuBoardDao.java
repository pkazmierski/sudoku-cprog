package pl.compprog.daos;


import pl.compprog.logs.FileAndConsoleLoggerFactory;
import pl.compprog.exceptions.ApplicationException;
import pl.compprog.exceptions.DaoException;
import pl.compprog.sudoku.SudokuBoard;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface representing an abstract DAO object.
 */
public class FileSudokuBoardDao extends AbstractDao<SudokuBoard> {

    private static final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(FileSudokuBoardDao.class.getName());

    /**
     * Name of processed file.
     */
    private final String filename;

    /**
     * FileOutputStream object used to write
     * an object to file.
     */
    private FileOutputStream fos;

    /**
     * ObjectOutputStream object used to write
     * an object to file.
     */
    private ObjectOutputStream oos;

    /**
     * FileInputStream object used to read
     * an object from file.
     */
    private FileInputStream fis;

    /**
     * ObjectInputStream object used to read
     * an object from file.
     */
    private ObjectInputStream ois;

    /**
     * FileSudokuBoardDao constructor.
     *
     *@param filename1 Name of processed file
     */
    public FileSudokuBoardDao(final String filename1) throws ApplicationException {
        if (filename1 == null) {
            throw new DaoException(DaoException.NULL_NAME);
        }
        filename = filename1;
    }

    public FileSudokuBoardDao(final String filename1, boolean wasGenerated[][]) throws ApplicationException {
        if (filename1 == null) {
            throw new DaoException(DaoException.NULL_NAME);
        }
        if (wasGenerated == null) {
            throw new DaoException(DaoException.NULL_BOARD);
        }
        filename = filename1;
        this.wasGenerated = wasGenerated;
    }

    /**
     * Reads SudokuBoard object from a file
     * and returns it.
     *
     *@return SudokuBoard object read form the file
     */
    @SuppressWarnings("Duplicates")
    @Override
    public final SudokuBoard readEx() throws DaoException {
        try {
            if (fis == null) {
                fis = new FileInputStream(filename);
                ois = new ObjectInputStream(fis);
            }
            if (wasGenerated != null) {
                wasGenerated = (boolean[][]) ois.readObject();
            }
            return (SudokuBoard) ois.readObject();
        } catch (IOException ioex) {
            throw new DaoException(DaoException.NULL_FILE, ioex);
        } catch (ClassNotFoundException cnfex) {
            throw new DaoException(DaoException.INVALID_CAST, cnfex);
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public SudokuBoard read() {
        try {
            if (fis == null) {
                fis = new FileInputStream(filename);
                ois = new ObjectInputStream(fis);
            }
            if (wasGenerated != null) {
                wasGenerated = (boolean[][]) ois.readObject();
            }
            return (SudokuBoard) ois.readObject();
        } catch (IOException ioex) {
            logger.log(Level.SEVERE, getDaoMessage(DaoException.NULL_FILE), ioex);
        } catch (ClassNotFoundException cnfex) {
            logger.log(Level.SEVERE, getDaoMessage(DaoException.INVALID_CAST), cnfex);
        }
        return null;
    }


    /**
     * Writes SudokuBoard object to a file.
     *
     * @param obj SudokuBoard object to be written
     */
    @SuppressWarnings("Duplicates")
    @Override
    public final void writeEx(final SudokuBoard obj) throws DaoException {
        if (obj == null) {
            throw new DaoException(DaoException.NULL_BOARD);
        }
        File file = new File(filename);
        try {
            if (fos == null) {
                fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(fos);
            }
            if (wasGenerated != null) {
                oos.writeObject(wasGenerated);
            }
            oos.writeObject(obj);
        } catch (IOException ioex) {
            throw new DaoException(DaoException.OPEN_ERROR, ioex);
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void write(SudokuBoard obj) {
        if (obj == null) {
            logger.log(Level.SEVERE, getDaoMessage(DaoException.NULL_BOARD));
            return;
        }
        File file = new File(filename);
        try {
            if (fos == null) {
                fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(fos);
            }
            if (wasGenerated != null) {
                oos.writeObject(wasGenerated);
            }
            oos.writeObject(obj);
        } catch (IOException ioex) {
            logger.log(Level.SEVERE, getDaoMessage(DaoException.OPEN_ERROR), ioex);
        }
    }

    /**
     * Overriden close method
     * to be sure that all used
     * resources are closed.
     */
    @SuppressWarnings("Duplicates")
    @Override
    public final void close() throws IOException {
        if (fos != null) {
            fos.close();
            oos.close();
        }
        if (fis != null) {
            fis.close();
            ois.close();
        }
    }

    /**
     * Overriden finalize method
     * to be sure that all used
     * resources are closed.
     */
    @Override
    public final void finalize() throws Exception {
       close();
    }

}
