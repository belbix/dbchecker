package pro.belbix.dbchecker;

public class Config {
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
        if(path == null) return;
    }

    public static void setPath(String path){
        Config.path = path;
    }

    private String dbClassName = "com.mysql.cj.jdbc.Driver";
    private String dbUrl = "jdbc:mysql://localhost:3306/tim?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private String dbUser = "belbix";
    private String dbPassword = "111111";
    private int loopTime = 10;
    private String dateSelect = "select t.date from ticks as t order by t.date desc limit 1";
    private int maxDiff = 100;

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
