package com.sample.trader.config;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.sample.trader.impl.AutoTrader;
import com.sample.trader.impl.AutoTraderImpl;
import com.sample.trader.impl.RuleHandler;
import com.sample.trader.impl.SymbolValidator;
import com.sample.trader.rules.Rule;
import com.sample.trader.rules.UnmatchedRule;
import com.sample.trader.rules.UpwardPricingTrendFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

@Configuration
public class Config
{
    public static final int PRICES_TO_COMPARE = 4;

    @Bean
    public Rule getUnmatchedRule()
    {
        return new UnmatchedRule();
    }

    @Bean
    public Rule getUpwardPricingTrendFinder()
    {
        return new UpwardPricingTrendFinder(PRICES_TO_COMPARE);
    }

    @Bean
    @Inject
    public RuleHandler getRuleHandler(List<Rule> ruleList)
    {
        return new RuleHandler(ruleList);
    }

    @Bean
    public SymbolValidator getSymbolValidator(
        @Value("${valid.symbols}")
        String validSymbols)
    {
        return new SymbolValidator(Sets.newHashSet(Splitter.on(",").split(validSymbols)));
    }

    @Bean
    @Inject
    public AutoTrader getAutoTrader(RuleHandler ruleHandler, SymbolValidator symbolValidator)
    {
        return new AutoTraderImpl(ruleHandler, symbolValidator);
    }

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() throws IOException
    {
        PropertyPlaceholderConfigurer props = new PropertyPlaceholderConfigurer();
        props.setLocations(new Resource[] {new ClassPathResource("application.properties")});
        return props;
    }

    @Bean
    public ThreadPoolExecutorFactoryBean getThreadPoolExecutorFactoryBean()
    {
        ThreadPoolExecutorFactoryBean threadPoolExecutorFactoryBean = new ThreadPoolExecutorFactoryBean();
        threadPoolExecutorFactoryBean.setCorePoolSize(5);
        threadPoolExecutorFactoryBean.setMaxPoolSize(10);
        threadPoolExecutorFactoryBean.setQueueCapacity(1000);
        threadPoolExecutorFactoryBean.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolExecutorFactoryBean.setThreadGroupName("Core Engine Thread Pool");
        return threadPoolExecutorFactoryBean;
    }
}
