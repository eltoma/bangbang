/**
 * 
 */
package com.BangBang.Util;
import java.io.IOException;

import org.apache.log4j.Logger;


/**
 * @author:bush2582
 * @date:2015å¹?0æœ?5æ—?
 * @fileName:System_Log.java
 */
public class System_Log
{
	private static Logger logger = Logger.getLogger("reachTopLog");
	
	public void Log(String InLogInfo) throws Exception
	{
		logger.fatal(InLogInfo);
	}
}
