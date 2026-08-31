package com.campusroute.strategy;

import com.campusroute.domain.TravelPreference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class RouteSelectionStrategyFactoryTest {

    private final RouteSelectionStrategyFactory factory = new RouteSelectionStrategyFactory();

    @Test
    void returnsFastestStrategyForFastestPreference() {
        assertInstanceOf(FastestRouteStrategy.class, factory.strategyFor(TravelPreference.FASTEST));
    }

    @Test
    void returnsAccessibleStrategyForAccessiblePreference() {
        assertInstanceOf(AccessibleRouteStrategy.class, factory.strategyFor(TravelPreference.ACCESSIBLE));
    }

    @Test
    void returnsSafeStrategyForSafePreference() {
        assertInstanceOf(SafeRouteStrategy.class, factory.strategyFor(TravelPreference.SAFE));
    }
}
