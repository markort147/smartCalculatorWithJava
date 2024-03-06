package calculator.math;

import calculator.log.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathExpressionUtils {

    private static final String VARIABLE_NAME_REGEX = "[a-zA-Z]+";
    private static final Pattern VARIABLE_NAME_PATTERN = Pattern.compile("(?<=\\b)" + VARIABLE_NAME_REGEX + "(?=\\b)");
    private static final Pattern WHOLE_STRING_AS_VARIABLE_NAME_PATTERN = Pattern.compile("^" + VARIABLE_NAME_REGEX + "$");

    public static Set<String> parseVariables(String expression) {
        Set<String> variables = new HashSet<>();
        Matcher variablesMatcher = VARIABLE_NAME_PATTERN.matcher(expression);
        while (variablesMatcher.find()) {
            String variable = variablesMatcher.group();
            variables.add(variable);
        }
        return variables;
    }

    public static String simplify(String expression) {
//        String simpleExpression = expression.replaceAll("(\\+\\+|--)", "+").replaceAll("-\\+", "-").replaceAll("(?<=\\d)-(?=\\d)", "+-").replaceAll("  ", " ");
        if(expression.startsWith("-")) {
            expression = "0".concat(expression);
        }
        String simpleExpression = expression
                .replaceAll("(\\+\\+|--)", "+")
                .replaceAll("(\\+-|-\\+)", "-")
                .replaceAll("\\(-", "(0-")
                .replaceAll("  ", " ");
        Logger.getInstance().debug("simplified \"" + expression + "\" to \"" + simpleExpression + "\"");
        if (!simpleExpression.equals(expression)) {
            simpleExpression = simplify(simpleExpression);
        }
        return simpleExpression;
    }

    public static boolean isValidVariableName(String variableName) {
        return WHOLE_STRING_AS_VARIABLE_NAME_PATTERN.matcher(variableName).find();
    }

}
