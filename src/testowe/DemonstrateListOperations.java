package testowe;

public class DemonstrateListOperations {
    public static void main(String[] args) {
        List list = new List(1);
        for(int i=2; i<=10; i++) {
            //list.head = list.preprend(i);
            list.append(i);
        }
        System.out.println("Zwykłe:");
        list.printOut();
        list.reverse();
        System.out.println("Odwrócone:");
        list.printOut();
    }
}

class List {
    public Node head;
    public List(int value) {
        this.head = new Node(value);
    }

    public Node preprend(int value) {
        Node newNode = new Node(value);
        newNode.next = this.head;
        return newNode;
    }

    public void append(int value) {
        Node head = this.head;
        while(head.next != null) {
            head = head.next;
        }
        head.next = new Node(value);
    }

    public void printOut() {
        Node head = this.head;
        while(head != null) {
            System.out.println(head.value);
            head = head.next;
        }
    }

    public void reverse() {
        Node prev = null;
        Node cur = this.head;
        while(cur.next != null) {
            Node temp = cur.next;
            cur.next = prev;
            prev = cur;
            cur = temp;
        }
        cur.next = prev;
        this.head = cur;
    }
}

class Node {
    public int value;
    public Node next;

    public Node(int value) {
        this.value = value;
        this.next = null;
    }
}