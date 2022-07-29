package org.velikokhatko.stratery1.services.api.custom;

import com.binance.api.client.constant.BinanceApiConstants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import velikokhatko.dto.CoinInfoDTO;

import java.util.List;

public interface BinanceCustomApiService {

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("sapi/v1/capital/config/getall")
    Call<List<CoinInfoDTO>> getAllCoinsInfo(@Query("timestamp") Long timestamp);
}
