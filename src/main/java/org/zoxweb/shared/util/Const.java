/*
 * Copyright 2012 ZoxWeb.com LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.zoxweb.shared.util;

import org.zoxweb.shared.db.QueryMarker;



/**
 * This class contains various lists of constants declared as enums or as arrays.
 * @author mzebib
 *
 */
public class Const
{
	/**
	 * Initializes LOGGER_NAME constant.
	 */
	public static final String LOGGER_NAME = "zoxweb-core";
	
	
	
	public enum Bool
		implements GetName, GetValue<Boolean>
	{
		
		TRUE("true", true),
		FALSE("false", false),
		ON("on", true),
		OFF("off", false),
		ENABLE("enable", true),
		DISABLE("disable", false),
		ACTIVE("active", true),
		INACTIVE("incative", false),
		
		;
		private final boolean value;
		private final String name;
		
		Bool(String name, boolean value)
		{
			this.name = name;
			this.value = value;
		}
		@Override
		public Boolean getValue() 
		{
			// TODO Auto-generated method stub
			return value;
		}

		@Override
		public String getName()
		{
			// TODO Auto-generated method stub
			return name;
		}
		
		public static Bool parse(String str)
		{
			return (Bool) SharedUtil.lookupEnum(Bool.values(), str);
		}
		
		
		public static boolean lookupValue(String str)
		{
			Bool ret = parse(str);
			if (ret == null)
			{
				throw new IllegalArgumentException("Invalid Bool token " + str);
			}
			
			return ret.value;
		}
		
		
	}
	
	
	
	
	
	public enum Status
	{
		ACTIVE,
		EXPIRED,
		INACTIVE,
		INVALID,
		PENDING,
		SUSPENDED,
		
	}
	
	
	public enum ResourceType
	{
		FILE,
		FOLDER,
		FORM,
		TEMP_FILE,
		NVENTITY
	}
	
	
	public enum Unit
		implements GetName
	{
		EM("em"),
		PIXEL("px"),
		PERCENT("%"),
		
		;

		private String name;
		
		Unit(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getName()
		{

			return name;
		}
	
		
		public static Unit parseUnit(String str)
		{
			if (!SharedStringUtil.isEmpty(str))
			{
				str = str.toLowerCase();
				
				for (Unit unit : Unit.values())
				{
					if (str.endsWith(unit.getName()))
					{
						return unit;
					}
				}
			}
			
			return null;
		}
	}
	
	
	
	public enum DeviceType
		implements GetName
	{
		ANDROID("Android"),
		IPAD("iPad"),
		IPHONE("iPhone"),
	
		;
		
		private final String name;
		
		DeviceType(String name)
		{
			this.name = name;
		}
	
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}
		
		
		public static DeviceType lookup(String toMatch)
		{
			for (DeviceType md : DeviceType.values())
			{
				if (SharedStringUtil.contains(toMatch, md.getName(), true))
					return md;
			}
			
			return null;
		}
		public static boolean isMobileDevice(String toMatch)
		{
			return (lookup(toMatch) != null);
		}
		
	}
	
	
	/**
	 * Number of bits inside a byte
	 */
	//public static final int BITS_IN_BYTE = 8;
	/**
	 * This enum represents size in bytes of default memory constants.
	 * @author mnael
	 *
	 */
	public enum SizeInBytes
		implements GetName
	{
		// Byte
		B("B", 1),
		
		// Kilo bytes
		K("KB", 1024),
		
		// Mega bytes
		M("MB", K.LENGTH*1024),
		
		// Giga bytes
		G("GB", M.LENGTH*1024),
		
		// Tera bytes
		T("TB", G.LENGTH*1024),
		
		// Peta bytes
		P("PB", T.LENGTH*1024),
		
		;
		
		/**
		 * This returns the size of bytes.
		 */
		public final long LENGTH;
		
		
		private String name;
		
		SizeInBytes(String name, long value)
		{
			this.name = name;
			LENGTH = value;
		}
		
		/**
		 * This method returns a long value of the parsed string.
		 * @param str
		 * @return
		 */
		public static long parse(String str)
		{
			str = str.toUpperCase();
			long multiplier = 1;
			SizeInBytes values[] = SizeInBytes.values();
			
			for(int i = values.length - 1 ; i >= 0; i--)
			{
				SizeInBytes bs = values[i];
				if (str.endsWith(bs.getName()))
				{
					multiplier = bs.LENGTH;
					str = str.substring(0, str.length() - bs.getName().length());
					break;
				}
				else if (str.endsWith(bs.name()))
				{
					multiplier = bs.LENGTH;
					str = str.substring(0, str.length() - bs.name().length());
					break;
				}
			}
			
			return Long.parseLong(str)* multiplier;
		}

		public long sizeInBits(int size)
		{
			return LENGTH*size*Byte.SIZE;
		}
		
		
		public long sizeInBytes(long size)
		{
			return LENGTH*size;
		}

		@Override
		public String getName() 
		{
			return name;
		}
	}
	
