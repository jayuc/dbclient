package com.github.jayuc.dbclient.act;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.config.MongoCommands;
import com.github.jayuc.dbclient.entity.MongoObj;
import com.github.jayuc.dbclient.entity.MongoSql;
import com.github.jayuc.dbclient.err.SqlHandlerException;
import com.github.jayuc.dbclient.iter.ISqlHandler;
import com.github.jayuc.dbclient.utils.MethodReturnUtil;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

/**
 * mogodb 处理器
 * @author yujie
 *
 */
@Component("mongodbHandler")
public class MongodbSqlHandler implements ISqlHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(MongodbSqlHandler.class);

	@Override
	public Map<String, Object> execute(Object pool, String sql, String token) 
			throws SqlHandlerException {
		LOG.debug("开始处理mongodb");
		
		if(null == pool) {
			throw new SqlHandlerException("pool: " + pool);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		MongoSql mongosql = parseSql(sql);
		LOG.debug("mongo sql: " + mongosql);
		if(null != mongosql) {
			try {
				MongoObj mongo = (MongoObj) pool;
				MongoClient client = mongo.getClient();
				MongoDatabase db = client.getDatabase(mongo.getDb());
				String level = mongosql.getLevel();
				String methodName = mongosql.getSql();
				Class<?>[] paramClass = mongosql.getParamClass();
				Object[] param = mongosql.getParam();
				
				Method method = null;
				Object obj = null;
				if("client".equals(level)) {
					method = MongoClient.class.getMethod(methodName, paramClass);
					obj = client;
				}else if("db".equals(level)) {
					method = MongoDatabase.class.getMethod(methodName, paramClass);
					obj = db;
				}
				
				if(null != method) {
					
					Class<?> methodReturnClass = method.getReturnType();
					LOG.info("return class type: " + methodReturnClass.getName());
					
					Object methodResult = method.invoke(obj, param);
					LOG.info("methodResult: " + methodResult);
					
					Map<String, Object> map = MethodReturnUtil.parse(methodReturnClass, methodResult);
					LOG.debug("map: " + map);
					if(null != map) {
						result = map;
					}else {
						result = parseResult(methodReturnClass, methodResult);
					}
					
				}else {
					throw new SqlHandlerException("方法名或参数不正确");
				}
				
			} catch (Exception e) {
				throw new SqlHandlerException(e.getMessage());
			}
		}else {
			throw new SqlHandlerException("语句不合法");
		}
		
		LOG.debug("结束处理mongodb");
		return result;
	}
	
	// 解析result
	@SuppressWarnings("unchecked")
	private Map<String, Object> parseResult(Class<?> clazz, Object obj){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<String> headerList = new ArrayList<String>();
		int total = 0;
		final String value = "value";
		
		if(MongoIterable.class == clazz) {
			MongoIterable<String> rows = (MongoIterable<String>) obj;
			for(String row:rows) {
				Map<String, Object> r = new HashMap<String, Object>();
				r.put(value, row);
				list.add(r);
				total ++;
			}
			headerList.add(value);
		}else if(MongoCollection.class == clazz) {
			MongoCollection<Document> rows = (MongoCollection<Document>) obj;
			FindIterable<Document> findIterable = rows.find();
		    MongoCursor<Document> cursor = findIterable.iterator();
		    int i = 0;
		    while(cursor.hasNext()) {
		    	if(i > 10) {
		    		break;
		    	}
		    	Document doc = cursor.next();
		    	Map<String, Object> r = new HashMap<String, Object>();
				r.put(value, doc.toJson());
				list.add(r);
				i++;
		    }
		    headerList.add(value);
		    total = (int) rows.count();
		}
		
		map.put("headers", headerList);
		map.put("rows", list);
		map.put("total", total);
		return map;
	}
	
	// 解析sql
	private MongoSql parseSql(String sql) {
		String jsql = trimAllBlank(sql);
		LOG.debug("jsql: " + jsql);
		if(MongoCommands.commands.containsKey(jsql)) {
			return MongoCommands.commands.get(jsql);
		}
		String[] sqlArr = sql.split("\\.");
		if(sqlArr.length > 1) {
			LOG.debug(sqlArr[0]);
			if("db".equals(sqlArr[0])) {
				MongoSql mongosql = new MongoSql();
				LOG.debug(sqlArr[1]);
				if(sqlArr[1].endsWith(")")) {
					mongosql.setLevel("client");
					mongosql.setSql(sqlArr[1]);
					return mongosql;
				}else if(sqlArr.length > 2) {
					LOG.debug(sqlArr[2]);
					if((sqlArr[2]).startsWith("find(")) {
						mongosql.setLevel("db");
						mongosql.setSql("getCollection");
						mongosql.setParam(new String[] {sqlArr[1]});
						mongosql.setParamClass(new Class[]{String.class});
						return mongosql;
					}
					if("find()".equals(sqlArr[sqlArr.length - 1])) {
						String pstr = "";
						for(int i=1; i<sqlArr.length - 1; i++) {
							pstr += sqlArr[i] + ".";
						}
						mongosql.setLevel("db");
						mongosql.setSql("getCollection");
						mongosql.setParam(new String[] {pstr.substring(0, pstr.length() - 1)});
						mongosql.setParamClass(new Class[]{String.class});
						return mongosql;
					}
				}
			}
		}
		return null;
	}
	
	// 去空格
	private String trimAllBlank(String sql) {
		LOG.debug("sql: " + sql);
		return sql.replace(" ", "");
	}

}
