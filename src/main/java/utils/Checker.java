package utils;

import dto.Request;
import java.math.BigDecimal;

/**
 * Класс для проверки попадания точки в график.
 *
 * @author rinat
 */
public class Checker {
    private static final BigDecimal NEGATIVE_ONE = new BigDecimal("-1");

    /**
     * Метод для проверки попадания.
     *
     * @param data Данные клиента
     * @return Попал ли
     */
    public boolean isHit(Request data) {
        final float x = data.getX();
        final BigDecimal y = data.getY();
        final float r = data.getR();
        final float halfR = r * 0.5f;

        return quarter1(x, y, halfR) || quarter2(x, y, halfR) || quarter4(x, y, r, halfR);
    }

    private boolean quarter1(float x, BigDecimal y, float r) {
        if (x >= 0 && y.compareTo(BigDecimal.ZERO) >= 0) {
            final BigDecimal ySquared = y.multiply(y);
            final float rightSide = (r * r) - (x * x);
            return ySquared.compareTo(BigDecimal.valueOf(rightSide)) <= 0;
        }
        return false;
    }

    private boolean quarter2(float x, BigDecimal y, float r) {
        if (x <= 0 && y.compareTo(BigDecimal.ZERO) >= 0) {
            final float rightSide = x + r;
            return y.compareTo(BigDecimal.valueOf(rightSide)) <= 0;
        }
        return false;
    }

    private boolean quarter4(float x, BigDecimal y, float r, float halfR) {
        if (x >= 0 && y.compareTo(BigDecimal.ZERO) <= 0) {
            final BigDecimal negativeR = BigDecimal.valueOf(r).multiply(NEGATIVE_ONE);
            return y.compareTo(negativeR) >= 0 && x <= halfR;
        }
        return false;
    }
}
