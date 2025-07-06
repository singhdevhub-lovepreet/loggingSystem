package logger.utils;

import java.io.*;

public class DeepCopyUtil {

    public static <T> T deepcopy(T original) throws IOException, ClassNotFoundException {
        // ObjectStream --> ByteStream
        // written into ObjectStream
        // ByteStream -->  ObjectStream
        // read from ObjectStream and make new object

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(original);
        oos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        T copy = (T) ois.readObject();

        oos.close();
        baos.close();
        ois.close();
        bais.close();

        return copy;

    }

}
