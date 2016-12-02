package com.BangBang.Util;


import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Map;  

import com.opensymphony.xwork2.ActionInvocation;  
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;  
import java.util.Map.Entry;
/**
 * @author:bush2582 
 * @date:2015年10月23日
 * @fileName:Interceptot_Log.java
 */
public class Interceptot_Log extends AbstractInterceptor 
{
	private static final long serialVersionUID = -1615930166094288049L;  
	  
    public void destroy() 
    {  
    }  
  
    public void init() 
    {  
    }  
	
    public String GetActionPara(Map ValueTreeMap)
    {
        Iterator ValueIterator = ValueTreeMap.entrySet().iterator();
        Map ValuesHashMap = new HashMap();
        while (ValueIterator.hasNext()) 
        {
           Entry entry = (Entry) ValueIterator.next();
           String Key = (String) entry.getKey();
           String[] Values = (String[]) entry.getValue();
           String ValueStr = "";
            if (Values.length > 1)
            {
            	ValueStr = "{";
                for (int i = 0; i < Values.length; i++)
                {
                	ValueStr += Values[i];
                    if (i != Values.length - 1)
                    {
                    	ValueStr += "\n";
                    }
                }
                ValueStr += "}";
            } 
            else if (Values.length == 1) 
            {
            	ValueStr = Values[0];
            } 
            else 
            {
            	ValueStr = "null";
            }
            ValuesHashMap.put(Key, ValueStr);
        }
        return ValuesHashMap.toString();
    }
    
    public String intercept(ActionInvocation invocation) throws Exception 
    {  
    	
    	long UseTime = 1;
    	String result= null;
    	String username =null;
    	String exception_InfoString = null;
    	
	    try
		{
	    	long startTime = System.currentTimeMillis();
			result = invocation.invoke();
	    	UseTime = System.currentTimeMillis() - startTime;
	    	
		} 
	    catch (Exception e)
		{
	    	
	    	String Message = e.toString();
	    	
	    	String CauseString= "";
	    	String LocalizedMessage="";
	    	if (e.getCause()==null) 
	    	{	CauseString ="无";}
			
	    	else 
	    	{	CauseString = e.getCause().toString();}
			
	    	if (e.getLocalizedMessage()==null) 
	    	{	LocalizedMessage ="无";}
			else 
			{	LocalizedMessage = e.getLocalizedMessage();}


	    	exception_InfoString = 	"\r\n"+
	    							"   1、Message："			+Message			+	"\r\n"+
	    							"   2、CauseString："		+CauseString		+	"\r\n"+
	    							"   3、LocalizedMessage："	+LocalizedMessage	+	"\r\n";

		}
	    finally
	    {
	    	//取得所有参数
	        Map ValueTreeMap = (Map) invocation.getInvocationContext().get("com.opensymphony.xwork2.ActionContext.parameters");
	        String ParaRet =  GetActionPara(ValueTreeMap) ;//获取传入Action的参数
	       
	        Map session = invocation.getInvocationContext().getSession();
		    username = (String)session.get("username");
	        
	        //获取访问的时间
	
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	        String CurrentTimeString = df.format(new Date());
	        
	        if (exception_InfoString  == null)
	        	exception_InfoString  ="无";	        
	        
	        String log_InfoString = "\r\n"+
	        						"================================================"				+"\r\n"+
	        						"用户：                           "+username 										+"\r\n"+
	        						"访问时间：                   "+CurrentTimeString								+"\r\n"+
	        						"访问执行时长/毫秒："+UseTime											+"\r\n"+
	        						"执行的Action：     "+invocation.getAction().getClass().getName()		+"\r\n"+
	        						"执行的方法：               "+invocation.getProxy().getMethod()				+"\r\n"+
	        						"传入的参数：               "+ParaRet											+"\r\n"+
	        						"异常信息：                   "+exception_InfoString							+"\r\n"+
	        						"================================================"				+"\r\n";
	       			
	        System.out.println(log_InfoString);
//	        System.out.println("用户                            ："+username);
//	        System.out.println("访问时间                    ："+CurrentTimeString);
//	        System.out.println("访问执行时长/ms ："+UseTime);
//	        System.out.println("执行的Action  ："+invocation.getAction().getClass().getName());
//	        System.out.println("执行的方法                ："+invocation.getProxy().getMethod());
//	        System.out.println("传入的参数                ："+ParaRet);
//	        System.out.println("异常信息                    ："+exception_InfoString);
//	        System.out.println("================================================");
	        System_Log mSystem_Log = new System_Log();
	        mSystem_Log.Log(log_InfoString);	
	        
	    }
    	
        return result;

    }  
}
