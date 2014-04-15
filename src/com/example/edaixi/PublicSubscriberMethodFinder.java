
package com.example.edaixi;

import de.greenrobot.event.EventBusException;
import de.greenrobot.event.SubscriberMethod;
import de.greenrobot.event.SubscriberMethodFinder;
import de.greenrobot.event.ThreadMode;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

class PublicSubscriberMethodFinder implements SubscriberMethodFinder {
 private final Map<String, List<SubscriberMethod>> methodCache = new HashMap<String, List<SubscriberMethod>>();

 @Override
 public List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass, String eventMethodName) {
     String key = subscriberClass.getName() + '.' + eventMethodName;
     List<SubscriberMethod> subscriberMethods;
     synchronized (methodCache) {
         subscriberMethods = methodCache.get(key);
     }
     if (subscriberMethods != null) {
         return subscriberMethods;
     }
     subscriberMethods = new ArrayList<SubscriberMethod>();
     Class<?> clazz = subscriberClass;
     HashSet<String> eventTypesFound = new HashSet<String>();
     StringBuilder methodKeyBuilder = new StringBuilder();
     while (clazz != null) {
         String name = clazz.getName();
         if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
             // Skip system classes, this just degrades performance
             break;
         }

         Method[] methods = clazz.getDeclaredMethods();
         for (Method method : methods) {
             if (Modifier.isPublic(method.getModifiers())) {
                 Class<?>[] parameterTypes = method.getParameterTypes();
                 assert method.getReturnType().equals(Void.TYPE);
                 assert parameterTypes.length == 1;
                 ThreadMode threadMode = ThreadMode.PostThread;
                 Class<?> eventType = parameterTypes[0];
                 methodKeyBuilder.setLength(0);
                 methodKeyBuilder.append(method.getName());
                 methodKeyBuilder.append('>').append(eventType.getName());
                 String methodKey = methodKeyBuilder.toString();
                 if (eventTypesFound.add(methodKey)) {
                     // Only add if not already found in a sub class
                     subscriberMethods.add(new SubscriberMethod(method, threadMode, eventType));
                 }
             }
         }
         clazz = clazz.getSuperclass();
     }
     if (subscriberMethods.isEmpty()) {
         throw new EventBusException("Subscriber " + subscriberClass + " has no methods called " + eventMethodName);
     } else {
         synchronized (methodCache) {
             methodCache.put(key, subscriberMethods);
         }
         return subscriberMethods;
     }
 }
}
