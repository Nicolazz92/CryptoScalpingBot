package com.velikokhatko.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "RATIO_PARAMS")
public class RatioParams extends BaseEntity {

    private String symbol;

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

    /**
     * В какой момент данные будут считаться устаревшими
     */
    private LocalDateTime freshLimit;

    public RatioParams(String symbol,
                       Integer deltaMinuteInterval,
                       Double deltaPercent,
                       LocalDateTime freshLimit) {
        this.symbol = symbol;
        this.deltaMinuteInterval = deltaMinuteInterval;
        this.deltaPercent = deltaPercent;
        this.freshLimit = freshLimit;
    }
}
