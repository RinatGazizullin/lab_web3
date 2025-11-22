package protocol;

import lombok.Getter;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Запрос от клиента.
 *
 * @author rinat
 */
@Getter
public class Request implements Serializable {
    private final Float x;
    private final BigDecimal y;
    private final Float r;

    /**
     * Конструктор.
     *
     * @param x Координата x
     * @param y Координата y
     * @param r Коэффициент
     */
    public Request(Float x, BigDecimal y, Float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    /**
     * Метод для получения пустой точки.
     *
     * @return Пуста точка
     */
    public static Request EmptyRequest() {
        return new Request(null, null, null);
    }
}
