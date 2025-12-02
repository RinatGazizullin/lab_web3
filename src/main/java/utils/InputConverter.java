package utils;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import static validator.RequestValidator.prepareString;

/**
 * Класс для конвертации данных перед валидатором.
 *
 * @author rinat
 */
@FacesConverter("inputConverter")
public class InputConverter implements Converter<String> {
    /**
     * Геттер для получения объекта.
     *
     * @param context Контект веб-приложения
     * @param component Конмпонент стрички
     * @param value Значение копонента
     * @return Исправленное значение
     */
    @Override
    public String getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return prepareString(value);
    }

    /**
     * Геттер для получения строки.
     *
     * @param context Контект веб-приложения
     * @param component Конмпонент стрички
     * @param value Значение копонента
     * @return Исправленное значение
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, String value) {
        if (value == null) {
            return "";
        }
        return prepareString(value);
    }
}