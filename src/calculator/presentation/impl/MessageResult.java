package calculator.presentation.impl;

import calculator.log.Logger;
import calculator.presentation.Result;

public class MessageResult implements Result {
    private String message;

    public MessageResult(String message) {
        this.message = message;
    }

    @Override
    public void present() {
        Logger.getInstance().info(message);
    }
}
