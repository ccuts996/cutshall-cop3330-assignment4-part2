/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Chase Cutshall
 */
package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class ListCatalogController {

    public static Stage ListCatalogStage;
    private Stage dialogStage = new Stage();

    private static ToDoList selected;

    private static ObservableList<ToDoList> catalog;

    public ListCatalogController() {
    }

    //for displaying the table
    @FXML private TableView<ToDoList> tableView;
    @FXML private TableColumn<ToDoList,String> nameColumn;

    public ListCatalogController (Stage dialogStage) {
        this.dialogStage = dialogStage;
        ListCatalogStage = this.dialogStage;
        ListCatalogStage.setResizable(false);
    }

    public static ToDoList getSelected() {
        return selected;
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        catalog = FXCollections.observableArrayList();
        catalog.addAll(ToDoApp.catalog.getCatalog());
        tableView.setItems(catalog);

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> updateSelected(newValue)
        );

    }

    private void updateSelected(ToDoList newValue) {
        selected = newValue;
        ToDoApp.activeList = newValue;
    }

    @FXML
    void renameButtonClicked (ActionEvent action) throws IOException {

        // open a new window to prompt the user for a new lists name to overwrite the old list's name
        // update the list's name to the new name
        // update window
        // update files

        if(selected == null)
            return;
        Parent root = FXMLLoader.load(getClass().getResource("RenameListGUI.fxml"));
        Scene scene = new Scene(root);

        dialogStage.setTitle("Edit List");
        dialogStage.setScene(scene);
        dialogStage.show();

        FXMLLoader loader = new FXMLLoader();
        RenameListController controller = new RenameListController();

        controller.setDialogStage(this.dialogStage);
        controller.setList(selected);
        loader.setController(controller);


    }

    @FXML
    void newButtonClicked (ActionEvent action) throws IOException {

        // prompt the user for a list name
        // create a new list using a name the user enters
        // update window

        Parent root = FXMLLoader.load(getClass().getResource("AddListGUI.fxml"));
        Scene scene = new Scene(root);

        dialogStage.setTitle("New List");
        dialogStage.setScene(scene);
        dialogStage.show();

        FXMLLoader loader = new FXMLLoader();
        AddListController controller = new AddListController();
        controller.setDialogStage(this.dialogStage);
        loader.setController(controller);
    }

    @FXML
    void deleteButtonClicked (ActionEvent action) {

        // delete the list and update the catalog
        // update the files

        if(selected == null)
            return;

        ToDoApp.catalog.deleteList(selected);
        selected = null;

        try {
            Parser.parseToCatalogFile(ToDoApp.catalog);

            ListCatalogController.ListCatalogStage.close();

            Parent root = FXMLLoader.load(getClass().getResource("ListCatalogGUI.fxml"));
            Scene scene = new Scene(root);

            ToDoApp.mainScene.setScene(scene);
            ToDoApp.mainScene.show();

            FXMLLoader loader = new FXMLLoader();
            ListCatalogController controller = new ListCatalogController(ToDoApp.mainScene);
            loader.setController(controller);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    void onOpenButtonClicked(ActionEvent action) throws IOException, ParseException {

        // create the list options window for the list that was opened

        if(selected == null)
            return;

        ToDoApp.activeList = new ToDoList(ToDoApp.activeList.getName());
        ToDoApp.activeList.addAllItems(Parser.loadList(ToDoApp.activeList.getName()));

        Parent root = FXMLLoader.load(getClass().getResource("ListOptionsGUI.fxml"));
        Scene scene = new Scene(root);

        ToDoApp.mainScene.setTitle("New List");
        ToDoApp.mainScene.setScene(scene);
        ToDoApp.mainScene.show();

        FXMLLoader loader = new FXMLLoader();
        ListOptionsController controller = new ListOptionsController();
        controller.setStage(ToDoApp.mainScene);
        loader.setController(controller);
    }

}
