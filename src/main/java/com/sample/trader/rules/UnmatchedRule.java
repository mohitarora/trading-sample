package com.sample.trader.rules;

/**
 * Default Rule that gets applied when no other rule matches.
 */
public class UnmatchedRule implements Rule<Object>
{
    @Override
    public boolean apply(Object event)
    {
        return true;
    }

    @Override
    public Class<Object> getType()
    {
        return Object.class;
    }

}
