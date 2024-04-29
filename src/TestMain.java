import java.util.Arrays;
import java.util.Stack;

public class TestMain {

    public static void main(String[] args) {
        int[] result = new int[]{73,74,75,71,69,72,76,73};
        Stack<Integer> stack = new Stack<>();

        System.out.println(Arrays.toString(dailyTemperatures(result)));


//        ListNode node4 = new ListNode(4, null);
//        ListNode node3 = new ListNode(3, node4);
//        ListNode node2 = new ListNode(2, node3);
//        ListNode node1 = new ListNode(1, node2);
//
//        System.out.println(swapPairs(node1));

    }

    public static int[] dailyTemperatures(int[] temperatures) {

        int n = temperatures.length;
        int[] result = new int[n];

        for(int i = 0; i < n; i++){
            int numDays = 1;
            for(int j = i+1; j < n; j++) {

                if(temperatures[i] > temperatures[j]) {
                    numDays++;
                } else {
                    result[i] = numDays;
                    break;
                }
            }
        }
        return result;
    }

    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }

    public static ListNode swapPairs(ListNode head) {

        if(head == null || head.next == null) return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        while(head != null) {
            ListNode currNext = head.next;
            ListNode current = head;
            if(currNext == null) break;

            current.next = current.next.next;
            currNext.next = current;
            dummy.next = currNext;

            head = currNext.next;

        }
        return dummy.next;
    }
}
