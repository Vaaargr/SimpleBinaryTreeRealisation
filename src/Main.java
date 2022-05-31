public class Main {
    public static void main(String[] args) {
        SimpleTree simpleTree = new SimpleTree();
        for (int i = 1; i < 258; i++) {
            simpleTree.add( String.valueOf(i));
        }
        System.out.println(simpleTree.size());
        simpleTree.remove("257");
        System.out.println(simpleTree.size());
        simpleTree.remove("500");
        simpleTree.getParent("18");
        simpleTree.printTree();
        simpleTree.findPathToElement("250");

    }
}
