package cn.com.elasticjob.schedule.elasticjob;


import cn.com.elasticjob.common.ConvertUtil;

/**
 * 分片算法帮助类
 *
 * @author caijinpeng
 * @date 2018/12/27 10:48
 */
public class ShardingHelper {

    /**
     * 判断任务序号是否符合当前的分片
     *
     * @param taskNo  任务序号
     * @param shardingTotalCount 总分片数
     * @param currentShardingNo 当前分片序号
     * @return
     */
    public static boolean judgeShard(long taskNo, int shardingTotalCount, int currentShardingNo){
        boolean b_myFlag = false;
        if(shardingTotalCount == 0){
            return b_myFlag;
        }

        if(taskNo % shardingTotalCount == currentShardingNo){
            b_myFlag = true;
        }

        return b_myFlag;
    }

    /**
     * 获取计算后的分片序号
     *
     * @param taskNo  任务序号
     * @param shardingTotalCount 总分片数
     * @return
     */
    public static int getCalcShard(long taskNo, int shardingTotalCount){
       int calcSharding = 0;
        if(shardingTotalCount == 0){
            return calcSharding;
        }

        calcSharding = ConvertUtil.Obj2int(taskNo % shardingTotalCount);
        return calcSharding;
    }
}
