package velikokhatko.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SymbolInfoShortDTO {

    private String symbol;
    private String baseAsset;
    private double lotSizeMin;
    private double marketLotSizeMin;
}
