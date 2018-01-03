package dae.fxcreator.io.codegen;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.tools.SimpleJavaFileObject;

/**
 * Represents the source text of a Java program in RAM
 * @author Koen
 */
public class CodeGeneratorClass extends SimpleJavaFileObject {

    /**
     * source text of the program to be compiled
     */
    private final String programText;

    /**
     * constructor
     *
     * @param className   class name, without package
     * @param programText text of the program.
     *
     * @throws java.net.URISyntaxException if malformed class name.
     */
    @SuppressWarnings({"SameParameterValue"})
    public CodeGeneratorClass(String packageName,String className, String programText) throws URISyntaxException {
        super(new URI(packageName.replace('.','/')+"/"+className + ".java"), Kind.SOURCE);
        this.programText = programText;
    }

    /**
     * Get the text of the java program
     *
     * @param ignoreEncodingErrors ignored.
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return programText;
    }
}
