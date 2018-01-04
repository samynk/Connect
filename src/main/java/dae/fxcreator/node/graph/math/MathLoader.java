package dae.fxcreator.node.graph.math;

import dae.fxcreator.io.loaders.XMLHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class loads a math formula defined in xml and converts it
 * to a MathFormula object.
 * @author Koen
 */
public class MathLoader extends XMLHandler {

    private File file;
    private Stack<MathElement> current = new Stack<MathElement>();
    private ArrayList<MathFormula> formulas = new ArrayList<MathFormula>();

    /**
     * Loads a math formula from file.
     * @param file the file to load.
     */
    public MathLoader(File file) {
        this.file = file;
        this.setRootElementName("mathformula");
    }
    

    public MathLoader() {
        this.setRootElementName("mathformula");
    }

    public void load() {

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(file, this);
            // create the node connections.
        } catch (IOException ex) {
            Logger.getLogger(MathLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MathLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(MathLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        
        if ("mathformula".equals(qName)) {
            MathFormula formula = new MathFormula();
            current.push(formula);
        } else if ("mathelement".equals(qName)) {
            MathElement parent = current.peek();
            String type = attributes.getValue("type");
            if (type != null) {

                if ("list".equals(type)) {
                    MultipleMathElement mme = new MultipleMathElement();
                    parent.addMathElement(mme);
                    current.push(mme);
                } else if ("binary".equals(type)) {
                    BinaryMathElement bme = new BinaryMathElement();
                    parent.addMathElement(bme);
                    current.push(bme);
                } else if ("unary".equals(type)) {
                    UnaryMathElement ume = new UnaryMathElement();
                    parent.addMathElement(ume);
                    current.push(ume);
                } else if ("variable".equals(type)) {
                    String name = attributes.getValue("name");
                    String sIsVector = attributes.getValue("isVector");
                    boolean isVector = false;
                    if (sIsVector != null) {
                        isVector = Boolean.parseBoolean(sIsVector);
                    }
                    MathVariable mv = new MathVariable();
                    mv.setVarName(name);
                    mv.setIsVector(isVector);
                    parent.addMathElement(mv);
                    current.push(mv);
                } else if ("function".equals(type)) {
                    String methodName = attributes.getValue("methodName");
                    FunctionMathElement fme = new FunctionMathElement(methodName);
                    parent.addMathElement(fme);
                    current.push(fme);
                }
            }
        } else if ("operation".equals(qName)) {
            MathElement parent = current.peek();
            String type = attributes.getValue("type");
            Operation op = new Operation(type);
            parent.addOperator(op);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("mathelement".equals(qName)) {
            current.pop();
        } else if ("mathformula".equals(qName)) {
            MathFormula formula = (MathFormula) current.pop();
            formulas.add(formula);
        }
    }

    public MathFormula getFormula() {
        if (formulas.size() > 0) {
            return this.formulas.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void reset() {
        current.clear();
        formulas = new ArrayList<MathFormula>();
    }

    @Override
    public Object getResult() {
        if (formulas.size() > 0 )
            return formulas.get(0);
        else
            return null;
    }
}
