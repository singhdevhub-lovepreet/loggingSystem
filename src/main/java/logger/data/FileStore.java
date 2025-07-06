package logger.data;

import logger.pojo.Log;

import java.io.*;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

public class FileStore implements Datastore{

    @Override
    public void appendLog(Collection<Log> logs) throws TimeoutException {
        try{
            File file = new File("test.log");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for(Log log: logs){
                oos.writeObject(log);
            }
            fos.close();
            oos.close();
        }catch (Exception ex){
            System.err.println("File not found or not able to open");
        }
    }

    @Override
    public void deleteLog(){
        // Todo: read file logs, delete old logs 30%, do file IO delete
    }


}
