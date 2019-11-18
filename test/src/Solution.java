//import java.util.Stack;
//
//public class Solution {
//
//    public boolean isValid(String s) {
//        Stack<Character> ls = new Stack<Character>();
//        if (s.isEmpty()) {
//            return true;
//        }
//        int i = 0;
//        if (res(s, i)) {
//            return false;
//        }
//        ls.push(s.charAt(0));
//        while (!ls.empty() || i < s.length()) {
//            if (unRes(s, i)) {
//                ls.push(s.charAt(i));
//            }
//            if (res(s, i)) {
//                char a = ls.pop();
//                if (a != s.charAt(i)) {
//                    return false;
//                }
//            }
//            i++;
//        }
//        if (ls.empty()) {
//            return true;
//        }
//        return false;
//    }
//    public boolean res(String s, int i) {
//        return s.charAt(i) == ')' || s.charAt(i) == '}'
//                || s.charAt(i) == ']';
//    }
//
//    public boolean unRes(String s, int i) {
//        return s.charAt(i) == '[' ||
//                s.charAt(i) == '{' || s.charAt(i) == '(';
//    }
//}