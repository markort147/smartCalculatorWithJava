package calculator.lineprocessor.impl;

import calculator.lineprocessor.LineProcessor;
import calculator.presentation.impl.MessageResult;
import calculator.presentation.Result;

public class UnknownCommandProcessor extends LineProcessor {

    @Override
    public Result process() {
        return new MessageResult("Unknown command");
    }
}
