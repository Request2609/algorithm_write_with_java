import java.util.Scanner;

public class Test {
    public static void main(String[] arg) {
        Scanner input = new Scanner(System.in);
        String s1 = input.next();
        String p = input.next();
        Solution ss = new Solution();
        ss.isMatch(s1, p);
    }
}

class Solution {
    public boolean isMatch(String s, String p) {
        //递归回溯的结果点
        if(p.isEmpty()) {
            return s.isEmpty() ;
        }
        boolean first_match = (!s.isEmpty() && (p.charAt(0) == s.charAt(0) || p.charAt(0) == '.')) ;
        //如果p长度大于2并且二位是*，这一步isMatch(s, p)可以判定用到*匹配０到多个字符
        if(p.length() >= 2 && p.charAt(1) =='*') {
            return (isMatch(s, p.substring(2)) || (first_match && isMatch(s.substring(1), p))) ;
        }
        else {
            //判断第二位不是*的情况都位移判定，没回递归都只判断都一位，知道空
            return first_match && isMatch(s.substring(1), p.substring(1)) ;
        }
    }
//        if (s.length() != p.length()) {
//            return false;
//        }
//        int ss = 0;
//        int ps = 0;
//        while (ss != s.length()) {
//            if (s.charAt(ss) == p.charAt(ps) || s.charAt(ss) == '.' || p.charAt(ps) == '.') {
//                ss ++ ;
//                ps ++ ;
//                continue;
//            } else if (s.charAt(ss) == '*' || p.charAt(ps) == '*') {
//                if(s.charAt(ss) == '*' && p.charAt(ps) == '*') {
//                    ss++ ;
//                    ps ++ ;
//                    continue ;
//                }
//                if(s.charAt(ss) == '*') {
//                }
//            }
//        }
//    }
//
//    public int getInfo(char a, char b) {
//        if (a == '.' && b == '.') {
//            return 2;
//        }
//        if (a == '.')
//            return 1;
//    }

}