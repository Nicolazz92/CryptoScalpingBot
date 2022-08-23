package com.velikokhatko.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class CoinInfo extends BaseEntity {
    private String coin;
    private boolean depositAllEnable;
    private boolean withdrawAllEnable;
    private String name;
    private String free;
    private String locked;
    private String freeze;
    private String withdrawing;
    private String ipoing;
    private String ipoable;
    private String storage;
    private boolean isLegalMoney;
    private boolean trading;
    @OneToMany
    private List<NetworkList> networkList;
}
