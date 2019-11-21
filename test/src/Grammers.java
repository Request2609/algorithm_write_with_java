
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
        ArrayList<String> tmp = new ArrayList<>(ls);
        First fir = new First();
        fir.findVtFirst(ls) ;
        return ;
     /*       fir.getFirst(ls);
        ls = tmp;
        start = ""+ls.get(0).charAt(0) ;
        Follow foll = new Follow();
        foll.init(ls, fir.getFirstMap());
        TipList tls = new TipList(fir.getFirstMap(), foll.getFollowMap(), ss, start, ls);
        System.out.println("follow集合:");
        print(foll.getFollowMap());
        System.out.println("first集合:");
        print(fir.getFirstMap());
        tls.process();
        String sss = input.next();
        tls.analy(sss);*/
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
    Map<Character, Set<String>> follMap;
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
            int flag = 0 ;
            for (char s : arr) {
                //在first集合中找到了
                if(sss.indexOf("e") != -1) flag = 1 ;
                String tmp = new String(sss) ;
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
                }else if (flag == 1&&find(start, s, 0)) {
                    value = sss.charAt(0) + "->" + "e";
                    System.out.printf("%15s", value);
                    yuceList.put(start + "+" + s, value);
                } else {
                    value = "Error";
                    System.out.printf("%15s", "Error");
                    yuceList.put(start + "+" + s, value);
                }
                flag = 0 ;
                sss = tmp ;
            }
            int index = value.indexOf("|");
            if (index != -1) {
                value = value.substring(0, index);
            }
            System.out.println();
        }
    }
    public String reverse(String ss) {
        int end = ss.length()-1 ;
        String aa = new String() ;
        for(int i=ss.length()-1; i>=0; i--) {
            aa += ss.charAt(i);
        }
        return aa ;
    }

    public void analy(String ss) {
        ss = reverse(ss)+"#" ;
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
                    return  ;
                } else {
                    //将相应的文法产生式加到栈中
                    pushToStack(wenFa, str);
                    System.out.printf("%20s%20s\n", ss, str);
                    printStack(wenFa);
                    top = wenFa.pop();
                }
                if(top.equals("i")) {
                    System.out.printf("%20s%20s\n", ss, "i") ;
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
                    if(top.equals(""+a)) {
                        System.out.printf("%20s%20s\n", ss, strs);
                    }
                }
                ss = ss.substring(1);
                continue;
            }
        }
        if(wenFa.size() ==0) {
            System.out.println("文法分析成功!");
            return ;
        }
        System.out.println("文法分析失败~~~");
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
        for (int i = str.length()-1; i >= 0; i--) {
            if(str.charAt(i) == 'e') continue ;
            s.push(str.charAt(i) + "");
        }
    }

    public void printLst() {
        for (Map.Entry<String, String> s : yuceList.entrySet()) {
            System.out.println(s.getKey() + "      " + s.getValue());
        }
    }

    public void getInfo(Stack<String> st, String aa) {
        int index = aa.indexOf("e");
        if (index != -1) return;
        for (int i = 0; i < aa.length(); i++) {
            st.push(aa.charAt(i) + "");
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
        if (flag == 1){
            ss = firstMap.get(key);
        }
        else {
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

    public Map<Character, Set<String>> getFollowMap() {
        return setMap;
    }

    public void init(ArrayList<String> ls, Map<Character, Set<String>> firstMap) {
        setMap = new HashMap<>();
        Set<String> set = null;
        Set<String> set1 = null;
        //先确定开始符号
        for (int i = 0; i < ls.size(); i++) {
            set = new HashSet<String>();
            String ss = ls.get(i);
            //获取非终结符号
            char tmp = ss.charAt(0);
            if (i == 0) {
                set.add("#");
                //将当前字符串集合传进getOther
                getOther(tmp, set, ls);
                set1 = new HashSet<String>(set);
            }
            this.firstMap = firstMap;
            setMap.put(tmp, set);
        }
        searchOther(setMap, ls, 0);
        for (String s : ls) {
            char a = s.charAt(0);
            setMap.get(a).addAll(set1);
        }
        setMap.get('F').add("+");
    }

    //找递归包含自身的表达式
    void searchOther(Map<Character, Set<String>> setmap, ArrayList<String> ls, int i) {
        //获取当前字符串
        if (i >= ls.size()) return;
        String ss = ls.get(i);
        //判断当前字符串最后是否为A->xA形式
        char a = ss.charAt(0);
        char end = ss.charAt(ss.length() - 1);
        //判断最后一个字符是非终结符号
        boolean res = Judge.isVn(end);
        //先将所有
        //X->aY形式的X的follow集合加入到Y中
        if (res) {
            if (end != a) {
                setmap.get(end).addAll(setmap.get(a));
                searchOther(setmap, ls, i + 1);
            }
        }
        if (end == 'e') {
            //是个消除做递归文法在产生式中找自己
            ss = ss.substring(1);
            int index = ss.indexOf(a);
            if (index == -1) {
                return;
            }
            char cc = ss.charAt(index - 1);
            if (Judge.isVn(cc)) {
                Set<String> set = firstMap.get(ss.charAt(index));
                for (String s : set) {
                    if (!s.equals("e"))
                        setmap.get(cc).add(s);
                }
            }
        }
        searchOther(setmap, ls, i + 1);
    }

    public void getOther(char a, Set<String> set, ArrayList<String> ls) {
        int flag = 0;
        for (int i = 1; i < ls.size(); i++) {
            String s = ls.get(i);
            int index = s.indexOf(a);
            if (index == -1) {
                continue;
            } else {
                //把后面的元素也加入到集合中
                char c = s.charAt(index + 1);
                if (c == '|') {
                    break;
                }
                if (!Judge.isVn(c)) {
                    flag = 1;
                    set.add(c + "");
                    continue;
                } else if (Judge.isVn(c) && flag == 1) {
                    break;
                }
            }
        }
    }
}

class First {
    HashMap<Character, Set<String>> setMap;

    public String getInfo(ArrayList<String>ls, char vn) {
        for(int i=0; i < ls.size(); i++) {
            String tmp = ls.get(i) ;
            char cc = ls.get(i).charAt(0) ;
            if(cc == vn) {
                int index = ls.indexOf("->") ;
                String tt = new String() ;
                tt = tmp.substring(index+2) ;
                return tt  ;
            }
        }
        return "";
    }
    public boolean noVn(char aa, ArrayList<String>ls) {
        String ss = null ;
        for(int i=0; i<ls.size(); i++) {
            ss = ls.get(i) ;
            char tmp = ss.charAt(0) ;
            if(tmp == aa) {
                break ;
            }
        }
        return ss.indexOf("e")== -1? true:false;
    }
    public void findFirst(Set<String>set, ArrayList<String>ls, int index, String info) {
        char aa = info.charAt(index) ;
        ///(Judge.isVn(aa) && noVn(aa, ls))
        if(index >= info.length()) {
            return  ;
        }
        //先在每个产生式中找该非终结符号
        char tmp = info.charAt(index) ;
        //是个非终结符号
        int flag = 0 ;
        if(Judge.isVn(tmp)) {
            info = getInfo(ls, tmp) ;
            //判断是否含有空串
            System.out.println("--->"+info);
            findFirst(set, ls, index, info);
        }
        else {
            set.add(tmp + "");
            int j = info.indexOf("|");
            set.add(info.charAt(j + 1) + "");
            findFirst(set, ls, index+1, info);
            return ;
        }
//        flag = info.indexOf("e") ;
        //如果当前非终结符号中好友空串，还需要找下一个集合的first集合
//        if(flag != -1) {
//            findFirst(set, ls, index+1, info);
//        }
    }

    public void findFollow(ArrayList<String>ls) {
        for(int i=0; i<0; i++) {

        }
    }

    //找终结符号开头的文法
    public ArrayList<String> findVtFirst(ArrayList<String> ls) {
        ArrayList<String> tmp = null;
        if (!Judge.systaxRight(ls)) {
            return tmp;
        }
        //替换/////////////////////////////////////
        for(int i=0; i<ls.size(); i++) {
            String str = ls.get(i) ;
            Set<String>set = new HashSet<String>() ;
            char start = str.charAt(0) ;
            int index = str.indexOf("->") ;
            str = str.substring(index+2) ;
            findFirst(set, ls, 0, str) ;
            setMap.put(start, set) ;

            for(String s:set) {
                System.out.printf("%s", s);
            }
             System.out.println();
        }
        /////////////////////////////////////////////
//        tmp = new ArrayList<String>();
//        for (int i = 0; i < ls.size(); i++) {
//            String s = ls.get(i);
//            if (!Judge.isVn(s.charAt(s.indexOf("->") + 2))) {
//                tmp.add(s);
//                ls.remove(s);
//                i--;
//            }
//        }
        return tmp;
    }

    //获取部分费终结符号的first集和
    public void getFirst(ArrayList<String> ls) {
        ArrayList<String> tmp = findVtFirst(ls);
        if (tmp == null) {
            return;
        }
        setMap = new HashMap<Character, Set<String>>();
        //先找第一个是终结符号的first集合
        for (int i = 0; i < ls.size(); i++) {
            Set<String> ss = new HashSet<String>();
            //初始化非终结符号的键值
            setMap.put(ls.get(i).charAt(0), ss);
        }
        getFirstEasy(tmp, setMap);
        getFirstHard(0, ls, setMap);
    }

    public void getFirstHard(int i, ArrayList<String> ls, HashMap<Character, Set<String>> setMap) {
        String ss = ls.get(i);
        int index = ss.indexOf("->");
        char model = ss.charAt(index + 2);
        Set<String> set = setMap.get(model);
        if (set.isEmpty()) {
            getFirstHard(i + 1, ls, setMap);
        }
        set = setMap.get(model);
        setMap.get(ss.charAt(0)).addAll(set);
    }

    public void getFirstEasy(ArrayList<String> ls, HashMap<Character, Set<String>> setMap) {
        String ss = null;
        int index = 0;

        for (int i = 0; i < ls.size(); i++) {
            Set<String> set = new HashSet<String>();
            ss = ls.get(i);
            char key = ss.charAt(0);
            index = ss.indexOf("->") + 2;
            ss = ss.substring(index);
            for (int j = 0; j < ss.length(); j++) {
                char aa = ss.charAt(j);
                if (aa != '|' && !Judge.isVn(aa)) {
                    set.add(aa + "");
                } else {
                    aa = ss.charAt(ss.length() - 1);
                    if (!Judge.isVn(aa)) {
                        set.add(aa + "");
                    }
                    break;
                }
            }
            setMap.put(key, set);
        }
        //遍历结果
    }

    public HashMap<Character, Set<String>> getFirstMap() {
        return setMap;
    }
}