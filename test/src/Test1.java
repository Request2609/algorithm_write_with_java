import java.util.Scanner;
import java.util.Stack;

public class Test1 {
    public static void main(String[] args) {
        Scanner in= new Scanner(System.in) ;
        String ss = in.next() ;
        System.out.println("结果:"+isValid(ss));
    }
    public static boolean isValid(String s) {
        Stack<Character> ls = new Stack<Character>();
        if (s.isEmpty()) {
            return true;
        }
        int i = 0;
        if (res(s, i)) {
            return false;
        }
        ls.push(s.charAt(0));
        i++ ;
        int flag = 0 ;
        while (i<s.length()) {
            if (unRes(s, i)) {
                ls.push(s.charAt(i));
            }
            else{
                char a = ls.pop();
                if(ls.empty()) {
                    flag = 1 ;
                }
                char aa = s.charAt(i) ;
                if (aa == ')') {
                    if(a != '(') {
                        return false ;
                    }
                }
                if (aa == '}') {
                    if(a != '{') {
                        return false ;
                    }
                }
                if (aa == ']') {
                    if(a != '[') {
                        return false ;
                    }
                }
            }
            i++;
            if(flag == 1 && i<s.length()) {
                flag = 0 ;
                if(!unRes(s, i)) {
                    return false ;
                }
                ls.push(s.charAt(i)) ;
                i++ ;
            }
        }
        if (ls.empty() && i == s.length()) {
            return true;
        }
        return false;
    }

    public static boolean res(String s, int i) {
        return s.charAt(i) == ')' || s.charAt(i) == '}'
                || s.charAt(i) == ']';
    }
    public static boolean unRes(String s, int i) {
        return s.charAt(i) == '{' || s.charAt(i) == '('
                || s.charAt(i) == '[';
    }
}
