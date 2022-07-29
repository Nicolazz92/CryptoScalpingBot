package velikokhatko.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RatioParamsDTO {

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

    public RatioParamsDTO(String symbol,
                          Integer deltaMinuteInterval,
                          Double deltaPercent,
                          LocalDateTime freshLimit) {
        this.symbol = symbol;
        this.deltaMinuteInterval = deltaMinuteInterval;
        this.deltaPercent = deltaPercent;
        this.freshLimit = freshLimit;
    }
}
