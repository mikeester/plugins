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

package net.runelite.client.plugins.hidetanner;

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
