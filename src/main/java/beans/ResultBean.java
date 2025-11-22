package beans;

import builder.Builder;
import builder.RequestBuilder;
import com.google.gson.Gson;
import exceptions.DataException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import protocol.Request;
import protocol.Result;
import utils.Checker;
import utils.SQLString;
import javax.sql.DataSource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс-bean для хранения данных приложения.
 *
 * @author rinat
 */
@Getter
@Named("resultBean")
@SessionScoped
public class ResultBean implements Serializable {
    private static final Builder<Request> BUILDER = new RequestBuilder();
    private static final Checker CHECKER = new Checker();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Gson GSON = new Gson();
    @Setter
    private String x;
    @Setter
    private String y;
    @Setter
    private Map<String, Boolean> r;
    @Resource(lookup = "java:jboss/datasources/PostgreSQLDS")
    private DataSource dataSource;
    private final List<Result> data;

    /**
     * Конструктор.
     */
    public ResultBean() {
        data = new ArrayList<>();
    }

    /**
     * Метод для работы с БД.
     */
    @PostConstruct
    public void init() {
        x = "2";
        y = "2";
        r = new HashMap<>();
        r.put("1.0", true);
        r.put("1.5", false);
        r.put("2.0", false);
        r.put("2.5", false);
        r.put("3.0", false);
        if (dataSource == null) {
            return;
        }
        createTableIfNotExists();
        loadResultsFromDB();
    }

    private void loadResultsFromDB() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLString.READ_INFO.getSql());
             ResultSet rs = stmt.executeQuery()) {

            List<Result> loadedResults = new ArrayList<>();
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
            data.clear();
            data.addAll(loadedResults);
        } catch (SQLException ignored) {
        }
    }

    private void createTableIfNotExists() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(SQLString.CREATE_TABLE.getSql());
        } catch (SQLException ignored) {
        }
    }

    /**
     * Кастомный сеттер для установления одного значения.
     *
     * @param r Нужное R
     */
    public void setSelectedRString(String r) {
        final List<String> tokens = Arrays.asList(r.split(","));
        this.r.entrySet().forEach(entry -> entry.setValue(tokens.contains(entry.getKey())));
    }

    /**
     * Метод для получения строки Json.
     *
     * @return Json-строка
     */
    public String getJsonPoints() {
        return GSON.toJson(data);
    }

    /**
     * Метод для проверки попадания точки.
     */
    public void checkPoint() {
        final Map<String, String> map = new HashMap<>();
        map.put("x", x);
        map.put("y", y);
        for (String r : getSelectedR()) {
            map.put("r", r);
            Result result;
            final Instant start = Instant.now();
            try {
                final Request request = BUILDER.build(map);
                final boolean isHit = CHECKER.isHit(request);
                result = new Result(request, isHit ? ExitCode.HIT : ExitCode.MISS,
                        isHit ? "Попадание" : "Промах",
                        FORMATTER.format(start.atZone(ZoneId.systemDefault())),
                        Duration.between(start, Instant.now()).toNanos() / 1000);
            } catch (DataException e) {
                result = new Result(Request.EmptyRequest(), ExitCode.ERROR, e.getMessage(),
                        FORMATTER.format(start.atZone(ZoneId.systemDefault())),
                        Duration.between(start, Instant.now()).toNanos() / 1000);
            }
            data.add(result);
            saveResultToDB(result);
        }
    }

    /**
     * Метод для получения всех выбранных R.
     *
     * @return Список выбранных R
     */
    public List<String> getSelectedR() {
        return r.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Метод для получения всех выбранных R в виде строки.
     *
     * @return Строка всех выбранных R.
     */
    public String getSelectedRString() {
        return String.join(",", getSelectedR());
    }

    /**
     * Метод для очистки истории.
     */
    public void clear() {
        data.clear();
        clearDB();
    }

    private void clearDB() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(SQLString.CLEAR.getSql());
        } catch (SQLException ignored) {
        }
    }

    private void saveResultToDB(Result result) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLString.ADD_INFO.getSql())) {

            stmt.setFloat(1, result.getRequest().getX());
            stmt.setFloat(2, result.getRequest().getY().floatValue());
            stmt.setFloat(3, result.getRequest().getR());
            stmt.setString(4, result.getResult().name());
            stmt.setString(5, result.getMessage());
            stmt.setString(6, result.getTimestamp());
            stmt.setLong(7, result.getTime());

            stmt.executeUpdate();
        } catch (SQLException ignored) {
        }
    }
}
