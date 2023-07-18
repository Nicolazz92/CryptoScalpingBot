package org.velikokhatko.period_invesment.services.api.custom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkList {
    private String network;
    private String coin;
    private String withdrawIntegerMultiple;
    private boolean isDefault;
    private boolean depositEnable;
    private boolean withdrawEnable;
    private String depositDesc;
    private String withdrawDesc;
    private String specialTips;
    private String specialWithdrawTips;
    private String name;
    private boolean resetAddressStatus;
    private String addressRegex;
    private String addressRule;
    private String memoRegex;
    private String withdrawFee;
    private String withdrawMin;
    private String withdrawMax;
    private int minConfirm;
    private int unLockConfirm;
    private boolean sameAddress;
    private int estimatedArrivalTime;
    private String depositDust;
}
