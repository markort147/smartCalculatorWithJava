package calculator.log;

public class Logger {

    public static final int ERROR = 0;
    public static final int WARN = 1;
    public static final int INFO = 2;
    public static final int DEBUG = 3;

    private static Logger instance;
    private int level;

    private Logger() {}

    public static void init(int level) {
        if(instance == null) {
            instance = new Logger();
        }
        instance.setLevel(level);
    }

    private void setLevel(int level) {
        this.level = level;
    }

    public static Logger getInstance() {
        if(instance == null) {
            init(INFO);
        }
        return instance;
    }

    public void debug(String message) {
        if (level >= DEBUG) {
            System.out.println("DEBUG - " + message);
        }
    }
    public void info(String message) {
        if (level >= INFO) {
            System.out.println(message);
        }
    }
    public void warn(String message) {
        if (level >= WARN) {
            System.out.println("WARN - " + message);
        }
    }
    public void error(String message) {
        if (level >= ERROR) {
            System.out.println("ERROR - " + message);
        }
    }

}
