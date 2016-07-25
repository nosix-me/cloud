package com.nosix.cloud.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	public static String outException(Exception e) {
		StringPrintWriter pw = new ExceptionUtil().new StringPrintWriter();
		e.printStackTrace(pw);
		return pw.out();
	}
	
	class StringPrintWriter extends PrintWriter {
		public StringPrintWriter() {
			super(new StringWriter());
		}
		
		public StringPrintWriter(int size) {
			super(new StringWriter(size));
		}
		
		public String out() {
			flush();
			return ((StringWriter)out).toString();
		}
		
		public String toString() {
			return out();
		}
	}
}