	/**
	 * This enum represents types of unit of time.
	 * @author mzebib
	 *
	 */
	public enum TimeUnitType
	{
		NANOS,
		MILLIS,
	}
	
	public enum FilenameSep
	{
		SLASH('/'),
		BACKSLASH('\\'),
		COLON(':'),
		SEMICOLON(';')
		
		;
		
		public final char sep;
		
		FilenameSep( char s)
		{
			this.sep=s;
		}
		
		public String toString()
		{
			return "" + sep;
		}
	}
	

	/**
	 * This enum represents units of time in milliseconds.
	 * @author mzebib
	 *
	 */
	public enum TimeInMillis
	{
		// One millisecond
		MILLI(1, "millis"),
		
		// One second in milliseconds
		SECOND(MILLI.MILLIS *1000, "seconds", "second", "secs","sec", "s"),
		
		// One minute in milliseconds
		MINUTE( SECOND.MILLIS * 60, "minutes", "minute", "mins", "min", "m"),

		// One hour in milliseconds
		HOUR( MINUTE.MILLIS * 60, "hours", "hour","h"),
		
		// One day in milliseconds
		DAY(HOUR.MILLIS * 24, "days", "day"),
		
		// One week in milliseconds
		WEEK(DAY.MILLIS* 7, "weeks", "week")
		
		;
		
		public final long MILLIS;
		private final String tokens[];
		
		TimeInMillis(long duration, String ...tokens)
		{
			this.MILLIS = duration;
			this.tokens = tokens;
		}
		
		/**
		 * This method converts string to time in milliseconds.
		 * @param time
		 * @return
		 */
		public static long toMillis(String time) 
			throws NullPointerException, IllegalArgumentException
		{
			time  = SharedStringUtil.toLowerCase(time).trim();
			TimeInMillis timeMatch = null;
			String tokenMatch = null;
			
			for (TimeInMillis tim : TimeInMillis.values())
			{
				for (String tok : tim.tokens)
				{
					if (time.endsWith(tok))
					{
						tokenMatch = tok;
						timeMatch = tim;
						break;
					}
					
					if (timeMatch != null)
					{
						break;
					}
				}
			}
			
			if (timeMatch == null)
			{
				throw new IllegalArgumentException("Invalid time token " + time);
			}
			
			String valueMatch[] = time.split(tokenMatch);
			
			if (valueMatch.length != 1)
			{
				throw new IllegalArgumentException("Invalid time token " + time);
			}
			
			long multiplier = Long.parseLong(valueMatch[0].trim());
			
			return timeMatch.MILLIS*multiplier;
		}
		
		/**
		 * 
		 * This method converts the time to nano second value (10 power -9 second)
		 * @param time
		 * @return
		 */
		public static long toNanos(String time)
				throws NullPointerException, IllegalArgumentException
		{
			return toMillis(time) * 1000000;
		}
		
		public static String nanosToString(long nanos)
		{
			long rest = nanos/1000000;
			if (rest > 0)
			{
				return rest + " millis";
			}
			return nanos + " nanos";
		}
		
