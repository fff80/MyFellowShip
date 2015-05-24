package com.hdos.utils;

import java.beans.PropertyVetoException;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.sql.TIMESTAMP;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author xt
 * 
 */
@SuppressWarnings("all")
public class DBAccess {
	/** # 是否输出SQL语句 # **/
	public boolean OUTSQL = true;
	private Connection conn;
	private static ComboPooledDataSource ds = new ComboPooledDataSource();

	/**
	 * @author xt
	 * @see dom4j解析xml文件
	 * @return null
	 */
	private static void loadXML() {
		SAXReader sr = new SAXReader();
		Document doc;
		try {
			// System.out.println("数据库配置文件路径："+Tool.APP_PATH+"WEB-INF\\JDBC.xml");
			doc = sr.read(new File(Tool
					.getPath("class:com/hdos/utils/JDBC.xml")));
			Element e = doc.getRootElement();
			Iterator<Element> it = e.elementIterator();
			while (it.hasNext()) {
				Element ele = (Element) it.next();
				Iterator<Element> childIter = ele.elementIterator();
				String driver = "";
				String url = "";
				String username = "";
				String password = "";
				int maxPoolSize = 30;
				int minPoolSize = 10;
				while (childIter.hasNext()) {
					Element chileEle = childIter.next();
					String ename = chileEle.getName();
					String evalue = chileEle.getText();
					if (ename.equals("driverClass")) {
						driver = evalue;
					} else if (ename.equals("url")) {
						url = evalue;
					} else if (ename.equals("username")) {
						username = evalue;
					} else if (ename.equals("password")) {
						password = evalue;
					} else if (ename.equals("maxPoolSize")) {
						maxPoolSize = Tool.num(evalue);
					} else if (ename.equals("minPoolSize")) {
						minPoolSize = Tool.num(evalue);
					}
				}
				createConection(driver, url, username, password, minPoolSize,
						maxPoolSize);
			}
		} catch (DocumentException e) {
			System.out.println("加载数据文件出错：" + e.getMessage());
		}
	}

	/**
	 * @author xt
	 * @see 设置c3p0连接属性
	 * @return null
	 */
	private static void createConection(String driver, String url,
			String username, String password, int minPoolSize, int maxPoolSize) {
		try {
			ds.setDriverClass(driver);
			ds.setJdbcUrl(url);
			ds.setUser(username);
			ds.setPassword(password);
			ds.setMinPoolSize(minPoolSize);
			ds.setMaxPoolSize(maxPoolSize);
		} catch (PropertyVetoException e) {
			System.out.println("加载数据驱动出现问题" + e.getMessage());
		}
	}

	static {
		DBAccess.loadXML();
	}

