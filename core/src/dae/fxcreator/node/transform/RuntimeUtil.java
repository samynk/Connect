package dae.fxcreator.node.transform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class RuntimeUtil {

    public static String unescape(String escaped, boolean skipFirst, boolean skipLast) {
        StringBuilder sb = new StringBuilder(escaped.length());

        int end = skipLast ? escaped.length() - 1 : escaped.length();

        for (int i = skipFirst ? 1 : 0; i < end; ++i) {
            char current = escaped.charAt(i);
            switch (current) {
                case '\\':
                    if (i + 1 < escaped.length()) {
                        char next = escaped.charAt(i + 1);
                        ++i;
                        switch (next) {
                            case 'n':
                                sb.append('\n');
                                break;
                            case 'b':
                                sb.append('\b');
                                break;
                            case 't':
                                sb.append('\t');
                                break;
                            case 'f':
                                sb.append('\f');
                                break;
                            case '\\':
                                sb.append('\\');
                                break;
                            case '\"':
                                sb.append('\"');
                                break;
                            case 'u':
                                if (i + 5 < escaped.length()) {
                                    String hex = escaped.substring(i + 2, i + 5);
                                    int hexValue = Integer.parseInt(hex, 16);
                                    if (Character.isValidCodePoint(hexValue)) {
                                        sb.append(Character.toChars(hexValue));
                                    }
                                    i += 4;
                                }
                                break;
                        }
                    }
                    break;
                default:
                    sb.append(current);
                    break;
            }
        }
        return sb.toString();
    }

    public static String convertToMethod(String property) {
        return "get"
                + Character.toUpperCase(property.charAt(0))
                + (property.length() > 1 ? property.substring(1) : "");
    }
    
    public static String convertToBooleanMethod(String property) {
        return "is"
                + Character.toUpperCase(property.charAt(0))
                + (property.length() > 1 ? property.substring(1) : "");
    }

    public static Object invokeGet(Object o, String method) {
        Object value = null;
        try {
            Method m = o.getClass().getMethod(method);
            value = m.invoke(o);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(RuntimeUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
}
