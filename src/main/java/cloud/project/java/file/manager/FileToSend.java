package cloud.project.java.file.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileToSend extends AbstractPackage{
    private FileInfo fileInfo;
    private byte[] data;
    private String fileName; // не лишнее ли? из файлИнфо же можно добыть имя

    public FileToSend(Path path) throws IOException {
        this.fileInfo = new FileInfo(path);
        this.data = Files.readAllBytes(path);
        fileName = path.getFileName().toString();
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
