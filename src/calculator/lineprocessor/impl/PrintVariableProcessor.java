package calculator.lineprocessor.impl;

import calculator.exception.VariableNotFoundException;
import calculator.lineprocessor.LineProcessor;
import calculator.math.MathExpressionUtils;
import calculator.presentation.impl.MessageResult;
import calculator.presentation.Result;
import calculator.state.AppState;

public class PrintVariableProcessor extends LineProcessor {
    private String variableName;

    public PrintVariableProcessor(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public Result process() {
        if(!MathExpressionUtils.isValidVariableName(variableName)) {
            return new MessageResult("Invalid identifier");
        }
        try {
            return new MessageResult(AppState.getInstance().getVariable(variableName).toString());
        } catch (VariableNotFoundException e) {
            return new MessageResult("Unknown variable");
        }
    }
}
