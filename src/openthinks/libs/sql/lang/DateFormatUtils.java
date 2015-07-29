package openthinks.libs.sql.lang;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Date formatter helper.
 * @author dailey
 *
 */
public class DateFormatUtils {

	static final Map<String, DateFormat> formatMap = new HashMap<String, DateFormat>();

	public static String format(Date date, String formatPattern) {
		DateFormat formater = formatMap.get(formatPattern);
		if (formater == null) {
			formater = new SimpleDateFormat(formatPattern);
			formatMap.put(formatPattern, formater);
		}
		return formater.format(date);
	}
}
