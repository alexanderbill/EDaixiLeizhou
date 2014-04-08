// -*- Mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
//
// Copyright (C) 2012 Opera Software ASA.  All rights reserved.
//
// This file is an original work developed by Opera Software ASA

package com.example.edaixi;

import de.greenrobot.event.EventBus;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

/**
 * Main event dispatcher for distributing/subscribing to events
 * sent and received on the main thread. It will only allow
 * posting events from the main thread and this is the expected
 * behavior and will be enough for almost all cases. In the odd
 * situation where events need to be posted from a background
 * thread the ThreadEventDispatcher can be used however all UI
 * updating must be done on the main thread so all such subscribers
 * MUST be registered to this event dispatcher.
 */
public class EventDispatcher {

    public enum Group {
        Main
    }

    private static final String TAG = "EventDispatcher";

    private final EventBus mEventBus;
    private final EnumMap<Group, List<Object>> mGroups = new EnumMap<Group, List<Object>>(
            Group.class);
    private static final EventDispatcher instance = new EventDispatcher();

    private EventDispatcher() {
        mEventBus = new EventBus(new PublicSubscriberMethodFinder());
        mEventBus.configureThrowSubscriberException(true);
    }

    /**
     * Posts an event to all registered handlers. This method will return
     * successfully after the event has been posted to all handlers, and
     * regardless of any exceptions thrown by handlers.
     *
     * <p>
     * If no handlers have been subscribed for {@code event}'s class, and
     * {@code event} is not already a {@link DeadEvent}, it will be wrapped in a
     * DeadEvent and reposted.
     *
     * @param event event to post.
     */
    static public void post(Object event) {
        SystemUtils.mainThreadEnforce("EventDispatcher.post");
        instance.mEventBus.post(event);
    }

    /**
     * Registers all handler methods on {@code object} to receive events.
     * Handler methods are selected and classified using this EventBus's
     * {@link HandlerFindingStrategy}; the default strategy is the
     * {@link AnnotatedHandlerFinder}.
     *
     * @param object object whose handler methods should be registered.
     */
    static public void register(Object object) {
        SystemUtils.mainThreadEnforce("EventDispatcher.register");
        instance.mEventBus.register(object);
    }

    /**
     * Unregisters all handler methods on a registered {@code object}.
     *
     * @param object object whose handler methods should be unregistered.
     */
    static public void unregister(Object object) {
        SystemUtils.mainThreadEnforce("EventDispatcher.unregister");
        try {
            instance.mEventBus.unregister(object);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error when unregistering event handler", e);
        }
    }

    /**
     * Registers all handler methods on {@code object} to receive events.
     * Handler methods are selected and classified using this EventBus's
     * {@link HandlerFindingStrategy}; the default strategy is the
     * {@link AnnotatedHandlerFinder}. The {@code object} will be associated
     * with the {@code group} and will be unregistered when the Group
     * is unregistered.
     *
     * @param object object whose handler methods should be registered.
     * @param group Group that the object belongs to.
     */
    static public void register(Object object, Group group) {
        List<Object> eventHandlers;

        if (instance.mGroups.containsKey(group)) {
            eventHandlers = instance.mGroups.get(group);
        } else {
            eventHandlers = new LinkedList<Object>();
            instance.mGroups.put(group, eventHandlers);
        }

        // Register to event bus first since it may throw exception and we
        // don't want to keep a reference in that case
        register(object);

        eventHandlers.add(object);
    }

    /**
     * Unregisters all event handlers from the given {@code group}.
     *
     * @param group Group whose event handlers should be unregistered.
     */
    static public void unregister(Group group) {
        List<Object> eventHandlers = instance.mGroups.get(group);
        if (eventHandlers != null) {
            for (Object eventHandler : eventHandlers) {
                unregister(eventHandler);
            }
            instance.mGroups.remove(group);
        }
    }
}
