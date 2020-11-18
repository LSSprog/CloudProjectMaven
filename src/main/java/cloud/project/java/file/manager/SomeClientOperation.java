package cloud.project.java.file.manager;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

public class SomeClientOperation {
    //операции подключения к серверу через Object stream
    //это всё в Controller???
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public SomeClientOperation() throws IOException {
        this.socket = new Socket("localhost", 1313);
        this.in = new ObjectInputStream(this.socket.getInputStream());
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
    }

    private void sendFile (String nameFile) { //// или здесь Path path лучше???
        try {
            out.writeUTF("upload"); //отпрвляем на сервер тип операции - это загрузка с компа на сервер должна быть
            out.writeUTF(nameFile); //path.getFileName().toString()); //берём имя файла для отправки на сервер
            File file = new File("Client/" + nameFile);

            //как то сериализовать файл и отпрапвить его на сервеер
            ByteArrayOutputStream byArOut = new ByteArrayOutputStream();
            //out(byArOut); //не то что-то
            //создать объект для отправки типа FileToSend
            ObjectOutputStream objOut = new ObjectOutputStream(byArOut);//не смог использовать верхний OUT почему то, пришлось ещё один создать
            FileToSend fileToSend = new FileToSend(file.toPath());
            out.writeObject(fileToSend);

            //или как в ДЗ делал, но это без сериализации
            long size = file.length();
            out.writeLong(size);
            FileInputStream fileByte = new FileInputStream(file);
            int read = 0;
            byte[] buf = new byte[256];
            while ((read = fileByte.read(buf)) != -1) {
                out.write(buf, 0, read);
            }
            out.flush();
            // По-моему это не очень подходит.... наверху что



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void takeFile() {

    }
}
