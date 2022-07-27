package cn.com.elasticjob.constants;

/**
 * 全局常量定义
 * @author caijinpeng
 * @date 2020/3/11 0:19
 * @Version 1.0
 */
public class GlobalConstant {


    /**
     * application.properties定义KEY
     */
    public static final String CONFIG_SERVER_PORT_KEY = "server.port";

    /**
     * 默认服务端口
     */
    public static final int CONFIG_SERVER_PORT_DEFAULT_VALUE = 45220;



    // 资源类型(中文) 资源类型(英文) 父类类名 父类类标题 外部数据源名称 属性（英文） 属性（中文） 属性（中文描述）
    // "属性类别(PM、CM、ParentNode：不存在的父类资源，只配置前4列即可)"
    // 继承属性英文名（属性值放入继承属性中，可为空） KPI_ID KPI分组(中文) KPI分组(英文) 属性单位
    // KPI_NO(值为正整数，可以为空，为空时自动生成)
    // "CM属性字段长度（值为正整数，可以为空）默认：50，即 VARCHAR(50)"
    // 指标是否隐藏：true，false，默认：false
    public static final String NULL = "null";
    public static final String ROOT = "root";
    public static final String POINT = ".";
    public static final String IP = "ip";
    public static final String USERNAME = "userName";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String PORT = "port";
    public static final String WINDOWS = "windows";
    public static final String MAC = "mac";
    public static final String XHX = "_";
    public static final String XH = "*";
    public static final String ZHX = "-";
    public static final String XG = "/";
    public static final String MH = ":";
    public static final String DH = ",";
    public static final String JHHTTP = "%2B";
    public static final String AMP = "&amp;";
    public static final String AND = "＆";
    public static final String JH = "＃";
    public static final String DYH = "'";
    public static final String STOP = "stop";
    public static final String DEL = "del";
    public static final String START = "start";
    public static final String STARTC = "启动";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String ADD = "add";
    public static final String MYSQL = "mysql";




}

