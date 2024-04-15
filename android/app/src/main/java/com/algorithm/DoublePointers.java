package com.algorithm;

import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DoublePointers {

    private static final String TAG = DoublePointers.class.getSimpleName();

    public static void demo() {
        findSumNumbersDemo();

        findMaxArea();
        trapSolution();
        moveZeroDemo();
    }

    /**
     * 题目：给定一个整型数组，找出数组中任意两数之和等于某个固定值（例如，target=13）的元素组合
     */
    private static void findSumNumbersDemo() {
        int[] numbers = {3, 2, 10, 5, 6, 7, 11, 8};
        int target = 13;
        List<List<Integer>> list = twoSum(numbers, target);
        Log.d(TAG, "twoSum 双指针法组合：");
        for (List<Integer> element : list) {
            Log.d(TAG, Arrays.toString(element.toArray()));
        }
    }

    /**
     * 题目：给定一个整型数组，找出数组中任意两数之和等于某个固定值（例如，target=13）的元素组合
     * 0. 对数组进行排序
     * 1. target =  a + b,
     * 2. 给定 left、right 两个指针，分别从左右两侧轮询
     * 3. numbers[left] + numbers[right] 跟 target 比较：== 保存元素组合，< 左指针向右移动，> 右指针向左移动
     * 4. left == right 移动结束
     *
     * @param numbers
     * @param target
     * @return
     */
    private static List<List<Integer>> twoSum(int[] numbers, int target) {

        Arrays.sort(numbers);

        List<List<Integer>> resultList = new ArrayList<>();
        int left = 0;
        int right = numbers.length - 1;
        while (left < right) {
            if (target == numbers[left] + numbers[right]) {
                resultList.add(Arrays.asList(numbers[left], numbers[right]));
                left++;
                right--;
            } else if (target < numbers[left] + numbers[right]) {
                left++;
            } else {
                right--;
            }
        }

        return resultList;
    }

    /**
     * 题目：接水滴，给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
     * 输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
     * 输出：6
     * 解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。
     * 示例 2：
     * 输入：height = [4,2,0,3,2,5]
     * 输出：9
     *
     * 分析：这是一个接雨水的问题，可以使用双指针法或者栈来解决。
     */
    private static void trapSolution(){
        // Example usage:
        int[] height1 = {0,1,0,2,1,0,1,3,2,1,2,1};
        Log.d(TAG,"Example 1: " + trap(height1)); // Output: 6

        int[] height2 = {4,2,0,3,2,5};
        Log.d(TAG,"Example 2: " + trap(height2)); // Output: 9

    }

    /**
     * 该算法使用两个指针 left 和 right，以及两个变量 leftMax 和 rightMax 来迭代数组，计算接雨水的面积。迭代过程中，
     * 通过比较左右两边的高度，移动较小高度的指针，并在遇到更高的柱子时更新 leftMax 和 rightMax。最终，累计的雨水面积即为结果。
     * @param height
     * @return
     */
    private static int trap(int[]height){
        if (height == null || height.length == 0) {
            return 0;
        }

        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int result = 0;

        while (left < right) {
            if (height[left] < height[right]) {
                // move left to right
                leftMax = Math.max(leftMax, height[left]);
                result += leftMax - height[left];
                left++;
            } else {
                // move right to left
                rightMax = Math.max(rightMax, height[right]);
                result += rightMax - height[right];
                right--;
            }
        }
        return result;
    }

    /**
     * 题目：给定一个长度为 n 的整数数组 height 。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i]) 。
     * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。返回容器可以储存的最大水量。
     */
    private static void findMaxArea() {
        int[] height = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int result = maxArea(height);
        Log.d(TAG,"Max Area: " + result);
    }

    private static int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        while (left < right) {
            int h = Math.min(height[left], height[right]);
            int w = right - left;
            int area = h * w;
            maxArea = Math.max(maxArea, area);

            // 移动较小高度的指针, do while 逻辑为了提高性能
            if (height[left] < height[right]) {
//              left++;
                int oldLeft = height[left];
                do {
                    left++;
                } while (left < right && height[left] < oldLeft);
            } else {
//                right--;
                int oldRight = height[right];
                do {
                    right--;
                } while (left < right && height[right] < oldRight);
            }
        }
        return maxArea;
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
     * 分析：
     * 双指针法的思路是使用一个指针 slow 来表示当前非零元素的位置，另一个指针 fast 用于遍历数组，将非零元素
     * 移到 slow 的位置。
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
     * 该算法使用两个指针 slow 和 fast，在遍历数组时，将非零元素移动到 slow 的位置。最后，slow 右侧的元素都设为零，
     * 实现了将所有零元素移动到数组末尾的效果。
     * @param nums
     */
    private static void moveElementForZero(int[] nums) {
//        输入: nums = [0,1,0,3,12]
        if (nums == null || nums.length == 0) {
            return;
        }
        int slow = 0; // 指向当前非 0 元素
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != 0) {
                // 非零元素移到slow位置
                nums[slow] = nums[fast];
                // 如果fast不等于slow，将当前fast位置设为0
                if (fast != slow) {
                    nums[fast] = 0;
                }
                // 更新slow指针
                slow++;
            }
        }
    }
}
