package dae.fxcreator.io.codegen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Formats code for display in an editor.
 * @author Koen
 */
public class CodeFormatter {

    private ArrayList<String> blockStarts = new ArrayList<String>();
    private ArrayList<String> blockEnds = new ArrayList<String>();
    
    /**
     * Creates an empty CodeFormatter object, only useful to remove empty
     * lines.
     */
    public CodeFormatter()
    {

    }

    /**
     * Creates a new CodeFormatter object with the specified blockStart character and
     * blockEnd character.
     * @param blockStart
     * @param blockEnd
     */
    public CodeFormatter(String blockStart, String blockEnd) {
        blockStarts.add(blockStart);
        blockEnds.add(blockEnd);
    }

    /**
     * Formats the source code and returns the result.
     * This method will read the source code line by line and insert
     * tabs where necessary for blocks of code and remove empty lines.
     */
    public String format(String sourceCode) {
        StringBuffer result = new StringBuffer();
        int nrOfTabs = 0;
        int startIndex = 0, endIndex = 0;
        int currentIndex = 0;
        TextLocation tlStart = new TextLocation();
        TextLocation tlEnd = new TextLocation();
        
        StringTokenizer st = new StringTokenizer(sourceCode, "\n");
        while (st.hasMoreTokens()) {
            String line = st.nextToken().trim();
            currentIndex = 0;
            if ( line.length() > 0)
            {
                appendTabs(result,nrOfTabs);
                do {
                    //startIndex = line.indexOf(blockStart, currentIndex);
                    //endIndex = line.indexOf(blockEnd, currentIndex);
                    startIndex = findBlockStart(line, currentIndex,tlStart);
                    endIndex = findBlockEnd(line,currentIndex,tlEnd);


                    if (startIndex >= 0 && ((startIndex < endIndex)||(endIndex==-1))) {
                        // remove all whitespace between startIndex and first non
                        // whitespace character and insert newline and correct number of tabs.
                        ++nrOfTabs;
                        result.append(line,currentIndex,startIndex+tlStart.size);
                        result.append("\n");
                        currentIndex = findFirstNonWhiteSpace(line,startIndex+1);
                    }

                    if (endIndex >= 0 && ((endIndex < startIndex)||(startIndex==-1))) {
                        // check correct number of newlines after end of block.
                        if ( currentIndex < endIndex){
                            //appendTabs(result,nrOfTabs);
                            result.append(line,currentIndex,endIndex);
                            result.append("\n");
                        }

                        --nrOfTabs;
                        int lastChar = result.length()-1;
                        if ( result.charAt(lastChar) == '\t')
                            result.deleteCharAt(lastChar);
                        

                        result.append(line,endIndex,endIndex+tlEnd.size);
                        result.append("\n\n");
                        currentIndex = findFirstNonWhiteSpace(line,endIndex+tlEnd.size);
                    }
                } while (startIndex >= 0 && endIndex >= 0);

                if ( currentIndex > 0 && currentIndex < line.length())
                    appendTabs(result,nrOfTabs);

                if ( currentIndex < line.length())
                {
                    result.append(line,currentIndex,line.length());
                    result.append("\n");
                }

            }
        }
        return result.toString();
    }

    private int findBlockStart(String line, int startIndex,TextLocation tl){
        for ( String chars:blockStarts){
            int index = line.indexOf(chars, startIndex );
            if (  index != -1){
                tl.index = index;
                tl.size = chars.length();
                return index;
            }
        }
        return -1;
    }

    private int findBlockEnd(String line, int startIndex,TextLocation tl)
    {
        for ( String chars:blockEnds){
            int index = line.indexOf(chars, startIndex );
            if (  index != -1){
                tl.index = index;
                tl.size = chars.length();
                return index;
            }
        }
        return -1;
    }

    private int findFirstNonWhiteSpace(String line,int beginIndex){
        for (int i= beginIndex; i< line.length(); ++i){
            char toCheck = line.charAt(i);
            if ( !Character.isWhitespace(toCheck))
                return i;
        }
        return line.length();
    }

    private void appendTabs(StringBuffer result, int nrOfTabs){
        for (int i = 0 ; i< nrOfTabs;++i)
            result.append("\t");
    }

    public static void main(String[] args) throws FileNotFoundException {
        try {
            StringBuffer result = new StringBuffer();
            File file = new File("d:/projects/fxcreator/format.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
                result.append('\n');
            }
            br.close();
            CodeFormatter cf = new CodeFormatter("{","};");
            cf.addBlockEnd("}");
            String formatted = cf.format(result.toString());

            file = new File("d:/projects/fxcreator/result.fx");
            FileWriter fw = new FileWriter(file);
            fw.write(formatted);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(CodeFormatter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addBlockEnd(String end) {
        blockEnds.add(end);
    }

    public void addBlockStart(String value) {
        blockStarts.add(value);
    }

    class TextLocation {
        public int index;
        public int size;
    }
}