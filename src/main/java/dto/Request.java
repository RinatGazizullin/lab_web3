package dto;

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
     * Геттер для X, но с ограничением длины.
     *
     * @return Ограниченный X
     */
    public String getXString() {
        final String s = String.valueOf(x == null ? "-" : x);
        if (s.length() > 10) {
            return s.substring(0, 10) + "...";
        }
        return s;
    }

    /**
     * Геттер для Y, но с ограничением длины.
     *
     * @return Ограниченный Y
     */
    public String getYString() {
        final String s = y == null ? "-" : y.toString();
        if (s.length() > 10) {
            return s.substring(0, 10) + "...";
        }
        return s;
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
