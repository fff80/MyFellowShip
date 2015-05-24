package com.hdos.dao;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.ast.QueryTranslatorImpl;

import com.hdos.utils.HibernateSessionFactory;
import com.hdos.utils.PageBean;

/**
 * 通用Dao<br>
 * 当非查询操作时,必须启用事务,且需捕捉错误
 * 
 * @author song.hu
 * @date 2012-3-21
 * @param
 * @return
 */
public class BaseDAO<T> extends BaseHibernateDAO {
	@SuppressWarnings("unchecked")
	protected Class entityType;// 所指类的class

	@SuppressWarnings("unchecked")
	public BaseDAO(Class entityType) {
		this.entityType = entityType;
	}

	/**
	 * 更新或保存
	 * 
	 * @param entity
	 * @return
	 */
	public T save(T entity) {
		getSession().saveOrUpdate(entity);
		getSession().flush();
		return entity;
	}
	/**
	 * 删除
	 * 
	 * @param entity
	 * @return
	 */
	public T delete(T entity) {
		getSession().delete(entity);
		getSession().flush();
		return entity;
	}

	/**
	 * 只做保存(wwj)
	 * 
	 * @param entity
	 * @return
	 */
	public T onlySave(T entity) {
		getSession().save(entity);
		getSession().flush();
		return entity;
	}

	/**
	 * 只更新数据(wwj)
	 * 
	 * @param entity
	 * @return
	 */
	public T onlyUpadte(T entity) {
		getSession().update(entity);
		getSession().flush();
		return entity;
	}

	public T merge(T entity) {// 整合,当session中有多个相同id对象的时候的时候
		getSession().merge(entity);
		getSession().flush();
		return entity;
	}

	/**
	 * 根据id获取对象<br>
	 * 当未找到对应对象,返回null
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(Integer index) {
		return (T) getSession().get(entityType, index);
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	protected Object get(Integer index, Class tClass) {
		return getSession().get(tClass, index);
	}

	protected void remove(Integer index) {
		getSession().delete(get(index));
		getSession().flush();
	}

	protected void remove(T entity) {
		getSession().delete(entity);
	}

	protected void evict(T entity) {
		getSession().evict(entity);
	}

	/**
	 * 根据map修改数据<br>
	 * 不推荐使用
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected int Update(final Map context, final Map condition) {
		String cn = entityType.getName();
		StringBuffer hql = new StringBuffer("UPDATE " + cn + " SET ");
		Object[] para = new Object[context.size() + condition.size()];
		int i = 0;
		Iterator itr = context.keySet().iterator();
		while (itr.hasNext()) {
			String k = itr.next().toString();
			para[i] = context.get(k);
			if (i > 0 && i < context.size()) {
				hql.append(",");
			}
			hql.append(k + "=?");
			i++;
		}
		hql.append(" WHERE ");
		itr = condition.keySet().iterator();
		i = 0;
		while (itr.hasNext()) {
			String k = itr.next().toString();
			para[i + context.size()] = condition.get(k);
			if (i > 0 && i < condition.size()) {
				hql.append(" AND ");
			}
			hql.append(k + "=?");
			i++;
		}
		return UpleteHql(hql.toString(), para);
	}

	/**
	 * 执行修改、删除的HQL<br>
	 * 返回受影响的记录数
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	protected int UpleteHql(String hql, Object[] args) {
		Query query = getSession().createQuery(hql.toString());
		if (args != null)
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		int i = query.executeUpdate();
		getSession().clear();// 清除缓存,防止修改数据后的脏读现象
		return i;
	}

	/**
	 * 根据HQL查询
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByHql(String hql, Object[] args) {
		Query query = getSession().createQuery(hql.toString());
		if (args != null)
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		return query.list();
	}

	/**
	 * 根据HQL分页查询
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByHql(String hql, Object[] args, int startSize, int pageSize) {
		Query query = getSession().createQuery(hql);
		query.setFirstResult(startSize).setMaxResults(pageSize);
		if (args != null)
			for (int i = 0; i < args.length; i++)
				query.setParameter(i, args[i]);
		return query.list();
	}

	/**
	 * 根据HQL分页查询
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	//this.findByHql(hql, args, new PageBean(ipage, 1));
	protected PageBean findByHql(String hql, Object[] args, PageBean page) {
		// 去除 order by 提高效率
		String UPhql = hql.toUpperCase();
		int endIndex = UPhql.indexOf(" ORDER BY ");
		if (UPhql.lastIndexOf(" ORDER BY ") != endIndex) {
			endIndex = -1;
		}
		String queryString = endIndex != -1 ? hql.substring(0, endIndex) : hql;
		// 转译 HQL 为 SQL
		QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(queryString, queryString, Collections.EMPTY_MAP, (SessionFactoryImplementor) HibernateSessionFactory.getSessionFactory());
		queryTranslator.compile(Collections.EMPTY_MAP, false);
		String sql = (new StringBuilder("select count(*) from (")).append(queryTranslator.getSQLString()).append(") tmp_count_t").toString();
		int TotalSize = this.getCountBySQL(sql, args);
		page.setTotalSize(TotalSize);// 设置总记录数
		List<T> list = findByHql(hql, args, page.getOffsetResult(), page.getPageSize());
		page.setResultlist(list);
		return page;
	}

	/**
	 * hql查询结果最多只能有一条记录返回<br>
	 * 当无记录返回,该方法返回值为null
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T findUnique(String hql, Object[] args) {
		Query query = getSession().createQuery(hql);
		if (args != null) {
			for (int i = 0; i < args.length; i++)
				query.setParameter(i, args[i]);
		}
		return (T) query.uniqueResult();
	}

	/**
	 * 查询必须返回一条数据,且只有一个int类型的字段
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	protected long getCountByHql(final String hql, final Object[] args) {
		Query query = getSession().createQuery(hql);
		if (args != null) {
			for (int i = 0; i < args.length; i++)
				query.setParameter(i, args[i]);
		}
		return (Long) query.uniqueResult();
	}

	/**
	 * 根据SQL分页查询
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	@SuppressWarnings( { "unchecked", "deprecation" })
	private List<T> findBySql(String sql, Object args[], int firstResult, int pageSize, String cols) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if (cols != null && !"".equals(cols)) {
			String[] col = cols.split(",");
			for (String tcol : col)
				query.addScalar(tcol, Hibernate.STRING);
		}
		if (pageSize > 0)
			query.setFirstResult(firstResult).setMaxResults(pageSize);
		if (args != null) {
			for (int i = 0; i < args.length; i++)
				query.setParameter(i, args[i]);
		}
		return query.list();
	}

	/**
	 * 执行SQL语句
	 * 
	 * @author song.hu
	 * @date 2012-5-3
	 * @param
	 * @return
	 */
	protected int execSQL(String sql, Object[] args) {
		Query query = getSession().createSQLQuery(sql);
		if (args != null) {
			for (int i = 0; i < args.length; i++)
				query.setParameter(i, args[i]);
		}
		return query.executeUpdate();
	}

