package com.sample.trader;

import static junit.framework.Assert.assertNull;

import javax.inject.Inject;

import com.sample.trader.config.Config;
import com.sample.trader.domain.Direction;
import com.sample.trader.domain.Price;
import com.sample.trader.domain.Trade;
import com.sample.trader.impl.AutoTrader;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = Config.class)
public class AutoTraderTest
{
    @Inject
    private AutoTrader autoTrader;

    @Before
    public void setUp()
    {
        autoTrader.buildTrades(new Price("BP", "7.61"));
        autoTrader.buildTrades(new Price("RSDA", "2201.00"));
        autoTrader.buildTrades(new Price("RSDA", "2209.00"));
        autoTrader.buildTrades(new Price("BP", "7.66"));
        autoTrader.buildTrades(new Price("BP", "7.64"));
    }

    @Test
    public void testBuyTrade()
    {
        Trade trade = autoTrader.buildTrades(new Price("BP", "7.67"));
        Assert.assertThat(trade.getProductName(), IsEqual.equalTo("BP"));
        Assert.assertThat(trade.getDirection(), IsEqual.equalTo(Direction.BUY));
        Assert.assertThat(trade.getPrice().toString(), IsEqual.equalTo("7.67"));
        Assert.assertThat(trade.getQuantity(), IsEqual.equalTo(1000));
    }

    @Test
    public void testNoTrade()
    {
        Trade trade = autoTrader.buildTrades(new Price("BP", "2.67"));
        assertNull(trade);
    }


}
