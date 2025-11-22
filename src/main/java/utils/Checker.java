package utils;

import protocol.Request;
import java.math.BigDecimal;

/**
 * Класс для проверки попадания точки в график.
 *
 * @author rinat
 */
public class Checker {
    /**
     * Метод для проверки попадания.
     *
     * @param data Данные клиента
     * @return Попал ли
     */
    public boolean isHit(Request data) {
        return quarter1(data) || quarter2(data) || quarter4(data);
    }

    private boolean quarter1(Request data) {
        if (data.getX() >= 0 && data.getY().compareTo(BigDecimal.ZERO) >= 0) {
            return data.getY().pow(2).compareTo(BigDecimal.valueOf(Math.pow(data.getR() / 2, 2) -
                    Math.pow(data.getX(), 2))) <= 0;
        }
        return false;
    }

    private boolean quarter2(Request data) {
        if (data.getX() <= 0 && data.getY().compareTo(BigDecimal.ZERO) >= 0) {
            return BigDecimal.valueOf(data.getX() + data.getR() / 2).compareTo(data.getY()) >= 0;
        }
        return false;
    }

    private boolean quarter4(Request data) {
        if (data.getX() >= 0 && data.getY().compareTo(BigDecimal.ZERO) <= 0) {
            return data.getY().compareTo(BigDecimal.valueOf(-data.getR())) >= 0 &&
                    data.getX() <= data.getR() / 2;
        }
        return false;
    }
}
