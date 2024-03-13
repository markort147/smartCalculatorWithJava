package calculator.state;

import calculator.exception.VariableNotFoundException;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AppState {
    private static AppState instance;
    private final Map<String, BigInteger> variables = new HashMap<>();
    private boolean running = true;

    private AppState() {
    }

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    public void terminateExecution() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setVariable(String key, BigInteger value) {
        variables.put(key, value);
    }

    public boolean containsVariable(String key) {
        return variables.containsKey(key);
    }

    public boolean containsAllVariables(Collection<String> variablesToCheck) {
        return variables.keySet().containsAll(variablesToCheck);
    }

    public BigInteger getVariable(String key) throws VariableNotFoundException {
        if(variables.containsKey(key)) {
            return variables.get(key);
        } else {
            throw new VariableNotFoundException();
        }
    }

}
