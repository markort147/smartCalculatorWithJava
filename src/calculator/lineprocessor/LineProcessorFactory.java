package calculator.lineprocessor;

import calculator.lineprocessor.impl.*;
import calculator.log.Logger;
import calculator.presentation.impl.EmptyResult;
import calculator.presentation.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class LineProcessorFactory {

    private static final Pattern COMMAND_PATTERN = Pattern.compile("^/.*");
    private static final Pattern PRINT_PATTERN = Pattern.compile("^[^+\\/\\*\\-=]+$");
    private static final Pattern SINGLE_NUMBER_PATTERN = Pattern.compile("^\\d+$");
    private static final Pattern ASSIGNMENT_PATTERN = Pattern.compile("^.+=.*");

    private static final Map<String, Supplier<LineProcessor>> COMMAND_SUPPLIERS = new HashMap<>();
    private static final Map<String, LineProcessor> COMMANDS = new HashMap<>();

    static {
        COMMAND_SUPPLIERS.put("/exit", ExitCommandProcessor::new);
        COMMAND_SUPPLIERS.put("/help", HelpCommandProcessor::new);
    }

    public static LineProcessor getLineProcessor(String line) {
        if (line.isEmpty()) {
            Logger.getInstance().debug("LineProcessorFactory. returning an EmptyResult");
            return new LineProcessor() {

                @Override
                public Result process() {
                    return new EmptyResult();
                }
            };
        }
        if (isCommand(line)) {
            Logger.getInstance().debug("LineProcessorFactory. returning a CommandProcessor");
            return getCommandProcessor(line);
        }
        if (isAssignment(line)) {
            Logger.getInstance().debug("LineProcessorFactory. returning an AssignmentProcessor");
            return new AssignmentProcessor(line);
        }
        if (isPrintVariable(line)) {
            Logger.getInstance().debug("LineProcessorFactory. returning a PrintVariableProcessor");
            return new PrintVariableProcessor(line);
        }
        Logger.getInstance().debug("LineProcessorFactory. returning a MathExpressionProcessor");
        return new MathExpressionProcessor(line);
    }

    private static boolean isCommand(String command) {
        return COMMAND_PATTERN.matcher(command).find();
    }

    private static LineProcessor getCommandProcessor(String command) {
        COMMANDS.computeIfAbsent(command, key -> COMMAND_SUPPLIERS.getOrDefault(key, UnknownCommandProcessor::new).get());
        return COMMANDS.get(command);
    }

    private static boolean isPrintVariable(String command) {
        return PRINT_PATTERN.matcher(command).find() && !SINGLE_NUMBER_PATTERN.matcher(command).find();
    }

    private static boolean isAssignment(String command) {
        return ASSIGNMENT_PATTERN.matcher(command).find();
    }

}
