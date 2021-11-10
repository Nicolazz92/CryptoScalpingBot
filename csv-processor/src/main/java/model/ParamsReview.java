package model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ParamsReview {

    /**
     * Дата, по которую (не включая) будет проводиться проверка
     */
    private LocalDate endReviewDate;

    /**
     * Срок в днях, на каком временном интервале будет проводиться проверка
     */
    private Integer fullReviewInterval;

    /**
     * Минутный интервал, в который должно произойти изменение цены
     */
    private Integer deltaMinuteInterval;

    /**
     * Дельта изменения цены в процентах, изменения меньше которой не учитываются
     */
    private Double deltaPercent;

    /**
     * Дельта конечного изменения денежного эквивалента после окончания проверки
     */
    private Double resultPercent;

    public ParamsReview(LocalDate endReviewDate,
                        Integer fullReviewInterval,
                        Integer deltaMinuteInterval,
                        Double deltaPercent) {
        this.endReviewDate = endReviewDate;
        this.fullReviewInterval = fullReviewInterval;
        this.deltaMinuteInterval = deltaMinuteInterval;
        this.deltaPercent = deltaPercent;
    }
}
