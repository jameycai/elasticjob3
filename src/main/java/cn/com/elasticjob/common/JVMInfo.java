package cn.com.elasticjob.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.management.VMManagement;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * jvm信息获取
 *
 * @author caijinpeng
 * @Version 1.0
 */
public class JVMInfo {

    private static final Logger log = LoggerFactory.getLogger(JVMInfo.class);

    private static final int CPUTIME = 500;
    private static final int PERCENT = 100;
    private static final int FAULTLENGTH = 10;
    private static String linuxVersion = "";

    /** 单例 */
    public static JVMInfo getInstance(){
        return SingletonHolder.instance;
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder{
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static JVMInfo instance = new JVMInfo();
    }

    /**
     * 私有化构造方法
     */
    private JVMInfo(){

    }

    /**
     * 获取jvm的ProcessId
     *
     * @return
     * @author caijinpeng
     * @date 2019/7/25 10:25
     */
    public int getJvmPid() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            VMManagement mgmt = (VMManagement) jvm.get(runtime);
            Method pidMethod = mgmt.getClass().getDeclaredMethod("getProcessId");
            pidMethod.setAccessible(true);
            int pid = (Integer) pidMethod.invoke(mgmt);
            return pid;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取jvm的ProcessId
     *
     * @return
     * @author caijinpeng
     * @date 2019/7/25 10:25
     */
    public int getJvmPid2() {
        try {
            // runtimeMXBean.getName()取得的值包括两个部分：PID和hostname，两者用@连接。
            String strPid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            int pid = ConvertUtil.Obj2int(strPid, -1);
            return pid;
        } catch (Exception e) {
            return -1;
        }
    }

    /***
     * 获取JVM内存占比
     * @author caijinpeng
     */
    public double getJvmMemoryRate() {
        double memoryRate = 0.0D;
        try {
            long freeMemory = Runtime.getRuntime().freeMemory();
            //long maxMemory = Runtime.getRuntime().maxMemory();
            long totalMemory = Runtime.getRuntime().totalMemory();
            if(totalMemory > 0L){
                memoryRate = ((totalMemory - freeMemory) * 1.0  / totalMemory) * 100;
                //保留两位小数
                memoryRate = NStringUtil.floatNumberKeepRound(String.valueOf(memoryRate), 2);
            }
        } catch (Exception ex) {
            log.warn("get jvm memory rate exception: ", ex);
        }
        return memoryRate;
    }



    /**
     * 释放资源
     */
    private void freeResource(InputStream is, InputStreamReader isr, BufferedReader br) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ioe) {
            log.warn(ioe.getMessage());
        }

        try {
            if (isr != null) {
                isr.close();
            }
        } catch (IOException ioe) {
            log.warn(ioe.getMessage());
        }

        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException ioe) {
            log.warn(ioe.getMessage());
        }
    }

    /**
     * Windows下获取应用JVM cpu利用率
     */
    private double getCpuRateForWindows() {
        try {
            String procCmd = System.getenv("windir") + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            // 睡眠500ms
            TimeUnit.MILLISECONDS.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                long totaltime = busytime + idletime;
                if(totaltime == 0L){
                    log.warn("getCpuRateForWindows busytime、idletime all zero. return 0.0");
                    return 0.0;
                }else {
                    return Double.valueOf(PERCENT * (busytime) / (busytime + idletime)).doubleValue();
                }
            } else {
                return 0.0;
            }
        } catch (Exception ex) {
            log.warn("getCpuRateForWindows exception: ", ex);
            return 0.0;
        }
    }

    /**
     * 读取CPU信息.
     */
    private long[] readCpu(Process proc) {
        long[] retn = new long[2];
        InputStreamReader ir = null;
        LineNumberReader input = null;
        try {
            proc.getOutputStream().close();
            ir = new InputStreamReader(proc.getInputStream());
            input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                //只取jvm进程,目前windows计算不准确
                if(line.toLowerCase().indexOf("java.exe") == -1){
                    continue;
                }
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption =line.substring( capidx, cmdidx - 1).trim();
                String cmd =line.substring(cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }

                try {
                    String s1 = line.substring(kmtidx, rocidx - 1).trim();
                    String s2 = line.substring(umtidx, wocidx - 1).trim();
                    if ("System Idle Process".equals(caption) || "System".equals(caption)) {
                        if (s1.length() > 0) {
                            idletime += Long.valueOf(s1).longValue();
                        }
                        if (s2.length() > 0) {
                            idletime += Long.valueOf(s2).longValue();
                        }
                        continue;
                    }
                    if (s1.length() > 0) {
                        kneltime += Long.valueOf(s1).longValue();
                    }
                    if (s2.length() > 0) {
                        usertime += Long.valueOf(s2).longValue();
                    }
                } catch(Exception ex){
                    log.warn("Analysis one line: {}, exception: ", line, ex);
                }
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ioe) {
                log.warn(ioe.getMessage());
            }

            try {
                if (ir != null) {
                    ir.close();
                }
            } catch (IOException ioe) {
                log.warn(ioe.getMessage());
            }

            try {
                if (proc != null) {
                    proc.getInputStream().close();
                    proc.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retn;
    }


    public static void main(String[] args) {
        System.out.println("jvm内存有率=" + JVMInfo.getInstance().getJvmMemoryRate());
        //System.out.println("cpu占有率=" + JVMInfo.getJVMCpuRate());
    }

}
