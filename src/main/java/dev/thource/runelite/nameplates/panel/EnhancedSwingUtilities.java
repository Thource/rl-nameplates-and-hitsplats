package dev.thource.runelite.nameplates.panel;

import java.awt.Component;
import java.awt.Container;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.util.SwingUtil;

/** EnhancedSwingUtilities fixes some inefficiencies with SwingUtilities. */
@Slf4j
public class EnhancedSwingUtilities {

    private static boolean isInSecondaryLoop;

    private EnhancedSwingUtilities() {
    }

    public static void fastRemoveAll(Container c) {
        if (!SwingUtilities.isEventDispatchThread()) {
            log.error("fastRemoveAll called outside of the EDT?", new Throwable());

            return;
        }

        fastRemoveAll(c, true);
    }

    private static void fastRemoveAll(Container c, boolean isMainParent) {
        // If we are not on the EDT this will deadlock, in addition to being totally unsafe
        assert SwingUtilities.isEventDispatchThread();

        // when a component is removed it has to be resized for some reason, but only if it's valid,
        // so we make sure to invalidate everything before removing it
        c.invalidate();
        for (int i = 0; i < c.getComponentCount(); i++) {
            Component ic = c.getComponent(i);

            // removeAll and removeNotify are both recursive, so we have to recurse before them
            if (ic instanceof Container) {
                fastRemoveAll((Container) ic, false);
            }

            // pumpPendingEvents can cause this function to be ran again
            // this conditional stops stack overflows as a result of that
            if (!isInSecondaryLoop) {
                isInSecondaryLoop = true;

                // each removeNotify needs to remove anything from the event queue that is for that widget
                // this however requires taking a lock, and is moderately slow, so we just execute all of
                // those events with a secondary event loop
                SwingUtil.pumpPendingEvents();

                isInSecondaryLoop = false;
            }

            // call removeNotify early; this is most of the work in removeAll, and generates events that
            // the next secondaryLoop will pick up
            ic.removeNotify();
        }

        if (isMainParent) {
            // Actually remove anything
            c.removeAll();
        }
    }
}
