package com.sample.trader.impl;

import com.sample.trader.domain.Price;
import com.sample.trader.domain.Trade;

public interface AutoTrader
{
    /**
     * Builds a trade to be executed based on the supplied prices.
     *
     * @param price data
     *
     * @return trade to execute
     */
    Trade buildTrades(Price price);
}
