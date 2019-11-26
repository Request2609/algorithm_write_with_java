import java.util.*;

import static java.lang.StrictMath.max;

public class Solution2 {
    public static int max(int tmp, int tmp1) {
        return tmp>tmp1 ?tmp:tmp1 ;
    }
    public static int maxPoint(int[] prices) {
        if(prices.length <= 1) return 0 ;
//        Map<Integer, Integer> map = new HashMap<Integer, Integer>() ;
        ArrayList<Integer>list = new ArrayList<>() ;
        for(int i=0; i<prices.length-1; i++) {
             list.add(prices[i+1]-prices[i]) ;
        }
        int last = 0 ;
        int profit = last ;
        for(int i= 0; i<list.size(); i++) {
            last = max(0, last+list.get(i)) ;
            profit = max(profit, last) ;
        }
        return profit ;
    }
    public static void main(String[] args) {
        int []a = {3,2,5,1,2,7,8,9} ;
        System.out.println(maxPoint(a)) ;
    }
}
