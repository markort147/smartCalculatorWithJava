package calculator.lineprocessor.impl;

import calculator.lineprocessor.LineProcessor;
import calculator.presentation.impl.MessageResult;
import calculator.presentation.Result;
import calculator.state.AppState;

public class ExitCommandProcessor extends LineProcessor {

    @Override
    public Result process() {
        AppState.getInstance().terminateExecution();
        return new MessageResult("Bye bye!");
    }
}
