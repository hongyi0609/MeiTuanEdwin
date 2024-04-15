package com.algorithm;


import android.annotation.SuppressLint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class HWInterview {

    private final static String TAG = HWInterview.class.getSimpleName();

    private static class Holder {
        public static HWInterview INSTANCE = new HWInterview();
    }

    private HWInterview() {

    }

    public static HWInterview getInstance() {
        return Holder.INSTANCE;
    }

    public void impl() {
        kindergartenDemo();
        mountainPeakDemo();
        SpecialEncryptionAlgorithm.demo();
        MinimumInitialFuel.demo();
        WoodCutter.demo();
        TaskSorter.demo();
        MartianCalculator.demo();
        rockPaperScissorsDemo();
        calculateMeetingRoomTimeDemo();
        bottleExchangeDemo();
        lastRemainingImpl();
        solveSudokuDemo();
    }

    // 【会议室占用时间】
    public static void calculateMeetingRoomTimeDemo() {
        int[][] meetings = {{1, 3}, {2, 4}, {3, 6}, {5, 7}, {8, 9}};
        Log.d(TAG, "calculateMeetingRoomTime" + calculateMeetingRoomTime(meetings));
    }

    /**
     * 【会议室占用时间】
     * <p>
     * 现有若干个会议，所有会议共享一个会议室，用数组表示各个会议的开始时间和结束时间，格式为：
     * <p>
     * [[会议1开始时间, 会议1结束时间], [会议2开始时间, 会议2结束时间]]
     * <p>
     * 请计算会议室占用时间段。
     *
     * @param meetings
     * @return
     */
    private static int calculateMeetingRoomTime(int[][] meetings) {
        if (meetings == null || meetings.length == 0) {
            return 0;
        }

        // 将会议的开始时间排序
        Arrays.sort(meetings, (a, b) -> {
            return (a[0] - b[0]);
        });
        // 初始化占用时间和当前会议的结束时间
        int totalTime = 0;
        int endTime = meetings[0][1];
        // 遍历会议
        for (int i = 1; i < meetings.length; i++) {
            int start = meetings[i][0];
            int end = meetings[i][1];

            if (start >= endTime) { // 当前会议的开始时间，在上一场会议的结束时间之后
                totalTime += endTime - meetings[i - 1][0];
                meetings[i - 1][0] = start;
                endTime = end;
            } else {
                // 当前会议的结束时间，在上一场会议的结束时间之后，更新结束时间
                if (end > endTime) {
                    endTime = end;
                }
            }

        }

        // 计算最后一个会议的占用时间
        totalTime += endTime - meetings[meetings.length - 1][0];

        return totalTime;

    }

    /**
     * 示例 1：
     * <p>
     * 输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
     * 输出：[[1,6],[8,10],[15,18]]
     * 解释：区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].
     * 示例 2：
     * <p>
     * 输入：intervals = [[1,4],[4,5]]
     * 输出：[[1,5]]
     * 解释：区间 [1,4] 和 [4,5] 可被视为重叠区间。
     */
    private static void mergeDemo() {
        int[][] intervals1 = new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        Log.d(TAG, "merge intervals " + merge(intervals1));
    }

    /**
     * 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。请你合并所有重叠的区间，
     * 并返回 一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间 。
     *
     * @param intervals
     * @return
     */
    public static int[][] merge(int[][] intervals) {
        if (intervals.length == 0) {
            return new int[0][2];
        }
        // 按照启动时间排序
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });
        // 初始化 merged 数组
        List<int[]> merged = new ArrayList<>();
        for (int i = 0; i < intervals.length; i++) {
            // 获取当前元素区间的左右元素
            int l = intervals[i][0];
            int r = intervals[i][1];
            if (merged.size() == 0 || merged.get(merged.size() - 1)[1] < l) {
                merged.add(new int[]{l, r});
            } else {
                merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], r);
            }
        }
        return merged.toArray(new int[merged.size()][]);
    }

    private static void rockPaperScissorsDemo() {
        // 定义三位玩家的出拳形状
        char[] shapes1 = {'A', 'B', 'C'};
        char[] shapes2 = {'A', 'B', 'B'};
        char[] shapes3 = {'A', 'A', 'A'};

        // 输出每种情况的结果
        Log.d(TAG, "Result 1: " + RockPaperScissors.determineWinner(shapes1));
        Log.d(TAG, "Result 2: " + RockPaperScissors.determineWinner(shapes2));
        Log.d(TAG, "Result 3: " + RockPaperScissors.determineWinner(shapes3));
    }

    /**
     * 这段代码模拟了三位玩家的石头剪刀布游戏，根据规则判断出胜利者或平局。每位玩家出拳形状用字符'A'、'B'、'C'表示，统计
     * 每种形状出现的次数，然后根据出现次数判断胜利者或平局。
     */
    private static class RockPaperScissors {
        public static String determineWinner(char[] shapes) {
            // 记录每种形状出现的次数
            Map<Character, Integer> countMap = new HashMap<>();
            countMap.put('A', 0);
            countMap.put('B', 0);
            countMap.put('C', 0);

            // 统计每种形状出现的次数
            for (char shape : shapes) {
                countMap.put(shape, countMap.get(shape) + 1);
            }
            // 查找出现次数最多的形状
            int maxCount = 0;
            char winner = ' ';
            for (char shape : countMap.keySet()) {
                int count = countMap.get(shape);
                if (count > maxCount) {
                    maxCount = count;
                    winner = shape;
                }
            }
            // 判断胜利者或平局
            if (maxCount == 1) {
                return "Draw"; // 平局
            } else {
                return "Winner：" + winner;// 胜利者
            }
        }
    }

    /**
     * 用双栈处理火星文
     */
    public static class MartianCalculator {
        public static void demo() {
            String message = "3#4$15#2$1";
            int result = calculate(message);
            Log.d(TAG, "火星文计算结果为：" + result);
        }

        public static int calculate(String message) {
            Stack<Integer> numStack = new Stack<>(); // 用于存储数字的栈
            Stack<Character> opStack = new Stack<>(); // 用于存储运算符的栈
            int num = 0; // 用于临时存储当前数字

            // 遍历火星文字符串报文
            for (int i = 0; i < message.length(); i++) {
                char c = message.charAt(i);

                // 如果当前字符是数字，则将其转换为整数并更新num值
                if (Character.isDigit(c)) {
                    num = num * 10 + (c - '0');
                } else if (c == '#' || c == '$' || i == message.length() - 1) {
                    // 如果当前字符是#或$，则根据之前的操作符进行部分计算，并将结果入栈
                    if (!opStack.isEmpty() && opStack.peek() == '#') {
                        opStack.pop(); // 弹出#
                        int prevNum = numStack.pop(); // 弹出前一个数字
                        numStack.push(4 * prevNum + 3 * num + 2); // 计算部分结果并入栈
                    } else {
                        numStack.push(num); // 将当前数字入栈
                    }
                    opStack.push(c); // 将当前操作符入栈
                    num = 0; // 重置num为0，准备存储下一个数字
                }
            }

            // 计算剩余的部分
            while (!opStack.isEmpty()) {
                char op = opStack.pop();
                int prevNum = numStack.pop();
                if (op == '$') {
                    numStack.push(2 * prevNum + num + 3);
                } else {
                    numStack.push(prevNum);
                }
            }

            return numStack.pop(); // 返回最终计算结果
        }
    }


    /**
     * 【启动多任务排序】
     * <p>
     * 1. 一个应用启动时，会有多个初始化任务需要执行，并且任务之间有依赖关系，例如A任务依赖B任务，那么必须在B任务执行完成之后，
     * 才能开始执行A任务。
     * <p>
     * 2. 现在给出多条任务依赖关系的规则，请输入任务的顺序执行序列，规则采用贪婪策略，即一个任务如果没有依赖的任务，则立刻开始
     * 执行，
     * <p>
     * 3. 如果同时有多个任务要执行，则根据任务名称字母顺序排序。
     * <p>
     * 例如：B任务依赖A任务，C任务依赖A任务，D任务依赖B任务和C任务，同时，D任务还依赖E任务。
     * <p>
     * 那么执行任务的顺序由先到后是：A任务，E任务，B任务，C任务，D任务。这里A和E任务都是没有依赖的，立即执行。【要求用
     * Java 代码实现，并且在代码中添加详细的注释】
     */
    private static class TaskSorter {
        public static void demo() {
            // 任务依赖关系列表
            Map<Character, List<Character>> dependencies = new HashMap<>();
            dependencies.put('A', new ArrayList<>(Arrays.asList('B', 'C')));
            dependencies.put('B', new ArrayList<>(Collections.singletonList('D')));
            dependencies.put('C', new ArrayList<>(Collections.singletonList('D')));
            dependencies.put('D', new ArrayList<>(Collections.singletonList('E')));
            dependencies.put('E', new ArrayList<>());

            // 执行任务排序
            List<Character> sortedTasks = sortTasks(dependencies);

            // 输出排序结果
            Log.d(TAG, "任务执行顺序:");
            for (char task : sortedTasks) {
                Log.d(TAG, String.valueOf(task));
            }
        }

        /**
         * 使用深度优先搜索（DFS）来排序任务，首先遍历每个任务，对于每个任务，递归地访问其依赖任务，直到没有依赖任务为止。
         * 排序时，按照任务名称的字母顺序进行排序，确保先执行没有依赖任务的任务。
         *
         * @param dependencies
         * @return
         */
        public static List<Character> sortTasks(Map<Character, List<Character>> dependencies) {
            List<Character> sortedTasks = new ArrayList<>();
            Set<Character> visited = new HashSet<>();

            // 遍历每个任务，进行深度优先搜索
            for (char task : dependencies.keySet()) {
                dfs(task, dependencies, sortedTasks, visited);
            }

            return sortedTasks;
        }

        /**
         * 深度优先搜索（DFS）：通过动态规划实现深度优先搜索
         *
         * @param task
         * @param dependencies
         * @param sortedTasks
         * @param visited
         */
        @SuppressLint("NewApi")
        private static void dfs(char task, Map<Character, List<Character>> dependencies, List<Character> sortedTasks, Set<Character> visited) {
            // 如果任务已经访问过，直接返回
            if (visited.contains(task)) {
                return;
            }

            // 标记任务为已访问
            visited.add(task);

            // 遍历当前任务的依赖任务
            for (char dependency : dependencies.getOrDefault(task, new ArrayList<>())) {
                // 递归调用深度优先搜索
                dfs(dependency, dependencies, sortedTasks, visited);
            }

            // 将当前任务加入排序结果中
            sortedTasks.add(task);
        }
    }


    /**
     * 为了使乘积最大化，我们可以将切割后的木头长度尽可能均匀分配，即使得所有木头长度尽可能相等。
     * <p>
     * 假设切割后的木头长度为 x，若切割为 k 段，则有 x = X / k。因此，要使得乘积最大化，k 越大越好，即木头长度 x 越小越好。
     * <p>
     * 最后的切割方案为将树木切割成长度为 x 的 k 段木头，且 x 取最小的正整数，使得 X 能够整除 x。
     * <p>
     * 这样得到的切割方案就能使得收益最大化。
     */
    public static class WoodCutter {
        public static void demo() {
            int X = 20; // 树木的长度
            int k = findMaxK(X); // 找到最大的 k
            int x = X / k; // 计算每段木头的长度
            int remainder = X % k; // 计算剩余的长度
            int[] woodLengths = new int[k]; // 保存每段木头的长度

            // 初始化木头长度数组
            for (int i = 0; i < k; i++) {
                woodLengths[i] = x;
            }

            // 将剩余的长度依次加到木头长度数组中
            for (int i = 0; i < remainder; i++) {
                woodLengths[i]++;
            }

            // 计算收益
            long profit = 1;
            for (int length : woodLengths) {
                profit *= length;
            }

            Log.d(TAG, "最大收益为：" + profit);
        }

        // 找到最大的 k，使得 k 能整除 X，且 k 尽可能大
        private static int findMaxK(int X) {
            for (int k = X; k >= 1; k--) {
                if (X % k == 0) {
                    return k;
                }
            }
            return -1; // 找不到合适的 k，返回-1
        }
    }


    /**
     * 有一辆汽车需要从m*n的地图的左上角(起点)开往地图的右下角(终点),去往每一个地区都需要消耗一定的油量,加油站可进行加油
     * 请你计算汽车确保从起点到达终点时所需的最少初始油量说明:
     * (1)智能汽车Q可以上下左右四个方向移动1
     * (2)地图上的数字取值是0或-1或者正整数:
     * 1:表示加油站,可以加满油,汽车的油箱容量最大为100;
     * 0:表示这个地区是障碍物,汽车不能通过
     * 正整数:表示汽车走过这个地区的耗油量
     * (3)如果汽车无论如何都无法到达终点,则返回-1
     * <p>
     * 解析：通过广度优先搜索（BFS）来解决。我们可以从起点开始，逐步探索所有可能的路径，并计算到达每个位置时的剩余油量。如
     * 果在某个位置上剩余油量不足以到达下一个位置，则可以在该位置上加油，使得剩余油量达到最大容量。
     */
    private static class MinimumInitialFuel {
        public static int minimumInitialFuel(int[][] map) {
            int m = map.length;
            int n = map[0].length;
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // 上下左右四个方向
            Queue<int[]> queue = new LinkedList<>();
            boolean[][] visited = new boolean[m][n];
            queue.offer(new int[]{0, 0, 0}); // 位置和剩余油量

            while (!queue.isEmpty()) {
                int[] current = queue.poll();
                int x = current[0];
                int y = current[1];
                int fuel = current[2];

                if (x == m - 1 && y == n - 1) { // 到达终点
                    return fuel;
                }

                for (int[] direction : directions) {
                    int newX = x + direction[0];
                    int newY = y + direction[1];

                    if (newX >= 0 && newX < m && newY >= 0 && newY < n && !visited[newX][newY]) {
                        int newFuel = fuel - map[newX][newY];
                        if (newFuel >= 0) { // 油量足够到达下一个位置
                            visited[newX][newY] = true;
                            queue.offer(new int[]{newX, newY, newFuel});
                        } else { // 油量不足，加油
                            visited[newX][newY] = true;
                            queue.offer(new int[]{newX, newY, 100 - map[newX][newY]});
                        }
                    }
                }
            }

            return -1; // 无法到达终点
        }

        public static void demo() {
            int[][] map = {
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 0},
                    {1, 1, 0, 0}
            };
            Log.d(TAG, String.valueOf(minimumInitialFuel(map)));
        }
    }


    /**
     * 1. 创建一个密码本，即二维数组Q，用于存储密码本中的数字。
     * 2. 遍历明文的每一位数字，根据规则在密码本中查找对应的数字串。如果找到多个匹配的数字串，则选择字符序最小的作为密文的
     * 一部分。
     * 3. 将每一位明文数字转换为对应的密文数字串，并按照规则拼接成最终的密文字符串。
     */
    private static class SpecialEncryptionAlgorithm {
        public static void demo() {
            int[][] book = {
                    {0, 1, 2},
                    {3, 4, 5},
                    {6, 7, 8}
            };
            String plaintext = "123456789";
            String ciphertext = encrypt(plaintext, book);
            Log.d(TAG, ciphertext);
        }

        @SuppressLint("NewApi")
        public static String encrypt(String plaintext, int[][] book) {
            StringBuilder ciphertext = new StringBuilder();
            Map<Integer, List<int[]>> digitMap = new HashMap<>(); // 用于记录每个数字的位置信息
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // 上下左右四个方向

            // 初始化digitMap
            for (int row = 0; row < book.length; row++) {
                for (int col = 0; col < book[0].length; col++) {
                    int digit = book[row][col];
                    if (!digitMap.containsKey(digit)) {
                        digitMap.put(digit, new ArrayList<>());
                    }
                    digitMap.get(digit).add(new int[]{row, col});
                }
            }

            // 遍历明文的每一位数字
            for (int i = 0; i < plaintext.length(); i++) {
                int digit = plaintext.charAt(i) - '0'; // 获取当前明文数字
                String candidate = "error"; // 初始化候选密文
                int minRow = Integer.MAX_VALUE; // 设置为最大值
                int minCol = Integer.MAX_VALUE; // 设置为最大值

                // 遍历密码本中数字digit的位置
                for (int[] pos : digitMap.getOrDefault(digit, new ArrayList<>())) {
                    StringBuilder currentCandidate = new StringBuilder();
                    currentCandidate.append(pos[0]).append(" ").append(pos[1]);
                    Queue<int[]> queue = new LinkedList<>();
                    Set<String> visited = new HashSet<>();
                    queue.offer(pos);
                    visited.add(pos[0] + " " + pos[1]);

                    // 使用BFS查找相邻的相同数字
                    while (!queue.isEmpty()) {
                        int[] currentPos = queue.poll();
                        for (int[] direction : directions) {
                            int nextRow = currentPos[0] + direction[0];
                            int nextCol = currentPos[1] + direction[1];
                            if (nextRow >= 0 && nextRow < book.length && nextCol >= 0 && nextCol < book[0].length &&
                                    book[nextRow][nextCol] == digit && !visited.contains(nextRow + " " + nextCol)) {
                                queue.offer(new int[]{nextRow, nextCol});
                                visited.add(nextRow + " " + nextCol);
                                currentCandidate.append(" ").append(nextRow).append(" ").append(nextCol);
                            }
                        }
                    }

                    // 更新候选密文和最小行列号
                    String currentCandidateStr = currentCandidate.toString();
                    if (currentCandidateStr.compareTo(candidate) < 0 ||
                            (currentCandidateStr.compareTo(candidate) == 0 && (pos[0] < minRow || (pos[0] == minRow && pos[1] < minCol)))) {
                        candidate = currentCandidateStr;
                        minRow = pos[0];
                        minCol = pos[1];
                    }
                }

                // 如果存在有效的密文候选，则将其添加到密文中
                if (!candidate.equals("error")) {
                    ciphertext.append(candidate).append(" ");
                }
            }

            return ciphertext.toString().trim(); // 返回最终的密文
        }
    }


    /**
     *
     */
    private static void mountainPeakDemo() {
        int[] map = {0, 1, 2, 4, 3, 1, 0, 0, 1, 2, 3, 1, 2, 1, 0};
        List<Integer> peaks = MountainPeak.findPeaks(map);

        Log.d(TAG, "Mountain peaks are at positions:");
        for (int peak : peaks) {
            Log.d(TAG, String.valueOf(peak));
        }
    }

    /**
     * 遍历地图数组，找到所有山峰的位置。
     * 判断一个位置是否为山峰的条件是：该位置的高度大于相邻位置的高度，或者是地图的边界且高度大于相邻位置的高度。
     * 将满足条件的山峰位置添加到结果列表中。
     */
    private static final class MountainPeak {
        public static List<Integer> findPeaks(int[] map) {
            List<Integer> peaks = new ArrayList<>();

            for (int i = 0; i < map.length; i++) {
                // Check if current position is a peak
                if ((i == 0 || map[i] > map[i - 1]) && (map[i] < map[i + 1] || i == map.length - 1)) {
                    peaks.add(map[i]);
                }
            }
            return peaks;
        }
    }

    /**
     * 允许在桶的右边将篮球放入，可以在桶的左边和右边将篮球取出===》先进先出，FIFO
     * 当桶只有一个篮球的情况下，必须从左边取出 ===》限定条件
     */
    private static void kindergartenDemo() {
        KindergartenBucket bucket = new KindergartenBucket();
        bucket.put(1);
        bucket.put(2);
        bucket.put(3);
        bucket.put(4);
        bucket.put(5);

        Log.d(TAG, String.valueOf(bucket.get(false)));
        Log.d(TAG, String.valueOf(bucket.get(false)));
        Log.d(TAG, String.valueOf(bucket.get(false)));
        Log.d(TAG, String.valueOf(bucket.get(false)));
        Log.d(TAG, String.valueOf(bucket.get(false)));
    }

    private static class KindergartenBucket {

        private LinkedList<Integer> balls;

        public KindergartenBucket() {
            balls = new LinkedList<>();
        }

        public void put(int ball) {
            balls.addLast(ball);
        }

        public int get(boolean fromRight) {
            if (balls.size() == 1) {
                return balls.removeFirst();
            }
            if (fromRight) {
                return balls.removeLast();
            } else {
                return balls.removeFirst();
            }
        }
    }

    /**
     * 汽水瓶
     * 某商店规定：三个空汽水瓶可以换一瓶汽水，允许向老板借空汽水瓶（但是必须要归还）。
     * 小张手上有n个空汽水瓶，她想知道自己最多可以喝到多少瓶汽水。
     * 注意：本题存在多组输入。输入的 0 表示输入结束，并不用输出结果。
     * <p>
     * 输入例子：
     * 3
     * 10
     * 81
     * 0
     * 输出例子：
     * 1
     * 5
     * 40
     * 例子说明：
     * 样例 1 解释：用三个空瓶换一瓶汽水，剩一个空瓶无法继续交换
     * 样例 2 解释：用九个空瓶换三瓶汽水，剩四个空瓶再用三个空瓶换一瓶汽水，剩两个空瓶，向老板借一个空瓶再用三个空瓶换一瓶汽水喝完得一个空瓶还给老板
     */

    private void bottleExchangeDemo() {
        new BottleExchange().exchange();
    }

    private static class BottleExchange {
        public void exchange() {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) { // 下一行
                int n = scanner.nextInt(); // 下一个
                if (n < 2) { // 瓶子数小于 3，换不了
                    break;
                }
                int total = 0; // 总共可以还多少汽水
                while (n >= 3) {
                    int num = n / 3; // 可以换 num 瓶汽水
                    total += num; // 总共可以换的汽水数
                    n = n / 3 + n % 3; // 剩余空瓶数，大于 3 可以继续换
                }
                if (n == 2) { // 跟老板借一个空瓶
                    total += 1;
                }
                System.out.println(total);
            }
            scanner.close();
        }
    }


    /**
     * 删数
     * 有一个数组 a[N] 顺序存放 0 ~ N-1 ，要求每隔两个数删掉一个数，到末尾时循环至开头继续进行，求最后一个被删掉的数的
     * 原始下标位置。以 8 个数 (N=7) 为例 :｛ 0，1，2，3，4，5，6，7 ｝，0 -> 1 -> 2 (删除) -> 3 -> 4 -> 5 (删除)
     * -> 6 -> 7 -> 0 (删除),如此循环直到最后一个数被删除。
     */
    private void lastRemainingImpl() {
        System.out.println(lastRemaining(8));
    }

    private int lastRemaining(int n) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        int index = 0;
        while (list.size() > 1) {
            // 每次跳过两个数
            index = (index + 2) % list.size();
            list.remove(index);
        }
        return list.get(0);
    }

    /**
     * 数独
     * 数独是一个我们都非常熟悉的经典游戏，运用计算机我们可以很快地解开数独难题，现在有一些简单的数独题目，请编写一个程序求解。
     * 如有多解，输出一个解
     *
     * 输入描述：
     * 输入9行，每行为空格隔开的9个数字，为0的地方就是需要填充的。
     * 输出描述：
     * 输出九行，每行九个空格隔开的数字，为解出的答案。
     */
    public void solveSudokuDemo() {
        int[][] board = new int[9][9];
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = scanner.nextInt();
            }
        }
        board = new int[][]{
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        scanner.close();

        solveSudoku(board);

        printBoard(board);
    }


    /**
     * 解决方案
     * @param board
     * @return
     */
    private boolean solveSudoku(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) { // 遍历二维数组每个元素
                if (board[row][col] == 0) { // 填充==0的位置
                    for (int num = 1; num <= 9; num++) { // 填充 1-9
                        if (isValid(board, row, col, num)) { // 数独规则判断
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 数独规则：同一行、同一列、以及每个 3*3 的子方格没有重复元素
     * @param board
     * @param row
     * @param col
     * @param num
     * @return
     */
    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            // 在数独中，每个数字在同一行、同一列和同一个3x3的子方格内必须是唯一的。因此，当我们填入一个数字时，需要检查这个数字在当前行、当前列和当前子方格内是否已经存在。
            //
            //row / 3 和 col / 3 用来确定当前格子所在的子方格的行和列。例如，如果 row = 4，col = 7，那么 row / 3 = 1，col / 3 = 2，说明当前格子在第二行第三列的子方格内。
            //
            //3 * (row / 3) 和 3 * (col / 3) 计算出当前子方格的左上角格子的行和列。继续上面的例子，左上角格子的坐标就是 (3, 6)。
            //
            //i / 3 和 i % 3 用来遍历当前子方格内的所有格子。这两个值分别表示当前格子在子方格内的行和列的偏移量。例如，当 i = 0 时，表示左上角格子；当 i = 1 时，表示左上角格子的右边一个格子，依此类推。
            if (board[row][i] == num || board[i][col] == num || board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    private void printBoard(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
