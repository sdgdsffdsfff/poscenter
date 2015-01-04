package mmboa.util;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class RandomUtil {
    public static Random rand = new Random(new Date().getTime());
    static int seq = 0;
    
    // 在访问频繁的时候可以假冒随机数
    public static int seqInt() {
    	if(seq > 100000000) {
    		seq = 1;
    		return 1;
    	} else {
    		return ++seq;
    	}
    }
    
    public static int seqInt(int i) {
    	return seqInt() % i;
    }
    
    public static int[] nextInts(int c, int n){
    	int[] r = new int[c];
    	for(int i = 0;i < c;i++)
    		r[i] = nextInt(n);
        return r;
    }
    
    public static int[] nextInts(int c, int from, int to){
    	int[] r = new int[c];
    	for(int i = 0;i < c;i++)
    		r[i] = nextInt(from, to);
        return r;
    }
    
    public static int nextInt(int i){
        return rand.nextInt(i);
    }
    
    public static int nextInt(int from, int to){
        return rand.nextInt(to - from) + from;
    }
    
    //macq_2006_12_29_随机数返回不包含零_start
    public static int nextIntNoZero(int i){
        return rand.nextInt(i)+1;
    }
    //macq_2006_12_29_随机数返回不包含零_end
    
    public static boolean percentRandom(int n) {
    	return nextInt(100) < n;
    }
    
    public static Object randomObject(List list) {
    	if(list.size() == 1)
    		return list.get(0); 
    	else
    		return list.get(nextInt(list.size()));
    }
    
    
    /**
     * 构建加权随机的数组
     * @return 总加权
     */
    public static int sumRate(int[] src) {
    	int sum = 0;
    	for(int i = 0;i < src.length;i++) {
    		sum += src[i];
    		src[i] = sum;
    	}
    	return sum;
    }
    
    /**
     * 加权随机
     * @return 随机
     */
    public static int randomRateInt(int[] dest, int total) {
    	total = nextInt(total);
    	for(int i = 0;i < dest.length;i++) {
    		if(total < dest[i])
    			return i;
    	}
    	return 0;	// 应该不能运行到这里
    }
    // 没有预先计算的数据直接运算
    public static int randomRateIntDirect(int[] src) {
    	int total = 0;
    	for(int i = 0;i < src.length;i++) {
    		total += src[i];
    	}
    	total = nextInt(total);
    	for(int i = 0;i < src.length - 1;i++) {
    		total -= src[i];
    		if(total < 0)
    			return i;
    		
    	}
    	return src.length - 1;
    }
}
