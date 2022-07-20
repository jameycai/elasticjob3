package cn.com.elasticjob.schedule.cluster;


import cn.com.elasticjob.common.ConvertUtil;
import cn.com.elasticjob.constants.ZooKeeperConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * ZooKeeper注册
 * @author caijinpeng
 * @date 2020/4/3 1:15
 */
@Order(value = 5)
@Configuration
public class ZooKeeperRegister {
    
	private static final Logger log = LoggerFactory.getLogger(ZooKeeperRegister.class);


	private String zkConnect = "192.168.67.23:2181";

	@Value("${zookeeper.connection.timeout:60000}")
	private int connectionTimeoutMs;
	@Value(("${zookeeper.session.timeout:60000}"))
	private int sessionTimeoutMs;

	private String isUseZKSecurity = "false";
	@Value("${zookeeper.password:Wlkj@2021zbvn1aok8i}")
	private String zkPassword;


	/**
	 * ZK连接参数
	 */
	@PostConstruct
	private void init() {
	    if (StringUtils.isEmpty(zkConnect)) {
        	 log.warn("Can't get zk.connect from [/conf/application.properties or consul config], zkConnect is null.");
        }

	    if (connectionTimeoutMs < 9000) {
			connectionTimeoutMs = 60000;
	    }
	    
	    if (sessionTimeoutMs < 9000) {
			sessionTimeoutMs = 60000;
	    }

		// ZK是否加密
		this.isUseZKSecurity = ConvertUtil.Obj2Str(this.isUseZKSecurity, "true");

		// ZK密码
		zkPassword = ConvertUtil.Obj2Str(zkPassword, ZooKeeperConstant.ZK_PASSWORD);
	}


	/**
	 * 初始化zookeeper连接
	 * @throws Exception
	 * @auther caijinpeng
	 */
	public CuratorFramework getZkClient() throws Exception{
		CuratorFramework client = null;
		try {
			// 默认是全局
			String zkNameSpace = ZooKeeperConstant.ZK_NAMESPACE;
			// ZK用户名称
			String zkUser = ZooKeeperConstant.ZK_USER;

			//1000 是重试间隔时间基数，5 是重试次数
			RetryPolicy retryPolicy = new RetryNTimes(5, 1000);

//			if(StringUtils.isNotBlank(isUseZKSecurity) && "true".equalsIgnoreCase(isUseZKSecurity)){
//				//默认创建的根节点是没有做权限控制的 ---- by caijinpeng
//				ACLProvider aclProvider = new ACLProvider() {
//					private List<ACL> acl ;
//					@Override
//					public List<ACL> getDefaultAcl() {
//						if(acl ==null){
//							ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
//							acl.clear();
//							acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", zkUser+":"+zkPassword) ));
//							this.acl = acl;
//						}
//						return acl;
//					}
//					@Override
//					public List<ACL> getAclForPath(String path) {
//						return acl;
//					}
//				};
//
//				String scheme = ZooKeeperConstant.ZK_AUTHSCHEME;
//				byte[] auth = (zkUser+":"+zkPassword).getBytes();
//
//				client = CuratorFrameworkFactory.builder().aclProvider(aclProvider)
//						.authorization(scheme, auth)
//						.connectString(zkConnect.trim())
//						.retryPolicy(retryPolicy)
//						.connectionTimeoutMs(connectionTimeoutMs)
//						.sessionTimeoutMs(sessionTimeoutMs)
//						.namespace(zkNameSpace)
//						.canBeReadOnly(false)
//						.defaultData(null)
//						.build();
//
//			}else {
				client = CuratorFrameworkFactory.builder()
						.connectString(zkConnect.trim())
						.retryPolicy(retryPolicy)
						.connectionTimeoutMs(connectionTimeoutMs)
						.sessionTimeoutMs(sessionTimeoutMs)
						.namespace(zkNameSpace)
						.canBeReadOnly(false)
						.defaultData(null)
						.build();

//			}
			client.start();

		} catch(Throwable ex){
			log.error("initZookeeper error!!  request zkConnectString: "+zkConnect+", connectionTimeout: "+connectionTimeoutMs
					+", sessionTimeout: "+sessionTimeoutMs, ex);
			throw ex;
		}
		return client;
	}


    /**
     * 重新初始化zk连接
     */
    public void refreshInit(){
		init();
	}
}