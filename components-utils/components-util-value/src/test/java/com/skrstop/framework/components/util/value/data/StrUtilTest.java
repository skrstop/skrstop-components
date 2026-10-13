package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/**
 * StrUtil 单元测试
 */
public class StrUtilTest {

    @Test
    public void testToString() {
        Assert.assertEquals("default", StrUtil.toString(null, "default"));
        Assert.assertEquals("123", StrUtil.toString(123, "default"));
        Assert.assertEquals("abc", StrUtil.toString("abc", "default"));
    }

    @Test
    public void testIsAnyBlankAndIsNoneBlank() {
        Assert.assertTrue(StrUtil.isAnyBlank());
        Assert.assertTrue(StrUtil.isAnyBlank("a", null));
        Assert.assertTrue(StrUtil.isAnyBlank("a", " "));
        Assert.assertFalse(StrUtil.isAnyBlank("a", "b"));
        Assert.assertFalse(StrUtil.isNoneBlank());
        Assert.assertTrue(StrUtil.isNoneBlank("a", "b"));
        Assert.assertFalse(StrUtil.isNoneBlank("a", null));
        Assert.assertFalse(StrUtil.isNoneBlank("a", " "));
    }

    @Test
    public void testIsNumeric() {
        Assert.assertFalse(StrUtil.isNumeric(null));
        Assert.assertFalse(StrUtil.isNumeric(""));
        Assert.assertFalse(StrUtil.isNumeric(" "));
        Assert.assertTrue(StrUtil.isNumeric("123"));
        Assert.assertFalse(StrUtil.isNumeric("12a3"));
        Assert.assertFalse(StrUtil.isNumeric("1.2"));
        Assert.assertFalse(StrUtil.isNumeric("a1"));
    }

    @Test
    public void testJoinCollection() {
        Assert.assertEquals("", StrUtil.join((Collection<?>) null));
        Assert.assertEquals("", StrUtil.join(Collections.emptyList()));
        Assert.assertEquals("a,b,c", StrUtil.join(Arrays.asList("a", "b", "c")));
        Assert.assertEquals("", StrUtil.join((Collection<?>) null, "-"));
        Assert.assertEquals("a-b-c", StrUtil.join(Arrays.asList("a", "b", "c"), "-"));
        Assert.assertEquals("a,b,c", StrUtil.collectionToCommaDelimitedString(Arrays.asList("a", "b", "c")));
        Assert.assertEquals("", StrUtil.collectionToDelimitedString(null, ","));
        Assert.assertEquals("a;b", StrUtil.collectionToDelimitedString(Arrays.asList("a", "b"), ";"));
        Assert.assertEquals("", StrUtil.collectionToDelimitedString(null, ";", "<", ">"));
        Assert.assertEquals("<a>|<b>", StrUtil.collectionToDelimitedString(Arrays.asList("a", "b"), "|", "<", ">"));
        // 单元素
        Assert.assertEquals("<a>", StrUtil.collectionToDelimitedString(Arrays.asList("a"), "|", "<", ">"));
    }

    @Test
    public void testJoinArray() {
        Assert.assertEquals("", StrUtil.join((Object[]) null));
        Assert.assertEquals("", StrUtil.join(new Object[]{}));
        Assert.assertEquals("a,b", StrUtil.join(new Object[]{"a", "b"}));
        Assert.assertEquals("", StrUtil.arrayToCommaDelimitedString(null));
        Assert.assertEquals("", StrUtil.join((Object[]) null, "-"));
        Assert.assertEquals("x", StrUtil.arrayToDelimitedString(new Object[]{"x"}, "-"));
        Assert.assertEquals("null", StrUtil.arrayToDelimitedString(new Object[]{null}, "-"));
        Assert.assertEquals("a-b", StrUtil.arrayToDelimitedString(new Object[]{"a", "b"}, "-"));
    }

    @Test
    public void testRandomUUID() {
        String uuid = StrUtil.randomUUID();
        Assert.assertEquals(32, uuid.length());
        Assert.assertFalse(uuid.contains("-"));
        Assert.assertNotEquals(uuid, StrUtil.randomUUID());
    }

