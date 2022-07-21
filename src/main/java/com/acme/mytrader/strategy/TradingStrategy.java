package com.acme.mytrader.strategy;

import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceSource;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
public class TradingStrategy {

    PriceListener priceListener;
    PriceSource priceSource;

    public TradingStrategy(PriceListener pListener, PriceSource pSource) {
	priceListener = pListener;
	priceSource = pSource;
	activateListener();
    }

    public void activateListener() {
	priceSource.addPriceListener(priceListener);
    }

    public void deactivateListener() {
	priceSource.removePriceListener(priceListener);
    }

}
