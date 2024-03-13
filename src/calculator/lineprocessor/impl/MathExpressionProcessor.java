package calculator.lineprocessor.impl;

import calculator.exception.InvalidExpressionException;
import calculator.lineprocessor.LineProcessor;
import calculator.math.MathExpressionEvaluator;
import calculator.presentation.impl.MessageResult;
import calculator.presentation.Result;

public class MathExpressionProcessor extends LineProcessor {
    private MathExpressionEvaluator mathExpressionEvaluator;

    public MathExpressionProcessor(String expression) {
        this.mathExpressionEvaluator = new MathExpressionEvaluator(expression);
    }

    @Override
    public Result process() {
        try {
            return new MessageResult(mathExpressionEvaluator.evaluate().toString());
        } catch (InvalidExpressionException e) {
            return new MessageResult("Invalid expression");
        }
    }
}
