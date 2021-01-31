package net.runelite.client.plugins.treetemplate.tasks;


import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.treetemplate.ConditionTimeout;
import net.runelite.client.plugins.treetemplate.LeafTask;
import net.runelite.client.plugins.treetemplate.TreeTemplatePlugin;
import net.runelite.client.plugins.treetemplate.TimeoutWhile;

import java.util.concurrent.Callable;

/**
 * NOTES:
 *
 */
@Slf4j
public class HandleTimeout extends LeafTask
{

    public HandleTimeout() { }

    @Override
    public String getTaskDescription()
    {
        return status;
    }

    public void finishTimeout()
    {
        TreeTemplatePlugin.conditionTimeout = null;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        if (TreeTemplatePlugin.timeout > 0)
        {
            TreeTemplatePlugin.timeout--;
            return;
        }

        ConditionTimeout conditionTimeout = TreeTemplatePlugin.conditionTimeout;

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
                    log.info("Condition met, timeout finished");
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
                        log.info("Timeout expired, timeout finished");
                        finishTimeout();
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

        }
    }
}