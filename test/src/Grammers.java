
import javax.lang.model.type.ArrayType;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grammers {
    static Set<Character> ss = null;
    static String start;

    public static void getSuanFu(String a) {
        for (int i = 0; i < a.length(); i++) {
            char cc = a.charAt(i);
            if (cc != 'e' && cc != '-' && cc != '>' && cc != '|' && (!Judge.isVn(cc))) {
                ss.add(cc);
            }
        }
    }
    public static void main(String[] args) {
        start = new String();
        ss = new HashSet<Character>();
        Scanner input = new Scanner(System.in);
        ArrayList<String> ls = new ArrayList<String>();
        while (true) {
            String tmp = input.nextLine();
            Pattern p = Pattern.compile("\\s*|\t|\r|\n|&nbsp;");
            Matcher m = p.matcher(tmp);
            tmp = m.replaceAll("");
            if (tmp.equals("#")) break;
            getSuanFu(tmp);
            ls.add(tmp);
        }
        //消除间接左递归
        ls = Judge.removeDirectLeftRecur(ls) ;
        ArrayList<String> tmp = new ArrayList<>(ls);
        First fir = new First();
        fir.findVtFirst(ls);
        ls = tmp;
        start = "" + ls.get(0).charAt(0);
        Follow foll = new Follow();
        foll.findFollow(ls, fir.getFirstMap());

        TipList tls = new TipList(fir.getFirstMap(), foll.getFollowMap(), ss, start, ls);
        System.out.println("follow集合:");
        print(foll.getFollowMap());
        System.out.println("first集合:");
        print(fir.getFirstMap());
        tls.process();
        System.out.println("请输入测试表达式：");
        String sss = input.next();
        tls.analy(sss);
    }

    public static void print(Map<Character, Set<String>> setMap) {
        for (Map.Entry<Character, Set<String>> oo : setMap.entrySet()) {
            Set<String> set = oo.getValue();
            System.out.println("非终结符号:" + oo.getKey());
            System.out.printf("集合:");
            for (String jj : set) {
                System.out.printf(jj + "   ");
            }
            System.out.println();
        }
    }
}

class TipList {
    Map<String, String> yuceList;
    Map<Character, Set<String>>
            follMap;
    Map<Character, Set<String>> firstMap;
    Set<Character> ss;
    String start;
    ArrayList<String> list;

    public TipList(Map<Character, Set<String>> firMap, Map<Character,
            Set<String>> follMap, Set<Character> ss, String start, ArrayList<String> ls) {
        yuceList = new HashMap<>();
        this.follMap = follMap;
        this.firstMap = firMap;
        this.ss = ss;
        this.start = start;
        list = ls;
    }

    public void process() {
        ArrayList<Character> arr = new ArrayList<>();
        ss.add('#');
        System.out.printf("\n%15s", " ");
        for (char a : ss) {
            arr.add(a);
            System.out.printf("%15s", a);
        }
        //构造预测分析表
        for (int i = 0; i < list.size(); i++) {
            String sss = list.get(i);
            char start = list.get(i).charAt(0);
            System.out.printf("\n%15s", start + "");
            String value = new String();
            int flag = 0;
            for (char s : arr) {
                //在first集合中找到了
                if (sss.indexOf("e") != -1) flag = 1;
                String tmp = new String(sss);
                if (find(start, s, 1)) {
                    //查看当前的字符串是否含有空集合
                    int index = sss.indexOf('|');
                    if (index != -1 && (sss.indexOf("i") == -1)) sss = sss.substring(0, index);
                    if (isChar(s) && sss.indexOf(s) != -1) {
                        value = start + "->" + s;
                        System.out.printf("%15s", value);
                        yuceList.put(start + "+" + s, value);
                    } else {
                        value = sss;
                        index = sss.indexOf('i');
                        if (index != -1)
                            value = value.substring(0, index - 1);
                        System.out.printf("%15s", value);
                        yuceList.put(start + "+" + s, value);
                    }
                } else if (flag == 1 && find(start, s, 0)) {
                    value = sss.charAt(0) + "->" + "e";
                    System.out.printf("%15s", value);
                    yuceList.put(start + "+" + s, value);
                } else {
                    value = "Error";
                    System.out.printf("%15s", "Error");
                    yuceList.put(start + "+" + s, value);
                }
                flag = 0;
                sss = tmp;
            }
            int index = value.indexOf("|");
            if (index != -1) {
                value = value.substring(0, index);
            }
            System.out.println();
        }
    }

