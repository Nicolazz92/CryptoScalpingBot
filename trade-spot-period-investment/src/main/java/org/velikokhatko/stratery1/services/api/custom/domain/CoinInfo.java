package org.velikokhatko.stratery1.services.api.custom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinInfo {
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
    private List<NetworkList> networkList;
}
