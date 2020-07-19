package com.oop.intelimenus.interfaces.actionable;

import com.google.common.base.Preconditions;
import lombok.NonNull;

import static com.oop.intelimenus.InteliMenus.getInteliMenus;

public interface Actionable<T extends Actionable> extends Openable<T>, Moveable<T>, Viewable<T>, Parentable<T>, Refreshable<T> {
    /*
    Get current action of the object
    If action is not set it will return NONE
    */
    MenuAction getCurrentAction();

    /*
    Set current action
    */
    void setCurrentAction(MenuAction action);

    default void refreshAction(Runnable callback) {
        executeAction(MenuAction.REFRESH, callback);
    }

    default void closeAction(Runnable callback) {
        executeAction(MenuAction.CLOSE, callback);
    }

    default void returnAction(Runnable callback) {
        executeAction(MenuAction.RETURN, callback);
    }

    default void moveAction(@NonNull T where, Runnable callback) {
        setMoving(where);
        executeAction(MenuAction.MOVE, callback);
    }

    default void executeAction(@NonNull MenuAction action, Runnable callback) {
        setCurrentAction(action);
        if (action == MenuAction.REFRESH) {
            open((T) this, () -> {
                setCurrentAction(MenuAction.NONE);
                if (callback != null)
                    callback.run();
            });

        } else if (action == MenuAction.MOVE) {
            Preconditions.checkArgument(getCurrentMoving().isPresent(), "Moving object is not set!");
            open(getCurrentMoving().get(), () -> {
                setCurrentAction(MenuAction.NONE);
                setMoving(null);
                if (callback != null)
                    callback.run();
            });

        } else if (action == MenuAction.CLOSE) {
            getInteliMenus().getUtil().ensureSync(() -> {
                getViewer().closeInventory();
                setCurrentAction(MenuAction.NONE);
                if (callback != null)
                    callback.run();
            });

        } else if (action == MenuAction.RETURN) {
            if (!getParent().isPresent()) {
                setCurrentAction(MenuAction.NONE);
                return;
            }

            open(getParent().get(), () -> {
                setCurrentAction(MenuAction.NONE);
                if (callback != null)
                    callback.run();
            });
        }
    }

    default void refresh() {
        refreshAction(null);
    }
}
