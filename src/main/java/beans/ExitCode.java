package beans;

import lombok.Getter;

import java.io.Serializable;

/**
 * Класс ответ работы.
 *
 * @author rinat
 */
@Getter
public enum ExitCode implements Serializable {
    HIT(0),
    MISS(1),
    ERROR(2);
    /**
     *  Геттер для поля.
     */
    private final int code;

    /**
     * Конструктор класса.
     *
     * @param code Ответ работы
     */
    ExitCode(int code) {
        this.code = code;
    }

}
