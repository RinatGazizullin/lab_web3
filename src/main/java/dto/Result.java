package dto;

import beans.ExitCode;
import lombok.Getter;
import java.io.Serializable;

/**
 * Результат вычислений одного вычисления.
 *
 * @author rinat
 */
@Getter
public class Result implements Serializable {
    private final Request request;
    private final ExitCode result;
    private final String message;
    private final String timestamp;
    private final long time;

    /**
     * Конструктор.
     *
     * @param request Запрос клиента
     * @param result Код результата
     * @param message Сообщение-результат работы
     * @param timestamp Время начала обработки
     * @param time Время обработки
     */
    public Result(Request request, ExitCode result, String message, String timestamp, long time) {
        this.request = request;
        this.result = result;
        this.message = message;
        this.timestamp = timestamp;
        this.time = time;
    }
}
