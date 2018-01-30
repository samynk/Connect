/*
 * Digital Arts and Entertainment 2017
 */
package dae.fxcreator.io.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author samynk
 */
public class PathUtil {

    private static final String CONFDIR = "conf";
    private static final Path USERDIRPATH;
    private static final Path USERHOMEPATH;

    static {
        Path userDir = Paths.get(System.getProperty("user.dir"));
        try {
            Path siblingConfDir = userDir.resolveSibling(CONFDIR);
            if (Files.exists(siblingConfDir)) {
                userDir = siblingConfDir;
            } else {
                Stream<Path> paths = Files.walk(userDir);// )*/);
                Optional<Path> confDir = paths.filter(p -> Files.isDirectory(p) && CONFDIR.equals(p.getFileName().toString()))
                        .findFirst();
                if (confDir.isPresent()) {
                    userDir = confDir.get();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PathUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        USERDIRPATH = userDir;
        USERHOMEPATH = Paths.get(System.getProperty("user.home"));
    }

    public static Path createUserDirPath(Path child) {
        return USERDIRPATH.resolve(child);
    }

    public static InputStream createUserDirStream(Path child) throws IOException {
        return new BufferedInputStream(Files.newInputStream(createUserDirPath(child)));
    }

    public static Path createUserDirPath(String child) {
        return USERDIRPATH.resolve(child);
    }

    public static InputStream createUserDirStream(String child) throws IOException {
        return new BufferedInputStream(Files.newInputStream(createUserDirPath(child)));
    }

    public static Path createUserHomePath(Path child) {
        return USERHOMEPATH.resolve(child);
    }

    public static Path createUserHomePath(String child) {
        return USERHOMEPATH.resolve(child);
    }

    public static InputStream createBufferedInputStream(Path toRead) throws IOException {
        return new BufferedInputStream(Files.newInputStream(toRead));
    }

    /**
     * Checks if the path ends with the given extension.
     *
     * @param path the path to check.
     * @param extension the extension to check.
     * @return
     */
    public static boolean checkExtension(Path path, String extension) {
        if (!Files.isDirectory(path)) {
            Path file = path.getFileName();
            return file.toString().toLowerCase().endsWith("." + extension);
        } else {
            return false;
        }
    }
}
