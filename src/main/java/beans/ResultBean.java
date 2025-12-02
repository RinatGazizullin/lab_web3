package beans;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import dto.Result;
import java.io.Serializable;
import java.util.*;

/**
 * Класс-bean для обработки данных приложения.
 *
 * @author rinat
 */
@Getter
@Named("resultBean")
@SessionScoped
public class ResultBean implements Serializable {
    private transient Gson gson;
    @Inject
    private transient DataBaseBean dataBaseBean;
    @Inject
    private transient CalculationBean calculationBean;
    @Setter
    private String x;
    @Setter
    private String y;
    @Setter
    private Map<String, Boolean> r;

    /**
     * Метод для работы с БД.
     */
    @PostConstruct
    public void init() {
        x = "2";
        y = "2";
        r = new HashMap<>();
        r.put("1.0", true);
        r.put("1.5", false);
        r.put("2.0", false);
        r.put("2.5", false);
        r.put("3.0", false);
    }

    private Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    /**
     * Кастомный сеттер для установления одного значения.
     *
     * @param r Нужное R
     */
    public void setSelectedRString(String r) {
        final List<String> tokens = Arrays.asList(r.split(","));
        this.r.entrySet().forEach(entry -> entry.setValue(tokens.contains(entry.getKey())));
    }

    /**
     * Метод для получения строки Json.
     *
     * @return Json-строка
     */
    public String getJsonPoints() {
        return getGson().toJson(dataBaseBean.loadResultsFromDB().getResults());
    }

    /**
     * Метод для проверки попадания точки.
     */
    public void checkPoint() {
        final List<Result> toSave = new ArrayList<>();
        final Map<String, String> map = new HashMap<>();
        map.put("x", x);
        map.put("y", y);
        for (String r : getSelectedR()) {
            map.put("r", r);
            toSave.add(calculationBean.calculatePoint(map));
        }
        dataBaseBean.save(toSave);
    }

    /**
     * Метод для получения всех выбранных R.
     *
     * @return Список выбранных R
     */
    public List<String> getSelectedR() {
        return r.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Метод для получения всех выбранных R в виде строки.
     *
     * @return Строка всех выбранных R.
     */
    public String getSelectedRString() {
        return String.join(",", getSelectedR());
    }

    /**
     * Метод для очистки истории.
     */
    public void clear() {
        dataBaseBean.clear();
    }

    /**
     * Геттер для данных с БД.
     *
     * @return Данные с БД
     */
    public List<Result> getData() {
        return dataBaseBean.loadResultsFromDB().getResults();
    }
}
