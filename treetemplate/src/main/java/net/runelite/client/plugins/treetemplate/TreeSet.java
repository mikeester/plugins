package net.runelite.client.plugins.treetemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TreeSet {

    private TreeTask root;

    public TreeSet() {}

    public TreeSet(TreeTask root) {
        this.root = root;
    }

    public void setRoot(TreeTask root) {
        this.root = root;
    }

    public LeafTask getLeafTask() {

        if (this.root == null) {
            return null;
        }

        TreeTask finalTask;
        TreeTask treeTask = finalTask = this.root;
        while (!treeTask.isLeaf())
        {

            if (finalTask.validate())
            {

                final TreeTask successTask;

                if ((successTask = finalTask.successTask()) == null)
                {
                    return null;
                }

                treeTask = (finalTask = successTask);

            }
            else
            {

                final TreeTask failureTask;

                if ((failureTask = finalTask.failureTask()) == null)
                {
                    return null;
                }

                treeTask = (finalTask = failureTask);

            }

        }

        return (LeafTask)finalTask;

    }

}
