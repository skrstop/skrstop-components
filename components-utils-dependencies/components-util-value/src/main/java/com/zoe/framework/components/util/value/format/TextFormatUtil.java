package com.zoe.framework.components.util.value.format;


import com.zoe.framework.components.util.constant.StringPoolConst;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author 蒋时华
 * @date 2020-09-04 11:10:39
 */
public class TextFormatUtil {

    private static final String FORMAT_SPECIFIER = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
    private static final Pattern MSG_PATTERN = Pattern.compile(FORMAT_SPECIFIER);

    public static String formatString(String message, Object... params) {
        if (MSG_PATTERN.matcher(message).find()) {
            return String.format(Locale.getDefault(Locale.Category.FORMAT), message, params);
        } else {
            return MessageFormatter.arrayFormat(message, params);
        }
    }

    private static final class MessageFormatter {
        static final char DELIM_START = '{';
        static final char DELIM_STOP = '}';
        static final String DELIM_STR = "{}";
        private static final char ESCAPE_CHAR = '\\';

        /**
         * Performs single argument substitution for the 'messagePattern' passed as
         * parameter.
         * <p>
         * For example,
         *
         * <pre>
         * MessageFormatter.format(&quot;Hi {}.&quot;, &quot;there&quot;);
         * </pre>
         * <p>
         * will return the string "Hi there.".
         * <p>
         *
         * @param messagePattern The message pattern which will be parsed and formatted
         * @param arg            The argument to be substituted in place of the formatting anchor
         * @return The formatted message
         */
        final public static String format(String messagePattern, Object arg) {
            return arrayFormatHandle(messagePattern, new Object[]{arg});
        }

        /**
         * Performs a two argument substitution for the 'messagePattern' passed as
         * parameter.
         * <p>
         * For example,
         *
         * <pre>
         * MessageFormatter.format(&quot;Hi {}. My name is {}.&quot;, &quot;Alice&quot;, &quot;Bob&quot;);
         * </pre>
         * <p>
         * will return the string "Hi Alice. My name is Bob.".
         *
         * @param messagePattern The message pattern which will be parsed and formatted
         * @param arg1           The argument to be substituted in place of the first formatting
         *                       anchor
         * @param arg2           The argument to be substituted in place of the second formatting
         *                       anchor
         * @return The formatted message
         */
        final public static String format(final String messagePattern, Object arg1, Object arg2) {
            return arrayFormatHandle(messagePattern, new Object[]{arg1, arg2});
        }


        final public static String arrayFormat(final String messagePattern, final Object[] argArray) {
            Throwable throwableCandidate = org.slf4j.helpers.MessageFormatter.getThrowableCandidate(argArray);
            Object[] args = argArray;
            if (throwableCandidate != null) {
                args = org.slf4j.helpers.MessageFormatter.trimmedCopy(argArray);
            }
            return arrayFormatHandle(messagePattern, args);
        }

        final public static String arrayFormatHandle(final String messagePattern, final Object[] argArray) {

            if (messagePattern == null) {
                return StringPoolConst.EMPTY;
            }

            if (argArray == null) {
                return messagePattern;
            }

            int i = 0;
            int j;
            // use string builder for better multicore performance
            StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);

            int L;
            for (L = 0; L < argArray.length; L++) {

                j = messagePattern.indexOf(DELIM_STR, i);

                if (j == -1) {
                    // no more variables
                    if (i == 0) { // this is a simple string
                        return messagePattern;
                    } else { // add the tail string which contains no variables and return
                        // the result.
                        sbuf.append(messagePattern, i, messagePattern.length());
                        return sbuf.toString();
                    }
                } else {
                    if (isEscapedDelimeter(messagePattern, j)) {
                        if (!isDoubleEscaped(messagePattern, j)) {
                            L--; // DELIM_START was escaped, thus should not be incremented
                            sbuf.append(messagePattern, i, j - 1);
                            sbuf.append(DELIM_START);
                            i = j + 1;
                        } else {
                            // The escape character preceding the delimiter start is
                            // itself escaped: "abc x:\\{}"
                            // we have to consume one backward slash
                            sbuf.append(messagePattern, i, j - 1);
                            deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                            i = j + 2;
                        }
                    } else {
                        // normal case
                        sbuf.append(messagePattern, i, j);
                        deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                        i = j + 2;
                    }
                }
            }
            // append the characters following the last {} pair.
            sbuf.append(messagePattern, i, messagePattern.length());
            return sbuf.toString();
        }

