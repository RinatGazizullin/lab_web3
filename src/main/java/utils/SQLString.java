package utils;

import lombok.Getter;

/**
 * Класс-шаблонизатор sql-запросов.
 *
 * @author rinat
 */
@Getter
public enum SQLString {
    CREATE_TABLE("""
            CREATE TABLE IF NOT EXISTS results (
                id SERIAL PRIMARY KEY,
                x_value DOUBLE PRECISION NOT NULL,
                y_value DOUBLE PRECISION NOT NULL,
                r_value DOUBLE PRECISION NOT NULL,
                exit VARCHAR(10) NOT NULL,
                message TEXT NOT NULL,
                time VARCHAR(50) NOT NULL, -- или TIMESTAMP, если вы храните как TIMESTAMP
                exec BIGINT NOT NULL
            );
            """),
    ADD_INFO("""
            INSERT INTO results (x_value, y_value, r_value, exit, message, time, exec)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """),
    READ_INFO("""
            SELECT id, x_value, y_value, r_value, exit, message, time, exec
            FROM results
            """),
    CLEAR("""
            TRUNCATE TABLE results
            """);
    private final String sql;

    /**
     * Конструктор.
     *
     * @param sql SQL-запрос
     */
    SQLString(String sql) {
        this.sql = sql;
    }
}