    @Test
    public void testEscapeHtmlAndCleanChars() {
        Assert.assertEquals("%3ca%3eh%26i", StrUtil.escapeHtml("<a>h&i"));
        Assert.assertEquals("abcd", StrUtil.cleanChars("a b　c`d"));
        Assert.assertEquals("x", StrUtil.cleanChars("x\f\t\u000B"));
    }

    @Test
    public void testFormat() {
        Assert.assertNull(StrUtil.format(null, "a"));
        Assert.assertEquals("abc", StrUtil.format("abc"));
        Assert.assertEquals("abc", StrUtil.format("abc", (String[]) null));
        Assert.assertEquals("  ", StrUtil.format("  ", "a"));
        Assert.assertEquals("this is a for b", StrUtil.format("this is {} for {}", "a", "b"));
        Assert.assertEquals("this is {} for a", StrUtil.format("this is \\{} for {}", "a", "b"));
    }

    @Test
    public void testIndexedFormat() {
        Assert.assertEquals("this is a for b", StrUtil.indexedFormat("this is {0} for {1}", "a", "b"));
    }

    @Test
    public void testFormatMap() {
        Assert.assertNull(StrUtil.format(null, (Map<?, ?>) new HashMap<>()));
        Assert.assertEquals("abc", StrUtil.format("abc", (Map<?, ?>) null));
        Assert.assertEquals("abc", StrUtil.format("abc", (Map<?, ?>) new HashMap<String, String>()));
        Map<String, String> map = new LinkedHashMap<>();
        map.put("a", "aValue");
        map.put("b", "bValue");
        Assert.assertEquals("aValue and bValue", StrUtil.format("{a} and {b}", map));
    }

    @Test
    public void testSplit() {
        Assert.assertEquals(0, StrUtil.split(null, ',', -1).size());
        Assert.assertEquals(Arrays.asList("a", "b"), StrUtil.split("a,b", ',', -1));
        Assert.assertEquals(Arrays.asList("a", "b"), StrUtil.split("a,b", ',', 0, false, false));
        Assert.assertEquals(Arrays.asList("a", "b"), StrUtil.split("a,b", (CharSequence) ",", -1, false, false));
        Assert.assertEquals(0, StrUtil.split(null, (CharSequence) ",", -1, false, false).size());
        // limit 限制分片数
        Assert.assertEquals(2, StrUtil.split("a,b,c", ',', 2).size());
        Assert.assertEquals(Arrays.asList("a", "b", "c"), StrUtil.splitTrim("a,b,c", ','));
        Assert.assertEquals(Arrays.asList("a", "b"), StrUtil.splitTrim("a , b", ','));
        Assert.assertEquals(Arrays.asList("a", "b"), StrUtil.splitTrim("a,b", ","));
        Assert.assertEquals(Arrays.asList("a", "b"), StrUtil.splitTrim("a,b", ",", -1));
        Assert.assertEquals(Arrays.asList("a", "b"), StrUtil.splitTrim("a,b", ',', -1));
        Assert.assertEquals(Arrays.asList("a", "b"), StrUtil.split("a,b", ',', false, false));
    }

    @Test
    public void testSplitByLength() {
        Assert.assertEquals(0, StrUtil.split(null, 2).length);
        Assert.assertArrayEquals(new String[]{"ab", "cd", "e"}, StrUtil.split("abcde", 2));
        Assert.assertArrayEquals(new String[]{"abc"}, StrUtil.split("abc", 5));
    }

    @Test
    public void testContains() {
        Assert.assertTrue(StrUtil.contains("abc", 'b'));
        Assert.assertFalse(StrUtil.contains("abc", 'd'));
    }

