package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParamsAndResult {

    /**
     * Минутный интервал, в который должно произойти изменение цены
     */
    private Integer minuteInterval;

    /**
     * Дельта изменения цены в процентах, изменения меньше которой не учитываются
     */
    private Double deltaPercent;

    /**
     * Дельта конечного изменения денежного эквивалента после окончания тестового прогона
     */
    private Double resultPercent;

    public ParamsAndResult(Integer minuteInterval, Double deltaPercent) {
        this.minuteInterval = minuteInterval;
        this.deltaPercent = deltaPercent;
    }
}
