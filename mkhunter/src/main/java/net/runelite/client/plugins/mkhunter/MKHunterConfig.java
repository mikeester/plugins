/*
 * Copyright (c) 2018, mikeester <https://github.com/mikeester>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.mkhunter;

import net.runelite.client.config.Button;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.ConfigTitleSection;
import net.runelite.client.config.Range;
import net.runelite.client.config.Title;
import net.runelite.client.plugins.mkhunter.tasks.falconry.Kebbit;

@ConfigGroup("mkhunter")
public interface MKHunterConfig extends Config
{

	@ConfigSection(
			keyName = "delayConfig",
			name = "Delay configuration",
			description = "Sleep is reaction time (1000ms = 1 second). Tick delays are timeouts after certain actions. Set tick delays to 0 for tick perfect performance (1 tick = ~600ms)",
			position = 0
	)
	default boolean delayConfig() { return false; }

	@Range(
			min = 60,
			max = 550
	)
	@ConfigItem(
			keyName = "sleepMin",
			name = "Sleep Min",
			description = "Minimum sleep time (ms)",
			position = 0,
			section = "delayConfig"
	)
	default int sleepMin()
	{
		return 60;
	}

	@Range(
			min = 0,
			max = 550
	)
	@ConfigItem(
			keyName = "sleepMax",
			name = "Sleep Max",
			description = "Maximum sleep time (ms)",
			position = 1,
			section = "delayConfig"
	)
	default int sleepMax()
	{
		return 350;
	}

	@Range(
			min = 0,
			max = 550
	)
	@ConfigItem(
			keyName = "sleepTarget",
			name = "Sleep Target",
			description = "Target or most common sleep time (ms)",
			position = 2,
			section = "delayConfig"
	)
	default int sleepTarget()
	{
		return 100;
	}

	@Range(
			min = 0,
			max = 550
	)
	@ConfigItem(
			keyName = "sleepDeviation",
			name = "Sleep Deviation",
			description = "How far delay can deviate from target but remain common (ms)",
			position = 3,
			section = "delayConfig"
	)
	default int sleepDeviation()
	{
		return 10;
	}

	@ConfigItem(
			keyName = "sleepWeightedDistribution",
			name = "Weighted Distribution",
			description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
			position = 4,
			section = "delayConfig"
	)
	default boolean sleepWeightedDistribution()
	{
		return false;
	}

	@Range(
			min = 0,
			max = 10
	)
	@ConfigItem(
			keyName = "tickDelayMin",
			name = "Game Tick Min",
			description = "Minimum tick delay",
			position = 5,
			section = "delayConfig"
	)
	default int tickDelayMin()
	{
		return 0;
	}

	@Range(
			min = 0,
			max = 10
	)
	@ConfigItem(
			keyName = "tickDelayMax",
			name = "Game Tick Max",
			description = "Maximum tick delay",
			position = 6,
			section = "delayConfig"
	)
	default int tickDelayMax()
	{
		return 2;
	}

	@Range(
			min = 0,
			max = 10
	)
	@ConfigItem(
			keyName = "tickDelayTarget",
			name = "Game Tick Target",
			description = "Target or most common tick delay",
			position = 7,
			section = "delayConfig"
	)
	default int tickDelayTarget()
	{
		return 1;
	}

	@Range(
			min = 0,
			max = 10
	)
	@ConfigItem(
			keyName = "tickDelayDeviation",
			name = "Game Tick Deviation",
			description = "How far delay can deviate from target but remain common",
			position = 8,
			section = "delayConfig"
	)
	default int tickDelayDeviation()
	{
		return 1;
	}

	@ConfigItem(
			keyName = "tickDelayWeightedDistribution",
			name = "Weighted Distribution",
			description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
			position = 9,
			section = "delayConfig"
	)
	default boolean tickDelayWeightedDistribution()
	{
		return false;
	}

	@ConfigTitleSection(
			keyName = "instructionsTitle",
			name = "Instructions",
			description = "",
			position = 1
	)
	default Title instructionsTitle()
	{
		return new Title();
	}

	@ConfigItem(
			keyName = "instructions",
			name = "",
			description = "Instructions. Don't enter anything into this field",
			position = 0,
			titleSection = "instructionsTitle"
	)
	default String instructions()
	{
		return "Select a method and configure it. For help with setups join the discord.";
	}

	@ConfigTitleSection(
			keyName = "settingsTitle",
			name = "Settings",
			description = "",
			position =  2
	)
	default Title settingsTitle() { return new Title(); }

	enum MethodChoice
	{
		MONKEYS("Maniacal Monkey"),
		FALCONRY("Falconry");

		public String name;

		MethodChoice(String s) { name = s; }

		@Override
		public String toString()
		{
			return name;
		}
	}

	@ConfigItem(
			keyName = "methodChoice",
			name = "Method",
			description = "Select the hunter method you would like to perform.",
			titleSection = "settingsTitle",
			position = 0
	)
	default MethodChoice methodChoice() { return MethodChoice.FALCONRY; }

	@ConfigItem(
			keyName = "kebbitChoice",
			name = "Kebbit",
			description = "Select the kebbit you want to catch",
			titleSection = "settingsTitle",
			hidden = true,
			unhide = "methodChoice",
			unhideValue = "Falconry",
			position = 1
	)
	default Kebbit kebbitChoice() { return Kebbit.DASHING; }

	@ConfigItem(
		keyName = "enableUI",
		name = "Enable UI",
		description = "Enable to turn on in game UI",
		position = 95
	)
	default boolean enableUI()
	{
		return true;
	}

	@ConfigItem(
		keyName = "startButton",
		name = "Start/Stop",
		description = "Starts or stops the plugin from running",
		position = 100
	)
	default Button startButton()
	{
		return new Button();
	}
}
