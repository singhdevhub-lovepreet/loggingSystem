package utils;

import java.io.*;

public class DeepCopyUtil {

    public static <T> T deepCopy(T original) throws IOException, ClassNotFoundException {
        // Write the object to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(original);
        oos.flush();

        // Read the object from the byte array (creating a new instance)
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        T copy = (T) ois.readObject();

        // Close streams
        oos.close();
        bos.close();
        ois.close();
        bis.close();

        return copy;
    }

}
