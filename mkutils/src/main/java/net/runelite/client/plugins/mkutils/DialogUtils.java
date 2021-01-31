package net.runelite.client.plugins.mkutils;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class DialogUtils
{
    @Inject
    private MKUtils utils;

    @Inject
    private Client client;

    public void continuePlayerDialog(long delay)
    {
        MenuEntry entry = new MenuEntry("Continue", "", 0, MenuOpcode.WIDGET_TYPE_6.getId(), -1, 14221315, false);
        utils.doActionMsTime(entry, new Point(0, 0), delay);
    }

    public void continueNpcDialog(long delay)
    {
        MenuEntry entry = new MenuEntry("Continue", "", 0, MenuOpcode.WIDGET_TYPE_6.getId(), -1, 15138819, false);
        utils.doActionMsTime(entry, new Point(0, 0), delay);
    }

    public void selectDialogOption(int index, long delay)
    {
        if (getOptionText(index) != null)
        {
            MenuEntry entry = new MenuEntry("Continue", "", 0, MenuOpcode.WIDGET_TYPE_6.getId(), index, 14352385, false);
            utils.doActionMsTime(entry, new Point(0, 0), delay);
        }
    }

    public String getNpcText()
    {
        if (client == null)
        {
            return null;
        }

        Widget dialogText = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
        if (dialogText != null && !dialogText.isHidden())
        {
            return dialogText.getText();
        }

        return null;
    }

    public String getPlayerText()
    {
        if (client == null)
        {
            return null;
        }

        Widget playerDialogText = client.getWidget(WidgetInfo.DIALOG_PLAYER_TEXT);
        if (playerDialogText != null && !playerDialogText.isHidden())
        {
            return playerDialogText.getText();
        }

        return null;
    }

    public String getOptionText(int childIndex)
    {
        if (client == null)
        {
            return null;
        }

        Widget dialogOption = client.getWidget(WidgetInfo.DIALOG_OPTION_OPTION1);
        if (dialogOption != null && !dialogOption.isHidden())
        {
            if (dialogOption.getChildren() == null || dialogOption.getChildren().length == 0)
            {
                return null;
            }

            Widget optionWidget = dialogOption.getChild(childIndex);
            if (optionWidget != null && !optionWidget.isHidden())
            {
                return optionWidget.getText();
            }
        }

        return null;
    }

}
