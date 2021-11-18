package org.velikokhatko.stratery1.services.context;

import com.binance.api.client.domain.general.ExchangeInfo;

import java.util.Map;
import java.util.TreeMap;

public final class Context {
    public static Map<String, ExchangeInfo> exchangeInfo = new TreeMap<>();
}
