package com.cathaypacific.mmbbizrule.util;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CollectionUtil {

	private CollectionUtil() {

	}

	public static <T> boolean isEqualList(List<T> listA, List<T> listB) {

		if (Objects.equals(listA, listB)) {
			return true;
		} else if (listA == null || listB == null) {
			return false;
		}

		if (listA.size() != listB.size()) {
			return false;
		}

		for (int i = 0; i < listA.size(); i++) {
			T a = listA.get(i);
			T b = listB.get(i);
			if (!Objects.equals(a, b)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Get not null object of <code>List</code>.
	 * 
	 * @param list
	 * @return If passed object is not null, return the passed one. Otherwise an
	 *         empty list.
	 */
	public static <T> List<T> getNotNullList(List<T> list) {
		if (list != null) {
			return list;
		} else {
			return Collections.emptyList();
		}
	}

}