    @Test
    public void testContainsAny() {
        Assert.assertFalse(StrUtil.containsAny(null, "a"));
        Assert.assertFalse(StrUtil.containsAny("abc", new CharSequence[]{}));
        Assert.assertFalse(StrUtil.containsAny("", "a"));
        Assert.assertTrue(StrUtil.containsAny("abc", "x", "b"));
        Assert.assertFalse(StrUtil.containsAny("abc", "x", "y"));
        Assert.assertEquals("b", StrUtil.getContainsStr("abc", "x", "b"));
        Assert.assertNull(StrUtil.getContainsStr("abc", "x", "y"));
        Assert.assertNull(StrUtil.getContainsStr(null, "a"));
        Assert.assertNull(StrUtil.getContainsStr("abc", (CharSequence[]) null));
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(StrUtil.containsIgnoreCase(null, null));
        Assert.assertFalse(StrUtil.containsIgnoreCase(null, "a"));
        Assert.assertTrue(StrUtil.containsIgnoreCase("abc", "BC"));
        Assert.assertFalse(StrUtil.containsIgnoreCase("abc", "xyz"));
        Assert.assertTrue(StrUtil.containsAnyIgnoreCase("abc", "x", "BC"));
        Assert.assertFalse(StrUtil.containsAnyIgnoreCase("abc", "x", "y"));
        Assert.assertEquals("BC", StrUtil.getContainsStrIgnoreCase("abc", "x", "BC"));
        Assert.assertNull(StrUtil.getContainsStrIgnoreCase("abc", "x", "y"));
        Assert.assertNull(StrUtil.getContainsStrIgnoreCase(null, "a"));
        Assert.assertNull(StrUtil.getContainsStrIgnoreCase("abc", (CharSequence[]) null));
    }

    @Test
    public void testSub() {
        Assert.assertEquals("", StrUtil.sub(null, 1, 2));
        Assert.assertEquals("", StrUtil.sub("", 1, 2));
        Assert.assertEquals("c", StrUtil.sub("abcdefgh", 2, 3));
        Assert.assertEquals("cde", StrUtil.sub("abcdefgh", 2, -3));
        // fromIndex<0 且绝对值超长 → 0
        Assert.assertEquals("abc", StrUtil.sub("abc", -100, 3));
        // fromIndex > len → len
        Assert.assertEquals("", StrUtil.sub("abc", 100, 100));
        // toIndex<0 → len+to；再<0 → len
        Assert.assertEquals("bc", StrUtil.sub("abc", 1, -100));
        Assert.assertEquals("abc", StrUtil.sub("abc", 0, -100));
        // toIndex > len → len
        Assert.assertEquals("bc", StrUtil.sub("abc", 1, 100));
        // to < from → 互换
        Assert.assertEquals("b", StrUtil.sub("abc", 2, 1));
        // from == to → ""
        Assert.assertEquals("", StrUtil.sub("abc", 1, 1));
        Assert.assertEquals("bc", StrUtil.sub("abc", -2, 3));
    }

    @Test
    public void testSubBefore() {
        Assert.assertNull(StrUtil.subBefore(null, "a", false));
        Assert.assertEquals("", StrUtil.subBefore("", "a", false));
        Assert.assertEquals("abc", StrUtil.subBefore("abc", null, false));
        Assert.assertEquals("", StrUtil.subBefore("abc", "", false));
        Assert.assertEquals("a", StrUtil.subBefore("abcba", "b", false));
        Assert.assertEquals("abc", StrUtil.subBefore("abcba", "b", true));
        Assert.assertEquals("abc", StrUtil.subBefore("abc", "z", false));
    }

    @Test
    public void testSubAfter() {
        Assert.assertNull(StrUtil.subAfter(null, "a", false));
        Assert.assertEquals("", StrUtil.subAfter("", "a", false));
        Assert.assertEquals("", StrUtil.subAfter("abc", null, false));
        Assert.assertEquals("cba", StrUtil.subAfter("abcba", "b", false));
        Assert.assertEquals("a", StrUtil.subAfter("abcba", "b", true));
        Assert.assertEquals("", StrUtil.subAfter("abc", "z", false));
        Assert.assertEquals("abc", StrUtil.subAfter("abc", "", false));
    }

    @Test
    public void testSubBetween() {
        Assert.assertNull(StrUtil.subBetween(null, "[", "]"));
        Assert.assertNull(StrUtil.subBetween("abc", null, "]"));
        Assert.assertNull(StrUtil.subBetween("abc", "[", null));
        Assert.assertEquals("b", StrUtil.subBetween("wx[b]yz", "[", "]"));
        // after 未找到
        Assert.assertNull(StrUtil.subBetween("wx[b]yz", "[", ")"));
        // before 未找到
        Assert.assertNull(StrUtil.subBetween("wx[b]yz", "(", ")"));
        Assert.assertEquals("abc", StrUtil.subBetween("tagabctag", "tag"));
        Assert.assertNull(StrUtil.subBetween(null, "tag"));
        Assert.assertEquals("", StrUtil.subBetween("", ""));
        Assert.assertNull(StrUtil.subBetween("", "tag"));
        Assert.assertEquals("", StrUtil.subBetween("tagabctag", ""));
    }

