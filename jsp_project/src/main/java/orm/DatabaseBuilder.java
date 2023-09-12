/*
package orm;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DatabaseBuilder {

	private static SqlSessionFactory factory;
	private static final String config = "orm/mybatisConfig.xml";
	
	static {
		try {
			factory = new SqlSessionFactoryBuilder().build(
					Resources.getResourceAsReader(config));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static SqlSessionFactory getFactory() { //공유가능하도록
		return factory;
	}
	
}
*/


package orm;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DatabaseBuilder {
	private static SqlSessionFactory factory;
	private static final String config = "orm/mybatisConfig.xml";
	
	static {//초기화블럭
		try {
			factory = new SqlSessionFactoryBuilder().build(
					Resources.getResourceAsReader(config));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static SqlSessionFactory getFactory() { //공유될수 있도록
		return factory;
	}
}
