package dto;

import lombok.Getter;
import java.util.List;

/**
 * Класс-результат загрузки данных с БД.
 *
 * @author rinat
 */
@Getter
public class Response {
    private final List<Result> results;
    private final String message;
    private final boolean isSuccess;

    private Response(List<Result> results, String message, boolean isSuccess) {
        this.results = results;
        this.message = message;
        this.isSuccess = isSuccess;
    }

    /**
     * Ответ-ошибка.
     *
     * @param error Сообщение об ощибке
     * @return Ответ с ошибкой
     */
    public static Response errorResponse(String error) {
        return new Response(List.of(), error, false);
    }

    /**
     * Ответ-успех.
     *
     * @param result Список точек
     * @param message Сообщение
     * @return Ответ с результатом
     */
    public static Response goodResponse(List<Result> result, String message) {
        return new Response(result, message, true);
    }
}
