/**
 * 
 */
package com.pollit.util;

import java.util.function.Predicate;

/**
 * @author Dominique Mas
 *
 */
public class EmptyStringPredicate<T> implements Predicate<T>{

	public boolean test(T varc) {
		if ("".equals(varc)) return true;
		return false;
	}
}
