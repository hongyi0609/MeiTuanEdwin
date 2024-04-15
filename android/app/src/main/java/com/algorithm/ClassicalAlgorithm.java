package com.algorithm;

import android.util.ArrayMap;
import android.util.Log;

import java.util.Map;

public class ClassicalAlgorithm {
    private final static String TAG = ClassicalAlgorithm.class.getSimpleName();
    private ClassicalAlgorithm() {

    }

    private static class Holder {
        public static ClassicalAlgorithm INSTANCE = new ClassicalAlgorithm();
    }

    public static ClassicalAlgorithm getINSTANCE() {
        return Holder.INSTANCE;
    }

    public void impl() {
        LruCacheDemo();
    }

    private void LruCacheDemo() {
        LruCache lruCache = new LruCache(16);
        lruCache.put(124234523, 'a');
        lruCache.put(124234524, 'b');
        lruCache.put(124234525, 'c');
        Log.d(TAG, "LruCache," + lruCache);
        lruCache.put(124234523, '0'-'a');
        Log.d(TAG, "LruCache," + lruCache);
    }

    private static class LruCache{
        // 双向链表 + hashMap
        private Map<Integer, Node> cache = new ArrayMap<>(8);
        private int size;
        private int capacity;
        private Node head,tail;
        // Lru 最近用过的放前面
        private void addNode(Node node) {
            // 插在 head 后边
            node.previous = head;
            node.next = head.next;

            // 告诉 head.next 他前面换人了
            head.next.previous = node;
            head.next = node;
        }

        private void removeNode(Node node) {
            Node prev = node.previous;
            Node next = node.next;

            // 相对位置移动
            prev.next = next;
            next.previous = prev;
        }

        private void moveToHead(Node node) {
            // 先从当前位置移除，再添加到头部
            removeNode(node);
            addNode(node);
        }

        /**
         * Your popTail method implementation in the LruCache class looks correct in the context of
         * removing the least recently used item from the cache, which is what LRU caching is all about.
         * This method removes the node right before the tail node, which represents the least
         * recently used item because your implementation adds newly used or added items directly
         * after the head. Thus, the item closest to the tail would naturally be the least recently used.
         *
         * @return
         */
        private Node popTail() {
            Node res = tail.previous;
            removeNode(res);
            return res;
        }

        public LruCache(int capacity) {
            this.size = 0;
            this.capacity = capacity;

            head = new Node();
            tail = new Node();
            head.next = tail;
            tail.previous = head;
        }

        public int get(int key) {
            Node node = cache.get(key);
            if (node == null) {
                return -1;
            }
            // 使用之后，把节点移到最前面
            moveToHead(node);

            return node.value;
        }

        public void put(int key, int value) {
            Node node = cache.get(key);
            if (node == null) {
                Node newNode = new Node();
                newNode.key = key;
                newNode.value = value;

                cache.put(key, newNode);
                addNode(newNode);

                size++;

                if (size > capacity) {
                    Node tail = popTail();
                    cache.remove(tail.key);
                    size--;
                }
            } else {
                node.value = value;
                moveToHead(node);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<?, Node> map : cache.entrySet()) {
                builder.append(map.getKey()).append('=').append(map.getValue().value).append(" ");
            }
            return "LruCache{" +
                    "cache：" + builder +
                    ", size=" + size +
                    ", capacity=" + capacity +
                    ", head=" + head +
                    ", tail=" + tail +
                    '}';
        }
    }
    private static class Node {
        int key;
        int value;
        Node previous;
        Node next;
    }

}
