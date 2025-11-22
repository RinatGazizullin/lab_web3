package validator;

import exceptions.ValidatorException;
import java.util.Map;

/**
 * Проверка запроса для обеспечения безопасности от атак.
 *
 * @author rinat
 */
public class RequestValidator implements Validator<Map<String, String>> {
    private static final String PATTERN = "[-+]?\\d+(\\.\\d+)?";

    /**
     * Основной метод проверки.
     *
     * @param request Объект для проверки
     * @throws ValidatorException Ошибка проверки
     */
    @Override
    public void validate(Map<String, String> request) throws ValidatorException {
        validateRequest(request);
        validateX(prepareString(request.get("x")));
        validateY(prepareString(request.get("y")));
        validateR(prepareString(request.get("r")));
    }

    private void validateRequest(Map<String, String> request) throws ValidatorException {
        if (request == null) {
            throw new ValidatorException("Запрос не может быть пустым");
        }
    }

    private void validateX(String s) throws ValidatorException {
        if (s == null) {
            throw new ValidatorException("Нужно выбрать X");
        }
        if (!s.matches(PATTERN)) {
            throw new ValidatorException("X не является числом");
        }
    }

    private void validateY(String s) throws ValidatorException {
        if (s == null) {
            throw new ValidatorException("Нужно выбрать Y");
        }
    }

    private void validateR(String s) throws ValidatorException {
        if (s == null) {
            throw new ValidatorException("Нужно выбрать R");
        }
        if (!s.matches(PATTERN)) {
            throw new ValidatorException("R не является числом");
        }
        final float r = Float.parseFloat(s);
        if (r < 1 || r > 3 || r % .5 != 0) {
            throw new ValidatorException("Неверное значение R");
        }
    }

    /**
     * Метод для подготовки сырых чисел для парсинга.
     *
     * @param s Сырое значение
     * @return Обработанное значение
     */
    public static String prepareString(String s) {
        return (s.startsWith(".") || s.startsWith(",") ? "0" : "")
                + s.replace(',', '.');
    }
}
