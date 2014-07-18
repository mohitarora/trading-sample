package com.sample.trader.domain;

import java.math.BigDecimal;

import com.google.common.base.Objects;

public class Trade
{
    private final String productName;
    private final Direction direction;
    private final BigDecimal price;
    private final int quantity;

    public Trade(String productName, Direction direction, BigDecimal price, int quantity)
    {
        this.productName = productName;
        this.direction = direction;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName()
    {
        return productName;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public int getQuantity()
    {
        return quantity;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(productName, direction, price, quantity);
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
        final Trade other = (Trade) obj;
        return Objects.equal(this.productName, other.productName) && Objects.equal(this.direction, other.direction) &&
            Objects.equal(this.price, other.price) && Objects.equal(this.quantity, other.quantity);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this).add("productName", productName).add("direction", direction)
            .add("price", price).add("quantity", quantity).toString();
    }
}
