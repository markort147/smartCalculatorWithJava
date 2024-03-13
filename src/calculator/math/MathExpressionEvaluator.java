package calculator.math;

import calculator.exception.InvalidExpressionException;
import calculator.exception.VariableNotFoundException;
import calculator.log.Logger;
import calculator.state.AppState;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathExpressionEvaluator {

    private final static String sumRegex = "\\+";
    private final static String subRegex = "\\-";
    private final static String prodRegex = "\\*";
    private final static String divRegex = "\\/";
    private final static String numberRegex = "\\d+";
    private final static String openBracketRegex = "\\(";
    private final static String closedBracketRegex = "\\)";
    private final static Pattern sumPattern = Pattern.compile(sumRegex);
    private final static Pattern subPattern = Pattern.compile(subRegex);
    private final static Pattern prodPattern = Pattern.compile(prodRegex);
    private final static Pattern divPattern = Pattern.compile(divRegex);
    private final static Pattern numberPattern = Pattern.compile(numberRegex);
    private final static Pattern openBracketPattern = Pattern.compile(openBracketRegex);
    private final static Pattern closedBracketPattern = Pattern.compile(closedBracketRegex);
    private final static Pattern operatorPattern = Pattern.compile(sumRegex + "|" + subRegex + "|" + prodRegex + "|" + divRegex);
    //    private final static Pattern operatorPattern = Pattern.compile(sumRegex + "|" + prodRegex + "|" + divRegex);
    private final static Pattern tokenPattern = Pattern.compile(numberRegex + "|(?<=\\d|\\))(" + operatorPattern + ")(?=\\d|\\()|" + openBracketRegex + "|" + closedBracketRegex);


    private final static Logger logger = Logger.getInstance();

    private final String expression;
    private final Set<String> variables;

    public MathExpressionEvaluator(String expression) {
        this.expression = MathExpressionUtils.simplify(expression);
        this.variables = MathExpressionUtils.parseVariables(expression);
    }

    public BigInteger evaluate() throws InvalidExpressionException {
        String patchedExpression = getPatchedExpression();
        List<String> tokenized = tokenizeExpression(patchedExpression);
        List<String> postFixExpression = postFix(tokenized);
        return evaluatePostfixExpression(postFixExpression);
    }

    private String getPatchedExpression() throws InvalidExpressionException {
        logger.debug("getPathcedExpression(). variables: " + variables);
        String finalExpression = expression;
        if (!variables.isEmpty()) {
            for (String variable : variables) {
                try {
                    logger.debug("getPathcedExpression(). replacing " + variable + " with " + AppState.getInstance().getVariable(variable));
                    finalExpression = finalExpression.replaceAll("(?<=\\b)" + variable + "(?=\\b)", AppState.getInstance().getVariable(variable).toString());
                } catch (VariableNotFoundException e) {
                    throw new InvalidExpressionException();
                }
            }
            finalExpression = MathExpressionUtils.simplify(finalExpression);
        }
        return finalExpression;
    }

    private List<String> tokenizeExpression(String expression) throws InvalidExpressionException {
        Logger.getInstance().debug("tokenize " + expression);
        Matcher tokenMatcher = tokenPattern.matcher(expression);
        List<String> tokenized = new ArrayList<>();
        StringBuilder reconstructedExpression = new StringBuilder();
        while (tokenMatcher.find()) {
            String token = tokenMatcher.group();
            tokenized.add(token);
            reconstructedExpression.append(token);
        }
        Logger.getInstance().debug("tokenized: " + tokenized);
        Logger.getInstance().debug("reconstructedExpression: " + reconstructedExpression);
        if (!reconstructedExpression.toString().equals(expression)) {
            throw new InvalidExpressionException();
        }
        return tokenized;
    }

    private List<String> postFix(List<String> tokenizedInFixExpression) throws InvalidExpressionException {
        Logger.getInstance().debug("postFix " + tokenizedInFixExpression);
        List<String> postFixExpression = new ArrayList<>();
        Deque<String> operatorsStack = new ArrayDeque<>();
        for (String token : tokenizedInFixExpression) {
            Logger.getInstance().debug("token " + token);
            if (numberPattern.matcher(token).find()) {
                postFixExpression.add(token);
            } else if (operatorPattern.matcher(token).find()) {
                processOperatorToken(token, operatorsStack, postFixExpression);
            } else if (openBracketPattern.matcher(token).find()) {
                operatorsStack.push(token);
            } else if (closedBracketPattern.matcher(token).find()) {
                processRightBracketToken(operatorsStack, postFixExpression);
            }
            Logger.getInstance().debug("result " + postFixExpression);
            Logger.getInstance().debug("stack " + operatorsStack);
        }
        while (!operatorsStack.isEmpty() && !openBracketPattern.matcher(operatorsStack.peek()).find() && !closedBracketPattern.matcher(operatorsStack.peek()).find()) {
            postFixExpression.add(operatorsStack.pop());
        }
        if (!operatorsStack.isEmpty()) {
            throw new InvalidExpressionException();
        }
        Logger.getInstance().debug("postFixed " + postFixExpression);
        return postFixExpression;
    }

    private static void processRightBracketToken(Deque<String> operatorsStack, List<String> postFixExpression) throws InvalidExpressionException {
        while (!openBracketPattern.matcher(operatorsStack.peek()).find()) {
            postFixExpression.add(operatorsStack.pop());
            if(operatorsStack.isEmpty()) {
                throw new InvalidExpressionException();
            }
        }
        operatorsStack.pop();
    }

    private static void processOperatorToken(String token, Deque<String> operatorsStack, List<String> postFixExpression) {
        if (operatorsStack.isEmpty() || openBracketPattern.matcher(operatorsStack.peek()).find()) {
            operatorsStack.push(token);
        } else if (sumPattern.matcher(token).find() || subPattern.matcher(token).find()) {
            pushOperatorOfSameOrHigherPriority(token, operatorsStack, postFixExpression);
        } else {
            if (sumPattern.matcher(operatorsStack.peek()).find() || subPattern.matcher(operatorsStack.peek()).find()) {
                operatorsStack.push(token);
            } else {
                pushOperatorOfSameOrHigherPriority(token, operatorsStack, postFixExpression);
            }
        }
    }

    private static void pushOperatorOfSameOrHigherPriority(String token, Deque<String> operatorsStack, List<String> postFixExpression) {
        while (!operatorsStack.isEmpty() && !openBracketPattern.matcher(operatorsStack.peek()).find()) {
            postFixExpression.add(operatorsStack.pop());
        }
        operatorsStack.push(token);
    }

    private BigInteger evaluatePostfixExpression(List<String> postfixExpression) {
        Logger.getInstance().debug("evaluatePostfixExpression " + postfixExpression);
        Deque<BigInteger> resultStack = new ArrayDeque<>();
        for (String token : postfixExpression) {
            if (numberPattern.matcher(token).find()) {
                Logger.getInstance().debug("number  " + token);
//                resultStack.push(Long.valueOf(token));
                resultStack.push(new BigInteger(token));
            } else if (operatorPattern.matcher(token).find()) {
                Logger.getInstance().debug("operator  " + token);
                BigInteger b = resultStack.pop();
                BigInteger a = resultStack.pop();
                if (sumPattern.matcher(token).find()) {
                    Logger.getInstance().debug(a + " + " + b);
                    resultStack.push(a.add(b));
                } else if (prodPattern.matcher(token).find()) {
                    Logger.getInstance().debug(a + " * " + b);
                    resultStack.push(a.multiply(b));
                } else if (subPattern.matcher(token).find()) {
                    Logger.getInstance().debug(a + " - " + b);
                    resultStack.push(a.subtract(b));
                } else if (divPattern.matcher(token).find()) {
                    Logger.getInstance().debug(a + " / " + b);
                    resultStack.push(a.divide(b));
                }
            }
        }
        return resultStack.peek();
    }
}
