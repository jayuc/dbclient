package com.github.jayuc.dbclient.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.jayuc.dbclient.entity.Result;
import com.github.jayuc.dbclient.param.SqlDataParam;
import com.github.jayuc.dbclient.utils.ResultUtils;

/**
 * 执行特殊的redis命令
 * @author yujie
 *
 */

@RestController
@RequestMapping("/redis")
public class RedisSpecialSqlController {
	
	private static final Logger LOG = LoggerFactory.getLogger(RedisSpecialSqlController.class);
	
	@Autowired
	SqlExecuteController executer;
	
	@GetMapping("/execute")
	public Map<String, Object> execute(@ModelAttribute("param") SqlDataParam param){
		LOG.debug("开始执行,sql: " + param.getSql());
		//开始时间
		long executeStartTime = System.currentTimeMillis();
		Result result = ResultUtils.normalResult();
		
		String sql = param.getSql();
		if("getRedisAllKeys".equals(sql)) {
			param.setSql("keys *");
			Map<String, Object> map = executer.execute(param);
			Map<String, Object> res = (Map<String, Object>) map.get("result");
			if(null != res.get("headers")) {
				List<String> headers = (List<String>) res.get("headers");
				List<Map<String, Object>> rows = (List<Map<String, Object>>) res.get("rows");
				if(headers.size() > 0) {
					for(Map<String, Object> item:rows) {
						String key = (String) item.get(headers.get(0));
						param.setSql("type " + key);
						Map<String, Object> keymap = executer.execute(param);
						Map<String, Object> keyres = (Map<String, Object>) keymap.get("result");
						LOG.debug(keyres.toString());
						if(null != keyres.get("headers")) {
							List<String> keyheaders = (List<String>) keyres.get("headers");
							List<Map<String, Object>> keyrows = (List<Map<String, Object>>) keyres.get("rows");
							String value = (String) keyrows.get(0).get(keyheaders.get(0));
							LOG.debug(keyrows.toString());
							item.put("type", value);
						}
					}
				}
			}
			return map;
		}
		
		//结束时间
		long executeEndTime = System.currentTimeMillis();
		result.setProperty("took", (executeEndTime - executeStartTime));
		LOG.debug("执行结束");
		return result.getResult();
	}

}
