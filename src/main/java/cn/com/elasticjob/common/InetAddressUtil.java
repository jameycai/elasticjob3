package cn.com.elasticjob.common;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * @Title:         获取IP地址公用类
 * @description:
 *
 * @company        XXXXX股份有限公司
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2021年01月05日 14:54:59
 **/
public class InetAddressUtil {
	//定义LOG的输出
	private static final Logger logger = LoggerFactory.getLogger(InetAddressUtil.class);

	private static String localHostHame = null;

	private static String localIp = null;

	private static String bindIp_fromFile = null;


	/**
	 * 获取本地主机名称
	 *
	 * @author caijinpeng
	 * @return
	 */
	public static String getLocalHostName() {
		try {
			InetAddress inetAddr=InetAddress.getLocalHost();
			localHostHame =inetAddr.getHostName();
		} catch (Exception e2) {
			logger.error("localHostHame error!!", e2);
			e2.printStackTrace();
		}
		return localHostHame;
	}

	/**
	 * 获取本地Ip地址
	 *
	 * @author caijinpeng
	 * @return
	 */
	public static String getLocalIp() {
		try{
			if(StringUtils.isEmpty(localIp) ){ //如果localIp为空

				try {
					// 优先从System中读取server.address
					localIp = ConvertUtil.Obj2Str(System.getProperty("server.address"),"");
					if(StringUtils.isBlank(localIp)) {
						//如果环境变量中没有server.address重新读取文件
						localIp = readBindIp();
					}else {
						logger.info("InetAddressUtil read bind ip=" + localIp + " from file[application.properties] key[server.address]");
						return localIp;
					}
				}catch(Exception ex) {
				}


				// 如果没有绑定ip，再通过网卡进行读取
				if(StringUtils.isBlank(localIp) || localIp.trim().equals("127.0.0.1")) {
					List<String> ips = getHostIpAddresss();
					if(null!=ips && ips.size()>0){
						localIp = ips.get(0);
					}

					if(StringUtils.isEmpty(localIp)){
						try {
							localIp = InetAddress.getLocalHost().getHostAddress();
						} catch (UnknownHostException e) {
							logger.error("getLocalIp error!!", e);
							e.printStackTrace();
						}
					}

				}
			}
			logger.info("getLocalIp get ip={} ", localIp);
		}catch (Exception ex){
			logger.error("getLocalIp error!", ex);
		}
	    return localIp;
	}

	/**
	 * 获取本地多网卡Ip地址
	 *
	 * @author caijinpeng
	 * @return
	 */
	public static List<String> getLocalMulticastIps(){
		return getHostIpAddresss();
	}

	/**
	 * 获取Host的Ip地址
	 *
	 * @author caijinpeng
	 * @return
	 */
	private static List<String> getHostIpAddresss(){
		List<String> result = new ArrayList<>();
		try{
		    // 获取 Linux的Ip地址
			//logger.info("current os is linux");
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ipAddress = null;
			String ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				if(null==ni || ni.getInetAddresses() == null || ni.isLoopback()) {
					continue;
				}

				String interfaceName = ni.getName();
				//System.out.println("== 1 ==>>> " + interfaceName);
				if(null==interfaceName || interfaceName.trim().length()==0
						|| interfaceName.trim().equals("lo")
						///|| interfaceName.trim().contains("wlan")
						|| interfaceName.trim().contains("ppp")
						|| interfaceName.trim().contains("docker")
						|| interfaceName.trim().contains("veth")
						|| interfaceName.trim().contains("virbr")) {
					continue;
				}


				Enumeration<InetAddress> ipEm = ni.getInetAddresses();
				while(ipEm.hasMoreElements()){
					ipAddress = ipEm.nextElement();
					//logger.info("current ipaddress "+ipAddress.toString());

					if (!ipAddress.isAnyLocalAddress()  // 当IP地址是通配符地址时
							&& !ipAddress.isLoopbackAddress() //  当IP地址是loopback地址
							&& !ipAddress.isLinkLocalAddress() // 当IP地址是本地连接地址（LinkLocalAddress）时
							&& !ipAddress.isMulticastAddress() // 当IP地址是广播地址（MulticastAddress）时
							&& !ipAddress.isMCGlobal()  // 当IP地址是全球范围的广播地址时
							&& !ipAddress.isMCLinkLocal() // 当IP地址是子网广播地址时
							&& !ipAddress.isMCNodeLocal() // 当IP地址是本地接口广播地址时
							&& !ipAddress.isMCOrgLocal() // 当IP地址是组织范围的广播地址时
							&& !ipAddress.isMCSiteLocal() // 当IP地址是站点范围的广播地址时
							&& ipAddress.getHostAddress().indexOf(":") == -1) {

						ip = ipAddress.getHostAddress();
						logger.info("ipAddress getHostAddress:"+ip + ", networkName: "+ipAddress+", interfaceName: "+interfaceName);

						if(null==ip || ip.trim().length()==0 || ip.equals("127.0.0.1") || ip.equals("192.168.56.1") ){
							continue;
						}
						//防止有的项目中，所有虚拟机存在一个共同地址，例如北京Bomc的192.168.122.1地址
						//可以统一在cmc的system.properties中进行地址过滤wsat.needFilterIp=192.168.122.1
						if(ip.equalsIgnoreCase(System.getProperty("wsat.needFilterIp"))) {
							continue;
						}

						if(!ipAddress.isReachable(6000)){
							continue;
						}

						// 判断是否为合法ip
						if(isIPv4Address(ip) || isIPv6Address(ip)){
							logger.info("InetAddressUtil host get ip="+ip);
							result.add(ip);
						}

					}
				}
			}
		}catch(Exception ex){
			logger.info("getHostIp catch an exception",ex);
		}

