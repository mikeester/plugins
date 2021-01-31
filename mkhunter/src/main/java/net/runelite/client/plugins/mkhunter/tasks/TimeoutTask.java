package net.runelite.client.plugins.mkhunter.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.mkhunter.ConditionTimeout;
import net.runelite.client.plugins.mkhunter.MKHunterPlugin;
import net.runelite.client.plugins.mkhunter.Task;
import net.runelite.client.plugins.mkhunter.TimeoutWhile;

import java.util.concurrent.Callable;

@Slf4j
public class TimeoutTask extends Task
{
	@Override
	public boolean validate()
	{
		ConditionTimeout conditionTimeout = MKHunterPlugin.conditionTimeout;
		if (conditionTimeout != null)
		{
			try
			{
				// Do not handle condition timeout if an exception condition passes
				Callable<Boolean> exception = conditionTimeout.getException();
				return exception == null || !exception.call();
			}
			catch (Exception e)
			{
				log.info("Exception handling timeout validation: " + e.getMessage());
			}
		}

		return MKHunterPlugin.timeout > 0;
	}

	@Override
	public String getTaskDescription()
	{
		return status;
	}

	public void finishTimeout()
	{
		MKHunterPlugin.conditionTimeout = null;
		MKHunterPlugin.timeoutFinished = true;
	}

	@Override
	public void onGameTick(GameTick event)
	{
		ConditionTimeout conditionTimeout = MKHunterPlugin.conditionTimeout;

		boolean timeoutWhile = conditionTimeout instanceof TimeoutWhile;

		if (conditionTimeout != null)
		{
			try {

				Callable<Boolean> condition = conditionTimeout.getCondition();

				// If no condition is set
				// OR if it is a timeoutUntil and the condition passes
				// OR if it is a timeoutWhile and the condition fails
				if (condition == null || (timeoutWhile ? !condition.call() : condition.call()))
				{
					// Condition met, finish
					finishTimeout();
					return;
				}

				Callable<Boolean> resetCondition = conditionTimeout.getResetCondition();

				if (resetCondition == null || !resetCondition.call())
				{

					// Increment ticks elapsed before expiration
					conditionTimeout.incrementTicksElapsed();

					if (conditionTimeout.isExpired())
					{
						// If ticks elapsed meets expiration ticks, finish
						finishTimeout();
						return;
					}

				}
				else
				{
					// Reset condition met, reset expiration ticks
					conditionTimeout.setTicksElapsed(0);
				}

			}
			catch (Exception ex)
			{
				log.info("Exception during handle timeout: " + ex);
			}
			return;
		}

		// Regular tick timeouts happen AFTER conditionals
		// This means you can have a basic timed delay after your condition is met/expires (for example if you know you are stalled for 2 ticks after an action)
		if (MKHunterPlugin.timeout > 0)
		{
			MKHunterPlugin.timeout -= 1;
			if (MKHunterPlugin.timeout == 0)
			{
				MKHunterPlugin.timeoutFinished = true;
			}
		}
	}
}