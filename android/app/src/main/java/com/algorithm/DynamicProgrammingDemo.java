package com.algorithm;

import android.util.Log;

public class DynamicProgrammingDemo {
    private static final String TAG = DynamicProgrammingDemo.class.getSimpleName();

    public static void impl() {
        maximumSubArraySum();
        longestIncreasingSubsequence();
        maxDeliciousCandiesDemo();
    }

    /**
     * 题目：小美因乐于助人的突出表现获得了老师的嘉奖。老师允许小美从一堆n个编号分别为1,2,...,n的糖果中选择任意多个糖果
     * 作为奖励（每种编号的糖果各一个），但为了防止小美一次吃太多糖果有害身体健康，老师设定了一个限制：如果选择了编号为 i
     * 的糖果，那么就不能选择编号为 i-1, i-2, i+1, i+2的四个糖果了。在小美看来，每个糖果都有一个对应的美味值，小美想让
     * 她选出的糖果的美味值之和最大！作为小美的好朋友，请你帮帮她！
     */
    private static void maxDeliciousCandiesDemo() {
        int[] candies = new int[]{3, 1, 2, 7, 10, 2, 4};
        // 输出结果
        int result = maxDeliciousValue(candies.length, candies);
        Log.d(TAG, "maxDeliciousValueDemo,candies result: " + result);
    }

    /**
     * 获取糖果的最优方案
     */
    private static int maxDeliciousValue(int n, int[] candies) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int[] dp = new int[n]; // 动态规划方程各最优值数组
        dp[0] = candies[0];
        dp[1] = Math.max(candies[0], candies[1]);
        dp[2] = Math.max(dp[1], candies[2]);

        for (int i = 3; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 3] + candies[i]); // 动态规划方程，由题目规定的获取糖果的位置决定
        }
        return dp[n - 1];
    }

    /**
     * 数组中最长上升子序列
     */

    private static void longestIncreasingSubsequence() {
        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        int result = lengthOfLIS(nums);
        Log.d(TAG,"Length of Longest Increasing Subsequence: " + result);
    }

    /**
     * 求一个数组中的最长上升子序列（Longest Increasing Subsequence，简称 LIS）是一个经典的动态规划问题。
     *
     * @param nums
     * @return
     */
    private static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        //  动态规划数组 dp 记录以每个元素结尾的最长上升子序列的长度。通过遍历数组，对于每个元素，内层循环找到比当前元素
        //  小的元素，更新以当前元素结尾的最长上升子序列的长度。最终，返回动态规划数组中的最大值即为结果。
        // 最长增序数组的动态规划方程核心是：要么包括 num[i]，要么不包括 num[i]
        int[] dp = new int[nums.length];
        dp[0] = 1; // 如果只有一个元素，长度自然就是 1
        int maxLength = 1;

        for (int i = 1; i < nums.length; i++) {
            // 从 index = 1 取子序列 subSequence = {num[0],num[1],num[2],…num[i],…}，
            int maxVal = 0;
            for (int j = 0; j < i; j++) {
                // 找到每个子序列里的最长增长序列，1-3-5-4-7，最长是 1-3-5-7，当然要从 1 开始找
                if(nums[i] > nums[j]){
                    // 说明当前子序列中还可能有更大的值，更新 maxVal
                    // 前面 j 个序列上升自序列长度是： maxVal.
                    // j < i, num[j] 要么并入序列，增加序列的长度，要么不并入序列（因为可能是降序）
                    maxVal = Math.max(maxVal, dp[j]);
                }
            }
            // i 每移动一次，就意味着最长 LIS 的长度会增加一个
            dp[i] = maxVal + 1;
            maxLength = Math.max(maxLength, dp[i]);
        }
        return maxLength;
    }

    /**
     * 连续子数组最大和
     */
    private static void maximumSubArraySum() {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int result = maxSubArray(nums);
        Log.d(TAG, "Maximum SubArray Sum: " + result);
    }

    // 在这个例子中，maxSubArray 方法接受一个整数数组 nums，并返回数组中连续子数组的最大和。使用两个变量 currentSum
    // 和 maxSum 分别表示当前累积和和全局最大和。
    //
    //通过遍历数组，对于每个元素，更新当前累积和，如果当前累积和变得比当前元素小，就从当前元素重新开始累积；同时更新全局最
    // 大和，保留历史最大值。
    //
    //在给定的示例中，数组 {-2, 1, -3, 4, -1, 2, 1, -5, 4} 的最大子数组和为 6，对应的子数组是 {4, -1, 2, 1}。
    private static int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int currentSum = nums[0];
        int maxSum = nums[0];

        for (int i = 1; i < nums.length; i++) {
            // 根据动态规划原则，最大和要么包含当前值，要么不包含当前值
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            // 找到最大值之后进行替换
            maxSum = Math.max(maxSum, currentSum);
        }
        return maxSum;
    }
}
