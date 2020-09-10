package com.algorithm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.BaseFragment;
import com.meituan.R;

import java.util.ArrayList;

/**
 * Created by Edwin,CHEN on 2020/8/11.
 */

public class AlgorithmFragment extends BaseFragment {

	private TextView reverseListTv;
	private TextView reverseListDisplayTv;

	public static BaseFragment createFragment(){
		return new AlgorithmFragment();
	}

	@Override
	public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@android.support.annotation.Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.algorithm_fragment, container, false);
		reverseListTv = view.findViewById(R.id.reverse_list_text_view);
		reverseListTv.setOnClickListener(listener);
		reverseListDisplayTv = view.findViewById(R.id.reverse_list_display_text_view);
		return view;
	}

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.reverse_list_text_view:
					handleReverseList();
					break;
				default:
					break;
			}
		}
	};

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

		if (reverseListDisplayTv != null) {
			reverseListDisplayTv.setText(
					"originNodes: " + originNodes + "\n" +
					"reverseNodes: " + listNodes + "\n" +
			        "recurveNodes: " + recurveNodes + "\n" +
			        "tripleNodes: " + tripleNodes);
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

			node1.next = node2;
			node2.next = node3;
			node3.next = node4;
			node4.next = node5;
			node5.next = node6;
			node6.next = null;

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
	}

}
