package builder;

import exceptions.DataException;
import exceptions.ValidatorException;
import dto.Request;
import validator.RequestValidator;
import validator.Validator;
import java.math.BigDecimal;
import java.util.Map;
import static validator.RequestValidator.prepareString;

/**
 * Класс для парсинга данных клиента.
 *
 * @author rinat
 */
public class RequestBuilder implements Builder<Request> {
    private static final Validator<Map<String, String>> VALIDATOR = new RequestValidator();

    /**
     * Метод для сборки запроса.
     *
     * @param data Данные для сборки
     * @return Собранный объект
     * @throws DataException Ошибка сборки
     */
    @Override
    public Request build(Map<String, String> data) throws DataException {
        try {
            VALIDATOR.validate(data);
        } catch (ValidatorException e) {
            throw new DataException(e.getMessage());
        }
        return new Request(Float.parseFloat(prepareString(data.get("x"))),
                BigDecimal.valueOf(Double.parseDouble(prepareString(data.get("y")))),
                Float.parseFloat(prepareString(data.get("r"))));
    }
}
