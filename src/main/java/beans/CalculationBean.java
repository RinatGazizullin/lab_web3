package beans;

import builder.Builder;
import builder.RequestBuilder;
import dto.Request;
import dto.Result;
import exceptions.DataException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import utils.Checker;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Класс-bean для проверки попадания точки.
 *
 * @author rinat
 */
@Named("calculation")
@ApplicationScoped
public class CalculationBean {
    private static final Checker CHECKER = new Checker();
    private static final Builder<Request> BUILDER = new RequestBuilder();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Метод для проверки попадания точки.
     *
     * @param map Данные для проверки
     * @return Результат проверки
     */
    public Result calculatePoint(Map<String, String> map) {
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
        return result;
    }
}
