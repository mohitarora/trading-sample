package com.sample.trader.rules;

public interface Rule<EventType>
{
    public boolean apply(EventType event);

    public Class<EventType> getType();

}
