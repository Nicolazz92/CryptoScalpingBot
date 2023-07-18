package org.velikokhatko.period_invesment.services.api.custom;

import com.binance.api.client.constant.BinanceApiConstants;
import org.velikokhatko.period_invesment.services.api.custom.domain.CoinInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import java.util.List;

public interface BinanceCustomApiService {

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("sapi/v1/capital/config/getall")
    Call<List<CoinInfo>> getAllCoinsInfo(@Query("timestamp") Long timestamp);
}
