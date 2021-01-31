package net.runelite.client.plugins.mkhunter;

import java.util.concurrent.Callable;

public class TimeoutUntil extends ConditionTimeout
{

    public TimeoutUntil(Callable<Boolean> condition, int expirationTicks)
    {
        setCondition(condition);
        setExpirationTicks(expirationTicks);
        setFrequency(1);
    }

    public TimeoutUntil(Callable<Boolean> condition, int minExpiration, int maxExpiration, int targetExpiration, int expirationDeviation)
    {
        setCondition(condition);
        setExpirationTicks(minExpiration, maxExpiration, targetExpiration, expirationDeviation);
        setFrequency(1);
    }

    public TimeoutUntil(Callable<Boolean> condition, Callable<Boolean> resetCondition, int expirationTicks)
    {
        setCondition(condition);
        setResetCondition(resetCondition);
        setExpirationTicks(expirationTicks);
        setFrequency(1);
    }

    public TimeoutUntil(Callable<Boolean> condition, Callable<Boolean> resetCondition, int minExpiration, int maxExpiration, int targetExpiration, int expirationDeviation)
    {
        setCondition(condition);
        setResetCondition(resetCondition);
        setExpirationTicks(minExpiration, maxExpiration, targetExpiration, expirationDeviation);
        setFrequency(1);
    }

    public TimeoutUntil(Callable<Boolean> condition, Callable<Boolean> resetCondition, int minExpiration, int maxExpiration, int targetExpiration, int expirationDeviation, int frequency)
    {
        setCondition(condition);
        setResetCondition(resetCondition);
        setExpirationTicks(minExpiration, maxExpiration, targetExpiration, expirationDeviation);
        setFrequency(frequency);
    }


}
