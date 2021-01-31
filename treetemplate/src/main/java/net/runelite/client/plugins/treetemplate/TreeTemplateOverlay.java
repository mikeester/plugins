package net.runelite.client.plugins.treeTemplate;

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
class TreeTemplateOverlay extends OverlayPanel
{
	@Inject
	private ItemManager itemManager;
	private final ModelOutlineRenderer modelOutlineRenderer;
	private final Client client;
	private final TreeTemplatePlugin plugin;
	private final TreeTemplateConfig config;

	String timeFormat;
	private String infoStatus = "Starting...";

	@Inject
	private TreeTemplateOverlay(final Client client, final TreeTemplatePlugin plugin, final TreeTemplateConfig config, final ModelOutlineRenderer modelOutlineRenderer)
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
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "MK Tree Template overlay"));
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (plugin.botTimer == null || !config.enableUI())
		{
			return null;
		}

		String titleString = "MK Tree Template";

		// panelComponent.setBackgroundColor(ColorUtil.fromHex("#121212")); //Material Dark default
		panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(titleString) + 105, 0));
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
				.right(TreeTemplatePlugin.status)
				.build();
		panelComponent.getChildren().add(status);

		// Stats

		return super.render(graphics);
	}
}