        final static boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {

            if (delimeterStartIndex == 0) {
                return false;
            }
            char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
            if (potentialEscape == ESCAPE_CHAR) {
                return true;
            } else {
                return false;
            }
        }

        final static boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
            if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR) {
                return true;
            } else {
                return false;
            }
        }

        // special treatment of array values was suggested by 'lizongbo'
        private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
            if (o == null) {
                sbuf.append("null");
                return;
            }
            if (!o.getClass().isArray()) {
                safeObjectAppend(sbuf, o);
            } else {
                // check for primitive array types because they
                // unfortunately cannot be cast to Object[]
                if (o instanceof boolean[]) {
                    booleanArrayAppend(sbuf, (boolean[]) o);
                } else if (o instanceof byte[]) {
                    byteArrayAppend(sbuf, (byte[]) o);
                } else if (o instanceof char[]) {
                    charArrayAppend(sbuf, (char[]) o);
                } else if (o instanceof short[]) {
                    shortArrayAppend(sbuf, (short[]) o);
                } else if (o instanceof int[]) {
                    intArrayAppend(sbuf, (int[]) o);
                } else if (o instanceof long[]) {
                    longArrayAppend(sbuf, (long[]) o);
                } else if (o instanceof float[]) {
                    floatArrayAppend(sbuf, (float[]) o);
                } else if (o instanceof double[]) {
                    doubleArrayAppend(sbuf, (double[]) o);
                } else {
                    objectArrayAppend(sbuf, (Object[]) o, seenMap);
                }
            }
        }

        private static void safeObjectAppend(StringBuilder sbuf, Object o) {
            try {
                String oAsString = o.toString();
                sbuf.append(oAsString);
            } catch (Throwable t) {
                sbuf.append("[FAILED toString()]");
            }

        }

        private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
            sbuf.append('[');
            if (!seenMap.containsKey(a)) {
                seenMap.put(a, null);
                final int len = a.length;
                for (int i = 0; i < len; i++) {
                    deeplyAppendParameter(sbuf, a[i], seenMap);
                    if (i != len - 1) {
                        sbuf.append(", ");
                    }
                }
                // allow repeats in siblings
                seenMap.remove(a);
            } else {
                sbuf.append("...");
            }
            sbuf.append(']');
        }

        private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            sbuf.append(']');
        }

        private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            sbuf.append(']');
        }

        private static void charArrayAppend(StringBuilder sbuf, char[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            sbuf.append(']');
        }

        private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            sbuf.append(']');
        }

        private static void intArrayAppend(StringBuilder sbuf, int[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            sbuf.append(']');
        }

        private static void longArrayAppend(StringBuilder sbuf, long[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            sbuf.append(']');
        }

        private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            sbuf.append(']');
        }

        private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            sbuf.append(']');
        }

        /**
         * Helper method to get all but the last element of an array
         *
         * @param argArray The arguments from which we want to remove the last element
         * @return a copy of the array without the last element
         */
        public static Object[] trimmedCopy(final Object[] argArray) {
            if (argArray == null || argArray.length == 0) {
                throw new IllegalStateException("non-sensical empty or null argument array");
            }

            final int trimmedLen = argArray.length - 1;

            Object[] trimmed = new Object[trimmedLen];

            if (trimmedLen > 0) {
                System.arraycopy(argArray, 0, trimmed, 0, trimmedLen);
            }

            return trimmed;
        }

    }


}
