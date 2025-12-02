package beans;

import dto.Response;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import dto.Request;
import dto.Result;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import utils.SQLString;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-bean для хранения и управления данных приложения.
 *
 * @author rinat
 */
@Named("dataBaseBean")
@ApplicationScoped
public class DataBaseBean {
    @Resource(lookup = "java:jboss/datasources/PostgreSQLDS")
    private DataSource dataSource;

    @PostConstruct
    private void init() {
        if (dataSource != null) {
            createTableIfNotExists();
        }
    }

    /**
     * Метод для загрузки данных из БД.
     *
     * @return Данные с БД
     */
    public Response loadResultsFromDB() {
        final List<Result> data = new ArrayList<>();
        if (dataSource == null) {
            return Response.errorResponse("База данных неактивна");
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLString.READ_INFO.getSql());
             ResultSet rs = stmt.executeQuery()) {

            final List<Result> loadedResults = new ArrayList<>();
            while (rs.next()) {
                final Float x = rs.getFloat("x_value");
                final Float y = rs.getFloat("y_value");
                final Float r = rs.getFloat("r_value");
                final Request request = new Request(x, y != null ? BigDecimal.valueOf(y) : null, r);

                String exitCodeName = rs.getString("exit");
                String message = rs.getString("message");
                String timestamp = rs.getString("time");
                long executionTime = rs.getLong("exec");

                final Result result = new Result(request, ExitCode.valueOf(exitCodeName),
                        message, timestamp, executionTime);
                loadedResults.add(result);
            }
            data.addAll(loadedResults);
        } catch (SQLException ignored) {
            return Response.errorResponse("Ошибка чтения");
        }
        return Response.goodResponse(data, "Успешно!");
    }

    private void createTableIfNotExists() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(SQLString.CREATE_TABLE.getSql());
        } catch (SQLException ignored) {
        }
    }

    /**
     * Метод для сохранения результатов.
     *
     * @param toSave Данные для сохранения
     * @return Сохранено ли
     */
    public boolean save(List<Result> toSave) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLString.ADD_INFO.getSql())) {
            for (Result result : toSave) {
                stmt.setFloat(1, result.getRequest().getX());
                stmt.setFloat(2, result.getRequest().getY().floatValue());
                stmt.setFloat(3, result.getRequest().getR());
                stmt.setString(4, result.getResult().name());
                stmt.setString(5, result.getMessage());
                stmt.setString(6, result.getTimestamp());
                stmt.setLong(7, result.getTime());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException ignored) {
            return false;
        }
        return true;
    }

    /**
     * Метод для очистки БД.
     *
     * @return Очищено ли
     */
    public boolean clear() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(SQLString.CLEAR.getSql());
        } catch (SQLException ignored) {
            return false;
        }
        return true;
    }
}
