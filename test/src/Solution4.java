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
            for (int j = len2-1; j >= 0; j--) {
                char aa = num2.charAt(j);
                //转换成整数
                int aa_int = aa - 48;
                tmp = (cc - 48) * aa_int;
                if (tmp >= 10 && j != 0) {
                    cur_wei = tmp % 10 + curcor ;
                    if (cur_wei >= 10) {
                        curcor = cur_wei/10+tmp/10;
                        cur_wei %= 10 ;
                    } else {
                        curcor = tmp/10 ;
                    }
                } else {
                    if(tmp < 10) {
                        cur_wei = tmp+curcor;
                    }
                    if(tmp >= 10) {
                        cur_wei = tmp+curcor ;
                        int wei = cur_wei ;
                        while(wei != 0) {
                            sss+=wei%10+"" ;
                            wei /=10 ;
                        }
                        break ;
                    }
                    curcor = 0 ;
                }
                sss += cur_wei + "";
            }

            sss = reverse(sss);
            sss_tmp = addString(sss_tmp, sss, len1-i-1);
            sss = "" ;
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
//        System.out.println(sss_tmp+"       "+sss);
        int len1 = sss_tmp.length();
        int len2 = sss.length();
        if (len1 == 0) return sss;
        //补0
        String res = new String() ;
        //补位
        res+=reverse(sss_tmp.substring(len1-index)) ;

        sss_tmp = sss_tmp.substring(0, len1-index) ;
//        System.out.println(sss_tmp+"      "+sss);
        int index1 = sss_tmp.length()-1 ;
        int index2 = len2-1 ;
        int cord = 0 ;
        //外层循环控制被加数的偏移量sss_tmp
        while(index2 >= 0 && index1>=0) {
            char t1 = sss_tmp.charAt(index1) ;
            char t2 = sss.charAt(index2) ;
            int a = cord + (t1-48)+(t2-48) ;
            if(a >= 10 && (index1 != 0 || index2 != 0)) {
                //记录高位的数字
                cord = a/10 ;
                //记录低位的数字
                a = a%10 ;
            }
            else {
                if(a >= 10) {
                    res+=a%10 ;
                    a = a/10 ;
                    res += a ;
                    index1-- ;
                    index2 -- ;
                    break ;
                 }
                cord = 0 ;
            }
            index1-- ;
            index2-- ;
            res += ""+a ;
        }

        int a = 0 ;
        while(index1>=0) {
            a += cord + sss_tmp.charAt(index1);
            if(a >= 10 && index1 != 0) {
                a = a%10 ;
                cord = a/10 ;
            }
            else {
                if(a >= 10) {
                    res += a%10 ;
                    res += a/10 ;
                    index1-- ;
                    index2-- ;
                    break ;
                }
            }
            res += a ;
            index1 -- ;
        }
        while(index2>=0) {
            a += cord + (sss.charAt(index2)-48);
            if(a >= 10 && index2 != 0) {
                a = a%10 ;
                cord = a/10 ;
            }
            else {
                if(a >= 10) {
                    res += a%10 ;
                    res += a/10 ;
                    break ;
                }
                cord = 0 ;
            }
            res += a ;
            index2 -- ;
        }
        //转置一下
        res = reverse(res) ;
        return res ;
    }


    public static void main(String[] args) {
        System.out.println(multiply("123456789", "987654321"));
    }
}