    public String reverse(String ss) {
        int end = ss.length() - 1;
        String aa = new String();
        for (int i = ss.length() - 1; i >= 0; i--) {
            aa += ss.charAt(i);
        }
        return aa;
    }

    public void analy(String ss) {
        ss = reverse(ss) + "#";
        Stack<String> wenFa = new Stack<String>();
        //将起始非终结符号要入栈中
        wenFa.push("#");
        System.out.println(start);
        wenFa.push(start);
        Stack<String> shi = new Stack<String>();
        //将起始非终结符号ya入栈中
        shi.push("#");
        for (int i = 0; i < ss.length(); i++) {
            shi.push("" + ss.charAt(i));
        }
        System.out.printf("%15s%15s%15s\n", "分析栈", "剩余输入串", "推倒式");
        while (!ss.isEmpty()) {
            printStack(wenFa);
            String top = wenFa.pop();
            //获取产生式的每一个字符
            char a = ss.charAt(0);

            //是操作数
            while (isChar(a) && !top.equals("i")) {
                //获取相应的文法产生式
                String str = yuceList.get(top + "+i");
                if (str == null && str.equals("Error")) {
                    System.out.println("错误表达式");
                    return;
                } else {
                    //将相应的文法产生式加到栈中
                    pushToStack(wenFa, str);
                    System.out.printf("%20s%20s\n", ss, str);
                    printStack(wenFa);
                    top = wenFa.pop();
                }
                if (top.equals("i")) {
                    System.out.printf("%20s%20s\n", ss, "i");
                }
            }
            if (top.equals("i")) {
                ss = ss.substring(1);
                continue;
            }
            //是运算符号
            if (!isChar(a)) {
                //当前弹出栈顶的元素是非终结符号
                while (Judge.isVn(top.charAt(0)) && !top.equals("" + a)) {
                    //获取当前非终结符号和运算符号映射的文法表达式
                    String strs = yuceList.get(top + "+" + a);
                    System.out.printf("%20s%20s\n", ss, strs);
                    //打印当前栈中的非终结符号
                    pushToStack(wenFa, strs);
                    printStack(wenFa);
                    top = wenFa.pop();
                    if (top.equals("" + a)) {
                        System.out.printf("%20s%20s\n", ss, strs);
                    }
                }
                ss = ss.substring(1);
                continue;
            }
        }
        if (wenFa.size() == 0) {
            System.out.println("文法分析成功!合法语句");
            return;
        }
        System.out.println("文法分析失败~~~不合法语句");
    }

    public void printStack(Stack<String> s) {
        System.out.println();
        String ss = new String();
        for (String c : s) {
            ss += c;
        }
        System.out.printf("%15s", ss);
    }

    public void pushToStack(Stack<String> s, String str) {
        int index = str.indexOf("->");
        str = str.substring(index + 2);
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) == 'e') continue;
            s.push(str.charAt(i) + "");
        }
    }


    public boolean isChar(char c) {

        if (c >= 'a' && c <= 'z') {
            return true;
        }
        return false;
    }

    public boolean find(char key, char a, int flag) {
        Set<String> ss = null;
        if (flag == 1) {
            ss = firstMap.get(key);
        } else {
            ss = follMap.get(key);
        }
        for (String c : ss) {
            if (c.charAt(0) == a) {
                return true;
            }
        }
        return false;
    }
}

