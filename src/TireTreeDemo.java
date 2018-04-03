/**
 * @author ceshi
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/4/315:55
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * jijs
 * 正向最大匹配
 */
public class TireTreeDemo {
    static class Node {
        //记录当前节点的字
        char c;
        //判断该字是否词语的末尾，如果是则为false
        boolean isEnd;
        //子节点
        List<Node> childList;

        public Node(char c) {
            super();
            this.c = c;
            isEnd = false;
            childList = new LinkedList<Node>();
        }

        //查找当前子节点中是否保护c的节点
        public Node findNode(char c){
            for(Node node : childList){
                if(node.c == c){
                    return node;
                }
            }

            return null;
        }
    }

    static class TrieTree{
        Node root = new Node(' ');

        //构建Trie Tree
        public void insert(String words){
            char[] arr = words.toCharArray();
            Node currentNode = root;
            for (char c : arr) {
                Node node = currentNode.findNode(c);
                //如果不存在该节点则添加
                if(node == null){
                    Node n = new Node(c);
                    currentNode.childList.add(n);
                    currentNode = n;
                }else{
                    currentNode = node;
                }
            }
            //在词的最后一个字节点标记为true
            currentNode.isEnd = true;
        }

        //判断Trie Tree中是否包含该词
        public boolean search(String word){
            char[] arr = word.toCharArray();
            Node currentNode = root;
            for (int i=0; i<arr.length; i++) {
                Node n = currentNode.findNode(arr[i]);
                if(n != null){
                    currentNode = n;
                    //判断是否为词的尾节点节点
                    if(n.isEnd){
                        if(n.c == arr[arr.length-1]){
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        //最大匹配优先原则
        public Map<String, Integer> tokenizer(String words){
            char[] arr = words.toCharArray();
            Node currentNode = root;
            Map<String, Integer> map = new HashMap<String, Integer>();
            //记录Trie Tree 从root开始匹配的所有字
            StringBuilder sb = new StringBuilder();;
            //最后一次匹配到的词，最大匹配原则，可能会匹配到多个字，以最长的那个为准
            String word="";
            //记录记录最后一次匹配坐标
            int idx = 0;
            for (int i=0; i<arr.length; i++) {
                Node n = currentNode.findNode(arr[i]);
                if(n != null){
                    sb.append(n.c);
                    currentNode = n;
                    //匹配到词
                    if(n.isEnd){
                        //记录最后一次匹配的词
                        word = sb.toString();
                        //记录最后一次匹配坐标
                        idx = i;
                    }
                }else{
                    //判断word是否有值
                    if(word!=null && word.length()>0){
                        Integer num = map.get(word);
                        if(num==null){
                            map.put(word, 1);
                        }else{
                            map.put(word, num+1);
                        }
                        //i回退到最后匹配的坐标
                        i=idx;
                        //从root的开始匹配
                        currentNode = root;
                        //清空匹配到的词
                        word = null;
                        //清空当前路径匹配到的所有字
                        sb = new StringBuilder();
                    }
                }
                if(i==arr.length-2){
                    if(word!=null && word.length()>0){
                        Integer num = map.get(word);
                        if(num==null){
                            map.put(word, 1);
                        }else{
                            map.put(word, num+1);
                        }
                    }
                }
            }

            return map;
        }
    }

    public static void main(String[] args) {
        TrieTree tree = new TrieTree();
        tree.insert("北京");
        tree.insert("海淀区");
        tree.insert("中国");
        tree.insert("中国人民");
        tree.insert("中关村");

        String word = "中国";
        //查找该词是否存在 Trid Tree 中
        boolean flag = tree.search(word);
        if(flag){
            System.out.println("Trie Tree 中已经存在【"+word+"】");
        }else{
            System.out.println("Trie Tree 不包含【"+word+"】");
        }

        //分词
        Map<String, Integer> map = tree.tokenizer("中国人民，中国首都是北京，中关村在海淀区,中国北京天安门。中国人");
        for (Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }

    }
}