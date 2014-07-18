package com.sample.trader.rules;

import static com.google.common.collect.Lists.newArrayList;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import com.google.common.util.concurrent.Striped;
import com.sample.trader.domain.Price;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.buffer.CircularFifoBuffer;

public class UpwardPricingTrendFinder implements Rule<Price>
{
    private final int pricesToCompare;

    private final BigDecimal divisor;

    private final ConcurrentHashMap<String, Buffer> priceBuffer = new ConcurrentHashMap<String, Buffer>();

    private final Striped<Lock> stripedLock = Striped.lock(10);

    public UpwardPricingTrendFinder(int pricesToCompare)
    {
        this.pricesToCompare = pricesToCompare;
        divisor = new BigDecimal(pricesToCompare);
    }

    /**
     * Checks if this rule can be applied on Event which is price for this specific rule.
     *
     * @param price
     *
     * @return
     */
    @Override
    public boolean apply(Price price)
    {
        Lock priceNameLock = stripedLock.get(price.getProductName());
        priceNameLock.lock();
        try
        {
            Buffer circularFifoBuffer = getPriceBufferForProductName(price);
            circularFifoBuffer.add(price);
            if (circularFifoBuffer.size() < pricesToCompare)
            {
                return false;
            }
            BigDecimal average =
                SimpleAverageCalculator.calculateAverage(newArrayList(circularFifoBuffer.iterator()), divisor);
            return isPriceFollowingUpwardTrend((Price) circularFifoBuffer.get(), average);
        }
        finally
        {
            priceNameLock.unlock();
        }
    }

    private Buffer getPriceBufferForProductName(Price price)
    {
        priceBuffer.putIfAbsent(price.getProductName(), new CircularFifoBuffer(pricesToCompare));
        return priceBuffer.get(price.getProductName());
    }

    private boolean isPriceFollowingUpwardTrend(Price price, BigDecimal average)
    {
        return average.compareTo(price.getPrice()) > 0;
    }


    @Override
    public Class<Price> getType()
    {
        return Price.class;
    }

    private static class SimpleAverageCalculator
    {

        public static BigDecimal calculateAverage(Collection<Price> numbers, BigDecimal divisor)
        {
            BigDecimal sum = new BigDecimal("0");

            for (Price price : numbers)
            {
                sum = sum.add(price.getPrice());
            }
            return sum.divide(divisor);
        }
    }
}
