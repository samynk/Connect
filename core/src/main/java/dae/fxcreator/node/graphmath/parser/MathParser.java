package dae.fxcreator.node.graphmath.parser;

import dae.fxcreator.io.math.parser.MathScriptLexer;
import dae.fxcreator.io.math.parser.MathScriptParser;
import dae.fxcreator.node.graphmath.MathFormula;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

/**
 * Utility class that hides the underlying ANTLR runtime dependency in the
 * main project.
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class MathParser {

    public static MathFormula parseMathCode(String code) {
        CharStream charStream = CharStreams.fromString(code);
        MathScriptLexer lexer = new MathScriptLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        MathScriptParser parser = new MathScriptParser(tokens);

        MathVisitor classVisitor = new MathVisitor();
        MathFormula result = (MathFormula) classVisitor.visit(parser.script());
        return result;
    }
}
