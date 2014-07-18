package com.sample.trader.impl;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.inject.Inject;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.sample.trader.rules.Rule;
import com.sample.trader.rules.UnmatchedRule;
import org.springframework.util.ClassUtils;

public class RuleHandler
{
    @Inject
    private ExecutorService executor;

    private Multimap<String, Rule> ruleMap = ArrayListMultimap.create();

    public RuleHandler(List<Rule> ruleList)
    {
        buildCache(ruleList);
    }

    /**
     * Build Cache of all the rules in map. Key will be context e.g Price in this coding challenge.
     *
     * @param ruleList
     */
    private void buildCache(List<Rule> ruleList)
    {
        for (Rule rule : ruleList)
        {
            String ruleContext = ClassUtils.getShortName(rule.getType());
            ruleMap.put(ruleContext, rule);
        }
    }

    public <EventType> Rule<EventType> submit(final EventType event)
    {
        final String ruleContext = ClassUtils.getShortName(event.getClass());

        Future<Rule<EventType>> ruleFuture = executor.submit(new Callable<Rule<EventType>>()
        {
            @Override
            public Rule<EventType> call() throws Exception
            {
                for (Rule rule : ruleMap.get(ruleContext))
                {
                    if (rule.apply(event))
                    {
                        return rule;
                    }
                }
                return (Rule<EventType>) new UnmatchedRule();
            }
        });

        try
        {
            //Get the Rule that gets applied on Event.
            return ruleFuture.get();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error occurred during price submission", e);
        }
    }

}
