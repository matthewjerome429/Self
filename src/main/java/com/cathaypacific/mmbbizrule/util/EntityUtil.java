package com.cathaypacific.mmbbizrule.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.cathaypacific.mbcommon.loging.LogAgent;

public class EntityUtil {
	
	private static LogAgent logger = LogAgent.getLogAgent(EntityUtil.class);
	
	private EntityUtil() {
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return results;
        }
        Class<T>[] c = null;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] tClass = constructor.getParameterTypes();
            if (list.get(0).length == tClass.length) {
                c = tClass;
                break;
            }
        }

        for (Object[] o : list) {
            try {
            	Constructor<T> constructor = clazz.getConstructor(c);
				results.add(constructor.newInstance(o));
			} catch (Exception e) {
				logger.error(String.format("EntityUtils.castEntity -> error casting to class[%s]", clazz.getName()), e);
			}
        }
 
        return results;
    }
	
}
