package pl.compprog.gui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import pl.compprog.daos.FileSudokuBoardDao;
import pl.compprog.daos.JdbcSudokuBoardDao;
import pl.compprog.daos.SudokuBoardDaoFactory;
import pl.compprog.exceptions.ApplicationException;
import pl.compprog.exceptions.DaoException;
import pl.compprog.logs.FileAndConsoleLoggerFactory;
import pl.compprog.sudoku.SudokuBoard;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pl.compprog.daos.JdbcSudokuBoardDao.getAllBoardsAsStrings;

public class DbLoadDialogue implements Initializable {
    public ComboBox dbComboBoxList;
    public Button loadDbButton;
    private final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(DbSaveDialogue.class.getName());
    private final ResourceBundle messagesBundle = ResourceBundle.getBundle("pl.compprog.messages");
    private List<String[]> allBoards;

    @SuppressWarnings("Duplicates")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            allBoards = getAllBoardsAsStrings();
        } catch(DaoException ex) {
            logger.log(Level.SEVERE, messagesBundle.getString(DaoException.OPEN_ERROR), ex);
        }

        for(int i = 0; i < allBoards.size(); i++) {
            String boardName = allBoards.get(i)[0];
            String date = allBoards.get(i)[1];
            dbComboBoxList.getItems().addAll(
                    boardName + ' ' + date
            );
        }

        dbComboBoxList.setPromptText(MainView.getInstance().getCurrentBundle().getString("choose_save"));
        loadDbButton.setText(MainView.getInstance().getCurrentBundle().getString("load"));
    }

    public void loadFromDb(ActionEvent actionEvent) {
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        if (dbComboBoxList.getValue() != null) {
            String boardName = (String) dbComboBoxList.getValue();
            boardName = boardName.substring(0, boardName.length() - 20);
            try (JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao) sudokuBoardDaoFactory.getDatabaseDao(boardName, MainView.getWasGenerated())) {
                SudokuBoard tempBoard = dao.readEx();
                MainView.setWasGenerated(dao.getWasGenerated());
                MainApp.getInstance().getController().reinitializeBoardLoading(tempBoard);
            } catch (DaoException | SQLException ex) {
                logger.log(Level.SEVERE, messagesBundle.getString(DaoException.OPEN_ERROR), ex);
            }catch (ApplicationException aex) {
                logger.log(Level.SEVERE, messagesBundle.getString(ApplicationException.RESOURCE_BUNDLE_IS_NULL), aex);
            }

            Stage stage = (Stage) loadDbButton.getScene().getWindow();
            stage.close();
        }
    }

}
