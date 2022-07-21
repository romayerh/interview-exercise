package com.acme.mytrader.price;

import com.acme.mytrader.execution.ExecutionService;

public class MyBuyPriceListener implements PriceListener {

    private ExecutionService myExecutionService;
    private double lowerThreshold = Double.parseDouble("0");
    private int volume = 0;

    // assuming this gets called by the PriceSource if the price changes after
    // adding this listener to the source
    @Override
    public void priceUpdate(String security, double price) {

	if (myExecutionService == null) {
	    throw new RuntimeException("MyBuyPriceListener: ExecutionService not set");
	}

	if (price < lowerThreshold) {
	    myExecutionService.buy(security, price, volume);
	}

    }

    public void setExecutionService(ExecutionService pExecutionService) {
	this.myExecutionService = pExecutionService;
    }

    public void setLowerThreshold(double pLowerThreshold) {
	this.lowerThreshold = pLowerThreshold;
    }

    public void setVolume(int pVolume) {
	this.volume = pVolume;
    }

}
