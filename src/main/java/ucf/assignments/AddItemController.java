/*
 *  UCF COP3330 Fall
 * 2021 Assignment 4 Solution
 *  Copyright 2021 Chase Cutshall
 */
package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class AddItemController {
    public static Stage AddItemStage;
    private Stage dialogStage;

    // gui nodes
    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private DatePicker date;


    public void setDialogStage(Stage stage) {
        dialogStage = stage;
        AddItemStage = stage;
        AddItemStage.setResizable(false);
    }

    public AddItemController() {
    }

    @FXML
    void submitButtonClicked(ActionEvent action) {

        // see if object data is considered valid
        // create a new object from user inputted data
        // add the object to the current active list

        if (isInputValid()) {
            ToDoItem newItem = new ToDoItem(name.getText(), description.getText(), date.getValue());
            ToDoApp.activeList.addItem(newItem);

            try {
                AddItemStage.close();
                ListOptionsController.ListOptionsStage.close();

                Parent root = FXMLLoader.load(getClass().getResource("ListOptionsGUI.fxml"));
                Scene scene = new Scene(root);

                ToDoApp.mainScene.setTitle("List Options");
                ToDoApp.mainScene.setScene(scene);
                ToDoApp.mainScene.show();

                FXMLLoader loader = new FXMLLoader();
                ListOptionsController controller = new ListOptionsController();
                controller.setStage(ToDoApp.mainScene);
                loader.setController(controller);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isInputValid() {

        // make sure the name is 1-20 characters in length
        // make sure the date exists
        // make sure the item description is 1-256 characters in length
        // if any of these are not met, create an error dialog and ask the user to enter the correct data

        String errorMessage = "";

        for (ToDoItem item : ToDoApp.activeList.getList())
            if (item.getName().equalsIgnoreCase(name.getText())) {
                errorMessage += "Item already exists!\n";
                break;
            }

        if (name.getText() == null || name.getText().length() == 0) {
            errorMessage += "Not a valid name!\n";
        }

        if (name.getText().length() > 20) {
            errorMessage += "Name too long!\n";
        }

        if (description.getText() == null || description.getText().length() == 0) {
            errorMessage += "Not a valid Description!\n";
        }

        if (description.getText().length() > 256) {
            errorMessage += "Description too long!\n";
        }

        if (date.getValue() == null) {
            errorMessage += "Invalid Date!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    @FXML
    void cancelButtonClicked(ActionEvent action) {
        dialogStage.close();
    }
}