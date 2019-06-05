
package controller;

import dbmanager.CategoryRepo;
import dbmanager.NoteRepo;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import models.Category;
import models.Note;

/**
 *
 * @author User
 */
public class NoteController implements Initializable {

    @FXML
    private ComboBox<Category> categorydb;
    @FXML
    private TextField namedb;
    @FXML
    private TextField daysdb;
    @FXML
    private CheckBox searchdb;
    @FXML
    private ToggleGroup select;
    @FXML
    private RadioButton completedDb;
    @FXML
    private RadioButton notCompletedDb;
    @FXML
    private TableView<Note> tabledb;
    @FXML
    private TableColumn<Note, String> noteNameb;
    @FXML
    private TableColumn<Note, Integer> leftDaysb;
    @FXML
    private TableColumn<Note, DatePicker> dateb;
    @FXML
    private TableColumn<Note, Category> categoryb;
    @FXML
    private TableColumn<Note, String> statusb;
    @FXML
    private Label lblcount;
    @FXML
    private Label lblmessage;

    NoteRepo noteRepo;
    CategoryRepo ctgrepo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noteNameb.setCellValueFactory(new PropertyValueFactory<>("name"));
        leftDaysb.setCellValueFactory(new PropertyValueFactory<>("days"));
        dateb.setCellValueFactory(new PropertyValueFactory<>("date"));
        categoryb.setCellValueFactory(new PropertyValueFactory<>("category"));
        statusb.setCellValueFactory(new PropertyValueFactory<>("status"));
        makeNumeric();
        table();
        fillCategory();
        writeMessage("Welcome to Note App!");
    }

    @FXML
    private void findC(ActionEvent event) {
        Map<String, String> fieldList = new TreeMap<>();
        noteRepo = new NoteRepo();
        Category category = new Category();
        if (searchdb.isSelected()) {
            if (categorydb.getValue().getName().equalsIgnoreCase("All category")) {
                category.setName("");
            } else {
                category.setName(categorydb.getValue().getName());
            }
            fieldList.put("name", namedb.getText());
            fieldList.put("days", daysdb.getText());
            fieldList.put("category", category.getName());
            List<Note> notelist = noteRepo.find(fieldList);
            tabledb.getItems().setAll(notelist);
            if (notelist.size() == 0) {
                writeMessage("Nothing found");
            } else {
                writeMessage("Found " + notelist.size() + " notes");
            }
            lblcount.setText(notelist.size() + " notes");
        }
    }

    @FXML
    private void addCategory(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Category");
        dialog.setHeaderText("Add Category");
        dialog.setContentText("Input category name");
        List<Category> categories = ctgrepo.findAll();
        Optional<String> result = dialog.showAndWait();
        boolean unique = false;
        if (!result.get().trim().isEmpty()) {
            while (!unique) {
                for (Category c : categories) {
                    if (!c.getName().equalsIgnoreCase(result.get())) {
                        unique = true;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "This category already exist", ButtonType.OK);
                        alert.showAndWait();
                        result = dialog.showAndWait();
                        unique = false;
                        break;
                    }
                }
            }
            Category category = new Category();
            category.setName(result.get());
            ctgrepo.add(category);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Category added succesfully", ButtonType.OK);
            alert.show();
            fillCategory();
            table();
        } else {
            writeMessage("Please,fill category name");
        }
    }

    @FXML
    private void saveNote(ActionEvent event) {
        Note selectedNote = tabledb.getSelectionModel().getSelectedItem();
        noteRepo = new NoteRepo();
        Note note = new Note();
        boolean isFilled = isFilled();
        if (isFilled && !categorydb.getValue().getName().equalsIgnoreCase("All category")) {
            if (selectedNote == null) {
                note.setName(namedb.getText().trim());
                note.setDays(Integer.parseInt(daysdb.getText()));
                note.setCategory(categorydb.getValue());
                noteRepo.add(note);
                clearField();
                writeMessage("Note added successfully!");
            } else {
                note.setId(selectedNote.getId());
                note.setName(namedb.getText().trim());
                note.setDays(Integer.parseInt(daysdb.getText()));
                note.setCategory(categorydb.getValue());
                noteRepo.add(note);
                clearField();
                writeMessage("Note added successfully!");
            }

        } else {
            writeMessage("Please, fill all the fields and choose a category!");
        }

    }

    public boolean isFilled() {
        boolean result = false;
        if (!namedb.getText().trim().isEmpty() && !daysdb.getText().trim().isEmpty()
                && categorydb.getValue() != null) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    @FXML
    private void find(KeyEvent event) {
        Map<String, String> fieldList = new TreeMap<>();
        noteRepo = new NoteRepo();
        Category category = new Category();
        if (searchdb.isSelected()) {
            if (categorydb.getValue().getName().equalsIgnoreCase("All Category")) {
                category.setName("");
            } else {
                category.setName(categorydb.getValue().getName());
            }
            fieldList.put("name", namedb.getText());
            fieldList.put("days", daysdb.getText());
            fieldList.put("category", category.getName());
            List<Note> notelist = noteRepo.find(fieldList);
            tabledb.getItems().setAll(notelist);
            if (notelist.size() == 0) {
                writeMessage("Nothing found");
            } else {
                writeMessage("Found " + notelist.size() + " notes");
            }
            lblcount.setText(notelist.size() + " notes");
        }
    }

    @FXML
    private void completed(ActionEvent event) {
        Note selectedNote = tabledb.getSelectionModel().getSelectedItem();
        noteRepo = new NoteRepo();
        if (selectedNote != null) {
            noteRepo.makeComplete(selectedNote);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Selected note completed", ButtonType.OK);
            alert.show();
            table();
        }
    }

    @FXML
    private void allCompleted(ActionEvent event) {
        noteRepo = new NoteRepo();
        noteRepo.makeAllComplete();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "All note completed", ButtonType.OK);
        alert.show();
        table();
    }

    @FXML
    private void delete(ActionEvent event) {
        Note selectedNote = tabledb.getSelectionModel().getSelectedItem();
        noteRepo = new NoteRepo();
        Note note = new Note();
        if (selectedNote != null) {
            note.setId(selectedNote.getId());
            noteRepo.remove(note);
            writeMessage("Note deleted successfully");
            table();
        }
    }

    @FXML
    private void showAllStatus(ActionEvent event) {
        table();
        writeMessage("All note showed");
    }

    @FXML
    private void showNoteByStatus(ActionEvent event) {
        noteRepo = new NoteRepo();
        if (completedDb.isSelected()) {
            List<Note> donedTask = noteRepo.showNoteByStatus("Completed");
            tabledb.getItems().setAll(donedTask);
            writeMessage(donedTask.size() + " notes completed");
            lblcount.setText(donedTask.size() + " notes");
        } else if (notCompletedDb.isSelected()) {
            List<Note> notDonedTask = noteRepo.showNoteByStatus("Not Completed");
            tabledb.getItems().setAll(notDonedTask);
            writeMessage(notDonedTask.size() + " notes not completed");
            lblcount.setText(notDonedTask.size() + " notes");
        }
    }

    @FXML
    private void clearField() {
        namedb.setText("");
        daysdb.setText("");
        fillCategory();
        table();
        searchdb.setSelected(false);
        writeMessage("Table refreshed!");
    }

    @FXML
    private void onNoteSelected(MouseEvent event
    ) {
        searchdb.setSelected(false);
        Note note = tabledb.getSelectionModel().getSelectedItem();
        namedb.setText(note.getName());
        daysdb.setText(note.getDays().toString());
        categorydb.setValue(note.getCategory());
        writeMessage("Note selected");
    }

    public void table() {
        noteRepo = new NoteRepo();
        tabledb.getItems().setAll(noteRepo.findAll());
        lblcount.setText(noteRepo.findAll().size() + " notes");
        searchdb.setSelected(false);
    }

    public void makeNumeric() {
        daysdb.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,5}(\\d{0,0})?")) {
                    daysdb.setText(oldValue);
                }
            }
        });
    }

    public void writeMessage(String message) {
        lblmessage.setText(message);
    }

    public void fillCategory() {
        ctgrepo = new CategoryRepo();
        Category category = new Category();
        category.setName("All category");
        List<Category> categories = ctgrepo.findAll();
        categories.add(0, category);
        categorydb.getItems().setAll(categories);
        categorydb.getSelectionModel().selectFirst();

    }

}
