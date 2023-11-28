package cn.auntec.framework.components.util.value.data;

/**
 * 字符串匹配工具类
 *
 * @Author: 蒋时华
 * @Date: 2019/3/25
 */
public class StringMatchUtil {

    /**
     * 朴素模式匹配
     *
     * @param source  目标串
     * @param pattern 模式串
     */
    public static boolean plain(String source, String pattern) {
        int sourceLength = source.length();
        int patternLength = pattern.length();
        for (int i = 0; i <= (sourceLength - patternLength); i++) {
            String str = source.substring(i, i + patternLength);
            if (str.equals(pattern)) {
                return true;
            }
        }
        return false;
    }

    public static boolean kmp(String source, String pattern) {
        int[] N = getN(pattern);
        int sourceLength = source.length();
        int patternLength = pattern.length();
        for (int i = 0; i <= (sourceLength - patternLength); ) {
            //要比较的字符串
            String str = source.substring(i, i + patternLength);
            int count = getNext(pattern, str, N);
            if (count == 0) {
                return true;
            }
            i = i + count;
        }
        return false;
    }

    /**
     * 得到下一次要移动的次数
     *
     * @param pattern
     * @param str
     * @param N
     * @return 0, 字符串匹配；
     */
    private static int getNext(String pattern, String str, int[] N) {
        int n = pattern.length();
        char v1[] = str.toCharArray();
        char v2[] = pattern.toCharArray();
        int x = 0;
        while (n-- != 0) {
            if (v1[x] != v2[x]) {
                if (x == 0) {
                    return 1;//如果第一个不相同，移动1步
                }
                return x - N[x - 1];//x:第一次出现不同的索引的位置，即j
            }
            x++;
        }
        return 0;
    }

    private static int[] getN(String pattern) {
        char[] pat = pattern.toCharArray();
        int j = pattern.length() - 1;
        int[] N = new int[j + 1];
        for (int i = j; i >= 2; i--) {
            N[i - 1] = getK(i, pat);
        }
        return N;
    }

    private static int getK(int j, char[] pat) {
        int x = j - 2;
        int y = 1;
        while (x >= 0 && compare(pat, 0, x, y)) {
            x--;
            y++;
        }
        return x + 1;
    }

    private static boolean compare(char[] pat, int b1, int e1, int b2) {
        int n = e1 - b1 + 1;
        while (n-- != 0) {
            if (pat[b1] != pat[b2]) {
                return true;
            }
            b1++;
            b2++;
        }
        return false;
    }

    public static void main(String[] args) {
        long current = System.nanoTime();
        String source = "苏州海管家物流有限公司;2346;HGJ";
        String target = "j";
        /*boolean res = kmp(source, target);
        System.out.println("结果:" + res + "耗时:" + (System.nanoTime() - current));*/
        boolean res1 = plain(source, target);
        System.out.println("结果:" + res1 + "耗时:" + (System.nanoTime() - current));
        /*for (int i = 0; i < 100; i++) {
            kmp(source, target);
        }*/
    }
}
