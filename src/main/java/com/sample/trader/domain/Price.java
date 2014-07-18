package com.sample.trader.domain;

import java.math.BigDecimal;

import com.google.common.base.Objects;

public class Price
{
    private final String productName;
    private final BigDecimal price;


    public Price(String productName, String price)
    {
        this.productName = productName;
        this.price = new BigDecimal(price);
    }

    public String getProductName()
    {
        return productName;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(productName, price);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        final Price other = (Price) obj;
        return Objects.equal(this.productName, other.productName) && Objects.equal(this.price, other.price);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this).add("productName", productName).add("price", price).toString();
    }
}
