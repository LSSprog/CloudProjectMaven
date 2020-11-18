package cloud.project.java.file.manager;

import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Controller {

    @FXML
    VBox leftPanel, rightPanel;

    public void btnExitOnClose(ActionEvent actionEvent) {
        Platform.exit();
    }


    public void btnCopyAction(ActionEvent actionEvent) {
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("controller");
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("controller");

        if (leftPC.getSelectedFileName() == null && rightPC.getSelectedFileName() == null) {
            Alert alertE = new Alert(Alert.AlertType.ERROR, "Выбирите какой-нибудь файл", ButtonType.OK);
            alertE.showAndWait();
            return;
        }

        PanelController fromPC = null, toPC = null;
        if (leftPC.getSelectedFileName() != null) {
            fromPC = leftPC;
            toPC = rightPC;
        }
        if (rightPC.getSelectedFileName() != null) {
            fromPC = rightPC;
            toPC = leftPC;
        }

        Path fromPath = Paths.get(fromPC.getCurrentPath(), fromPC.getSelectedFileName());
        Path toPath = Paths.get(toPC.getCurrentPath(), fromPC.getSelectedFileName());

        try {
            Files.copy(fromPath, toPath);
            toPC.updateList(Paths.get(toPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alertE2 = new Alert(Alert.AlertType.ERROR, "Такой файл уже существует", ButtonType.OK);
            alertE2.showAndWait();
        }
    }

    public void btnUpdateList(ActionEvent actionEvent) {
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("controller");
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("controller");
        rightPC.updateList(Paths.get("./Server"));
        leftPC.updateList(Paths.get("./Client"));

    }
}
