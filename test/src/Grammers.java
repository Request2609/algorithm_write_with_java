import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grammers {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in) ;
        ArrayList<String> ls = new ArrayList<String>();
        while(true) {
            String tmp = input.nextLine();
            Pattern p = Pattern.compile("\\s*|\t|\r|\n|&nbsp;");
            Matcher m = p.matcher(tmp) ;
            tmp = m.replaceAll("") ;
            if(tmp.equals("#")) break;
            ls.add(tmp) ;
        }
        ArrayList<String>tmp = ls ;
        First fir = new First() ;
        fir.getFirst(ls) ;
        ls = tmp ;


    }
}

class Judge {
    //是否为终结符号
    public static boolean isVn(char c) {
        //代表非终结符号
        if(c >='A' && c <= 'Z') {
            return true ;
        }
        return false ;
    }
    public static boolean systaxRight(ArrayList<String>ls) {
        for(String ss : ls){
            if(ss.indexOf("->") == -1) {
                return false ;
            }
        }
        return true ;
    }
}

class Follow {
    Map<Character, Set<String>>setMap ;
    public void init(ArrayList<String>ls) {
        setMap = new HashMap<>() ;
        //先确定开始符号
        for(int i=0; i< ls.size(); i++) {
            Set<String> set = new HashSet<String>() ;
            String ss = ls.get(i) ;
            char tmp = ss.charAt(0) ;
            if(i == 0) {
                set.add("#") ;
                getOther(tmp, set, ls) ;
            }
            setMap.put(tmp, set) ;
        }
    }
    public void getOther(char a, Set<String>set, ArrayList<String>ls) {
        for (int i = 1; i < ls.size(); i++) {
            String s = ls.get(i) ;
            int index = s.indexOf(a) ;
            if(index == -1) {
                continue ;
            }
            else {
                //把后面的元素也加入到集合中
                set.add()
            }
        }
    }
}
class First {
    HashMap<Character, Set<String>>setMap ;
    //找终结符号开头的文法
    public ArrayList<String> findVtFirst(ArrayList<String> ls) {
        ArrayList<String>tmp = null;
        if(!Judge.systaxRight(ls)) {
            return tmp ;
        }
        tmp= new ArrayList<String>() ;
        for(int i=0; i<ls.size(); i++) {
            String s = ls.get(i) ;
            if(!Judge.isVn(s.charAt(s.indexOf("->")+2))) {
                tmp.add(s) ;
                ls.remove(s) ;
                i-- ;
            }
        }
        return tmp ;
    }
    //获取部分费终结符号的first集和
    public  void getFirst(ArrayList<String>ls) {
        ArrayList<String>tmp = findVtFirst(ls) ;
        if(tmp == null) {
            return;
        }
       setMap = new HashMap<Character, Set<String>>() ;
        //先找第一个是终结符号的first集合
        for(int i=0; i<ls.size(); i++) {
            Set<String>ss = new HashSet<String>() ;
            //初始化非终结符号的键值
            setMap.put(ls.get(i).charAt(0), ss) ;
        }
        getFirstEasy(tmp, setMap) ;
        getFirstHard(0 ,ls, setMap);
        for(Map.Entry<Character, Set<String>> oo :setMap.entrySet()) {
            Set<String>set = oo.getValue();
            System.out.println("非终结符号:"+oo.getKey());
            System.out.printf("first集合:");
            for(String jj : set) {
                System.out.printf(jj+"   ");
            }
            System.out.println();
        }

    }

    public void getFirstHard(int i , ArrayList<String>ls, HashMap<Character, Set<String>> setMap){
         String ss = ls.get(i) ;
         int index = ss.indexOf("->") ;
         char model = ss.charAt(index+2) ;
         Set<String>set = setMap.get(model) ;
         if(set.isEmpty()) {
             getFirstHard(i + 1, ls, setMap);
         }
         set = setMap.get(model) ;
         setMap.get(ss.charAt(0)).addAll(set) ;
    }

    public void getFirstEasy(ArrayList<String>ls, HashMap<Character, Set<String>> setMap) {
        String ss = null;
        int index = 0 ;

        for(int i=0; i<ls.size(); i++) {
            Set<String> set = new HashSet<String>() ;
            ss= ls.get(i) ;
            char key = ss.charAt(0) ;
            index = ss.indexOf("->")+2 ;
            ss = ss.substring(index) ;
            for(int j=0; j<ss.length(); j++) {
                char aa = ss.charAt(j) ;
                if(aa != '|' && !Judge.isVn(aa)) {
                    set.add(aa+"") ;
                }
                else {
                     aa =  ss.charAt(ss.length()-1) ;
                     if(!Judge.isVn(aa)) {
                         set.add(aa+"") ;
                     }
                     break ;
                }
            }
            setMap.put(key, set) ;
        }
        //遍历结果
    }

    public HashMap<Character, Set<String>>getFirstMap() {
        return setMap ;
    }
}