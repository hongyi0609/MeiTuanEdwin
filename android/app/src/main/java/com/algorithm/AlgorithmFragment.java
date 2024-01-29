package com.algorithm;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.BaseFragment;
import com.meituan.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Edwin,CHEN on 2020/8/11.
 */

public class AlgorithmFragment extends BaseFragment {

	private static final String TAG = AlgorithmFragment.class.getSimpleName();
	private TextView reverseListTv;
	private TextView reverseListDisplayTv;

	private TextView findSecondMaxElementTv;

	public static BaseFragment createFragment(){
		return new AlgorithmFragment();
	}

	@Override
	public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@androidx.annotation.Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.algorithm_fragment, container, false);
		reverseListTv = view.findViewById(R.id.linked_list_text_view);
		reverseListTv.setOnClickListener(listener);
		reverseListDisplayTv = view.findViewById(R.id.reverse_list_display_text_view);
		findSecondMaxElementTv = view.findViewById(R.id.find_second_max_element_text_view);
		findSecondMaxElementTv.setOnClickListener(listener);
		view.findViewById(R.id.binary_tree_text_view).setOnClickListener(listener);
		view.findViewById(R.id.double_pointers_text_view).setOnClickListener(listener);
		view.findViewById(R.id.dynamic_programing_text_view).setOnClickListener(listener);
		view.findViewById(R.id.array_text_view).setOnClickListener(listener);

		return view;
	}

	private View.OnClickListener listener = v -> {
		switch (v.getId()) {
			case R.id.linked_list_text_view:
				linkedListDemo();
				break;
			case R.id.find_second_max_element_text_view:
				findSecondMaxElement();
				break;
			case R.id.binary_tree_text_view:
				BinaryTree.binaryTreeDemo();
				break;
			case R.id.double_pointers_text_view:
				DoublePointers.demo();
				break;
			case R.id.dynamic_programing_text_view:
				DynamicProgrammingDemo.impl();
			default:
				ArrayDemo.impl();
				break;
		}
	};

	private void findSecondMaxElement(){
		int[] arr = {5, 10, 3, 8, 15};
		Log.d(TAG, "arr = " + Arrays.toString(arr));
		int secondLargest = findSecondLargest(arr);
		Log.d(TAG,"Second largest element: " + secondLargest);
	}

	public static int findSecondLargest(int[] arr) {
		int largest = Integer.MIN_VALUE;
		int secondLargest = Integer.MIN_VALUE;

		for (int num : arr) {
			if (num > largest) {
				secondLargest = largest;
				largest = num;
			} else if (num > secondLargest && num != largest) {
				secondLargest = num;
			}
		}

		return secondLargest;
	}

	private void linkedListDemo() {
		handleReverseList();
		Node.intersectionNodeDemo();
		Node.reverseBetweenDemo();
	}

	/**
	 * 链表反转
	 */
	private void handleReverseList() {
		Node head = Node.createNodes();
		String originNodes = Node.printNodes(head);
		// 列表反转
		Node listHead = Node.reverseListNode(head);
		String listNodes = Node.printNodes(listHead);
		// 回归反转
		Node recurveNode = Node.recurveReverseNode(listHead);
		String recurveNodes = Node.printNodes(recurveNode);

		// 三指针反转
		Node tripleNode = Node.tripleReferencesReverseNode(recurveNode);
		String tripleNodes = Node.printNodes(tripleNode);

		// 删除重复节点
		Node duplicateNode = Node.removeRepeatedElement(tripleNode);
		String removeRepeatedNodes = Node.printNodes(duplicateNode);

		if (reverseListDisplayTv != null) {
			reverseListDisplayTv.setText(
					"originNodes: " + originNodes + "\n" +
					"reverseNodes: " + listNodes + "\n" +
			        "recurveNodes: " + recurveNodes + "\n" +
			        "tripleNodes: " + tripleNodes + "\n" +
							"removeRepeatedNodes: " + removeRepeatedNodes);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private static class Node {
		String value;
		Node next;

		public Node(String value) {
			this.value = value;
		}

		static Node createNodes() {

			Node node1 = new Node("111");
			Node node2 = new Node("112");
			Node node3 = new Node("113");
			Node node4 = new Node("114");
			Node node5 = new Node("115");
			Node node6 = new Node("116");
			Node node7 = new Node("116");

			node1.next = node2;
			node2.next = node3;
			node3.next = node4;
			node4.next = node5;
			node5.next = node6;
			node6.next = node7;
			node7.next = null;

			return node1;
		}

		static String printNodes(Node head) {
			StringBuilder builder = new StringBuilder();
			while (head != null) {
				builder.append(head.value);
				builder.append("-->");
				head = head.next;
			}
			builder.append("null");
			return builder.toString();
		}

		/**
		 * 反转
		 * @param head
		 * @return
		 */
		static Node reverseListNode(Node head) {
			ArrayList<Node> list = new ArrayList<>();
			while (head != null) {
				list.add(head);
				head = head.next;
			}
			int size = list.size();
			for (int i = size - 1; i > 0; i--) {
				Node tail = list.get(i);
				Node previous = list.get(i - 1);
				tail.next = previous;
				previous.next = null;
			}
			// 反向之后，最后一个节点变成头节点
			head = list.get(size - 1);
			return head;
		}

		/**
		 * 借助递归反转
		 */
		static Node recurveReverseNode(Node head) {
			if (head == null || head.next == null) {
				return head;
			}
			// 递归调用，穷举到最后一个节点。由于链表反向，新链表以尾结点为头节点
			// 特别注意：回归传参的隐藏信息：head = head.next
			Node tail = recurveReverseNode(head.next);
			head.next.next = head; // 反转，指向前一个节点，head容易引起歧义
			head.next = null;  // 前一个节点旧的next指针没用了，置空
			return tail;
		}

		/**
		 * 三指针反向
		 * - —— - —— - ——
		 * |ref1|ref2|ref3|
		 */
		static Node tripleReferencesReverseNode(Node head) {
			if (head == null || head.next == null) {
				return head;
			}
			Node ref1 = head;
			Node ref2 = head.next;
			Node ref3;
			while (ref2 != null) {
				// 保存ref2.next至ref3
				ref3 = ref2.next;

				// 反转，ref2指向前一个节点
				ref2.next = ref1;

				// 向前移动指针
				ref1 = ref2;
				ref2 = ref3;
			}
			// 结束后将头节点的引用置空，并将ref1给头节点
			head.next = null;
			head = ref1;
			return head;
		}

		/**
		 * 一个有序链表删除重复元素
		 */

		static Node removeRepeatedElement(Node head){
			if (head == null || head.next == null) {
				return head;
			}

			Node current = head;
			while (current.next != null) {
				if(Objects.equals(current.value, current.next.value)){
					// 删除重复节点
					current.next = current.next.next;
				} else {
					// 向前移动一个节点
					current = current.next;
				}
			}
			return head;
		}

		/**
		 * 链表相交判断
		 */
		private static void intersectionNodeDemo() {
			// Example usage:
			// Construct two intersecting linked lists
			Node commonNode = new Node("7");
			Node listA = new Node("1");
			listA.next = new Node("2");
			listA.next.next = commonNode;

			Node listB = new Node("3");
			listB.next = new Node("4");
			listB.next.next = new Node("5");
			listB.next.next.next = commonNode;

			Node intersectionNode = getIntersectionNode(listA, listB);
			Log.d(TAG,"Intersection Node Value: " + (intersectionNode != null ? intersectionNode.value : "null"));
			intersectionNode = getIntersectionNodeWithList(listA, listB);
			Log.d(TAG,"With list Intersection Node Value: " + (intersectionNode != null ? intersectionNode.value : "null"));

		}

		/**
		 * 双指针寻找交叉节点: 双指针法，创建了两个指针 pointerA 和 pointerB 分别指向链表A和链表B的头部。它们同时遍历链表，
		 * 如果遇到尾部，则指向另一个链表的头部，直到找到相交的节点或者同时到达链表尾部（即不相交情况）。
		 * @param headA
		 * @param headB
		 * @return
		 */
		private static Node getIntersectionNode(Node headA, Node headB) {
			if (headA == null || headB == null) {
				return null;
			}

			Node pointerA = headA;
			Node pointerB = headB;
			while (pointerA != pointerB) {
				// 注意着两个指针，到达链表尾部后交换指向，相当于每个指针都可以最长执行 headA.length + headB.length 的
				// 长度。当然，如果有相交节点，就不会执行到尾部。
				pointerA = (pointerA == null) ? headB : pointerA.next;
				pointerB = (pointerB == null) ? headA : pointerB.next;
			}
			return pointerA;
		}

		/**
		 * 利用数组，缓存一个链表节点进入数组 list，遍历另一个链表B 确认list 中是否包含 B 链表中节点，这个容易理解就是
		 * 太耗内存
		 * @param headA
		 * @param headB
		 * @return
		 */
		private static Node getIntersectionNodeWithList(Node headA, Node headB) {
			ArrayList<Node> list = new ArrayList<>();
			while (headA != null) {
				list.add(headA);
				headA = headA.next;
			}
			while (headB != null) {
				if (list.contains(headB)) {
					return headB;
				}
				headB = headB.next;
			}
			return null;
		}

		private static void reverseBetweenDemo() {
			Node node = new Node("1");
			node.next =  new Node("2");
			node.next.next =  new Node("3");
			node.next.next.next =  new Node("4");
			node.next.next.next.next =  new Node("5");
			node.next.next.next.next.next =  new Node("6");
		}

		/**
		 * 题目：链表反转第 m 到 n 部分的子链，例如： 1->2->3->4->5->6  反转第 2 到 5 个节点后，1->5->4->3->2->6
		 * 步骤：以 1->2->3->4->5->6、1->3->2->4->5->6 为例
		 * 1. 使用三指针（prev、current、next）反转；
		 * 2. prev 要先移动到 m-1 位置；
		 * 3. 反转要切断当前节点 current(2)与后续节点（3）的关联，所以切断前先保存后续节点的指针，不然后续就接不上了。
		 * next = current.next; current.next = next.next;（把 3 摘除）
		 * 4. 将摘除的节点（3）的 next 指针指向反转部分的头节点（2），实现了反转，next.next = prev.next;
		 * 5. next （3）还得拼接到 prev 的后面（1）
		 * 6. prev 的位置不会变动，整个反转过程更像是位置更换：先摘除即将反转的节点，再将后续节点插入到 prev.next，所以
		 * @param head
		 * @param m
		 * @param n
		 * @return
		 */
		private static Node reverseBetween(Node head, int m, int n) {
			if (head == null || m >= n) {
				return head;
			}
			// dummy 节点避免了 m = 1 的情况，也不必对 head 节点进行特殊处理了
			Node dummy = new Node("0");
			dummy.next = head;

			Node prev = dummy;
			// Move to the (m-1)-th node
			for (int i = 0; i < m - 1; i++) {
				prev = prev.next;
			}
			Node current = prev.next;
			Node next = null;
			for (int i = 0; i < n - m + 1; i++) {
				next = current.next;
				current.next = next.next;
				next.next = prev.next;
				prev.next = next;
			}
			return dummy.next;
		}
	}

}
