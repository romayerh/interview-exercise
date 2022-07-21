package com.acme.mytrader.strategy;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.acme.mytrader.execution.MyExecutionService;
import com.acme.mytrader.price.MyBuyPriceListener;
import com.acme.mytrader.price.MyPriceSource;

public class TradingStrategyTest {

    @Mock
    MyExecutionService executionService;

    @InjectMocks
    MyBuyPriceListener priceListener;

    @Mock
    MyPriceSource priceSource;

    TradingStrategy tradingStrategy;

    @Captor
    ArgumentCaptor<String> captorString;
    @Captor
    ArgumentCaptor<Double> captorDouble;
    @Captor
    ArgumentCaptor<Integer> captorInt;

    private static final String security = "abcd";

    private static final double price = Double.parseDouble("10");

    private static final double DELTA = 1e-15;

    @Before
    public void initMocks() {

	MockitoAnnotations.initMocks(this);

	priceListener = new MyBuyPriceListener();
	priceListener.setExecutionService(executionService);

	doAnswer(new Answer<Void>() {
	    public Void answer(InvocationOnMock invocation) {
		priceListener.priceUpdate(security, price);
		return null;
	    }
	}).when(priceSource).addPriceListener(any());
    }

    @Test
    public void test1_SuccessBuy() {
	double threshold = 50;
	int volume = 100;

	priceListener.setLowerThreshold(threshold);
	priceListener.setVolume(volume);

	tradingStrategy = new TradingStrategy(priceListener, priceSource);
	verify(executionService).buy(captorString.capture(), captorDouble.capture(), captorInt.capture());

	assertEquals(security, captorString.getValue());
	assertEquals(price, captorDouble.getValue().doubleValue(), DELTA);
	assertEquals(volume, captorInt.getValue().intValue());

    }

    @Test
    public void test2_SuccessNoBuy() {
	double threshold = 5;
	int volume = 100;

	priceListener.setLowerThreshold(threshold);
	priceListener.setVolume(volume);

	tradingStrategy = new TradingStrategy(priceListener, priceSource);
	verify(executionService, never()).buy(captorString.capture(), captorDouble.capture(), captorInt.capture());

    }

    @Test
    public void test3_SuccessBuyNoVolume() {
	double threshold = 50;

	priceListener.setLowerThreshold(threshold);

	tradingStrategy = new TradingStrategy(priceListener, priceSource);
	verify(executionService).buy(captorString.capture(), captorDouble.capture(), captorInt.capture());

	assertEquals(security, captorString.getValue());
	assertEquals(price, captorDouble.getValue().doubleValue(), DELTA);
	assertEquals(0, captorInt.getValue().intValue());

    }

    @Test(expected = RuntimeException.class)
    public void test4_ExceptionNoExecutionService() {
	double threshold = 50;
	int volume = 100;

	priceListener.setLowerThreshold(threshold);
	priceListener.setVolume(volume);
	priceListener.setExecutionService(null);

	tradingStrategy = new TradingStrategy(priceListener, priceSource);

    }
}