    @Test
    public void testRemovePrefix() {
        Assert.assertEquals("", StrUtil.removePrefix(null, "a"));
        Assert.assertEquals("", StrUtil.removePrefix("abc", ""));
        Assert.assertEquals("", StrUtil.removePrefix("", "a"));
        Assert.assertEquals("bc", StrUtil.removePrefix("abc", "a"));
        Assert.assertEquals("abc", StrUtil.removePrefix("abc", "x"));
        Assert.assertEquals("BC", StrUtil.removePrefixIgnoreCase("ABC", "a"));
        Assert.assertEquals("", StrUtil.removePrefixIgnoreCase("abc", ""));
        Assert.assertEquals("ABC", StrUtil.removePrefixIgnoreCase("ABC", "x"));
        Assert.assertEquals("", StrUtil.removePrefixIgnoreCase(null, "a"));
        Assert.assertEquals("", StrUtil.removeSuffix(null, "c"));
        Assert.assertEquals("", StrUtil.removeSuffix("abc", ""));
        Assert.assertEquals("ab", StrUtil.removeSuffix("abc", "c"));
        Assert.assertEquals("abc", StrUtil.removeSuffix("abc", "x"));
        Assert.assertEquals("AB", StrUtil.removeSuffixIgnoreCase("ABC", "c"));
        Assert.assertEquals("", StrUtil.removeSuffixIgnoreCase("abc", ""));
        Assert.assertEquals("abc", StrUtil.removeSuffixIgnoreCase("abc", "X"));
        Assert.assertEquals("", StrUtil.removeSuffixIgnoreCase(null, "c"));
        Assert.assertEquals("myName", StrUtil.removeSufAndLowerFirst("MyNameVO", "VO"));
        Assert.assertEquals("myName", StrUtil.removeSufAndLowerFirst("myNameVO", "VO"));
    }

    @Test
    public void testFirstCharCase() {
        Assert.assertEquals("aBC", StrUtil.lowerFirst("ABC"));
        Assert.assertEquals("aBC", StrUtil.lowerFirst("aBC"));
        Assert.assertEquals("1BC", StrUtil.lowerFirst("1BC"));
        Assert.assertEquals("Abc", StrUtil.upperFirst("abc"));
        Assert.assertEquals("Abc", StrUtil.upperFirst("Abc"));
        Assert.assertEquals("1bc", StrUtil.upperFirst("1bc"));
        Assert.assertEquals("{bc", StrUtil.upperFirst("{bc"));
        Assert.assertEquals("sub", StrUtil.subPre("substring", 3));
        Assert.assertNull(StrUtil.subSuf(null, 1));
        Assert.assertEquals("string", StrUtil.subSuf("substring", 3));
    }

    @Test
    public void testIndexOfChar() {
        Assert.assertEquals(1, StrUtil.indexOf("abc", 'b'));
        Assert.assertEquals(-1, StrUtil.indexOf("abc", 'z'));
        Assert.assertEquals(1, StrUtil.indexOf((CharSequence) "abc", 'b', 0));
        Assert.assertEquals(1, StrUtil.indexOf(new StringBuilder("abc"), 'b'));
        Assert.assertEquals(3, StrUtil.indexOf(new StringBuilder("abcbc"), 'b', 2));
        // start<0 → 0
        Assert.assertEquals(1, StrUtil.indexOf("abc", 'b', -1, -1));
        // start>len → 0
        Assert.assertEquals(1, StrUtil.indexOf("abc", 'b', 100, -1));
        // end>len → len
        Assert.assertEquals(1, StrUtil.indexOf("abc", 'b', 0, 100));
        // end<0 → len
        Assert.assertEquals(1, StrUtil.indexOf("abc", 'b', 0, -5));
        // 范围内找不到
        Assert.assertEquals(-1, StrUtil.indexOf("abc", 'c', 0, 2));
    }