class Judge {
    //检查是否为值接左递归
    public static String getA(char c, String ss) {
        int sindex = ss.indexOf("->") ;
        String str = new String() ;
        for(int i=sindex+3; i< ss.length(); i++) {
            if(ss.charAt(i) == '|') break ;
            str+=ss.charAt(i) ;
        }
        return str ;
    }

    public static ArrayList<String> removeDirectLeftRecur(ArrayList<String>ls) {
        char x = 'X' ;
        ArrayList<String>tmp = new ArrayList<>();
        System.out.println("原来的文法产生式：");
        for(String s : ls) {
            char start = s.charAt(0) ;
            System.out.println(start+"      " +s);
            int index = s.indexOf("->") ;
            if(index == -1) {
                continue ;
            }
            String cur = s ;
            s = s.substring(index) ;
            index = s.indexOf(start) ;
            if(index == -1) {
                tmp.add(cur) ;
                continue ;
            }
            //P->Pa|b    消除左递归的规则  P->bX, X->aX|e
            String a = getA(start, s) ;
            int index1 = s.indexOf('|') ;
            if(index1 == -1)  {
                tmp.add(s) ;
                continue ;
            }
            String b = s.substring(index1+1) ;
            tmp.add(start+"->"+b+x) ;
            tmp.add(x+"->"+a+x+"|"+'e') ;
            x+=1 ;
        }
        //消除左递归后
        System.out.println("消除直接左递归后的结果：") ;
        for(String s: tmp) {
            System.out.println(s) ;
        }
        return tmp ;
    }
    public String getInfo(char start, String s, int index) {
        String ss = new String() ;
        for(int i=index; i>=0; i--) {
            if(s.charAt(i) == start) {
                break ;
            }
            if(Judge.isVn(s.charAt(i))) {
                ss+=s.charAt(i) ;
            }
        }
        return ss ;
    }
    //是否为终结符号
    public static boolean isVn(char c) {
        //代表非终结符号
        if (c >= 'A' && c <= 'Z') {
            return true;
        }
        return false;
    }

    public static boolean systaxRight(ArrayList<String> ls) {
        for (String ss : ls) {
            if (ss.indexOf("->") == -1) {
                return false;
            }
        }
        return true;
    }
}

class Follow {
    Map<Character, Set<String>> setMap;
    Map<Character, Set<String>> firstMap;
    String start;
    int status;
    ArrayList<String> list;

    public Map<Character, Set<String>> getFollowMap() {
        return setMap;
    }

    public void findFollow(ArrayList<String> ls, Set<String> set, char start, int index) {
        int len = ls.size();
        if (index >= len) {
            return;
        }
        String tmp = ls.get(index);
        int tmpLen = tmp.length();
        char cur = tmp.charAt(0);
        //在集合中找start
        int tipIndex = tmp.indexOf(start);
        if (tipIndex == -1 || tipIndex == 0) {
            findFollow(ls, set, start, index + 1);
        }
        //在文法中找到了相应的非终结符号
        else {
            int i = tipIndex + 1;
            //A->aB这种情况
            if (i >= tmpLen) {
                //将当前这个文法开始符号加到list中，退出递归后将
                list.add(cur + "+" + start);
                return;
            }
            for (i = tipIndex + 1; i < tmpLen; i++) {
                char next = tmp.charAt(i);
                //是非终结符号,考虑第二种情况
                if (next == '|') break;
                //是终结符号直接加到follow表中
                if (!Judge.isVn(next)) {
                    set.add(next + "");
                }
                if (Judge.isVn(next)) {
                    //获取当前非终结符号的first集合
                    Set<String> t = firstMap.get(next);
                    if (!t.isEmpty()) {
                        addFirToFollow(t, set);
                        //第三种情况第二种A->aBc
                        if (firHasEmpty(t)) {
                            list.add(next + "+" + start);
                        }
                    }
                }
            }
        }
    }

    public void addFirToFollow(Set<String> setSrc, Set<String> setDst) {
        for (String s : setSrc) {
            if (!s.equals("e"))
                setDst.add(s);
        }
    }