	/**
	 ** 
	 * @author song.hu
	 * @throws SQLException
	 * @description 未使用事务
	 */
	private DBAccess() {
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author xt
	 * @see 修改数据操作
	 * @param sql
	 *            objs[]
	 * @return 受影响的记录数
	 * @throws SQLException
	 */
	public int exeUpdate(String sql, Object objs[]) throws SQLException {
		if (this.OUTSQL) {
			System.out.println("SQL:" + sql);
		}
		int flag = -1;
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(sql);
		if (objs != null) {
			for (int i = 0; i < objs.length; i++)
				pstmt.setObject(i + 1, objs[i]);
		}
		flag = pstmt.executeUpdate();
		pstmt.close();
		return flag;
	}

	/**
	 * @author xt
	 * @see 返回主键
	 * @param sql
	 * @return int 0表示没有对应数据
	 * @throws SQLException
	 */
	public int exeInsert(String sql, Object objs[]) throws SQLException {
		// 获取序列名称
		String seq = sql.substring(0, sql.indexOf("("));
		seq = seq.replaceAll("^\\s+|\\s+$", "");
		seq = seq.substring(seq.lastIndexOf(" ") + 1);
		if (seq.indexOf("tb_") != -1) {
			seq = "sequence_" + seq;
		} else {
			seq = "seq_" + seq + "_iid";
		}
		return this.exeInsert(sql, objs, seq);
	}

	/**
	 * @author xt
	 * @see 返回主键
	 * @param sql
	 * @return int 0表示没有对应数据
	 * @throws SQLException
	 */
	public int exeInsert(String sql, Object objs[], String seq)
			throws SQLException {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Statement smt = null;
		int iid = -1;
		// STEP 1.处理序列,获取下一个iid
		String seqsql = "select " + seq + ".nextval from dual";
		if (this.OUTSQL)
			System.out.println("SQL:" + seqsql);
		smt = conn.createStatement();
		rs = smt.executeQuery(seqsql);
		if (rs.next())
			iid = rs.getInt(1);
		// STEP 2.处理sql脚本
		int flag = sql.indexOf("(");
		sql = sql.substring(0, flag + 1) + "iid, " + sql.substring(flag + 1);
		flag = sql.indexOf("(", flag + 1);
		sql = sql.substring(0, flag + 1) + iid + ", " + sql.substring(flag + 1);
		if (this.OUTSQL)
			System.out.println("SQL:" + sql);
		pstmt = conn.prepareStatement(sql);
		if (objs != null) {
			for (int i = 0; i < objs.length; i++)
				pstmt.setObject(i + 1, objs[i]);
		}
		pstmt.execute();
		rs.close();
		smt.close();
		pstmt.close();
		return iid;
	}

	public int exeInsert(String sql) throws SQLException {

		PreparedStatement pstmt = null;

		pstmt = conn.prepareStatement(sql);
		pstmt.execute();
		pstmt.close();
		return 1;
	}
	
	public int exeInserts(String sql) throws SQLException {
		PreparedStatement pstmt = null;
        String str[]=null;
        if(!"".equals(sql)) str=sql.split(";");
        for(int i=0;i<str.length;i++){
        	pstmt = conn.prepareStatement(str[i]);
        	pstmt.execute();
        }
		pstmt.close();
		return str.length;
	}

	public PageBean QueryForPageBean(String sql, Object objs[], PageBean pb)
			throws SQLException {
		// TODO 这个分页有待完善
		List<Map> list = this.QueryForList(sql, objs);
		pb.setTotalSize(list.size());
		int offset = pb.getOffsetResult();
		List<Map> result = new ArrayList<Map>();
		for (int idx = pb.getOffsetResult(); idx - pb.getOffsetResult() < pb
				.getPageSize() && idx < list.size(); idx++)
			result.add(list.get(idx));
		pb.setResultlist(result);
		return pb;
	}

	public PageBean QueryForPageBean(List<Map<String, String>> list, PageBean pb)
			throws SQLException {
		// TODO 这个分页有待完善
		pb.setTotalSize(list.size());
		int offset = pb.getOffsetResult();
		List<Map> result = new ArrayList<Map>();
		for (int idx = pb.getOffsetResult(); idx - pb.getOffsetResult() < pb
				.getPageSize() && idx < list.size(); idx++)
			result.add(list.get(idx));
		pb.setResultlist(result);
		return pb;
	}

	/**
	 * 分页查询（没有进行封装）
	 * 
	 * @param sql
	 * @param objs
	 * @param currpage
	 * @param pagesize
	 * @return
	 * @throws SQLException
	 */
	public PageBean QueryForPageBean(String sql, Object objs[], int currpage,
			int pagesize) throws SQLException {
		int totalCount = this.getCount(sql);
		
		sql = " SELECT * FROM (" + " SELECT TEMP.*,ROWNUM RN FROM ( " + sql
				+ ") TEMP WHERE ROWNUM <=" + (currpage) * pagesize
				+ ") WHERE RN>=" + ((currpage - 1) * pagesize + 1);
		System.out.println(sql);
		List<Map> list = this.QueryForList(sql, objs);
		PageBean pb = new PageBean(currpage, pagesize);
		pb.setTotalSize(totalCount);
		pb.setResultlist(list);
		return pb;
	}

	public PageBean QueryForPageBeanLower(String sql, Object objs[],
			int currpage, int pagesize) throws SQLException {
		int totalCount = this.getCount(sql);
		sql = " SELECT * FROM (" + " SELECT TEMP.*,ROWNUM RN FROM ( " + sql
				+ ") TEMP WHERE ROWNUM <=" + (currpage) * pagesize
				+ ") WHERE RN>=" + ((currpage - 1) * pagesize + 1);
		List<Map<String, Object>> list = this.QueryForListLower(sql, objs);
		PageBean pb = new PageBean(currpage, pagesize);
		pb.setTotalSize(totalCount);
		pb.setResultlist(list);
		return pb;
	}

	/**
	 * 分页查询（进行了封装）
	 * 
	 * @param sql
	 * @param cla
	 *            进行封装的VO
	 * @param currpage
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	public PageBean QueryVOForPageBean(String sql, Class cla, int currpage,
			int pagesize) throws Exception {

		int totalCount = this.getCount(sql);
		sql = " SELECT * FROM (" + " SELECT TEMP.*,ROWNUM RN FROM ( " + sql
				+ ") TEMP WHERE ROWNUM <=" + (currpage) * pagesize
				+ ") WHERE RN>=" + ((currpage - 1) * pagesize + 1);
		List<Object> list = this.findListVOBySql(sql, cla);
		PageBean pb = new PageBean(currpage, pagesize);
		pb.setTotalSize(totalCount);
		pb.setResultlist(list);
		return pb;
	}

	public int getCount(String sql) throws SQLException {
		String sql1 = sql.toUpperCase();
		int endIndex = sql1.indexOf(" ORDER BY ");
		if (sql1.lastIndexOf(" ORDER BY ") != endIndex) {
			endIndex = -1;
		}
		sql = endIndex != -1 ? sql.substring(0, endIndex) : sql;
		sql = "select count(*) from ( " + sql + " )";
		return this.QueryForInt(sql, null);
	}

	/**
	 * 查询列表（没有进行封装）
	 * 
	 * @param sql
	 * @param objs
	 * @return
	 * @throws SQLException
	 */
	public List<Map> QueryForList(String sql, Object objs[])
			throws SQLException {
		if (this.OUTSQL) {
			// System.out.println("SQL:" + sql);
		}
		List<Map> list = new ArrayList<Map>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(sql);
		if (objs != null) {
			for (int i = 0; i < objs.length; i++)
				pstmt.setObject(i + 1, objs[i]);
		}
		rs = pstmt.executeQuery();
		ResultSetMetaData meta = rs.getMetaData();
		String[] cols = new String[meta.getColumnCount()];
		for (int i = cols.length; i > 0; i--) {
			cols[i - 1] = meta.getColumnName(i);
		}
		while (rs.next()) {
			Map map = new HashMap();
			for (String col : cols) {
				Object value = rs.getObject(col);
				if (value != null && value.getClass() == TIMESTAMP.class) {
					value = (Date) ((TIMESTAMP) value).timestampValue();
				}
				map.put(col, value);
			}
			list.add(map);
		}
		rs.close();
		pstmt.close();
		return list;
	}

	public List<Map<String, Object>> QueryForListLower(String sql,
			Object objs[]) throws SQLException {
		if (this.OUTSQL) {
			// System.out.println("SQL:" + sql);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(sql);
		if (objs != null) {
			for (int i = 0; i < objs.length; i++)
				pstmt.setObject(i + 1, objs[i]);
		}
		rs = pstmt.executeQuery();
		ResultSetMetaData meta = rs.getMetaData();
		String[] cols = new String[meta.getColumnCount()];
		for (int i = cols.length; i > 0; i--) {
			cols[i - 1] = meta.getColumnName(i);
		}
		while (rs.next()) {
			Map map = new HashMap();
			for (String col : cols) {
				Object value = rs.getObject(col);
				if (value != null && value.getClass() == TIMESTAMP.class) {
					value = (Date) ((TIMESTAMP) value).timestampValue();
				}
				map.put(col.toLowerCase(), value);
			}
			list.add(map);
		}
		rs.close();
		pstmt.close();
		return list;
	}

	/**
	 * 查询列表（进行了封装）
	 * 
	 * @param sql
	 * @param cla
	 * @return
	 * @throws Exception
	 */
	public List<Object> findListVOBySql(String sql, Class cla) throws Exception {
		if (this.OUTSQL) {
			System.out.println("SQL:" + sql);
		}
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		List list = new ArrayList<Object>();
		while (rs.next()) {
			Object obj = cla.newInstance();
			// 得到类所有方法
			Method[] methods = cla.getMethods();
			for (int j = 1; j <= count; j++) {
				String colName = meta.getColumnName(j);
				String methodName = getMethodNameByColumnName(colName);
				for (Method m : methods) {
					if (m.getName().equals(methodName)) {
						Object value = rs.getObject(colName);
						Class[] types = m.getParameterTypes();
						if (types[0] == int.class || types[0] == Integer.class) {
							if (value != null) {
								value = Integer.parseInt(value.toString());
							} else {
								value = 0;
							}
							m.invoke(obj, value);

						} else if (types[0] == double.class
								|| types[0] == Double.class) {
							if (value != null) {
								value = Double.parseDouble(value.toString());
							} else {
								value = 0.0;
							}
							m.invoke(obj, value);

						} else if (types[0] == String.class) {

							m.invoke(obj, value);
						} else if (types[0] == Date.class) {
							SimpleDateFormat df = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							if (value != null) {
								if (value.getClass() == TIMESTAMP.class) {
									value = ((TIMESTAMP) value)
											.timestampValue();
								}
								String dateStr = df.format(value);
								value = df.parse(dateStr);
							}
							m.invoke(obj, value);
						}

						break;
					}
				}
			}
			list.add(obj);
		}
		rs.close();
		pstmt.close();
		return list;
	}

	// 返回唯一的值
	public Object QueryForObject(String sql, Object objs[]) throws SQLException {
		if (this.OUTSQL) {
			System.out.println("SQL:" + sql);
		}
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Object value = null;
		pstmt = conn.prepareStatement(sql);
		if (objs != null) {
			for (int i = 0; i < objs.length; i++)
				pstmt.setObject(i + 1, objs[i]);
		}
		rs = pstmt.executeQuery();
		if (rs.next())
			value = rs.getObject(1);
		rs.close();
		pstmt.close();
		return value;
	}

	/**
	 * 返回一个VO
	 * 
	 * @param sql
	 * @param cla
	 * @return
	 * @throws Exception
	 */
	public Object findVOBySql(String sql, Class cla) throws Exception {
		if (this.OUTSQL) {
			System.out.println("SQL:" + sql);
		}
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		Object obj = null;
		if (rs.next()) {
			obj = cla.newInstance();
			// 得到类所有方法
			Method[] methods = cla.getMethods();
			for (int j = 1; j <= count; j++) {
				String colName = meta.getColumnName(j);
				String methodName = getMethodNameByColumnName(colName);
				for (Method m : methods) {
					if (m.getName().equals(methodName)) {
						Object value = rs.getObject(colName);
						Class[] types = m.getParameterTypes();
						if (types[0] == int.class || types[0] == Integer.class) {
							if (value != null) {
								value = Integer.parseInt(value.toString());
							} else {
								value = 0;
							}
							m.invoke(obj, value);

						} else if (types[0] == double.class
								|| types[0] == Double.class) {
							if (value != null) {
								value = Double.parseDouble(value.toString());
							} else {
								value = 0.0;
							}
							m.invoke(obj, value);

						} else if (types[0] == String.class) {

							m.invoke(obj, value);
						} else if (types[0] == Date.class) {
							SimpleDateFormat df = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							if (value != null) {
								if (value.getClass() == TIMESTAMP.class) {
									value = ((TIMESTAMP) value)
											.timestampValue();
								}
								String dateStr = df.format(value);
								value = df.parse(dateStr);
							}
							m.invoke(obj, value);
						}

						break;
					}
				}

			}

		}
		rs.close();
		pstmt.close();
		return obj;
	}

	private String getMethodNameByColumnName(String colName) {
		int place = colName.indexOf("_");
		if (place != -1) {
			colName = colName.substring(0, 1).toUpperCase()
					+ colName.substring(1, place).toLowerCase()
					+ colName.substring(place + 1, place + 2).toUpperCase()
					+ colName.substring(place + 2).toLowerCase();
		} else {
			colName = colName.substring(0, 1).toUpperCase()
					+ colName.substring(1).toLowerCase();
		}
		return "set" + colName;
	}

	public Map<String, Object> QueryForMap(String sql, Object objs[])
			throws SQLException {
		List<Map> list = this.QueryForList(sql, objs);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	public Map<String, Object> QueryForMapLower(String sql, Object objs[])
			throws SQLException {
		List<Map<String, Object>> list = this.QueryForListLower(sql, objs);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	public Integer QueryForInt(String sql, Object objs[])
			throws NumberFormatException, SQLException {
		Object object = this.QueryForObject(sql, objs);
		if (object == null) {
			return 0;
		}
		return Integer.valueOf(object.toString());
	}

	public String buildTimeSql(String sql, String timeField, String startDate,
			String endDate) {
		if (startDate != null && !startDate.trim().equals("")) {
			sql += " and " + timeField + ">= to_date('" + startDate
					+ "','yyyy-mm-dd HH24:MI:SS')";
		}
		if (endDate != null && !endDate.trim().equals("")) {
			sql += " and " + timeField + "<= to_date('" + endDate
					+ "','yyyy-mm-dd HH24:MI:SS')";
		}
		return sql;
	}

	/** ## 回滚 并释放连接 ## */
	public void rollback() {
		try {
			if (!conn.getAutoCommit())
				conn.rollback();
		} catch (SQLException e) {
		}
	}

	/** ## 提交数据 不释放连接 ## */
	public void commit() {
		try {
			if (!conn.getAutoCommit()) {
				conn.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void close() {
		try {
			if (conn.isClosed())
				return;
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static ThreadLocal<DBAccess> POOL = new ThreadLocal<DBAccess>();

	public static boolean hasConnection() {
		return POOL.get() != null;
	}

	public static DBAccess getInstance() {
		DBAccess db = POOL.get();
		if (db == null) {
			db = new DBAccess();
			POOL.set(db);
		}
		return db;
	}

	public static void clearInstance() {
		DBAccess db = POOL.get();
		if (db == null)
			return;
		db.close();
		POOL.set(null);
	}

	public String buildUpdateSql(Object obj, String table, String whereString) {
		StringBuilder sb = new StringBuilder("UPDATE ");
		// 获得持久化类
		// 获得表名称并添加到sql语句
		sb.append(table);
		sb.append(" SET ");
		// 获得主键名称
		String id = "IID";
		// 类的主键字段
		Field fieldId = null;
		// 得到对象的所有字段
		Field[] fields = obj.getClass().getDeclaredFields();
		// 遍历对象
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			// 设置字段可以访问
			field.setAccessible(true);
			// 获得字段类型
			Class fieldType = field.getType();
			try {
				// //如果字段的值为null不添加到update语句中
				// if(field.get(obj)==null)
				// {
				// continue;
				// }
				// 获得根据字段名称获得数据库对应的字段名称
				String columnName = null;
				columnName = getColumnName(field.getName());
				if (columnName == null) {
					continue;
				}
				// 主键跳出修改增加
				if (id.toLowerCase().equals(columnName.toLowerCase())) {
					fieldId = field;
					continue;
				}
				if (java.lang.String.class == fieldType) {
					// if(((String)field.get(obj)).trim().equals("")) //如果为空字符串
					// {
					// continue;
					// }
					sb.append(columnName + "= '" + field.get(obj) + "'");
				} else if (Integer.class == fieldType || int.class == fieldType) {
					// 如果字段的值为0跳过
					// if(((Integer)field.get(obj)).intValue()==0)
					// {
					// continue;
					// }
					sb.append(columnName + "= " + field.get(obj));
				} else if (double.class == fieldType
						|| Double.class == fieldType) {
					// 如果字段的值为0跳过
					// if(((Double)field.get(obj)).doubleValue()==0)
					// {
					// continue;
					// }
					sb.append(columnName + "= " + field.get(obj));
				} else if (Date.class == fieldType) {
					// 格式化时间对象
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					if ((Date) field.get(obj) == null)
						continue;
					// 格式化时间
					String datestr = sdf.format((Date) field.get(obj));
					String dialect = "OracleDialect";
					// 如果是Oracle需要使用Oracle转换时间的转换
					if (dialect.equals("OracleDialect")) {
						// sb.append(columnName);
						sb.append(columnName + "= to_date('" + datestr
								+ "','yyyy-mm-dd HH24:MI:SS')");
						// 如果是Sql server和MySql直接像字符串一样操作即可
					} else if (dialect.equals("SQLServerDialect")
							|| dialect.equals("MySQLDialect")) {
						sb.append(columnName);
						sb.append(columnName + "='" + datestr + "'");
					}
				}
				sb.append(",");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		// 去掉最后一个逗号
		sb.deleteCharAt(sb.toString().length() - 1);
		// 到最后添加主键或者whereString条件
		if (whereString == null || whereString.trim().equals("")) {
			try {
				if (java.lang.String.class == fieldId.getType()) {
					sb.append(" where " + id + "='" + fieldId.get(obj) + "'");
					// sb.append( columnName + "= '"+ field.get(obj) + "'");
				} else if (Integer.class == fieldId.getType()
						|| int.class == fieldId.getType()) {
					sb.append(" where " + id + "=" + fieldId.get(obj) + "");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			sb.append(" " + whereString);
		}
		return sb.toString();
	}

	public String buildInsertSql(Object obj, String table) {
		StringBuilder sb = new StringBuilder("insert into ");
		StringBuilder values = new StringBuilder();
		// 获得持久化类
		// 获得表名称并添加到sql语句
		sb.append(table);
		sb.append("(");
		// 获得主键名称
		String id = "IID";
		// 类的主键字段
		Field fieldId = null;
		// 得到对象的所有字段
		Field[] fields = obj.getClass().getDeclaredFields();
		// 遍历对象
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			// 设置字段可以访问
			field.setAccessible(true);
			// 获得字段类型
			Class fieldType = field.getType();
			try {
				// 如果字段的值为null不添加到insert语句中
				if (field.get(obj) == null) {
					continue;
				}
				// 获得根据字段名称获得数据库对应的字段名称
				String columnName = null;
				columnName = getColumnName(field.getName());
				if (columnName == null) {
					continue;
				}
				// 主键跳出修改增加
				if (id.toLowerCase().equals(columnName.toLowerCase())) {
					fieldId = field;
					continue;
				}
				if (java.lang.String.class == fieldType) {
					if (((String) field.get(obj)).trim().equals("")) // 如果为空字符串
					{
						continue;
					}
					sb.append(columnName);
					values.append("'" + field.get(obj) + "'");
				} else if (Integer.class == fieldType || int.class == fieldType) {
					// 如果字段的值为0跳过
					// if(((Integer)field.get(obj)).intValue()==0)
					// {
					// continue;
					// }
					sb.append(columnName);
					values.append(field.get(obj));
				} else if (double.class == fieldType
						|| Double.class == fieldType) {
					// 如果字段的值为0跳过
					// if(((Double)field.get(obj)).doubleValue()==0)
					// {
					// continue;
					// }
					sb.append(columnName);
					values.append(field.get(obj));
				} else if (Date.class == fieldType) {
					// 格式化时间对象
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					// 格式化时间
					String datestr = sdf.format((Date) field.get(obj));
					String dialect = "OracleDialect";
					// 如果是Oracle需要使用Oracle转换时间的转换
					if (dialect.equals("OracleDialect")) {
						sb.append(columnName);
						values.append("to_date('" + datestr
								+ "','yyyy-mm-dd HH24:MI:SS')");
						// 如果是Sql server和MySql直接像字符串一样操作即可
					} else if (dialect.equals("SQLServerDialect")
							|| dialect.equals("MySQLDialect")) {
						sb.append(columnName);
						values.append("'" + datestr + "'");
					}
				}
				sb.append(",");
				values.append(",");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		// 去掉最后一个逗号
		sb.deleteCharAt(sb.toString().length() - 1);
		values.deleteCharAt(values.toString().length() - 1);
		sb.append(") values ( ").append(values).append(")");
		return sb.toString();
	}

	private static String getColumnName(String name) {
		String name1 = name;
		for (int i = 0; i < name.toCharArray().length; i++) {
			String str = String.valueOf(name.charAt(i));
			if (str.matches("[A-Z]")) {
				name1 = name1.replace(str, "_" + str.toLowerCase());
			}
		}
		return name1;

	}

	public void exeBatchSQL(List<String> sqllist) throws SQLException {
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		int count = 1000;
		for (int i = 0; i < sqllist.size(); i++) {
			String sql = sqllist.get(i);
			stmt.addBatch(sql);// 连续添加多条静态SQL
			if ((i + 1) % count == 0) {
				stmt.executeBatch();
				conn.commit();
			}
		}
		stmt.executeBatch();
		conn.commit();
		stmt.close();
		conn.setAutoCommit(true);
	}

	public void exeBatchSQL(String sql, List<String> list) throws SQLException {
		conn.setAutoCommit(false);
		PreparedStatement ps = conn.prepareStatement(sql);
		int count = 1000;
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			ps.setString(0, str);
			ps.addBatch();
			if ((i + 1) % count == 0) {
				ps.executeBatch();
				conn.commit();
			}
		}
		ps.executeBatch();
		conn.commit();
		ps.close();
	}
	
	public int[] insertBatch(List<String> sqllist, int batchSize){
		int counter = 0;
	    int pointer = 0;
	    int size = sqllist.size();
	    int[] result = new int[size];
	    Statement st;
		try {
			st = conn.createStatement();
			 for (int i = 0; i < size; i++) {
			      st.addBatch((String)sqllist.get(i));
			      counter++; 
			      if (counter >= batchSize) {
			        counter = 0;
			        int[] r = st.executeBatch();
			        conn.commit();
			        for (int k = 0; k < r.length; k++)
			          result[(pointer++)] = r[k];
			      }
			    }
			    int[] r = st.executeBatch();
			    conn.commit();
			    for (int k = 0; k < r.length; k++)
			      result[(pointer++)] = r[k];
			    st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return null;
		}finally{
			clearInstance();
		}
	    return result;
		
	}
	public int[] insertBatch(List<String> sqllist, int batchSize, int startindex){
		int counter = 0;
	    int pointer = 0;
	    int size = sqllist.size();
	    int[] result = new int[startindex+batchSize];
	    Statement st;
		try {
			st = conn.createStatement();
			 for (int i = startindex; i < startindex+batchSize; i++) {
			      st.addBatch((String)sqllist.get(i));
			      counter++; 
			      if (counter >= (startindex+batchSize)) {
			        counter = 0;
			        int[] r = st.executeBatch();
			        conn.commit();
			        for (int k = 0; k < r.length; k++)
			          result[(pointer++)] = r[k];
			      }
			    }
			    int[] r = st.executeBatch();
			    conn.commit();
			    for (int k = 0; k < r.length; k++)
			      result[(pointer++)] = r[k];
			    st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return null;
		}finally{
			clearInstance();
		}
	    return result;
		
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getColumnName("userNameOrUserId"));

	}
}
