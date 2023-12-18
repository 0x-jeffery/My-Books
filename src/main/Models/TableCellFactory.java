package main.Models;

import main.App;
import main.Controllers.EditPageController;
import main.Controllers.DetailsPageController;

import java.io.IOException;

import javafx.util.Callback;
import javafx.stage.Modality;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;


/**
 * Base class for a JavaFX table row component with the addition of a cell on the right, allowing
 * the application user to view/edit/delete a corresponding transaction on the table. This
 * component is in charge of opening a new application window, depending on the user input
 * on the main application. Used within the home-pane and history-pane tables.
 */
public class TableCellFactory implements Callback<TableColumn<Transaction, MenuButton>, TableCell<Transaction, MenuButton>> {

    @Override
    public TableCell<Transaction, MenuButton> call(final TableColumn<Transaction, MenuButton> param) {
        return new TableCell<>() {
            final MenuButton button = new MenuButton();
            final MenuItem deleteOption = new MenuItem("Delete");
            final MenuItem editOption = new MenuItem("Edit");
            final MenuItem detailsOption = new MenuItem("Details");


            // Initializing options on transaction drop-down MenuButton
            {
                deleteOption.setOnAction(e -> {
                    deleteTransaction();
                });
                editOption.setOnAction(actionEvent -> {
                    try {
                        openEditPage();
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }
                });
                detailsOption.setOnAction(actionEvent -> {
                    try {
                        openDetailsPage();
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }
                });
                button.getItems().addAll(deleteOption, editOption, detailsOption);
            }

            @Override
            /**
             * Needed method in order to update the view of
             * the JavaFX component on the window
             */
            public void updateItem(MenuButton item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(button);
                    setText(null);
                }
            }

            /**
             * Removes transaction row from parent table, and
             * removes it from within application data.
             */
            private void deleteTransaction(){
                Transaction transaction = getTableView().getItems().get(getIndex());
                getTableView().getItems().remove(getIndex());
                //TODO: Might need to redefine how App.removeTransaction() works.
                App.removeTransaction(transaction);
            }

            /**
             * Opens EditPage using FXML loader, loads all EditPage
             * components from transaction being viewed
             * @throws IOException If ERROR with loading FXML
             */
            private void openEditPage() throws IOException {
                AppStage appStage = new AppStage("../FXML/EditPage.fxml");
                EditPageController controller = appStage.loader.getController();
                Transaction transaction = getTableView().getItems().get(getIndex());

                controller.setTransaction(transaction);
                controller.setId(transaction.getId());
                controller.setTitle(transaction.getTitle());
                controller.setDatePicker(transaction.getDate());
                controller.setAmount(transaction.getAmount());
                controller.setAccount(transaction.getAccount());
                controller.setType(transaction.getType());
                controller.setSubtype(transaction.getSubtype());
                controller.setNote(transaction.getNote());

                appStage.stage.setTitle(transaction.getId());
                appStage.stage.initModality(Modality.APPLICATION_MODAL);
                appStage.stage.showAndWait();
            }

            /**
             * Opens DetailsPage using FXML loader, passing all data
             * from transaction being viewed directly onto the controller
             * @throws IOException If ERROR with loading
             */
            private void openDetailsPage() throws IOException{
                AppStage appStage = new AppStage("../FXML/DetailsPage.fxml");
                DetailsPageController controller = appStage.loader.getController();
                Transaction transaction = getTableView().getItems().get(getIndex());

                //TODO: Might change function naming on this to match the other opening function
                controller.setIdLabel(transaction.getId());
                controller.setTitleLabel(transaction.getTitle());
                controller.setDateLabel(transaction.getDate());
                controller.setAmountLabel(transaction.getAmount());
                controller.setAccountLabel(transaction.getAccount());
                controller.setTypeLabel(transaction.getType());
                controller.setSubtypeLabel(transaction.getSubtype());
                controller.setNoteLabel(transaction.getNote());

                appStage.stage.setTitle(transaction.getId());
                appStage.stage.initModality(Modality.APPLICATION_MODAL);
                appStage.stage.showAndWait();
            }
        };
    }
}
