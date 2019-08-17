package pro.belbix.dbchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

public class Config {
    private final static Logger log = LoggerFactory.getLogger(Config.class);
    private static String path;
    private static Config instance;

    private Config() {
        init();
    }

    public static Config getInstance() {
        if (instance != null) return instance;
        synchronized (Config.class) {
            if (instance != null) return instance;
            instance = new Config();
        }
        return instance;
    }

    private void init() {
        if (path == null) return;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = br.readLine()) != null) {
                readLine(s);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

    private void readLine(String s) {
        String[] splt = s.split(":", 2);
        if (splt.length < 2) throw new IllegalStateException("Wrong config: " + s);
        String name = splt[0].trim();
        String value = splt[1].trim();

        try {
            Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            switch (field.getType().getSimpleName()) {
                case "String":
                    field.set(this, value);
                    break;
                case "Integer":
                    field.set(this, Integer.valueOf(value));
                    break;
                default:
                    throw new UnsupportedOperationException("Type not supported: " + field.getType().getSimpleName());
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void setPath(String path) {
        Config.path = path;
    }

    private String dbClassName = "com.mysql.cj.jdbc.Driver";
    private String dbUrl = "jdbc:mysql://localhost:3306/tim?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private String dbUser = "belbix";
    private String dbPassword = "111111";
    private Integer loopTime = 10;
    private String dateSelect = "select t.date from ticks as t order by t.date desc limit 1";
    private Integer maxDiff = 100;

    public String getDbClassName() {
        return dbClassName;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public int getLoopTime() {
        return loopTime;
    }

    public String getDateSelect() {
        return dateSelect;
    }

    public int getMaxDiff() {
        return maxDiff;
    }
}
