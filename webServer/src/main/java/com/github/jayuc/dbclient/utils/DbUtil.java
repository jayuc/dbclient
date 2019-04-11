package com.github.jayuc.dbclient.utils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 数据库操作工具类
 * @author yujie
 * 2019年4月9日 下午4:45:34
 */
public class DbUtil {
	
	private static final Logger log = LoggerFactory.getLogger(DbUtil.class);
	

	/**
	 * 执行无查询结果的查询,
	 * 比如：不常用
	 * @throws SQLException 
	 */
	public static boolean execute(Connection conn, String sql) throws SQLException{
		if(null == conn) {
			throw new SQLException("connection连接为空");
		}
 	    Statement stmt = null;   
 	    stmt = conn.createStatement();
		boolean b = stmt.execute(sql);
		stmt.close();
		conn.close();
		return b;
	}
	
	/**
	 * 更新数据 删除
	 */
	public static int update(Connection conn, String sql) throws SQLException{
		if(null == conn) {
			throw new SQLException("connection连接为空");
		}
 	    Statement stmt = null;   
 	    stmt = conn.createStatement();
		int re = stmt.executeUpdate(sql);
		stmt.close();
		conn.close();
		return re;
	}
	
    /**
     * @throws SQLException  
     * @Title: insertDataHasRet 
     * @Description: 插入数据
     * @param @param sql    设定文件 
     * @return void    返回类型 
     * @throws 
     */
     public static int insert(Connection conn, String sql) throws SQLException{
    	if(null == conn) {
    		throw new SQLException("connection连接为空");
 		}
     	PreparedStatement  preparedStatement = null;   
 	    int retVal=0;
 	    preparedStatement = conn.prepareStatement(sql);
		retVal=	preparedStatement.executeUpdate();
		preparedStatement.close();
		conn.close();
		return retVal;
     }
     
   /**
 * @throws SQLException  
    * @Title: executeBatchArryData 
    * @Description: 根据出入的数组信息批量插入数据
    * @param @param sql
    * @param @param params  [{Object,java.sql.Types.类型}]
    * @return void    返回类型 
    * @throws 
    */
    public static int[] executeBatchArryData(Connection conn, String sql,Object[]...params) throws SQLException{
		if(null == conn) {
			throw new SQLException("connection连接为空");
		}
    	int  size  = params[0].length;
     	PreparedStatement  stmt = null;   
     	stmt = conn.prepareStatement(sql);
		//提交数据条数
		for(int s =0;s<size;s++){
	        //需要传参数??
			for(int i=0;i<params.length;i++){
				Object[]   temp = (Object[]) params[i][s];
				stmt.setObject(i+1, temp[0],(int)temp[1]);
			}
			stmt.addBatch();
		}
		int[] re = stmt.executeBatch();
		stmt.close();
		conn.close();
		return re;
     }
    
    /**
     * @throws SQLException  
     * @Title: insertData 
     * @Description: 插入数据
     * @param @param sql    设定文件 
     * @return void    返回类型 
     * @throws 
     */
     public static int[] executeBatchData(Connection conn, List<String> sqls) throws SQLException{
 		if(null == conn) {
 			throw new SQLException("connection连接为空");
		}
 	    Statement  stmt = null;   
		conn.setAutoCommit(false);
		stmt = conn.createStatement();
		for(String  sql:sqls){
			stmt.addBatch(sql);
		}
		int[] re = stmt.executeBatch();
		conn.commit();
		stmt.close();
		conn.close();
		return re;
     }
     
   /**
 * @throws SQLException  
    * @Title: queryData 
    * @Description:根据传入的SQL获取JSON数据 
    * @param @param sql
    * @param @return    设定文件 
    * @return JSONArray    返回类型 
    * @throws 
    */
    public static JSONArray queryJSONData(Connection conn, String sql) throws SQLException{
		if(null == conn) {
			throw new SQLException("connection连接为空");
		}
    	JSONArray  list =null;
	    Statement  stmt = null;   
	    ResultSet  rs = null; //表示接收数据库的查询结果
	    stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		list = createJSONArray(rs);
		rs.close();
		stmt.close();
		conn.close();
		if(rs!=null){
			rs.close(); //出现异常关闭链接
		}
		if(stmt!=null){
			stmt.close();//出现异常关闭链接
		}
		if(conn!=null){
			conn.close();
		}
		return list;
    } 
    
