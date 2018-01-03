/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.io.templates.parser;

import java.io.StringReader;

/**
 *
 * @author Koen
 */
public class NewClass {
       public static void main(String args[]) throws ParseException {
           int quote = (int)'\'';
           System.out.println("quote:"+quote);
      String test  ="technique10 <%=node.name%>{"+
		"<%"+
			"for (Pass pass : node.passes ) "+
			"{"+
			"	// call the main pass template and store the result in"+
			"	// the techniques buffer."+
                        "       template(pass,\"main\",\"techniques\");"+
			"}"+
		"%>" +
		"}" ;

      StringReader sr = new StringReader(test);
    TemplateParser parser = new TemplateParser(sr);
    parser.Input();
    System.out.println(parser.getOutput().toString());
  
    }
}