		return result;

	}



    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

	/**
     * 是否为一个IPV4地址
     *
     * @param input
     * @return
     */
    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

	/**
     * 是否为一个IPV6地址
     *
     * @param input
     * @return
     */
    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }

    private static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    private static boolean isIPv6HexCompressedAddress(final String input) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }




    //十进制ip地址转化为长整型（59.225.0.0-->1004601344L）
    public static long stringIpToLong(String ip) {
        String[] ips = ip.split("\\.");
        long num =  16777216L*Long.parseLong(ips[0]) + 65536L*Long.parseLong(ips[1]) + 256*Long.parseLong(ips[2]) + Long.parseLong(ips[3]);
        return num;
    }

    //长整型转化为十进制ip地址（1004601344L-->59.225.0.0）
    public static String longIpToString(long ipLong) {
        long mask[] = {0x000000FF,0x0000FF00,0x00FF0000,0xFF000000};
        long num = 0;
        StringBuffer ipInfo = new StringBuffer();
        for(int i=0;i<4;i++){
            num = (ipLong & mask[i])>>(i*8);
            if(i>0) {
            	ipInfo.insert(0,".");
            }
            ipInfo.insert(0,Long.toString(num,10));
        }
        return ipInfo.toString();
    }

    //根据IP和子网掩码获取IP网段
    public static String getIPStartAndEnd(String ip,String mask){
        long s1 = stringIpToLong(ip);
        long s2 = stringIpToLong(mask);
        String erj = Long.toBinaryString(s2);
        long s3 = s1 & s2;
        long start = stringIpToLong(longIpToString(s3));
        String wl = Long.toBinaryString(s3);
        if(wl.length()<32) {
            int le = 32-wl.length();
            for(int i=0; i<le; i++) {
                wl = "0"+wl;
            }
        }
        String gbl = wl.substring(0,erj.indexOf("0"))+wl.substring(erj.indexOf("0"),wl.length()).replace("0", "1");
        long s4 = Long.parseLong(gbl, 2);
        long end = stringIpToLong(longIpToString(s4));
        return start+"|"+end;
    }


    /**
     * 从application.properties 获取 server.address属性
     * @return
     */
    public static String readBindIp() {
    	if(bindIp_fromFile != null) {
    		//防止多次读文件,不等于null时,直接返回
    		return bindIp_fromFile;
    	}

		try {
//			InputStream inputStream = InetAddressUtil.class.getClassLoader().getResourceAsStream("application.properties");
//          Properties properties = new Properties();
//          properties.load(inputStream);

			Map<String, Object> properties = LocalYamlConfig.initConfigProperties("application.yml");
			if(null!=properties) {
				bindIp_fromFile = ConvertUtil.Obj2Str(properties.get("spring.cloud.consul.discovery.hostname"), "");
				if(StringUtils.isNotBlank(bindIp_fromFile)) {
					System.setProperty("server.address", bindIp_fromFile);
				}


				String needFilterIp = ConvertUtil.Obj2Str(properties.get("wsat.needFilterIp"), "");
				if(StringUtils.isNotBlank(needFilterIp)) {
					System.setProperty("wsat.needFilterIp", needFilterIp);
				}
			}
		}catch(Exception e) {
			///logger.warn("Load bindIp from server file[application.properties] exception!", e.getMessage());
		}

		if(bindIp_fromFile == null) {
			bindIp_fromFile = "";
		}
		return bindIp_fromFile;
	}


    public static void main(String[]arg) {
//        System.out.println(getIPStartAndEnd("59.224.192.0", "255.255.192.0"));
//        System.out.println(stringIpToLong("1.12.0.0"));
//        System.out.println(longIpToString(1000L));
//        System.out.println(longIpToString(1000000L));
    	System.out.println(getLocalHostName());
    	System.out.println(getLocalIp());

    }





}
