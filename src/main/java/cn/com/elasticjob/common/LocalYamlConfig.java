package cn.com.elasticjob.common;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;


/**
 * @Title:         获取本地yaml文件配置
 * @description:
 *
 * @company        XXXXX股份有限公司
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2021年01月05日 14:54:59
 **/
public class LocalYamlConfig {
    //定义LOG的输出
    private static Logger log = LoggerFactory.getLogger(LocalYamlConfig.class);

    private static Map<String, Object> sysProp = new HashMap<>();

    /**
     * 初始化本地系统参数
     *
     * @param confile
     */
    public static Map<String, Object> initConfigProperties(String confile) {
        sysProp = new HashMap<>();

        InputStream is = null;
        URL url = null;
        try {
            // 从本地读取配置文件
            log.info("local loading1 " + confile + " ...");
            url = LocalYamlConfig.class.getClassLoader().getResource(confile);
            is = url.openStream();
        } catch (Exception ex) {
            try{
                // 从本地读取配置文件
                log.info("local loading2 " + confile + " ...");
                url = Thread.currentThread().getContextClassLoader().getResource(confile);
                is = url.openStream();
            }catch (Exception e){
                log.error("local load2 " + confile + " catch an exception! ", e);
            }
        }

        if(null==is){
            try{
                // 从本地读取配置文件
                log.info("local loading3 " + confile + " ...");
                is = new FileInputStream(confile);
            }catch (Exception ex){
                log.error("local load3 " + confile + " catch an exception! ", ex);
            }
        }

        String str = null;
        try{
            int len = -1;
            //缓冲区
            byte buf[] = new byte[128];
            //捕获内存缓冲区的数据转换为字节数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //循环读取内容,将输入流的内容放进缓冲区中
            while ((len=is.read(buf))!=-1){
                //将缓冲区内容写进输出流，0是从起始偏移量，len是指定的字符个数
                baos.write(buf,0,len);
            }
            //最终结果，将字节数组转换成字符
            str = new String(baos.toByteArray());
        }catch (Exception ex){
            log.error("local load4 " + confile + " catch an exception", ex);
        }finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException ioe) {
                // ignore
            }
        }

        try{
            if(StringUtils.isNotBlank(str)){
                str = str.replaceAll("--", "# -- ");
            }

            // 将yaml转换为prop
            Map<String, Object> map = yaml2Prop(str);
            if(null!=map){
                sysProp.putAll(map);
            }
            log.info("sysProp=" + sysProp.size());
            System.getProperties().putAll(sysProp);
            log.info("local load5 " + confile + " completely!");
        }catch (Exception ex){
            log.error("local load5 " + confile + " catch an exception", ex);
        }finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException ioe) {
                // ignore
            }
        }

        return sysProp;
    }


    /**
     * tranfer yaml file to properties
     * @param docodedValue the path of yaml file
     */
    private static Map<String, Object> yaml2Prop(String docodedValue) {
        InputStream in = new ByteArrayInputStream(docodedValue.getBytes());

        Map<String, Object> map = new LinkedHashMap<>();

        List<String> lines = new LinkedList<>();

        // DOT用于隔开key中不同的键
        final String DOT = "*";

        //layerOfArray表示当前数组是第几层数组，默认为0即没有数组；每遍历到"["就增加1
        int layerOfArray = 0;

        // inArrays的索引表示yml文件中遍历到的token位于第几层数组，而元素内容表示当前遍历到的token仍在数组内部，元素默认值为false
        boolean[] inArrays = new boolean[4];
        // arrayIndexs的索引表示yml文件中遍历到的token位于第几层数组，而元素内容表示其对应的索引，元素默认值为0
        int[] arrayIndexs = new int[4];
        // arrayCuteds的索引表示yml文件中遍历到的token位于第几层数组，而元素内容表示 含有中括号的键是否已被切去，元素默认值为false
        boolean[] arrayCuteds = new boolean[4];
        // 注意：上面3个数组，目前均初始化了4个元素值，对应0、1、2、3，表示可以解析最多3层数组嵌套；
        // 若要更多层，修改该初始值即可

        try {
            YAMLFactory yamlFactory = new YAMLFactory();
            YAMLParser parser = yamlFactory.createParser(new InputStreamReader(in, Charset.forName("utf-8")));

            String key = ""; //这里的key是最终转换出的properties文件中key，并不是yml文件中的键值对中的键
            String value = null;
            // 先获取到基于json格式的第一个token，便于后面的遍历
            JsonToken token = parser.nextToken();
            while (token != null) {

                // 基于json格式，如果是一个对象开始(即遇到了左花括号"{"时)
                if (JsonToken.START_OBJECT.equals(token)) {
                    // do nothing
                } else if (JsonToken.FIELD_NAME.equals(token)) {   // 基于json格式，如果遇到键值对的键时

                    // 使用点"."分割每层的key
                    if (key.length() > 0) {
                        // 如果该对象在数组中，并且key包含中括号的数量不等于 当前所在数组的层次时，则添加上数组索引
                        if (inArrays[layerOfArray] == true && containNumbers(key, "[") != layerOfArray) {
                            key = key + "[" + arrayIndexs[layerOfArray] + "]";
                        }
                        key = key + DOT;
                    }
                    key = key + parser.getCurrentName();

                    // 继续遍历下一个token
                    token = parser.nextToken();
                    /******************************************************************************************/
                    //如果遇到左中括号"["，表示数组的开始
                    if (JsonToken.START_ARRAY.equals(token)) {
                        // 进入第一层数组
                        layerOfArray++;
                        inArrays[layerOfArray] = true;

                        token = parser.nextToken();
                    }
                    /******************************************************************************************/

                    // 如果遇到子对象的开始(即"{")，则跳入下一个循环
                    if (JsonToken.START_OBJECT.equals(token)) {
                        continue;
                    }

                    /******************************************************************************************/
                    // 此时，当前token遍历到了一个键值对的值时(即到了一个分支尽头)，需要将其放入string数组中
                    value = parser.getText();
                    //如果这个值是单独被包含在中括号数组内(中括号内没有键值对 对应的键)，则key肯定还没有在相应的键上添加索引，所以这里要补上索引
                    if (inArrays[layerOfArray] == true && containNumbers(key, "[") != layerOfArray) {
                        key = key + "[" + arrayIndexs[layerOfArray] + "]";
                    }
                    lines.add(key + "=" + value);

                    /******************************************************************************************/
                    // 每当遍历完一个分支，需要将 key截断到倒数第二个键
                    int dotOffset = key.lastIndexOf(DOT);
                    if (key.length() - 1 == key.lastIndexOf("]")) {
                        arrayCuteds[layerOfArray] = true;
                    }
                    if (dotOffset > 0) {
                        key = key.substring(0, dotOffset);
                    } else {
                        // 若原key中没有"."，则key直接置为""
                        key = "";
                    }


                    // 若截断后剩下的key的最后一个键含有中括号，也就是该键的索引即将更新，则去除掉该中括号子串以便于后面添加新的索引
                    if (key.length() > 0 && key.length() - 1 == key.lastIndexOf("]")) {
                        key = key.substring(0, key.lastIndexOf("["));
                    }

                }else if (JsonToken.END_OBJECT.equals(token)) {    // 基于json格式，如果是一个对象结束(即遇到了右花括号"}"时)

                    // 如果当前token在数组内部，则不需要截断
                    if (inArrays[layerOfArray]) {
                        arrayIndexs[layerOfArray]++;
                    } else {
                        int dotOffset = key.lastIndexOf(DOT);
                        if (dotOffset > 0) {
                            // 若原key中还有"."，则截断到倒数第二个键
                            key = key.substring(0, dotOffset);
                        } else {
                            // 若原key中没有"."，则key直接置为""
                            key = "";
                        }
                    }

                } else if (JsonToken.END_ARRAY.equals(token)) {  //如果遇到右中括号"]"，表示数组的结束
                    // 若当前层次中 含有中括号的键未被切去
                    if (!arrayCuteds[layerOfArray]) {
                        int dotOffset = key.lastIndexOf(DOT);
                        if (dotOffset > 0) {
                            // 若原key中还有"."，则截断到倒数第二个键
                            key = key.substring(0, dotOffset);
                        } else {
                            // 若原key中没有"."，则key直接置为""
                            key = "";
                        }
                    }

                    // 重置该层的变量
                    inArrays[layerOfArray] = false;
                    arrayIndexs[layerOfArray] = 0;
                    arrayCuteds[layerOfArray] = false;

                    // 回退到上一层
                    layerOfArray--;

                    // 若截断后剩下的key的最后一个键含有中括号，也就是上一层中 该键的索引即将更新，则去除掉该中括号子串 以便于后面添加新的索引
                    if (key.length() > 0 && key.length() - 1 == key.lastIndexOf("]")) {
                        key = key.substring(0, key.lastIndexOf("["));
                    }
                }
                token = parser.nextToken();
            }
            parser.close();

            // 将String字符串数组中的内容打印出来
            for (String line : lines) {
                line = line.replace(DOT, ".");
                //System.out.println(line);

                String[] array = line.split("=");
                map.put(array[0],array[1]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    /**
     * 计算 字符串str中包含多少个 模式串s
     * @param str 主字符串
     * @param s 模式串
     * @return
     */
    private static int containNumbers(String str, String s) {
        int count = 0;
        for(int i = 0; i < str.length(); ){
            int c = -1;
            c = str.indexOf(s);
            if (c != -1){
                str = str.substring(c + 1);
                count ++;
            } else {
                break;
            }
        }
        return count;
    }

    

}
