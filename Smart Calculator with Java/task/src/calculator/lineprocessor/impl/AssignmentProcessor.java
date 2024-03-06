package calculator.lineprocessor.impl;

import calculator.exception.InvalidExpressionException;
import calculator.lineprocessor.LineProcessor;
import calculator.math.MathExpressionEvaluator;
import calculator.math.MathExpressionUtils;
import calculator.presentation.impl.EmptyResult;
import calculator.presentation.Result;
import calculator.presentation.impl.MessageResult;
import calculator.state.AppState;

public class AssignmentProcessor extends LineProcessor {
    private MathExpressionEvaluator mathExpressionEvaluator;
    private String variableName;

    public AssignmentProcessor(String line) {
        String[] members = line.split("=", 2);
        this.variableName = members[0];
        this.mathExpressionEvaluator = new MathExpressionEvaluator(members[1]);
    }

    @Override
    public Result process() {
        if(!MathExpressionUtils.isValidVariableName(variableName)) {
            return new MessageResult("Invalid identifier");
        }
        try {
            AppState.getInstance().setVariable(variableName, mathExpressionEvaluator.evaluate());
        } catch (InvalidExpressionException e) {
            return new MessageResult("Invalid Assignment");
        }
        return new EmptyResult();
    }
}