	/**
	 * 查询必须返回一条数据,且只有一个int类型的字段
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	private int getCountBySQL(final String sql, final Object[] args) {
		Query query = getSession().createSQLQuery(sql);
		if (args != null) {
			for (int i = 0; i < args.length; i++)
				query.setParameter(i, args[i]);
		}
		return Integer.valueOf(query.uniqueResult().toString());
	}

	/**
	 * 根据SQL查询
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List findBySql(String sql, Object[] args) {
		return this.findBySql(sql, args, 0, 0, null);
	}

	@SuppressWarnings("unchecked")
	protected List findBySql(String sql, Object[] args, String cols) {
		return this.findBySql(sql, args, 0, 0, cols);
	}

	protected PageBean findBySql(String sql, Object[] args, PageBean page) {
		return this.findBySql(sql, args, page, null);
	}

	/**
	 * 根据SQL分页查询
	 * 
	 * @author song.hu
	 * @date 2012-3-21
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected PageBean findBySql(String sql, Object[] args, PageBean page, String cols) {
		String tsql = sql.toLowerCase();
		// 去掉ORDER BY
		int edex = tsql.lastIndexOf("order by");
		if (edex != -1)
			tsql = "select count(*) from (" + sql.substring(0, edex) + ") tmp_count_t";
		int totalSize = this.getCountBySQL(tsql, args);
		page.setTotalSize(totalSize);
		List list = this.findBySql(sql, args, page.getOffsetResult(), page.getPageSize(), cols);
		page.setResultlist(list);
		return page;
	}

	/**
	 * 查找一个到多个字段值的数组(author:wenwj)
	 * 
	 * @param hql
	 * @param args hql语句中的问号占位符的值
	 * @return Object
	 */
	protected Object findField(String hql, Object[] args) {
		Query query = getSession().createQuery(hql);
		if (args != null) {
			for (int i = 0; i < args.length; i++)
				query.setParameter(i, args[i]);
		}
		return (Object) query.uniqueResult();
	}

	/**
	 * 查找一个到多个字段值的集合数组(author:wenwj)
	 * 
	 * @param hql
	 * @param args
	 * @return
	 */
	protected Object findAllFields(String hql, Object[] args) {
		Query query = getSession().createQuery(hql);
		if (args != null) {
			for (int i = 0; i < args.length; i++)
				query.setParameter(i, args[i]);
		}
		return query.list();
	}
}