package org.velikokhatko.stratery1.services.ratio.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ParamsReview {

    /**
     * Дата, по которую (не включая) будет проводиться проверка
     */
    private LocalDateTime endReviewLDT;

    /**
     * Срок в днях, на каком временном интервале будет проводиться проверка
     */
    private Integer fullDayReviewInterval;

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

    /**
     * Подсчет количества сделок
     */
    private Integer dealsCount = 0;

    public ParamsReview(LocalDateTime endReviewLDT,
                        Integer fullDayReviewInterval,
                        Integer deltaMinuteInterval,
                        Double deltaPercent) {
        this.endReviewLDT = endReviewLDT;
        this.fullDayReviewInterval = fullDayReviewInterval;
        this.deltaMinuteInterval = deltaMinuteInterval;
        this.deltaPercent = deltaPercent;
    }
}
