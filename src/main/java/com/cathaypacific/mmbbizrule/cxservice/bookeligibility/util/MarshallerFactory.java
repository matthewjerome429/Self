package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class MarshallerFactory {

    private MarshallerFactory() {

    }

    private static Map<String, Jaxb2Marshaller> jaxb2MarshallerMap = new HashMap<>();

    /**
     * get the Marshaller from cached map, create new one and put to the map if
     * cannot find the Marshaller in map.
     * 
     * @param classType
     * @return
     */
    public static <T> Jaxb2Marshaller getMarshaller(Class<T> classType) {
        Jaxb2Marshaller marshaller = jaxb2MarshallerMap.get(classType.getPackage().getName());
        if (marshaller == null) {
            marshaller = createMarshaller(classType);
            jaxb2MarshallerMap.put(classType.getPackage().getName(), marshaller);
        }
        return marshaller;

    }

    /**
     * 
     * @param classType
     * @return
     */
    private static <T> Jaxb2Marshaller createMarshaller(Class<T> classType) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(classType.getPackage().getName());
        return marshaller;
    }
}
