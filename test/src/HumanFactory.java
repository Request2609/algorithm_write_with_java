public class HumanFactory extends AbstractFactory{
    @Override
    public <T extends Human> T createHuman(Class<T> c) {
        Human human = null ;
        try {
            //产生一个人种实例
            human = (T) Class.forName(c.getName()).newInstance();
        }catch(Exception e) {
            System.out.println("人中识别错误");
        }
        return (T)human;
    }
}