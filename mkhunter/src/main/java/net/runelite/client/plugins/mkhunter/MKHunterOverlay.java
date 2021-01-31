package net.runelite.client.plugins.mkhunter;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.game.ItemManager;
import net.runelite.client.graphics.ModelOutlineRenderer;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

import static net.runelite.api.MenuOpcode.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

@Slf4j
@Singleton
class MKHunterOverlay extends OverlayPanel
{
	@Inject
	private ItemManager itemManager;
	private final ModelOutlineRenderer modelOutlineRenderer;
	private final Client client;
	private final MKHunterPlugin plugin;
	private final MKHunterConfig config;

	String timeFormat;
	private String infoStatus = "Starting...";

	@Inject
	private MKHunterOverlay(final Client client, final MKHunterPlugin plugin, final MKHunterConfig config, final ModelOutlineRenderer modelOutlineRenderer)
	{
		super(plugin);
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		this.modelOutlineRenderer = modelOutlineRenderer;
		setPosition(OverlayPosition.BOTTOM_LEFT);
		setPriority(OverlayPriority.HIGHEST);
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "MK Hunter overlay"));
	}

	public String getMethodString()
	{
		if (config.methodChoice() == MKHunterConfig.MethodChoice.MONKEYS)
		{
			return config.methodChoice().name;
		}
		else if (config.methodChoice() == MKHunterConfig.MethodChoice.FALCONRY)
		{
			return config.methodChoice().name + " (" + config.kebbitChoice().name().toLowerCase() + ")";
		}
		return "";
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (plugin.botTimer == null || !config.enableUI())
		{
			return null;
		}

		String titleString = "MK Hunter";

		panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(titleString) + 115, 0));
		panelComponent.setBorder(new Rectangle(5, 5, 5, 5));
		panelComponent.setBackgroundColor(new Color(0, 0, 0, 55));


		// Title
		TitleComponent title = TitleComponent.builder()
				.text(titleString)
				.color(ColorUtil.fromHex("#00c3ff"))
				.build();
		panelComponent.getChildren().add(title);

		// Duration
		Duration duration = Duration.between(plugin.botTimer, Instant.now());
		timeFormat = (duration.toHours() < 1) ? "mm:ss" : "HH:mm:ss";
		LineComponent time = LineComponent.builder()
				.left("Time running:")
				.right(formatDuration(duration.toMillis(), timeFormat))
				.build();
		panelComponent.getChildren().add(time);

		// Status
		LineComponent status = LineComponent.builder()
				.left("Status:")
				.right(MKHunterPlugin.status)
				.build();
		panelComponent.getChildren().add(status);

		LineComponent method = LineComponent.builder()
				.left("Method:")
				.right(getMethodString())
				.build();
		panelComponent.getChildren().add(method);

		// Stats
		LineComponent caught = LineComponent.builder()
				.left("Caught:")
				.right(plugin.caught + " (" + plugin.caughtPerHour + "/h)")
				.build();
		panelComponent.getChildren().add(caught);

		return super.render(graphics);
	}
}
