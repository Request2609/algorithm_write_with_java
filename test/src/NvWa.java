public class NvWa {
    public static void main(String[] args) {
        //产生阴阳炉对象
        AbstractFactory yinyanglu = new HumanFactory() ;
        System.out.println("女娲造人");
        Human yell = yinyanglu.createHuman(YellowHuman.class) ;
        yell.getColor();
        yell.talk();
        //产生了黑人
        Human black = yinyanglu.createHuman(BlackHuman.class);
        black.getColor();
        black.talk();
    }
}
