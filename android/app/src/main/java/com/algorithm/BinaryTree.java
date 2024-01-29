package com.algorithm;

import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree {

    private static final String TAG = BinaryTree.class.getSimpleName();

    public static void binaryTreeDemo() {
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
}
