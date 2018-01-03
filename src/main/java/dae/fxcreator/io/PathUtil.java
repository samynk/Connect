/*
 * Digital Arts and Entertainment 2017
 */
package dae.fxcreator.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author samynk
 */
public class PathUtil {
    private static final Path userDirPath;
    private static final Path userHomePath;
    
    static{
        userDirPath = Paths.get(System.getProperty("user.dir"));
        userHomePath = Paths.get(System.getProperty("user.home"));
    }
    
    public static Path createUserDirPath(Path child){
        return userDirPath.resolve(child);
    }
    
    public static InputStream createUserDirStream(Path child) throws IOException{
        return new BufferedInputStream(Files.newInputStream(createUserDirPath(child)));
    }
    
    public static Path createUserDirPath(String child){
        return userDirPath.resolve(child);
    }
    
    public static InputStream createUserDirStream(String child) throws IOException{
        return new BufferedInputStream(Files.newInputStream(createUserDirPath(child)));
    }
    
    public static Path createUserHomePath(Path child){
        return userHomePath.resolve(child);
    }
    
    public static Path createUserHomePath(String child){
        return userHomePath.resolve(child);
    }
}
