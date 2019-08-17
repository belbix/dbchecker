package pro.belbix.dbchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


public class DBService {
    private final static Logger log = LoggerFactory.getLogger(DBService.class);
    private final Config config;
    private static DBService instance;
    private Connection conn;
    private Map<String, PreparedStatement> statements = new HashMap<>();

    private DBService() {
        this.config = Config.getInstance();
        init();
    }

    public static DBService getInstance() {
        if (instance != null) return instance;
        synchronized (DBService.class) {
            if (instance != null) return instance;
            instance = new DBService();
        }
        return instance;
    }

    private void init() {
        try {
            Class.forName(config.getDbClassName());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        log.info("Connect to " + config.getDbUrl());
        try {
            conn = DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPassword());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private PreparedStatement findStatement(String sql) {
        PreparedStatement statement;
        statement = statements.get(sql);
        if (statement != null) return statement;
        try {
            statement = conn.prepareStatement(sql);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        statements.put(sql, statement);
        return statement;
    }

    public Instant selectLastDate(String sql) {
        PreparedStatement statement = findStatement(sql);
        Instant result = null;
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                result = rs.getTimestamp(1).toInstant();
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }


}
