package com.algorithm;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 滑动窗口
 */
public class SlidingWindowDemo {
    private static final String TAG = SlidingWindowDemo.class.getSimpleName();

    public static void impl() {
        minWindowDemo();

        maxSlidingWindowDemo();

        findAnagramsDemo();

        lengthOfLongestSubstringDemo();
    }

    /**
     * 题目：最小覆盖子串
     */
    private static void minWindowDemo() {
        String s = "ADOBECODEBANC", t = "ABC";
        Log.d(TAG, "minWindowDemo1, " + minWindow(s, t));
    }

    /**
     * 题目：最小覆盖子串
     *
     * 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
     *
     *
     *
     * 注意：
     *
     * 对于 t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 t 中该字符数量。
     * 如果 s 中存在这样的子串，我们保证它是唯一的答案。
     *
     *
     * 示例 1：
     *
     * 输入：s = "ADOBECODEBANC", t = "ABC"
     * 输出："BANC"
     * 解释：最小覆盖子串 "BANC" 包含来自字符串 t 的 'A'、'B' 和 'C'。
     * 示例 2：
     *
     * 输入：s = "a", t = "a"
     * 输出："a"
     * 解释：整个字符串 s 是最小覆盖子串。
     * 示例 3:
     *
     * 输入: s = "a", t = "aa"
     * 输出: ""
     * 解释: t 中两个字符 'a' 均应包含在 s 的子串中，
     * 因此没有符合条件的子字符串，返回空字符串。
     * @param s
     * @param t
     * @return
     */
    private static String minWindow(String s, String t) {
        if (s == null || s.length() == 0 || t == null || t.length() == 0) {
            return "";
        }

        // 记录目标字符串 t 中字符的出现次数
        int[] tCount = new int[128]; // 128 表示ASCII表中所有的字符数
        for (char c : t.toCharArray()) {
            tCount[c]++;
        }
        // 录入 t 中的字符后，tCount 就是滑动窗口了

        int left = 0; // 滑动窗口的左边界
        int right = 0; // 滑动窗口的右边界
        int minLength = Integer.MAX_VALUE; // 最小窗口长度
        int minStart = 0; // 最小窗口的起始位置
        int count = t.length(); // 窗口中还需要匹配的字符数量

        while (right < s.length()) {
            char rightChar = s.charAt(right);
            // 如果窗口（tCount表示窗口）中包含了 s 中的字符，则将 count 减一   'AAABC'和'ABC'
            if (tCount[rightChar] > 0) {
                count--;
            }
            // 更新滑动窗口 tCount 中字符的出现次数。如果滑动窗口中不包括当前字符，对应字符统计数为负数
            tCount[rightChar]--;
            right++; // 右指针右移，扩大窗口。直到 tCount 中包含了所有目标字符，这个时候 count 也正好减为 0 了

            // 当窗口中包含了 t 中所有字符时，进入内层循环，通过移动左指针 left 缩小窗口，以找到最小子窗口。
            while (count == 0) {
                // 移动 left 后最小子窗口的长度会变小，所以要更新最小窗口的长度和起始位置
                if (right - left < minLength) {
                    minLength = right - left;
                    minStart = left;
                }

                char leftChar = s.charAt(left);
                // 移动左指针left前，先看看将要丢掉的左字符是否在目标字符串里。因为刚刚在外循环统计时，所有字符个数都做了
                // 自减操作，现在统一做自增操作。
                tCount[leftChar]++;
                // 如果 tCount 中字符的出现次数大于 0，说明字符丢多了，表示窗口中不再包含 t 中所有字符，这个时候通过
                // count 自增退出内循环
                if (tCount[leftChar] > 0) {
                    count++;
                }
                left++; // 左指针右移，缩小窗口
            }
        }

        // 返回最小窗口子串
        return minLength == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLength);
    }




    /**
     * 滑块窗口最大值
     */
    private static void maxSlidingWindowDemo() {
        int[] nums = {1,3,-1,-3,5,3,6,7};
        int k = 3;
        Log.d(TAG, "maxSlidingWindowDemo, " + Arrays.toString(maxSlidingWindow(nums, k)));
    }

    /**
     * 滑块窗口最大值
     *
     * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
     *
     * 返回 滑动窗口中的最大值 。
     *
     *
     *
     * 示例 1：
     *
     * 输入：nums = [1,3,-1,-3,5,3,6,7], k = 3
     * 输出：[3,3,5,5,6,7]
     * 解释：
     * 滑动窗口的位置                最大值
     * ---------------               -----
     * [1  3  -1] -3  5  3  6  7       3
     *  1 [3  -1  -3] 5  3  6  7       3
     *  1  3 [-1  -3  5] 3  6  7       5
     *  1  3  -1 [-3  5  3] 6  7       5
     *  1  3  -1  -3 [5  3  6] 7       6
     *  1  3  -1  -3  5 [3  6  7]      7
     * 示例 2：
     *
     * 输入：nums = [1], k = 1
     * 输出：[1]
     * @param nums
     * @param k
     * @return
     */
    private static int[] maxSlidingWindow(int[] nums, int k) {
        if(nums.length == 0 || k == 0) return new int[0];

        // 核心思想，保持队列从大到小倒排

        // 创建双向队列，存储窗口内的元素，主要目的是为了找到对应窗口中最大值
        // 使用双向队列，结合出队策略可以使最大值永远在顶部，另外双向队列方便操作数据，降低时间复杂度
        Deque<Integer> deque = new LinkedList<>();
        // 创建数组存储每个窗口的最大值
        int res[] = new int[nums.length - k + 1];
        for (int i = 0, j = 1 - k; i < nums.length; i++, j++) {

            // 窗口移动时，有新元素，如果旧窗口（deque）中的元素有比当前元素 nums[i]小的元素，都移除掉
            // 因为 nums[i] 称霸了，留着那些小元素没用
            while (!deque.isEmpty() && deque.peekLast() < nums[i]) {
                deque.removeLast();
            }
            deque.addLast(nums[i]);

            // 删除 deque 中对应的 nums[j-1]
            if (j > 0 && deque.peekFirst() <= nums[j - 1]) {
                deque.removeFirst();
            }

            // 当窗口长度为k时 保存当前窗口中最大值
            if (j >= 0) {
                res[j] = deque.peekFirst();
            }
        }
        return res;
    }

    /**
     * 找到字符串中所有字母异位词
     */
    private static void findAnagramsDemo() {
        String s = "cbaebabacd";
        String p = "abc";
        Log.d(TAG, "findAnagramsDemo1, " + findAnagrams(s,p));
        Log.d(TAG, "findAnagramsDemo1 with cursor window, " + findAnagramsWithCursorWindow(s,p));

        s = "abab";
        p = "ab";
        Log.d(TAG, "findAnagramsDemo2, " + findAnagrams(s,p));
        Log.d(TAG, "findAnagramsDemo2 with cursor window, " + findAnagramsWithCursorWindow(s,p));


    }
    /**
     *
     * 给定两个字符串 s 和 p，找到 s 中所有 p 的 异位词 的子串，返回这些子串的起始索引。不考虑答案输出的顺序。
     *
     * 异位词 指由相同字母重排列形成的字符串（包括相同的字符串）。
     *
     * 示例 1:
     *
     * 输入: s = "cbaebabacd", p = "abc"
     * 输出: [0,6]
     * 解释:
     * 起始索引等于 0 的子串是 "cba", 它是 "abc" 的异位词。
     * 起始索引等于 6 的子串是 "bac", 它是 "abc" 的异位词。
     *  示例 2:
     *
     * 输入: s = "abab", p = "ab"
     * 输出: [0,1,2]
     * 解释:
     * 起始索引等于 0 的子串是 "ab", 它是 "ab" 的异位词。
     * 起始索引等于 1 的子串是 "ba", 它是 "ab" 的异位词。
     * 起始索引等于 2 的子串是 "ab", 它是 "ab" 的异位词。
     *
     *
     * @param s
     * @param p
     * @return
     */
    public static List<Integer> findAnagrams(String s, String p) {
        // 1. 异位词p转换为字符数组并排序
        // 2. 将排序后的异位词重新转换为string 字符串
        // 3. 源字符串s通过双指针检查，其中：left = 0，right = left + p.length();
        // 4. 对每一个子串child做同样的类 p处理，然后判断是否相同
        // 5. 相同将 child 在 s中的起始位置保存
        // 6. List<Integer> result = new ArrayList<>(); 用于保存结果
        List<Integer> result = new ArrayList<>();
        int left = 0;
        int right;
        char[] pChars = p.toCharArray();
        Arrays.sort(pChars);
        String sortedP = new String(pChars);
        while (left < s.length() - sortedP.length()+1) {
            right = left + sortedP.length();

            String child = s.substring(left, right);
            char[] childChars = child.toCharArray();
            Arrays.sort(childChars);
            String sortedChild = new String(childChars);

            if (sortedChild.equals(sortedP)) {
                // s 中存在异位词
                result.add(left + s.substring(left).indexOf(child));
            }
            left++;
        }
        return result;
    }


    /**
     * 使用滑块窗口
     * 1. 字母一共 26 个
     * 2. p 中每个字母出现的数量是固定的
     * 3. 统计p 中每个字符出现的数量，放在数组中
     * 4. 同样的方式，统计 s中等长子数组
     * 5. compare 两个数组，相等则为异位词
     * @param s
     * @param p
     * @return
     */
    private static List<Integer> findAnagramsWithCursorWindow(String s, String p) {
        int sLen = s.length();
        int pLen = p.length();
        if (sLen < pLen) {
            return new ArrayList<>();
        }
        int[] sCount = new int[26];
        int[] pCount = new int[26];
        for (int i = 0; i < pLen; i++) {
            // 注意：每个字母的 ASCII 码减去字母 a 的 ASCII 正好在 [0,25]的作用域内
            ++sCount[s.charAt(i) - 'a'];
            ++pCount[p.charAt(i) - 'a'];
        }
        List<Integer> result = new ArrayList<>();
        if (Arrays.equals(sCount, pCount)) {
            result.add(0);
        }
        for (int i = 0; i < sLen - pLen; i++) {
            // 注意：滑块向右移动，滑块左侧的字母减去一次计算，滑块右侧的字母加上一次计算
            // 滑块两端相差一个 pLen
            --sCount[s.charAt(i) - 'a'];
            ++sCount[s.charAt(i+pLen)-'a'];
            if (Arrays.equals(sCount, pCount)) {
                result.add(i + 1);
            }
        }
        return result;
    }


        /**
         * 题目：给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
         */
    private static void lengthOfLongestSubstringDemo() {
        String s1 = "abcabcbb";
        Log.d(TAG, "lengthOfLongestSubstringDemo1, " + lengthOfLongestSubstring(s1));
        String s2 = "bbbbb";
        Log.d(TAG, "lengthOfLongestSubstringDemo2, " + lengthOfLongestSubstring(s2));
        String s3 = " ";
        Log.d(TAG, "lengthOfLongestSubstringDemo3, " + lengthOfLongestSubstring(s3));
    }

    /**
     * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
     *
     *
     *
     * 示例 1:
     *
     * 输入: s = "abcabcbb"
     * 输出: 3
     * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
     * 示例 2:
     *
     * 输入: s = "bbbbb"
     * 输出: 1
     * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
     * 示例 3:
     *
     * 输入: s = "pwwkew"
     * 输出: 3
     * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
     *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
     *
     *
     * 解题：
     * 1. 使用两个指针 left 和 right 来表示滑动窗口的左右边界，初始时都指向字符串的开头。
     * 2. 使用一个哈希集合 set 来存储当前窗口中的字符，初始时为空集合。
     * 3. 不断地移动右指针 right，并将对应的字符加入到哈希集合中，直到遇到重复字符为止。
     * 4. 在每次移动右指针时，更新最长子串的长度。
     * 5. 如果遇到重复字符，则移动左指针 left，并将对应的字符从哈希集合中移除，直到窗口中不再有重复字符为止。
     * 6. 重复步骤 3~5，直到右指针到达字符串的末尾。
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();
        int left =0;
        int right = 0;
        int maxLen = 0;
        while (right < s.length()) {
            char ch = s.charAt(right);
            // 如果窗口中不存在当前字符，则将其加入窗口，并更新最长子串长度
            if (!set.contains(ch)) {
                set.add(ch);
                maxLen = Math.max(maxLen, set.size());
                right++;
            } else {
                // 如果窗口中存在当前字符，则移动左指针直到窗口中不再有重复字符
                while (set.contains(ch)) {
                    set.remove(s.charAt(left));
                    left++;
                }
            }
        }
        return maxLen;
    }
}
