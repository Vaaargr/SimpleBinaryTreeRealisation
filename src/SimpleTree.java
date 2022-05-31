import java.util.ArrayList;

public class SimpleTree {
    private class Entry {
        private String elementName;
        private Entry parent;
        private Entry leftSibling;
        private Entry rightSibling;
        private boolean availableToAddLeftSibling;
        private boolean availableToAddRightSibling;

        public Entry(String elementName) {
            this.elementName = elementName;
            this.availableToAddLeftSibling = true;
            this.availableToAddRightSibling = true;
        }

        public Entry getParent() {
            return parent;
        }

        public String getElementName() {
            return elementName;
        }

        public void setParent(Entry parent) {
            this.parent = parent;
        }

        public Entry getLeftSibling() {
            return leftSibling;
        }

        public void setLeftSibling(Entry leftSibling) {
            this.leftSibling = leftSibling;
        }

        public Entry getRightSibling() {
            return rightSibling;
        }

        public void setRightSibling(Entry rightSibling) {
            this.rightSibling = rightSibling;
        }

        public boolean canAddSiblings() {
            return availableToAddLeftSibling || availableToAddRightSibling;
        }

        public boolean addSibling(Entry sibling) {
            if (this.availableToAddLeftSibling) {
                setLeftSibling(sibling);
                sibling.setParent(this);
                this.availableToAddLeftSibling = false;
                return true;
            } else if (this.availableToAddRightSibling) {
                setRightSibling(sibling);
                sibling.setParent(this);
                this.availableToAddRightSibling = false;
                return true;
            } else return false;
        }

        public boolean removeSibling(String siblingName) {
            if (this.getLeftSibling() != null && siblingName.equals(this.getLeftSibling().getElementName())) {
                this.setLeftSibling(null);
                this.availableToAddLeftSibling = true;
                return true;
            } else if (this.getRightSibling() != null && siblingName.equals(this.getRightSibling().getElementName())) {
                this.setRightSibling(null);
                this.availableToAddRightSibling = true;
                return true;
            } else return false;
        }

        public ArrayList<Entry> getSiblings() {
            ArrayList<Entry> siblings = new ArrayList<>();
            if (this.getLeftSibling() != null) siblings.add(this.getLeftSibling());
            if (this.getRightSibling() != null) siblings.add(this.getRightSibling());
            return siblings;
        }
    }

    private final Entry root;
    private int size;

    public SimpleTree() {
        this.root = new Entry("root");
        this.size = 0;
    }

    public void add(String elementName) {
        if (elementName == null) {
            throw new NullPointerException("Попытка добавить пустой элемент.");
        }
        Entry sibling = new Entry(elementName);
        boolean marker = false;
        if (root.canAddSiblings()) {
            marker = root.addSibling(sibling);
        } else {
            marker = addSibling(root.getSiblings(), sibling, 2);
        }
        if (marker) {
            System.out.println("Элемент " + elementName + " был добавлен.");
        } else {
            System.out.println("Элемент " + elementName + " не может быть добавлен.");
        }
    }

    private boolean addSibling(ArrayList<Entry> siblings, Entry sibling, int nextLevelSize) {
        if (siblings.size() == 0) {
            throw new NullPointerException("Нет доступных мест для нового элемента.");
        }
        ArrayList<Entry> nextLevelSiblings = new ArrayList<>((int) Math.pow(2, nextLevelSize));
        for (Entry current : siblings) {
            if (current.canAddSiblings()) {
                return current.addSibling(sibling);
            } else {
                nextLevelSiblings.addAll(current.getSiblings());
            }
        }
        return addSibling(nextLevelSiblings, sibling, ++nextLevelSize);
    }

    public void remove(String elementName) {
        Entry current = getElementByName(root.getSiblings(), elementName, 2);
        if (current != null && current.getParent().removeSibling(elementName)){
            System.out.println("Элемент " + elementName + " был удалён из дерева.");
        } else {
            System.out.println("Элемент " + elementName + " не найден.");
        }
    }

    public void getParent(String elementName){
        Entry element = getElementByName(root.getSiblings(), elementName, 2);
        if (element == null){
            System.out.println("Элемент " + elementName + " не найден.");
        } else {
            System.out.println("Предок элемента " + elementName + " - " + element.getParent().getElementName());
        }
    }

    private Entry getElementByName(ArrayList<Entry> siblings, String elementName, int nextLevelSize){
        if (siblings.size() == 0){
            return null;
        }
        ArrayList<Entry> nextLevelSiblings = new ArrayList<>((int) Math.pow(2, nextLevelSize));
        for (Entry current: siblings) {
            if (current.getElementName().equals(elementName)){
                return current;
            } else {
                nextLevelSiblings.addAll(current.getSiblings());
            }
        }
        return getElementByName(nextLevelSiblings, elementName, ++nextLevelSize);
    }

    public void printTree(){
        printAllElements(root.getSiblings(), 2);
    }

    private void printAllElements(ArrayList<Entry> siblings, int nextLevelSize){
        if (siblings.size() > 0){
            ArrayList<Entry> nextLevelSiblings = new ArrayList<>((int) Math.pow(2, nextLevelSize));
            for (Entry current: siblings) {
                StringBuilder builder = new StringBuilder("Элемент "
                        + current.getElementName()
                        + " предок "
                        + current.getParent().getElementName());
                if (current.getLeftSibling() == null && current.getRightSibling() == null){
                    builder.append(" потомков нет");
                } else {
                    if (current.getLeftSibling() != null){
                        builder.append(" левый потомок ").append(current.getLeftSibling().getElementName());
                    }
                    if (current.getRightSibling() != null){
                        builder.append(" правый потомок ").append(current.getRightSibling().getElementName());
                    }
                }
                builder.append(".");
                System.out.println(builder);
                nextLevelSiblings.addAll(current.getSiblings());
            }
            printAllElements(nextLevelSiblings, ++nextLevelSize);
        }
    }

    public int size(){
        this.size = 0;
        count(root.getSiblings(), 2);
        return size;
    }

    private void count(ArrayList<Entry> siblings, int nextLevelSize){
        if (siblings.size() > 0){
            ArrayList<Entry> nextLevelSiblings = new ArrayList<>((int) Math.pow(2, nextLevelSize));
            for (Entry current: siblings) {
                if (current != null){
                    size++;
                    nextLevelSiblings.addAll(current.getSiblings());
                }
            }
            count(nextLevelSiblings, ++nextLevelSize);
        }
    }

    public void findPathToElement(String elementName){
        Entry element = getElementByName(root.getSiblings(), elementName, 2);
        if (element == null){
            System.out.println("Элемент " + elementName + " не найден.");
        } else {
            System.out.println("Путь к элементу " + elementName + " - " + pathToElement(element));
        }
    }

    private String pathToElement(Entry element){
        if (element.getParent() == null){
            return element.getElementName();
        } else {
            return pathToElement(element.getParent()) + "\\" + element.getElementName();
        }
    }
}
