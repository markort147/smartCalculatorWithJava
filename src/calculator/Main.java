package calculator;

import calculator.lineprocessor.LineProcessor;
import calculator.lineprocessor.LineProcessorFactory;
import calculator.log.Logger;
import calculator.presentation.Result;
import calculator.state.AppState;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Logger.init(Logger.INFO);
        Scanner scanner = new Scanner(System.in);
        while (AppState.getInstance().isRunning()) {
            String line = getInput(scanner);
            processInput(line);
        }
    }

    private static String getInput(Scanner scanner) {
        String line = scanner.nextLine();
//        line = "33 + 20 + 11 + 49 + n - 9 + 1 - 80 + 4";
        return line.replaceAll(" ", "");
    }

    private static void processInput(String line) {
        LineProcessor lineProcessor = LineProcessorFactory.getLineProcessor(line);
        Result result = lineProcessor.process();
        result.present();
    }

}