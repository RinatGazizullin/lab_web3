package validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import static validator.RequestValidator.prepareString;

/**
 * JSF-валидатор полей для отправки данных через форму.
 */
@FacesValidator("floatValidator")
public class NumberValidator implements Validator<String> {
    @Override
    public void validate(FacesContext context, UIComponent component, String value)
            throws ValidatorException {
        if (value == null || value.trim().isEmpty()) {
            return;
        }
        final String s = prepareString(value);
        try {
            double num = Float.parseFloat(s);
            if (num < -5 || num > 3) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Число вне диапазона",
                        "Введите число от -5 до 3"
                ));
            }
        } catch (NumberFormatException e) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Некорректный формат числа",
                    "Используйте формат: 2.5 или 2,5"
            ));
        }
    }
}
