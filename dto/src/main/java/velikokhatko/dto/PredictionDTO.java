package velikokhatko.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class PredictionDTO {

    private boolean canBuy;
    private LocalDateTime freshLimit;

    public PredictionDTO(boolean canBuy, int predictionHoursTTL) {
        this.canBuy = canBuy;
        this.freshLimit = LocalDateTime.now().plusHours(predictionHoursTTL).plusMinutes(RandomUtils.nextLong(0, 60));
    }
}
