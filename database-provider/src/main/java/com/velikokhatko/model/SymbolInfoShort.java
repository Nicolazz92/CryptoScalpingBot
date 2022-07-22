package com.velikokhatko.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "SYMBOL_INFO_SHORT")
public class SymbolInfoShort extends BaseEntity {

    private String symbol;
    private String baseAsset;
    private double lotSizeMin;
    private double marketLotSizeMin;
}
