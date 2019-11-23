import java.util.ArrayList;
import java.util.List;

class Grammar {
    private String left;
    private String right;
    private List<String> lefts;
    private List<String> rights;
    private int id;
    private static int ID = 0;

    public Grammar() {
        super();
        id = ID++;
        lefts = new ArrayList<String>();
        rights = new ArrayList<String>();
    }

    public Grammar(String left, String right) {
        super();
        this.left = left;
        this.right = right;
        lefts = new ArrayList<String>();
        rights = new ArrayList<String>();
        id = ID++;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left.replace(" ", "");
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right.replace(" ", "");
    }

    public int getId() {
        return id;
    }

    public List<String> getLefts() {
        return lefts;
    }

    public void setLefts(List<String> lefts) {
        this.lefts = lefts;
    }

    public List<String> getRights() {
        return rights;
    }

    public void setRights(List<String> rights) {
        this.rights = rights;
    }

    public void createTaken() {
        this.getLefts().add(this.getLeft());
        String right = this.getRight();
        if (right.equals("epsilon")) {
            this.getRights().add(right);
        } else {
            while (right.contains("epsilon")) {
                right = right.replaceFirst("epsilon", "#");
            }
            if (right.length() == 1) {
                this.getRights().add(right);
            } else {
                for (int i = 0; i < right.length(); i++) {
                    if ((i + 1 < right.length())
                            && String.valueOf(right.charAt(i + 1)).equals("'")) {
                        this.getRights().add(right.charAt(i) + "'");
                    } else {
                        if (!(String.valueOf(right.charAt(i)).equals("'"))) {
                            if (String.valueOf(right.charAt(i)).equals("#")) {
                                this.getRights().add("epsilon");
                            } else {
                                this.getRights().add(
                                        String.valueOf(right.charAt(i)));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Grammar [left=" + left + ", right=" + right + "]";
    }

}