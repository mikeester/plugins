package net.runelite.client.plugins.hidetanner.tasks.npchandling;

import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.hidetanner.BranchTask;
import net.runelite.client.plugins.hidetanner.TreeTask;
import net.runelite.client.plugins.hidetanner.tasks.itemhandling.CanTanHide;

public class IsTanInterfaceOpen extends BranchTask {

    public IsTanInterfaceOpen() {

    }

    @Override
    public boolean validate() {

        Widget tanInterface = client.getWidget(324, 96);
        return tanInterface != null && !tanInterface.isHidden();
    }

    @Override
    public TreeTask successTask() {
        return injector.getInstance(CanTanHide.class);
    }

    @Override
    public TreeTask failureTask() {
        return injector.getInstance(HasHideToTan.class);
    }

}
