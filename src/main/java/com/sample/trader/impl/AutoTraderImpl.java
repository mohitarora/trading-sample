package com.sample.trader.impl;

import static com.google.common.base.Preconditions.checkArgument;

import com.sample.trader.domain.Direction;
import com.sample.trader.domain.Price;
import com.sample.trader.domain.Trade;
import com.sample.trader.rules.Rule;
import com.sample.trader.rules.UpwardPricingTrendFinder;
import org.springframework.util.TypeUtils;

public class AutoTraderImpl implements AutoTrader
{
    private final RuleHandler handler;
    private final SymbolValidator symbolValidator;


    public AutoTraderImpl(RuleHandler handler, SymbolValidator symbolValidator)
    {
        this.handler = handler;
        this.symbolValidator = symbolValidator;
    }

    @Override
    public Trade buildTrades(Price price)
    {
        checkArgument(symbolValidator.isSymbolValid(price.getProductName()), "Symbol is not supported by the system");
        Rule<Price> appliedRule = handler.submit(price);
        Direction direction = getDirection(appliedRule);
        if (direction != Direction.UNKNOWN)
        {
            return new Trade(price.getProductName(), direction, price.getPrice(), 1000);
        }
        return null;
    }

    /**
     * Get Direction from the Rule.
     *
     * @param rule
     *
     * @return
     */
    private Direction getDirection(Rule rule)
    {
        //Defaulting to Unknown
        Direction direction = Direction.UNKNOWN;
        if (TypeUtils.isAssignable(UpwardPricingTrendFinder.class, rule.getClass()))
        {
            direction = (Direction.BUY);
        }
        return direction;
    }
}
