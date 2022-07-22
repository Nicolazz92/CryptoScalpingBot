package com.velikokhatko.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.RandomUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PREDICTIONS")
public class Prediction extends BaseEntity {

    private boolean canBuy;
    private LocalDateTime freshLimit;

    public Prediction(boolean canBuy, int predictionHoursTTL) {
        this.canBuy = canBuy;
        this.freshLimit = LocalDateTime.now().plusHours(predictionHoursTTL).plusMinutes(RandomUtils.nextLong(0, 60));
    }
}
