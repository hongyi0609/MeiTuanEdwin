package com.algorithm;

import android.annotation.SuppressLint;
import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ArrayDemo {
    private static final String TAG = ArrayDemo.class.getSimpleName();

    public static void impl() {

        simulateStackWithArrayDemo();

        subarraySumDemo();

        longestConsecutiveDemo();

        groupAnagramsDemo();

        findTwoSumNumbersDemo();
        findSumDemo();

        kthLargestElement();
        rearrangeArrayDemo();
        moveZeroDemo();
        dexToBinaryDemo();
        findSecondMaxElement();
        dexToBDemo();
    }

    private static void dexToBDemo(){
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
//            String ipv4 = scanner.next();
//            Log.d(TAG, "ipv4 ="+ ipv4 + ": " + dexToB(ipv4));
//        }
        String ipv4 = "255.168.0.0";
                Log.d(TAG, "ipv4 ="+ ipv4 + ": " + dexToB(ipv4));
    }
    /**
     * 给定一个ipv4地址，比如192.168.1.0，把它转化成二进制：11000000.10100000.1.0
     * @param ip
     */
    private static String dexToB(String ip){
        if(ip == null){
            return "";
        }
        String[] fields = ip.split("\\.");
        Log.d(TAG, "fields " + fields);
        StringBuilder s = new StringBuilder();
        for (String field : fields) {
            int d = Integer.parseInt(field);
            s.append(dexToBinary(d)).append(".");
        }
        return s.substring(0, s.length() - 1);
    }

    private static void findSecondMaxElement(){
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

    private static void dexToBinaryDemo() {
        String binaryNumber100 = dexToBinary(100);
        String binaryNumber289 = dexToBinary(289);
        Log.d(TAG, "binaryNumber100 is " + binaryNumber100 +
                ",binaryNumber289 is " + binaryNumber289);
    }
    /**
     * 十进制转二进制
     * 将十进制数转换为二进制数可以使用除二取余法，具体步骤如下：
     *  1. 将给定的十进制数除以 2，得到商和余数。
     *  2. 将商继续除以 2，得到新的商和余数，直到商为 0 为止。
     *  3. 将每次得到的余数倒序排列，即为该十进制数的二进制表示。
     * @param iDex
     * @return
     */
    private static String dexToBinary(int iDex) {
        if (iDex == 0) {
            return "0";
        }
        StringBuilder binaryNumber = new StringBuilder();
        while (iDex > 0) {
            int remainder = iDex % 2;
            binaryNumber.insert(0, remainder);
            iDex = iDex / 2;
        }
        return binaryNumber.toString();
    }

    /**
     * stack 的核心思想：先入后出
     * 数组模拟栈关键点：栈顶指针控制数据的存储，存储容器是数组
     * 主要方法：构造方法、
     */
    private static void simulateStackWithArrayDemo() {
        ArrayStack stack = new ArrayStack(5);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("栈顶元素：" + stack.peek());
        System.out.println("出栈：" + stack.pop());
        System.out.println("出栈：" + stack.pop());
        System.out.println("栈是否为空：" + stack.isEmpty());
        System.out.println("出栈：" + stack.pop());
        System.out.println("栈是否为空：" + stack.isEmpty());
    }

    private static class ArrayStack {
        int[] array; // 存储容器
        int top = -1; // 栈顶指针

        /**
         * s构造方法，size 是初始大小
         * @param size
         */
        public ArrayStack(int size) {
            array = new int[size];
            top = -1; // 栈顶指针初始值-1，表述stack 为空
        }

        /**
         * 入栈
         * @param value
         */
        public void push(int value) {
            if (top == array.length - 1) {
                Log.d(TAG,"ArrayStack, 栈已满无法入栈");
                return;
            }
            array[++top] = value; // 栈顶指针先加 1 再赋值
        }

        /**
         * 出栈操作
         * @return
         */
        public int pop() {
            if (top == -1) {
                Log.d(TAG,"ArrayStack, 栈已空无法出栈");
                return -1;
            }
            return array[top--];// 返回栈顶元素，栈顶指针先减1
        }

        public int peek() {
            if (top == -1) {
                Log.d(TAG,"ArrayStack, 栈已空无法取出栈顶元素");
                return -1;
            }
            return array[top--];// 返回栈顶元素，栈顶指针先减1
        }

        public boolean isEmpty() {
            return top == -1;//
        }

        public boolean isFull() {
            return top == array.length - 1;
        }
    }

    /**
     * 和为 K 的子数组
     */
    private static void subarraySumDemo() {
        int[] nums1 = {1, 1, 1};
        int k1 = 2;
        Log.d(TAG, "和为 K 的子数组1: "+subarraySum(nums1, k1)); // Output: 2
        Log.d(TAG, "和为 K 的子数组1: "+subarraySumWithMap(nums1, k1)); // Output: 2


        int[] nums2 = {1, 2, 3};
        int k2 = 3;
        Log.d(TAG, "和为 K 的子数组1: "+subarraySum(nums2, k2)); // Output: 2
        Log.d(TAG, "和为 K 的子数组1: "+subarraySumWithMap(nums2, k2)); // Output: 2

    }

    /**
     * 给你一个整数数组 nums 和一个整数 k ，请你统计并返回 该数组中和为 k 的子数组的个数 。
     * <p>
     * 子数组是数组中元素的连续非空序列。
     * <p>
     * 示例 1：
     * <p>
     * 输入：nums = [1,1,1], k = 2
     * 输出：2
     * 示例 2：
     * <p>
     * 输入：nums = [1,2,3], k = 3
     * 输出：2
     *
     * @param nums
     * @param k
     * @return
     */
    private static int subarraySum(int[] nums, int k) {
        // 枚举法，把所有的子数组找出来，数组 sum == k 计入 count
        int count = 0;
        for (int start = 0; start < nums.length; start++) {
            int sum = 0;
            // 从完整数组逐渐减少元素数
            for (int end = start; end < nums.length; end++) {
                sum += nums[end];
                if (sum == k) {
                    // 从 start 到 end 的数组中，每出现一次 sum == k 就说明有一个子数组和为 K
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 使用 map 优化时间复杂度为 O（n）
     *
     * @param nums
     * @param k
     * @return
     */
    @SuppressLint("NewApi")
    private static int subarraySumWithMap(int[] nums, int k) {
        // 根据高三数学数列的原理，已知数列前 n项和等于 S[n] = a1 + a2 + ai + ... + an
        // 则有 ai = S[i]-S[i-1]
        // 如果 k =  ai + ai-1 + ai-2，则有 K = （S[i]-S[i-1]） + （S[i-1]-S[i-2]） + (S[i-2]-S[i-3]）
        // = S[i]-S[i-1] + S[i-1]-S[i-2] + S[i-2]-S[i-3]
        // = S[i] - S[i-3]
        // K =  S[i] - S[i-3] 的问题转换成了，前 i项和减去前（i-3）和，高中数学来也～
        // 既然 i-3 成立，那么 i-j 也必然成立，所以有通用公式：
        // K  = S[i] - S[i-j],
        // 问题就变成了两数之和的问题

        Map<Integer, Integer> map = new ArrayMap<>();
        int sum = 0; // 前 n项和初始值
        int count = 0; // sum 出现次数
        map.put(0, 1);
        for (int element : nums) {
            sum += element; // 计算数组前 n 项和

            // sum - k 相当于 S[i] - K，核对是否存在前（i-j）项和S[i-j],使得 S[i] - K = S[i-j]
            // 如果存在，有多少个组合map.get(S[i-j])，也即是map.get(sum - k) = count
            if (map.containsKey(sum - k)) {
                count += map.get(sum - k);
            }
            map.put(sum, map.getOrDefault(sum, 0) + 1);

        }
        return count;
    }

    /**
     * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
     * <p>
     * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
     */
    private static void longestConsecutiveDemo() {
        int[] nums1 = {100, 4, 200, 1, 3, 2};
        int[] nums2 = {0, 3, 7, 2, 5, 8, 4, 6, 0, 1};
        Log.d(TAG, "nums1->" + longestConsecutive(nums1)); // 输出：4
        Log.d(TAG, "nums2->" + longestConsecutive(nums2)); // 输出：9
    }

    /**
     * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
     * <p>
     * 示例 1：
     * <p>
     * 输入：nums = [100,4,200,1,3,2]
     * 输出：4
     * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
     * 示例 2：
     * <p>
     * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
     * 输出：9
     *
     * <p>要计算这个算法的时间复杂度，我们需要考虑两个方面的因素：
     * 1. 将所有元素添加到哈希表中的时间复杂度是 O(n)，其中 n 是数组的长度。
     * 2. 对于每个元素，我们最多会进入内部的 while 循环一次，内部循环的时间复杂度取决于最长的连续序列长度。由于
     * 每个数字最多会被访问两次（一次作为起点，一次作为终点），所以内部 while 循环的总时间复杂度也是 O(n)。
     * 因此，整个算法的时间复杂度是 O(n) + O(n) = O(n)。
     * </p>
     *
     * @param nums
     * @return
     */
    private static int longestConsecutive(int[] nums) {
        HashSet<Integer> numSet = new HashSet<>();
        for (Integer num : nums) {
            numSet.add(num); // 数组放入 hashSet 去重
        }

        int longestStreak = 0;

        for (int num : nums) {
            if (!numSet.contains(num - 1)) { // 前面没有连续的值，是起点啊
                int currentNum = num; // 当前序列起点
                int currentStreak = 1; // 当前序列长度
                while (numSet.contains(currentNum + 1)) { // 后面还有连续值啊
                    currentNum++;
                    currentStreak++;
                }
                longestStreak = Math.max(longestStreak, currentStreak);
            }
        }
        return longestStreak;
    }

    /**
     * 异位词
     */
    private static void groupAnagramsDemo() {
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> result = groupAnagrams(strs);
        Log.d(TAG, "异位词demo1：" + result);

        String[] strs2 = {""};
        List<List<String>> result2 = groupAnagrams(strs);
        Log.d(TAG, "异位词demo2：" + result2);
    }

    /**
     * 字母异位词 是由重新排列源单词的所有字母得到的一个新单词。
     * <p>
     * 示例 1:
     * <p>输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]<p/>
     * <p>输出: [["bat"],["nat","tan"],["ate","eat","tea"]]</p>
     *
     * @param strs
     * @return
     */
    private static List<List<String>> groupAnagrams(String[] strs) {
        // ArrayMap 存储异位词组
        Map<String, List<String>> map = new ArrayMap<>();
        // 轮询 string 数组中的每个单词
        for (String word : strs) {
            // 对每一个单词转换成 char[] 然后进行排序，异位词字母组具有相同的顺序
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            // 排序后的数组转换为 string，作为异位词组的 key
            String key = new String(chars);
            if (!map.containsKey(key)) { // 首次存储，要先创建存储 list 作为容器
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(word);
        }
        return new ArrayList<>(map.values()); // good api
    }

    /**
     * 题目：给定一个整型数组，找出数组中任意两数之和等于某个固定值（例如，target=13）的元素组合
     */
    private static void findTwoSumNumbersDemo() {
        int[] nums = {3, 2, 10, 5, 6, 7, 11, 8};
        int target = 13;
        List<List<Integer>> list = twoSum(nums, target);
        Log.d(TAG, "twoSum 组合：");
        for (List<Integer> element : list) {
            Log.d(TAG, Arrays.toString(element.toArray()));
        }
    }

    /**
     * 题目：给定一个整型数组，找出数组中任意两数之和等于某个固定值（例如，target=13）的元素组合
     * 1. target =  a + b,
     * 2. 将数组存放到 hashmap 里，其中 key = numbers[index],value = index。
     * 3. 对数组进行遍历，每一个元素对应一个 key1，如果 map 中存在另一个 key2 = target-key1,(key1,key2),
     * 即为所要寻找的目标元素对
     *
     * @param numbers
     * @param target
     * @return
     */
    private static List<List<Integer>> twoSum(int[] numbers, int target) {
        Map<Integer, Integer> map = new ArrayMap<>();
        for (int i = 0; i < numbers.length; i++) {
            map.put(numbers[i], i);
        }
        List<List<Integer>> resultList = new ArrayList<>();
        for (int i = 0; i < numbers.length; i++) {
            int other = target - numbers[i];
            if (map.containsKey(other) && map.get(other) != i) {
                resultList.add(Arrays.asList(numbers[i], other));
                map.remove(other); // 为防止找到重复的元素对，移除匹配元素
                map.remove(numbers[i]);
            }
        }
        return resultList;
    }

    private static void findSumDemo() {
        int[] nums = {3, 2, 10, 5, 6, 7, 11, 8, 4};
        int target = 13;
        List<List<Integer>> list = findSum(nums, target);
        Log.d(TAG, "n Sum 组合：");
        for (List<Integer> element : list) {
            Log.d(TAG, Arrays.toString(element.toArray()));
        }
    }

    /**
     * 多数之和
     *
     * @param candidates
     * @param target
     * @return
     */
    private static List<List<Integer>> findSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> currentList = new ArrayList<>();
        findSumRecursive(candidates, target, 0, currentList, resultList);
        return resultList;
    }

    /**
     * 递归寻找 target = 多数之和
     *
     * @param candidates
     * @param target
     * @param start
     * @param currentList
     * @param resultList
     */
    private static void findSumRecursive(int[] candidates, int target, int start, List<Integer> currentList, List<List<Integer>> resultList) {
        if (target == 0) {
            resultList.add(new ArrayList<>(currentList));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            if (i > start && candidates[i] == candidates[i - 1]) {
                continue; // 避免重复组合
            }
            if (candidates[i] > target) {
                break; // 数组已排序，如果当前元素大于目标值，后续元素一定大于目标值，可以直接退出循环
            }
            currentList.add(candidates[i]);
            // 传递下一个要处理的索引 i + 1
            findSumRecursive(candidates, target - candidates[i], i + 1, currentList, resultList);
            currentList.remove(currentList.size() - 1);
        }
    }

    /**
     * 题目：要使用最小堆找到数组中的第 k 大元素
     */
    private static void kthLargestElement() {
        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
        int result = findKthLargestElement(nums, k);
        Log.d(TAG, "第 " + k + " 大元素是：" + result);
    }

    /**
     * 题目：要使用最小堆找到数组中的第 k 大元素
     * 步骤：
     * 1. 创建一个大小为 K 的最小堆
     * 2. 遍历数组中的每个元素，插入到堆中
     * 3. 如果堆大小超过 K，将堆顶元素移除（即当前堆中最小元素）移除，保持堆大小为 K
     * 4. 最终，堆顶元素即为数组中的第 K 大元素
     *
     * @return
     */
    private static int findKthLargestElement(int[] nums, int k) {

        if (nums == null || nums.length == 0 || k < 1) {
            throw new IllegalArgumentException(" nums or k is illegal ");
        }
        // 创建一个大小为 K 的最小堆，使用优先队列
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);

//        创建一个大小为 K 的最大堆，使用逆序比较器初始化优先队列
//        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(k, Collections.reverseOrder());


        for (int element : nums) {
            // 将元素添加到堆中
            minHeap.offer(element);
            if (minHeap.size() > k) {
                // 大于K，移除堆顶元素，minHeap自己实现了调整
                minHeap.poll();
            }
        }
        // 返回堆顶元素，即为第 k 大元素
        return minHeap.peek();
    }

    private static void rearrangeArrayDemo() {
        int[] numbs = {1, 2, 30, 4, 5, 6, 7};
        rearrangeArray(numbs);
        StringBuilder builder = new StringBuilder("rearrangeArray : ");
        for (int num : numbs) {
            builder.append(num).append(',');
        }
        Log.d(TAG, builder.toString());
    }

    /**
     * 题目：给一个数组将数组中的奇数放在数组前面，偶数放在数组后面，并且保持元素的相对位置不变。例如：输入{1,2,30,4,5,6,7}，
     * 输出{1,5,7,30,2,4,6}，要求空间复杂度最大是 n，数组元素的相对位置保持不变，用 Java 代码实现
     */
    private static void rearrangeArray(int[] nums) {
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
     *
     * @param nums
     * @param from
     * @param to
     */
    private static void moveElement(int[] nums, int from, int to) {
        int temp = nums[from];
        for (int i = from; i > to; i--) {
            nums[i] = nums[i - 1]; // 数据左移动
        }
        nums[to] = temp;
    }

    /**
     * 找到数组剩余部分的第一个奇数
     *
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

    /**
     * 题目：
     * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
     * 请注意 ，必须在不复制数组的情况下原地对数组进行操作。
     * 示例 1:
     * <p>
     * 输入: nums = [0,1,0,3,12]
     * 输出: [1,3,12,0,0]
     * 示例 2:
     * <p>
     * 输入: nums = [0]
     * 输出: [0]
     */
    private static void moveZeroDemo() {
        // 示例1
        int[] nums1 = {0, 1, 0, 3, 12};
        moveElementForZero(nums1);
        Log.d(TAG, "Array Example 1: " + Arrays.toString(nums1)); // Output: [1, 3, 12, 0, 0]

        // 示例2
        int[] nums2 = {0};
        moveElementForZero(nums2);
        Log.d(TAG, "Array Example 2: " + Arrays.toString(nums2)); // Output: [0]
    }

    /**
     * 这个算法使用一个非零元素的指针 nonZeroIndex，遍历数组，当遇到非零元素时，将其与 nonZeroIndex 指向的位置交换，
     * 并将 nonZeroIndex 向前移动。最终，所有非零元素都被移到数组的前面，而数组的末尾都是零。这样就实现了将所有零移动
     * 到数组末尾的目标。
     *
     * @param nums 注意： DoublePointers 中的 demo 节省了一个指针的内存
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
