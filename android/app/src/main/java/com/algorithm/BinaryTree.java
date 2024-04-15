package com.algorithm;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree {

    private static final String TAG = BinaryTree.class.getSimpleName();

    public static void binaryTreeDemo() {

        miniHeapDemo();

        LinkedList<Integer> linkedList = new LinkedList<>(Arrays.asList(12, 23, 34, 123, 120, 2343, 24433, 347));
        TreeNode root = createBinaryTree(linkedList);
        preOrder(root);

        Integer[] values = new Integer[]{12, 23, 34, 123, 120, 2343, 24433, 347};
        root = createBinaryTreeWithNonRecursive(values);
        preOrderWithStack(root);

        int height = getHeight(root);
        Log.d(TAG, "height = " + height);
        getHeightWithQueue(root);
    }

    /**
     * 获得二叉树的高度
     *
     * @param treeNode
     * @return
     */
    private static int getHeight(TreeNode treeNode) {
        if (treeNode == null) {
            return 0;
        } else {
            int leftHeight = getHeight(treeNode.leftChild);
            int rightHeight = getHeight(treeNode.rightChild);
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }

    /**
     * 层序遍历获取高度
     *
     * @param root
     */
    private static void getHeightWithQueue(TreeNode root) {
        int height = 0;
        if (root != null) {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                int levelSize = queue.size();
                for (int i = 0; i < levelSize; i++) {
                    TreeNode currentNode = queue.poll();
                    if (currentNode != null) {
                        if (currentNode.leftChild != null) {
                            queue.offer(currentNode.leftChild);
                        }
                        if (currentNode.rightChild != null) {
                            queue.offer(currentNode.rightChild);
                        }
                    }
                }
                height++;
            }
        }
        Log.d(TAG, "height with queue is  " + height);
    }

    private static void preOrder(TreeNode treeNode) {
        String preOrderNodes = "preOrderWithRecursive: ";
        if (treeNode == null) {
            return;
        }
        preOrderNodes += treeNode.val + "->";
        Log.d(TAG, preOrderNodes);
        preOrder(treeNode.leftChild);
        preOrder(treeNode.rightChild);
    }

    /**
     * 依赖 stack 进行先序遍历
     * @param treeNode
     */
    private static void preOrderWithStack(TreeNode treeNode) {
        StringBuilder preOrderWithStack = new StringBuilder("preOrderWithStack: ");
        Stack<TreeNode> stack = new Stack<>();
        stack.push(treeNode);
        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();
            if (current != null) {
                preOrderWithStack.append(current.val).append("->");

                // 在处理节点时，先将右子节点压入栈中，再将左子节点压入栈中，保证了先处理左子树。
                if (current.rightChild != null) {
                    stack.push(current.rightChild);
                }
                if (current.leftChild != null) {
                    stack.push(current.leftChild);
                }
            }
        }
        Log.d(TAG, preOrderWithStack.toString());
    }

    /**
     * 这段代码是用来从一个给定的整型数组中非递归地创建一棵二叉树。这里使用的是广度优先搜索（BFS）策略，利用队列来实现。
     *
     * @param values
     * @return
     */
    private static TreeNode createBinaryTreeWithNonRecursive(Integer[] values) {
        if (values == null || values.length == 0) {
            return null;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        Integer value = values[0];
        TreeNode root = new TreeNode(value);
        queue.offer(root);
        int index = 1; // 从数组的第二个元素开始处理
        while (!queue.isEmpty() && index < values.length) {
            TreeNode treeNode = queue.poll();// 取出当前节点
            if (treeNode != null) {
                // 为当前节点创建左子节点
                if (values[index] != null) {
                    treeNode.leftChild = new TreeNode(values[index]);
                    queue.offer(treeNode.leftChild);
                }
                index++;
                // 为当前节点创建右子节点
                if (index < values.length && values[index] != null) {
                    treeNode.rightChild = new TreeNode(values[index]);
                    queue.offer(treeNode.rightChild);
                }
                index++;
            }
        }
        return root;
    }

    private static TreeNode createBinaryTree(LinkedList<Integer> inputList) {
        TreeNode node = null;
        if (inputList == null || inputList.isEmpty()) {
            return null;
        }
        Integer data = inputList.remove();
        if (data != null) {
            node = new TreeNode(data);
            node.leftChild = createBinaryTree(inputList);
            node.rightChild = createBinaryTree(inputList);
        }
        return node;
    }

    private static class TreeNode {
        TreeNode leftChild;
        TreeNode rightChild;
        int val;

        TreeNode(int val) {
            this.val = val;
        }
    }

    private static void miniHeapDemo() {
        MinHeap minHeap = new MinHeap(5);

        minHeap.insert(3);
        minHeap.insert(2);
        minHeap.insert(1);
        minHeap.insert(5);
        minHeap.insert(4);

        Log.d(TAG, "Heap size: " + minHeap.getSize());
        Log.d(TAG, "Min element: " + minHeap.peek());

        while (!minHeap.isEmpty()) {
            Log.d(TAG, "Removing min: " + minHeap.removeMin());
        }
    }

    /**
     * 最小堆是一种二叉堆，它满足以下性质：
     *
     * 1. 父节点的值小于或等于其子节点的值（最小堆的性质）。
     * 2. 是一个完全二叉树，即除了最后一层，其它层的节点都被完全填充，且最后一层的节点都靠左排列。
     * 3. 最小堆通常用于实现优先队列等数据结构，其中可以快速地访问和删除最小元素。在最小堆中，根节点始终是最小的元素。
     */
    private static class MinHeap{

        private int capacity;
        private int size;
        private int[]heap;
        public MinHeap(int capacity) {
            this.capacity = capacity;
            this.size = 0;
            this.heap = new int[capacity];
        }

        /**
         * 父结点位置
         * @param i
         * @return
         */
        private int parent(int i) {
            return (i - 1) / 2;
        }


        private int leftChild(int i) {
            return 2 * i + 1;
        }

        private int rightChild(int i) {
            return 2 * i + 2;
        }

        /**
         * 1. 插入一个元素到堆中。
         * 2. 将元素添加到堆的末尾。
         * 3. 然后通过不断向上比较元素和其父节点的值，进行交换直到满足最小堆的性质（父节点小于等于子节点）。
         * @param element
         */
        public void insert(int element) {
            if (size >= capacity) {
                throw new IllegalStateException("Heap is full");
            }
            size++;
            int index = size - 1;
            heap[index] = element;

            while (index != 0 && heap[parent(index)] > heap[index]) {
                // 当前节点与父节点交换
                swap(index, parent(index));

                index = parent(index);
            }
        }

        private void swap(int i, int j) {
            int temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
        }

        /**
         * 1. 移除堆中的最小元素。
         * 2. 将堆顶元素（最小值）与最后一个元素交换。
         * 3. 然后删除最后一个元素，并通过 minHeapify(0) 方法维护堆的性质。
         * @return
         */
        private int removeMin() {
            if (size < 0) {
                throw new IllegalStateException("Heap is empty");
            }
            if (size == 1) {
                size--;
                return heap[0];
            }
            int root = heap[0];
            heap[0] = heap[size - 1];
            size--;
            minHeapify(0);

            return root;
        }

        /**
         * 1. 从节点 i 开始向下调整堆，保持最小堆的性质。
         * 2. 比较节点 i 和其左右子节点的值，找出其中最小的节点，如果最小节点不是节点 i，则交换节点 i 和最小节点的值，
         * 然后递归调用 minHeapify
         *
         * @param i
         */
        private void minHeapify(int i) {
            int left = leftChild(i);
            int right = rightChild(i);
            int smallest = i;

            if (left < size && heap[left] < heap[i]) {
                smallest = left;
            }
            if (right < size && heap[right] < heap[smallest]) {
                smallest = right;
            }
            if (smallest != i) {
                swap(smallest, i);
                minHeapify(smallest);
            }
        }

        public int peek() {
            if (size == 0) {
                throw new IllegalStateException("Heap is empty");
            }
            return heap[0];
        }
        public boolean isEmpty() {
            return size == 0;
        }
        public int getSize() {
            return size;
        }
    }
}
