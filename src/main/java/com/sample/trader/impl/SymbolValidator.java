package com.sample.trader.impl;

import java.util.Set;

public class SymbolValidator
{
    private final Set<String> validSymbols;

    public SymbolValidator(Set<String> validSymbols)
    {

        this.validSymbols = validSymbols;
    }

    /**
     * Validates if the symbol is in the list of allowed trade symbols.
     *
     * @param symbol
     *
     * @return
     */
    public boolean isSymbolValid(String symbol)
    {
        return validSymbols.contains(symbol);
    }
}