		/**
		 * 
		 * This method converts the time to micro second value (10 power -6 second)
		 * @param time
		 * @return
		 */
		public static long toMicros(String time)
				throws NullPointerException, IllegalArgumentException
		{
			return toMillis(time) * 1000;
		}
		
		
	}
	
	
	public enum TimeZoneOffset
		implements GetName
	{
		UTC_LESS_1200("UTC-12:00", '-', 12, 0),
		UTC_LESS_1100("UTC-11:00", '-', 11, 0),
		UTC_LESS_1000("UTC-10:00", '-', 10, 0),
		UTC_LESS_0930("UTC-9:30", '-', 9, 30),
		UTC_LESS_0900("UTC-9:00", '-', 9, 0),
		UTC_LESS_0800("UTC-8:00", '-', 8, 0),
		UTC_LESS_0700("UTC-7:00", '-', 7, 0),
		UTC_LESS_0600("UTC-6:00", '-', 6, 0),
		UTC_LESS_0500("UTC-5:00", '-', 5, 0),
		UTC_LESS_0430("UTC-4:30", '-', 4, 30),
		UTC_LESS_0400("UTC-4:00", '-', 4, 0),
		UTC_LESS_0330("UTC-3:30", '-', 3, 30),
		UTC_LESS_0300("UTC-3:00", '-', 3, 0),
		UTC_LESS_0200("UTC-2:00", '-', 2, 0),
		UTC_LESS_0100("UTC-1:00", '-', 1, 0),
		UTC("UTC�00:00", '+', 0, 0),
		UTC_PLUS_0100("UTC+1:00", '+', 1, 0),
		UTC_PLUS_0200("UTC+2:00", '+', 2, 0),
		UTC_PLUS_0300("UTC+3:00", '+', 3, 0),
		UTC_PLUS_0330("UTC+3:30", '+', 3, 30),
		UTC_PLUS_0400("UTC+4:00", '+', 4, 0),
		UTC_PLUS_0430("UTC+4:30", '+', 4, 30),
		UTC_PLUS_0500("UTC+5:00", '+', 5, 0),
		UTC_PLUS_0530("UTC+5:30", '+', 5, 30),
		UTC_PLUS_0545("UTC+5:45", '+', 5, 45),
		UTC_PLUS_0600("UTC+6:00", '+', 6, 0),
		UTC_PLUS_0630("UTC+6:30", '+', 6, 30),
		UTC_PLUS_0700("UTC+7:00", '+', 7, 0),
		UTC_PLUS_0800("UTC+8:00", '+', 8, 0),
		UTC_PLUS_0845("UTC+8:45", '+', 8, 45),
		UTC_PLUS_0900("UTC+9:00", '+', 9, 0),
		UTC_PLUS_0930("UTC+9:30", '+', 9, 30),
		UTC_PLUS_1000("UTC+10:00", '+', 10, 0),
		UTC_PLUS_1030("UTC+10:30", '+', 10, 30),
		UTC_PLUS_1100("UTC+11:00", '+', 11, 0),
		UTC_PLUS_1130("UTC+11:30", '+', 11, 30),
		UTC_PLUS_1200("UTC+12:00", '+', 12, 0),
		UTC_PLUS_1245("UTC+12:45", '+', 12, 45),
		UTC_PLUS_1300("UTC+13:00", '+', 13, 0),
		UTC_PLUS_1400("UTC+14:00", '+', 14, 0)
		
		;
	
		private String name;
		private char sign;
		private int hours;
		private int minutes;
		
		TimeZoneOffset(String name, char sign, int hours, int minutes)
		{
			this.name = name;
			this.sign = sign;
			this.hours = hours;
			this.minutes = minutes;
		}
	
		@Override
		public String getName() 
		{
			return name;
		}
		
		public char getSign()
		{
			return sign;
		}
		
		public int getHours()
		{
			return hours;
		}
		
		public int getMinutes()
		{
			return minutes;
		}
		
		public int getOffsetInMinutes(TimeZoneOffset timeZone)
		{
			return timeZone.getHours()*60 + timeZone.getMinutes();
		}
		
		public int getOffsetInMillis(TimeZoneOffset timeZone)
		{
			return getOffsetInMinutes(timeZone)*1000;
		}
	
	}
	
	
	/**
	 * This enum represents data type in bytes.
	 * @author mzebib
	 *
	 */
	public enum TypeInBytes
	{
		BYTE(1),
		SHORT(2),
		INT(4),
		LONG(8)
		
		;
		
		/**
		 * This returns the byte size of the data type.
		 */
		public final int size;
		
		TypeInBytes(int s)
		{
			size = s;
		}
	}
	
	
	
	
	