    /**
     * 获取的结果是map
     * 结构是{
     * rows: 结果,
     * headers: 头部结果
     * }
     */
    public static Map<String, Object> queryJSONMap(Connection conn, String sql) throws SQLException{
		if(null == conn) {
			throw new SQLException("connection连接为空");
		}
	    Statement  stmt = null;   
	    ResultSet  rs = null; //表示接收数据库的查询结果
	    stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		Map<String, Object> map = createJSONArrayMap(rs);
		rs.close();
		stmt.close();
		conn.close();
		if(rs!=null){
			rs.close(); //出现异常关闭链接
		}
		if(stmt!=null){
			stmt.close();//出现异常关闭链接
		}
		if(conn!=null){
			conn.close();
		}
		return map;
    }

    /**
     * @throws SQLException  
    * @Title: createJSONArray 
    * @Description: 创建JSON集合 
    * @param @param rs
    * @param @return    设定文件 
    * @return JSONArray    返回类型 
    * @throws 
    */
    private static JSONArray createJSONArray(ResultSet rs) throws SQLException {
    	JSONArray list  = new JSONArray();
		while(rs.next()){
			JSONObject jsonObject  = new JSONObject();
	        try{
	            int  length = rs.getMetaData().getColumnCount();
	            for(int i=0;i<length;i++){
	            	String  key  = rs.getMetaData().getColumnName(i+1);
	            	String  classType = rs.getMetaData().getColumnClassName(i+1);
	            	jsonObject.put(key,Class.forName(classType).cast(rs.getObject(i+1)));
	            }
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	        list.add(jsonObject);
		}
		return list;
	}
    
    private static Map<String, Object> createJSONArrayMap(ResultSet rs) throws SQLException {
    	JSONArray list  = new JSONArray();
    	Map<String, Object> map = new HashMap<String, Object>();
    	List<String> headers = new ArrayList<String>();
    	int c = 1;
		while(rs.next()){
			JSONObject jsonObject  = new JSONObject();
	        try{
	            int  length = rs.getMetaData().getColumnCount();
	            for(int i=0;i<length;i++){
	            	String  key  = rs.getMetaData().getColumnName(i+1);
	            	String  classType = rs.getMetaData().getColumnClassName(i+1);
	            	jsonObject.put(key,Class.forName(classType).cast(rs.getObject(i+1)));
	            	if(c == 1) {
	            		headers.add(key);
	            	}
	            }
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	        list.add(jsonObject);
	        c++;
		}
		map.put("rows", list);
		map.put("headers", headers);
		return map;
	}

	/**
	 * @throws SQLException  
	* @Title: queryData 
	* @Description: 根据传入的接口获取Object对象
	* @param @param sql
	* @param @param className
	* @param @return    设定文件 
	* @return List    返回类型 
	* @throws 
	*/
	public static List queryData(Connection conn, String sql,Class className) throws SQLException {
		if(null == conn) {
			throw new SQLException("connection连接为空");
		}
		List  list =null;
	    Statement  stmt = null;   
	    ResultSet  rs = null; //表示接收数据库的查询结果
	    stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		list = createList(rs,className);
		rs.close();
		stmt.close();
		conn.close();
		return list;
	}
	/**
	 * @throws ClassNotFoundException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException  
	* @Title: createList 
	* @Description: 封装LIST OBJ
	* @param @param rs
	* @param @param string
	* @param @return    设定文件 
	* @return List    返回类型 
	* @throws 
	*/
	private static List createList(ResultSet rs, Class classType) throws SQLException{
		List list  = new ArrayList();
		Method[]  methods  = classType.getDeclaredMethods();
		while(rs.next()){
		    Object objInstance=null;
	        try{
	        	objInstance=classType.newInstance();
	        	for(Method method:methods){
	        		String methodName  = method.getName();
	        		if(methodName.startsWith("set")){
	        			//以set开头的方法都需要执行
	        			//要获取数据库的字段信息以删除SET字符，第一个字母小写
	        			String  fileld = methodName.replace("set", "");
	        			fileld =fileld.toUpperCase().substring(0, 1)+fileld.substring(1, fileld.length());
	        			try{
	        				method.invoke(objInstance,new Object[]{(method.getParameterTypes()[0]).cast(rs.getObject(fileld))});
	        			}catch(Exception e){
	        				log.info(methodName+"赋值 失败，程序不做处理！");
	        			}
	        		}
	        		
	        	}
	            list.add(objInstance);
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		return list;
	}

}
