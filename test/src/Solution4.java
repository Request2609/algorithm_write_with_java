public class Solution4 {
    public static String multiply(String num1, String num2) {
        int len1 = num1.length();
        int len2 = num2.length();
        String sss = new String();
        String sss_tmp = new String();
        for (int i = len1 - 1; i >= 0; i--) {
            char cc = num1.charAt(i);
            int cur_wei = 0;
            int curcor = 0;
            int tmp = 0;
            for (int j = 0; j < len2; j++) {
                char aa = num2.charAt(j);
                //转换成整数
                int aa_int = aa - 48;
                tmp = (cc - 48) * aa_int;
                if (tmp > 10) {
                    cur_wei = tmp % 10 + curcor;
                    if (cur_wei > 10) {
                        curcor = cur_wei / 10;
                    } else {
                        curcor = 0;
                    }
                    curcor += tmp / 10;
                } else {
                    cur_wei = tmp;
                }
                sss += cur_wei + "";
            }
            System.out.println("乘积值---------------->"+sss);
            sss = reverse(sss);
            sss_tmp = addString(sss_tmp, sss, len1 - i -1);
        }
        return sss_tmp ;
    }

    public static String reverse(String sss) {
        int len = sss.length();
        String ss = new String();
        for (int i = len - 1; i >= 0; i--) {
            ss += sss.charAt(i);
        }
        return ss;
    }

    //sss还是颠倒的
    public static String addString(String sss_tmp, String sss, int index) {
        int len1 = sss_tmp.length();
        int len2 = sss.length();
        if (len1 == 0) return sss;
        //补0
        String res = new String() ;
        //补位
        for(int i=0; i<index; i++) {
            res+=sss_tmp.charAt(i) ;
        }
        int index1 = index ;
        int index2 = 0 ;
        int cord = 0 ;
        //外层循环控制被加数的偏移量sss_tmp
        while(index1 < len1&&index2 < len2) {
            char t1 = sss_tmp.charAt(index1) ;
            char t2 = sss_tmp.charAt(index2) ;
            int a = cord + (t1-48)+(t2-48) ;
            if(a > 10) {
                cord = a/10 ;
                a = a%10 ;
            }
            else {
                cord = 0 ;
            }
            index1++ ;
            index2++ ;
            res += a ;
        }
        while(index1 < len1) {
            res += sss_tmp.charAt(index1) ;
            index1 ++ ;
        }
        while(index2<len2) {
            res += sss_tmp.charAt(index2) ;
            index2++ ;
        }
        //转置一下
        res = reverse(res) ;
        return res ;
    }


    public static void main(String[] args) {
        System.out.println(multiply("123", "456"));
    }
}
