package logger.data;

import logger.pojo.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

public class FileStore implements Datastore{

    @Override
    public void addLog(Log log){

    }

    @Override
    public void appendLog(Collection<Log> logs) throws TimeoutException {
        try{
            // here is where io will happen
            File file = new File("test.log");
            try(FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
                for (Log log : logs){
                    oos.writeObject(log);
                }
            }
        }catch (Exception ex){
            //
        }
    }


}
