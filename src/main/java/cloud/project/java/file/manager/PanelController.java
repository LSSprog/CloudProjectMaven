package cloud.project.java.file.manager;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PanelController implements Initializable {

    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>("Тип");//FI - что хранится, Str - в каком виде
        fileTypeColumn.setCellValueFactory(
                param -> new SimpleStringProperty(param.getValue().getType().getTypeName())); //достаём тип D или F
        fileTypeColumn.setPrefWidth(24); // длина столбца в пикселях

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Имя");
        fileNameColumn.setCellValueFactory(
                param -> new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(240);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(
                param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%, d bytes", item);
                        if (item == -1L) {
                            text = "DIR";
                        }
                        setText(text);
                    }
                }
            };
        });
        fileSizeColumn.setPrefWidth(120);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDataColumn = new TableColumn<>("Дата изменения");
        fileDataColumn.setCellValueFactory(
                param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileDataColumn.setPrefWidth(120);



        fileList.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileDataColumn); //вызвали столбец в таблицу
        fileList.getSortOrder().add(fileTypeColumn);

        boxOfDisk.getItems().clear();
        for (Path p: FileSystems.getDefault().getRootDirectories()) { //системно достаём корни папки
            boxOfDisk.getItems().add(p.toString());
        }
        boxOfDisk.getSelectionModel().select(0); // выбираем оп умолчанию первых их дисков

        fileList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Path newPath = Paths.get(pathField.getText())
                            .resolve(fileList.getSelectionModel().getSelectedItem().getFileName());
                    if (Files.isDirectory(newPath)) {
                        updateList(newPath);
                    }
                }
            }
        });

        updateList(Paths.get("./Server"));

    }

    @FXML
    TableView<FileInfo> fileList;

    public void updateList (Path path) {
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            fileList.getItems().clear();
            fileList.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            // берём путь и получаем от него стрим пасов для каждой дир или файла и в колцию это всё
            fileList.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Не удалось обновить список файлов", ButtonType.OK); //создание сообщения
            alert.showAndWait();
        }
    }

    @FXML
    ComboBox<String> boxOfDisk;

    @FXML
    TextField pathField;

    public void btnPathUpAction(ActionEvent actionEvent) {
        Path upPath = Paths.get(pathField.getText()).getParent();
        if (upPath != null) {
            updateList(upPath);
        }
    }

    public void changeDiskAction(ActionEvent actionEvent) { //если один диск, то он как бы в нём и переход не осуществляет
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }

    public String getSelectedFileName() {
        if (!fileList.isFocused()) {
            return null;
        }
        return fileList.getSelectionModel().getSelectedItem().getFileName();
    }

    public String getCurrentPath() {
        return pathField.getText();
    }
}