    @Test
    public void testIndexOfString() {
        Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase(null, "a"));
        Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase("abc", null));
        Assert.assertEquals(0, StrUtil.indexOfIgnoreCase("", ""));
        Assert.assertEquals(0, StrUtil.indexOfIgnoreCase("aabaabaa", "A"));
        Assert.assertEquals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "B"));
        Assert.assertEquals(1, StrUtil.indexOfIgnoreCase("aabaabaa", "AB"));
        Assert.assertEquals(5, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3));
        Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9));
        Assert.assertEquals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1));
        Assert.assertEquals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "", 2));
        // ignoreCase=false 走 JDK
        Assert.assertEquals(1, StrUtil.indexOf("aabaabaa", "ab", 0, false));
        Assert.assertEquals(1, StrUtil.indexOf("aabaabaa", "AB", 0, true));
        // null 参数
        Assert.assertEquals(-1, StrUtil.indexOf(null, "a", 0, true));
        Assert.assertEquals(-1, StrUtil.indexOf("abc", null, 0, true));
        // fromIndex > endLimit
        Assert.assertEquals(-1, StrUtil.indexOf("abc", "bcd", 5, true));
        // 空搜索串
        Assert.assertEquals(1, StrUtil.indexOf("abc", "", 1, true));
        // 找不到
        Assert.assertEquals(-1, StrUtil.indexOf("abc", "xyz", 0, true));
    }

    @Test
    public void testLastIndexOf() {
        Assert.assertEquals(7, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "A"));
        Assert.assertEquals(5, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 8));
        Assert.assertEquals(2, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 2));
        Assert.assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 1));
        // ignoreCase=false
        Assert.assertEquals(5, StrUtil.lastIndexOf("aabaabaa", "b", 8, false));
        Assert.assertEquals(4, StrUtil.lastIndexOf("aabaabaa", "AB", 8, true));
        // null
        Assert.assertEquals(-1, StrUtil.lastIndexOf(null, "a", 0, true));
        Assert.assertEquals(-1, StrUtil.lastIndexOf("abc", null, 0, true));
        // fromIndex<0 → 0
        Assert.assertEquals(-1, StrUtil.lastIndexOf("abc", "a", -1, true));
        // 空搜索串 → fromIndex
        Assert.assertEquals(2, StrUtil.lastIndexOf("abc", "", 2, true));
        // fromIndex>len → len
        Assert.assertEquals(3, StrUtil.lastIndexOf("abc", "", 100, true));
        // 找不到
        Assert.assertEquals(-1, StrUtil.lastIndexOf("abc", "z", 3, true));
        // 长搜索串
        Assert.assertEquals(-1, StrUtil.lastIndexOf("ab", "abc", 2, true));
    }

    @Test
    public void testOrdinalIndexOf() {
        Assert.assertEquals(-1, StrUtil.ordinalIndexOf(null, "a", 1));
        Assert.assertEquals(-1, StrUtil.ordinalIndexOf("abc", null, 1));
        Assert.assertEquals(-1, StrUtil.ordinalIndexOf("abc", "a", 0));
        Assert.assertEquals(0, StrUtil.ordinalIndexOf("abc", "", 1));
        Assert.assertEquals(0, StrUtil.ordinalIndexOf("aabaabaa", "a", 1));
        Assert.assertEquals(1, StrUtil.ordinalIndexOf("aabaabaa", "a", 2));
        Assert.assertEquals(2, StrUtil.ordinalIndexOf("aabaabaa", "b", 1));
        Assert.assertEquals(5, StrUtil.ordinalIndexOf("aabaabaa", "b", 2));
        Assert.assertEquals(-1, StrUtil.ordinalIndexOf("aabaabaa", "z", 1));
    }

    @Test
    public void testIsSubEqualsAndEquals() {
        Assert.assertFalse(StrUtil.isSubEquals(null, 0, "a", 0, 1, false));
        Assert.assertFalse(StrUtil.isSubEquals("a", 0, null, 0, 1, false));
        Assert.assertTrue(StrUtil.isSubEquals("abcd", 1, "bcde", 0, 2, false));
        Assert.assertTrue(StrUtil.isSubEquals("aBCd", 1, "bcd", 0, 2, true));
        Assert.assertFalse(StrUtil.isSubEquals("abd", 1, "bcd", 0, 2, false));
        // equals
        Assert.assertTrue(StrUtil.equals(null, null));
        Assert.assertFalse(StrUtil.equals(null, "a"));
        Assert.assertFalse(StrUtil.equals("a", null));
        Assert.assertTrue(StrUtil.equals("abc", "abc"));
        Assert.assertFalse(StrUtil.equals("abc", "ABC"));
        Assert.assertTrue(StrUtil.equalsIgnoreCase("abc", "ABC"));
        Assert.assertTrue(StrUtil.equalsIgnoreCase(null, null));
        Assert.assertFalse(StrUtil.equalsIgnoreCase("abc", null));
        Assert.assertTrue(StrUtil.equals("abc", "ABC", true));
        Assert.assertFalse(StrUtil.equals("abc", "ABC", false));
    }

    @Test
    public void testBuilder() {
        Assert.assertEquals(0, StrUtil.builder().length());
        Assert.assertEquals(10, StrUtil.builder(10).capacity());
        Assert.assertEquals("abc", StrUtil.builder("a", "b", "c").toString());
        Assert.assertEquals("abc", StrUtil.appendBuilder(StrUtil.builder(), "a", "b", "c").toString());
        Assert.assertEquals("", StrUtil.builder().toString());
    }

    @Test
    public void testReaderAndWriter() throws Exception {
        Assert.assertNull(StrUtil.getReader(null));
        StringReader reader = StrUtil.getReader("abc");
        Assert.assertEquals('a', reader.read());
        StringWriter writer = StrUtil.getWriter();
        Assert.assertNotNull(writer);
    }

    @Test
    public void testCount() {
        Assert.assertEquals(0, StrUtil.count(null, "a"));
        Assert.assertEquals(0, StrUtil.count("", "a"));
        Assert.assertEquals(0, StrUtil.count("abba", null));
        Assert.assertEquals(0, StrUtil.count("abba", ""));
        Assert.assertEquals(0, StrUtil.count("abba", "xxxx"));
        Assert.assertEquals(2, StrUtil.count("abba", "a"));
        Assert.assertEquals(1, StrUtil.count("abba", "ab"));
        Assert.assertEquals(0, StrUtil.count("abba", "xxx"));
        Assert.assertEquals(0, StrUtil.count("abb", "xxxx"));
        Assert.assertEquals(1, StrUtil.count("aaa", "aa"));
        // char
        Assert.assertEquals(0, StrUtil.count(null, 'a'));
        Assert.assertEquals(0, StrUtil.count("", 'a'));
        Assert.assertEquals(2, StrUtil.count("abba", 'a'));
        Assert.assertEquals(0, StrUtil.count("bbb", 'a'));
    }

    @Test
    public void testUnderlineToHump() {
        Assert.assertEquals("userName", StrUtil.underlineToHump("user_name"));
        Assert.assertEquals("userName", StrUtil.underlineToHump("_user_name"));
        Assert.assertEquals("userName", StrUtil.underlineToHump("User_Name"));
    }

    @Test
    public void testHumpToUnderline() {
        Assert.assertEquals("user_name", StrUtil.humpToUnderline("userName"));
        Assert.assertEquals("user_name", StrUtil.humpToUnderline("UserName"));
        Assert.assertEquals("user_name_x", StrUtil.humpToUnderline("userNameX"));
    }

    @Test
    public void testLineToHump() {
        Assert.assertEquals("userName", StrUtil.lineToHump("user-name"));
        Assert.assertEquals("userName", StrUtil.lineToHump("User-Name"));
    }

    @Test
    public void testHumpToLine() {
        Assert.assertEquals("user-name", StrUtil.humpToLine("userName"));
        Assert.assertEquals("user-name-x", StrUtil.humpToLine("userNameX"));
        Assert.assertEquals("user-name", StrUtil.humpToLine("UserName"));
    }

    @Test
    public void testSortAscll() {
        Assert.assertNull(StrUtil.sortAscll(null));
        List<String> empty = new ArrayList<>();
        Assert.assertTrue(StrUtil.sortAscll(empty).isEmpty());
        List<String> list = new ArrayList<>(Arrays.asList("b", "a", "c"));
        Collection<String> asc = StrUtil.sortAscll(list);
        Assert.assertEquals(Arrays.asList("a", "b", "c"), new ArrayList<>(asc));
        List<String> list2 = new ArrayList<>(Arrays.asList("b", "a", "c"));
        Collection<String> desc = StrUtil.sortAscll(list2, true);
        Assert.assertEquals(Arrays.asList("c", "b", "a"), new ArrayList<>(desc));
    }
}
