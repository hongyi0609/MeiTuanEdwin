package com.algorithm;

import android.util.Log;

import java.util.Arrays;

public class ArrayDemo {
    private static final String TAG = ArrayDemo.class.getSimpleName();

    public static void impl() {
        rearrangeArrayDemo();
        moveZeroDemo();
    }

    private static void rearrangeArrayDemo() {
        int[] nums = {1, 2, 30, 4, 5, 6, 7};
        rearrangeArray(nums);
        StringBuilder builder = new StringBuilder("rearrangeArray : ");
        for (int num : nums) {
            builder.append(num).append(',');
        }
        Log.d(TAG, builder.toString());
    }
    /**
     * 题目：给一个数组将数组中的奇数放在数组前面，偶数放在数组后面，并且保持元素的相对位置不变。例如：输入{1,2,30,4,5,6,7}，
     * 输出{1,5,7,30,2,4,6}，要求空间复杂度最大是 n，数组元素的相对位置保持不变，用 Java 代码实现
     */
    private static void rearrangeArray(int[] nums){
        int len = nums.length;

        for (int i = 0; i < len; i++) {
            // 先找到数组剩余部分的第一个 Odd 的位置
            int minIndex = findMinOddIndex(nums, i, nums.length);
            // 然后把 找到的奇数保存起来，从 i 到 minIndex的元素右移
            moveElement(nums, minIndex, i);
        }
    }

    /**
     * 把 找到的奇数保存起来，从 i 到 minIndex的元素右移
     * @param nums
     * @param from
     * @param to
     */
    private static void moveElement(int[] nums, int from, int to) {
        int temp = nums[from];
        for (int i = from; i > to; i--) {
            nums[i] = nums[i-1]; // 数据左移动
        }
        nums[to] = temp;
    }

    /**
     * 找到数组剩余部分的第一个奇数
     * @param nums
     * @param start
     * @param end
     * @return
     */
    private static int findMinOddIndex(int[] nums, int start, int end) {
        int minIndex = start;
        for (int i = start; i < end; i++) {
            if (nums[i] % 2 == 1) {
                minIndex = i;
                break;
            }
        }
        return minIndex;
    }

    /** 题目：
     * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
     * 请注意 ，必须在不复制数组的情况下原地对数组进行操作。
     * 示例 1:
     *
     * 输入: nums = [0,1,0,3,12]
     * 输出: [1,3,12,0,0]
     * 示例 2:
     *
     * 输入: nums = [0]
     * 输出: [0]
     *
     */
    private static void moveZeroDemo() {
        // 示例1
        int[] nums1 = {0, 1, 0, 3, 12};
        moveElementForZero(nums1);
        Log.d(TAG,"Array Example 1: " + Arrays.toString(nums1)); // Output: [1, 3, 12, 0, 0]

        // 示例2
        int[] nums2 = {0};
        moveElementForZero(nums2);
        Log.d(TAG,"Array Example 2: " + Arrays.toString(nums2)); // Output: [0]
    }

    /**
     *
     * 这个算法使用一个非零元素的指针 nonZeroIndex，遍历数组，当遇到非零元素时，将其与 nonZeroIndex 指向的位置交换，
     * 并将 nonZeroIndex 向前移动。最终，所有非零元素都被移到数组的前面，而数组的末尾都是零。这样就实现了将所有零移动
     * 到数组末尾的目标。
     * @param nums
     *
     * 注意： DoublePointers 中的 demo 节省了一个指针的内存
     */
    private static void moveElementForZero(int[] nums) {
//        输入: nums = [11，0,1,0,3,12]
        if (nums == null || nums.length == 0) {
            return;
        }
        int nonZeroIndex = 0; // 指向当前非 0 元素
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                // 交换位置，将非 0 值移动到前面
                int temp = nums[nonZeroIndex];
                nums[nonZeroIndex] = nums[i];
                nums[i] = temp;
                nonZeroIndex++;
            } else {
                // 0 值不用动，本来就该在右边，通过nonZeroIndex 和 i 相结合，一个指向当前非 0 值，一个查询下一个非 0 值，
                // 交替将 0 值向右移动
            }
        }
    }

}
