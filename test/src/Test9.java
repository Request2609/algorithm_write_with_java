import java.lang.reflect.Array;
import java.util.*;

public class Test9 {
    ArrayList<String>tmp ;
    public static void getVN(ArrayList<String>ls, ArrayList<String>tmp) {
            int len = ls.size() ;
            for(int i=0; i<len; i++) {
                String sss = ls.get(i) ;
                int len1 = sss.length() ;
                for(int j=0; j<len1; j++) {
                    if(isVn(sss.charAt(j))) {
                        tmp.add(""+sss.charAt(j)) ;
                    }
                }
            }
    }

    public static boolean isVn(char c) {
        //代表非终结符号
        if (c >= 'A' && c <= 'Z') {
            return true;
        }
        return false;
    }

    public static void test(ArrayList<String> ls) {

    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in) ;
        ArrayList<String>ls  = new ArrayList<String>() ;
        ArrayList<String>tmp = new ArrayList<>() ;
        while(true) {
            String ss = new String() ;
            ss = input.next();
            if(ss.equals("#")) {
                break;
            }
            ls.add(ss) ;
        }
        //提取公共因子

    //       getVN(ls, tmp) ;

    }
    //获取左公共因子
    public static void getLeft(ArrayList<String>ls) {
        Map<String, String>map = new HashMap<>() ;
        Set<String>set = new HashSet<>() ;
        String zuo = new String() ;
        String yin = new String() ;
        int len = ls.size() ;
        int tip = 0 ;
        for(int i=0; i<len; i++) {
            String ss = ls.get(i) ;
            //获取当前的开始推导符号
            char start = ss.charAt(0);
            //找推导的目标文法式
            int index = ss.indexOf("->") ;
            ss = ss.substring(index+2) ;
            int slen = ss.length() ;
            //标记是否已经遇到过非终结符号
            int flag = 0 ;
            int hasVn = 0 ;
            for(int j=0; j<slen; j++) {
                char cur = ss.charAt(j) ;
                //是非终结符号
                if(isVn(cur)&& cur != '|') {
                    flag = 1 ;
                }
                if(flag == 0 && cur != '|'&&!isVn(cur) && i== 0) {
                    yin+=cur ;
                }
                if(cur == '|') {
                    flag = 2 ;
                    hasVn = 0 ;
                }
                //说明已经出现过|，这时就不要往set集合中加元素了
                if(flag == 2) {
                    //|后面遇到了非终结符号
                    if(isVn(cur)) {
                        hasVn = 1 ;
                    }
                    if(!isVn(cur)&& hasVn != 1) {
                        //判断是否存在最左公因子
                        if(hasLefts(yin, cur)) {
                            yin+=cur ;
                        }
                    }
                }
            }
        }
    }
    public static boolean hasLefts(String set, char cur) {
        return set.indexOf(cur) == -1 ?false:true ;
    }
}