	/**
	 * This enum declares the file type.
	 * @author mzebib
	 *
	 */
	public enum DocumentType
		implements GetName
	{
		FILE("File"),
		FOLDER("Folder"),
		FORM("Form")
		
		;
		
		
		private String name;
		
		DocumentType(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getName() 
		{
			return name;
		}
	}
	
	
	public enum DayOfWeek
		implements GetNameValue<Integer>
	{
		SUNDAY("Sunday", 0),
		MONDAY("Monday", 1),
		TUESDAY("Tuesday", 2),
		WEDNESDAY("Wednesday", 3),
		THURSDAY("Thursday", 4),
		FRIDAY("Friday", 5),
		SATURDAY("Saturday", 6),
		
		;
		
		private Integer value;
		private String name;
		
		DayOfWeek(String name, Integer value)
		{
			this.name = name;
			this.value = value;
		}
		
		@Override
		public String getName() 
		{
			return name;
		}		
		
		@Override
		public Integer getValue() 
		{
			return value;
		}
	}
	
	
	/**
	 * This enum represents months in Gregorian calendar year.
	 * @author mzebib
	 *
	 */
	public enum Month
		implements GetName
	{
		JANUARY("01-Jan"),
		FEBRUARY("02-Feb"),
		MARCH("03-Mar"),
		APRIL("04-Apr"),
		MAY("05-May"),
		JUNE("06-Jun"),
		JULY("07-Jul"),
		AUGUST("08-Aug"),
		SEPTEMBER("09-Sep"),
		OCTOBER("10-Oct"),
		NOVEMBER("11-Nov"),
		DECEMBER("12-Dec")
		
		;

		private String name;
		
		Month(String name)
		{
			this.name = name;
		}
		
		
		@Override
		public String getName()
		{
			return name;
		}
		
		
		
	}
	
	/**
	 * This enum represents days in a month and includes the maximum number 
	 * of possible days in a month.
	 * @author mzebib
	 *
	 */
	public enum DaysInMonth
		implements GetValue<Integer>
	{
		ONE(1),
		TWO(2),
		THREE(3),
		FOUR(4),
		FIVE(5),
		SIX(6),
		SEVEN(7),
		EIGHT(8),
		NINE(9),
		TEN(10),
		ELEVEN(11),
		TWELVE(12),
		THIRTEEN(13),
		FOURTEEN(14),
		FIFTEEN(15),
		SIXTEEN(16),
		SEVENTEEN(17),
		EIGHTEEN(18),
		NINETEEN(19),
		TWENTY(20),
		TWENTY_ONE(21),
		TWENTY_TWO(22),
		TWENTY_THREE(23),
		TWENTY_FOUR(24),
		TWENTY_FIVE(25),
		TWENTY_SIX(26),
		TWENTY_SEVEN(27),
		TWENTY_EIGHT(28),
		TWENTY_NINE(29),
		THIRTY(30),
		THIRTY_ONE(31),
		
		;
		
		private Integer value;
		
		DaysInMonth(Integer value)
		{
			this.value = value;
		}
		
		@Override
		public Integer getValue() 
		{

			return value;
		}
	}
	
	
	
	public enum DayPeriod
		implements GetName
	{
		AM("AM"),
		PM("PM")
		;


		private String name;
		
		DayPeriod(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getName() 
		{
			return name;
		}
	
	}
	

	public enum Bidi
	{
		LTR,
		RTL
		
		;
	}
		
	/**
	 * This enum contains logical operators.
	 * @author mzebib
	 *
	 */
	public enum LogicalOperator 
		implements GetValue<String>, QueryMarker
	{
		AND("AND"),
		OR("OR"),
		
		;

		private String value;
		
		LogicalOperator(String value)
		{
			this.value = value;
		}
		
		@Override
		public String getValue() 
		{
			return value;
		}
		
		public String toString()
		{
			return getValue();
		}
	}
	
	
	/**
	 * This enum contains relational operators.
	 * @author mzebib
	 *
	 */
	public enum RelationalOperator 
		implements GetValue<String>
	{
		EQUAL("="),
		NOT_EQUAL("!="),
		GT(">"),
		GTE(">="),
		LT("<"),
		LTE("<="),

		;

		String value;
		
		RelationalOperator(String value)
		{
			this.value = value;
		}
		
		@Override
		public String getValue() 
		{
			return value;
		}
	}
	
	
	
	/**
	 * Utility Date pattern that can be used on the client and server side.
	 */
	public enum DateTimePattern
		implements GetValue<String>
	{
		
		GMT_ZZZ("+00:00"),
		YEAR_MONTH_TZ("yyyy-MM ZZZ"),
		YEAR_MONTH_DAY_TZ("yyyy-MM-dd ZZZ"),
		YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_TZ("yyyy-MM-dd hh:mm:ss ZZZ"),
		HOURS_MINUTES_SECONDS_TZ("hh:mm:ss ZZZ"),
		
		;

		private String value;
		
		DateTimePattern(String value)
		{
			this.value = value;
		}
		
		@Override
		public String getValue() 
		{
			return value;
		}
		
	}

	/**
	 * This enum contains NVPair display property
	 * as either default (depends on context), name, or value.
	 * @author mzebib
	 *
	 */
	public enum NVDisplayProp
	{
		DEFAULT,
		NAME,
		NAME_VALUE,
		VALUE
	}
	
	
	/**
	 * 
	 * @author mnael
	 *
	 */
	public enum ReturnType
	{
		NVENTITY,
		NVENTITY_LIST,
		VOID,
		BOOLEAN,
		STRING,
		INTEGER,
		LONG,
		FLOAT,
		DOUBLE,
		MAP,
		DYNAMIC_ENUM_MAP,
		DYNAMIC_ENUM_MAP_LIST
		
	}
	
	
	/**
	 * Mapping types
	 */
	public final static  DynamicEnumMap ASSOCIATION_TYPE = 
			new DynamicEnumMap("AssociationType",
					new NVPair("ACCESS_CODE_TO_SYSTEM", "Access code of a remote system"),
					new NVPair("API_KEY_TO_CONFIG", "APIKey to APIConfig")
			);
	
	/**
	 * An Object that can be used for locking mechanism.
	 */
	public static final Object LOCK = new Object();
	
	/**
	 * This character array contains the numbers and letter representations 
	 * for hexadecimal values. This array is used to convert between string 
	 * characters and hexadecimal byte and vice versa. 
	 */
	public final static char HEX_TOKENS[] = 
		{
			'0',
			'1',
			'2',
			'3',
			'4',
			'5',
			'6',
			'7',
			'8',
			'9',
			'A',
			'B',
			'C',
			'D',
			'E',
			'F'
		};
	
	/**
	 * This array contains a list of char values. 
	 */
	public static final CharSequence HTML_FILTERS[] =
		{
			"<pre>",
			"<PRE>",
			"</pre>",
			"</PRE>",
			"amp;",
			"AMP;"
		};
	
	/**
	 * This method is used to wrap primitive types declared
	 * as the data type itself or as a class of the data type.
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> wrap(Class<T> c) 
	{
		if (c.isPrimitive())
		{
			for (Class<?>[] temp : PRIMITIVES_TO_WRAPPERS)
			{
				if (temp[0] == c)
				{
					return (Class<T>) temp[1];
				}
			}
		}
		else if (c.isArray())
		{
			for (Class<?>[] temp : PRIMITIVES_ARRAY_TO_WRAPPERS)
			{
				if (temp[0] == c)
				{
					return (Class<T>) temp[1];
				}
			}
		}
		
		return c;
	}
	
	/**
	 * This array contains the type and the class of the primitive type.
	 */
	private final static Class<?>[][] PRIMITIVES_TO_WRAPPERS = 
		{
			{boolean.class, Boolean.class},
			{byte.class, Byte.class},
			{char.class, Character.class},
			{double.class, Double.class},
			{float.class, Float.class},
			{int.class, Integer.class},
			{long.class, Long.class},
			{short.class, Short.class},
			{void.class, Void.class}
		};

	/**
	 * This array contains the type and the class of the primitive array type.
	 */
	private final static Class<?>[][] PRIMITIVES_ARRAY_TO_WRAPPERS = 
		{
			{double[].class, Double[].class},
			{float[].class, Float[].class},
			{int[].class, Integer[].class},
			{long[].class, Long[].class},
		};

	
}