    public boolean firHasEmpty(Set<String> ls) {
        for (String s : ls) {
            if (s.equals("e")) {
                return true;
            }
        }
        return false;
    }

    public void findFollow(ArrayList<String> ls, Map<Character, Set<String>> firstMap) {
        list = new ArrayList<>();
        setMap = new HashMap<>();
        Set<String> set = null;
        this.firstMap = firstMap;
        //先确定开始符号
        for (int i = 0; i < ls.size(); i++) {
            status = 0;
            set = new HashSet<String>();
            String ss = ls.get(i);
            //获取非终结符号
            char tmp = ss.charAt(0);
            if (i == 0) {
                start = tmp + "";
                set.add("#");
            }
            findFollow(ls, set, tmp, 0);
            setMap.put(tmp, set);
            if (!list.isEmpty()) {
                for (String s : list) {
                    char src = s.charAt(0);
                    char dst = s.charAt(s.length() - 1);
                    setMap.get(dst).addAll(setMap.get(src));
                }
            }
        }
    }
}

class First {
    HashMap<Character, Set<String>> setMap;

    public String getInfo(ArrayList<String> ls, char vn) {
        for (int i = 0; i < ls.size(); i++) {
            String tmp = ls.get(i);
            char cc = ls.get(i).charAt(0);
            if (cc == vn) {
                int index = tmp.indexOf("->");
                String tt = new String();
                tt = tmp.substring(index + 2);
                return tt;
            }
        }
        return "";
    }

    public boolean hasEmpty(char aa, ArrayList<String> ls) {
        String ss = null;
        for (int i = 0; i < ls.size(); i++) {
            ss = ls.get(i);
            char tmp = ss.charAt(0);
            if (tmp == aa) {
                break;
            }
        }
        return ss.indexOf("e") != -1 ? true : false;
    }

    public void findFirst(Set<String> set, ArrayList<String> ls, char start, int index, String info) {
        if (index >= info.length()) {
            return;
        }
        //先在每个产生式中找该非终结符号
        char tmp = info.charAt(index);
        //是个非终结符号
        if (tmp != '|' && Judge.isVn(tmp)) {
            info = getInfo(ls, tmp);
            //判断是否含有空串
            findFirst(set, ls, start, index, info);
            if (index < info.length()) {
                char yy = info.charAt(index + 1);
                //是当前非终结符号包含e，则需要向下再推倒
                if (Judge.isVn(yy) && hasEmpty(tmp, ls) && start != yy) {
                    findFirst(set, ls, start, index + 1, info);
                }
            }
            return;
        } else if (tmp != 'e') {
            if (tmp == '|') {
                return;
            }
            int i = 0;
            for (i = index; i < info.length(); i++) {
                char cc = info.charAt(i);
                if (!Judge.isVn(cc) && cc != '|') {
                    set.add(cc + "");
                } else {
                    break;
                }
            }
            int j = info.indexOf("|");
            if (j != -1) {
                set.add(info.charAt(j + 1) + "");
            }
            if (i >= info.length() || info.charAt(i) == start) {
                return;
            }
            //继续访问下一个是否为终结符号
            char tt = info.charAt(i);
            if (Judge.isVn(tt) && hasEmpty(tt, ls))
                findFirst(set, ls, start, index + 1, info);
            return;
        } else {
            set.add(tmp + "");
        }
    }


    //找终结符号开头的文法
    public ArrayList<String> findVtFirst(ArrayList<String> ls) {
        setMap = new HashMap<>();
        ArrayList<String> tmp = null;
        if (!Judge.systaxRight(ls)) {
            return tmp;
        }
        //替换/////////////////////////////////////
        for (int i = 0; i < ls.size(); i++) {
            String str = ls.get(i);
            Set<String> set = new HashSet<String>();
            char aa = str.charAt(0);
            int index = str.indexOf("->");
            str = str.substring(index + 2);
            findFirst(set, ls, aa, 0, str);
            setMap.put(aa, set);
        }
        return tmp;
    }

    public HashMap<Character, Set<String>> getFirstMap() {
        return setMap;
    }
}