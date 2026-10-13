package com.skrstop.framework.components.util.serialization.format.fastjson;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author 蒋时华
 */
public class FastjsonFormatTest {

    private static String write(ObjectWriterInvoker invoker) {
        JSONWriter writer = JSONWriter.of();
        invoker.invoke(writer);
        writer.close();
        return writer.toString();
    }

    private interface ObjectWriterInvoker {
        void invoke(JSONWriter writer);
    }

    private static String readString(JSONReader reader) {
        String s = reader.readString();
        reader.close();
        return s;
    }

    @Test
    public void testDateObjectWriter() {
        String json = write(w -> new DateObjectWriter("yyyy-MM-dd HH:mm:ss")
                .write(w, SimpleDateFormatHolder.parse("2024-01-02 03:04:05"), null, Date.class, 0));
        Assert.assertEquals("\"2024-01-02 03:04:05\"", json);
    }

    @Test
    public void testLocalDateObjectWriter() {
        String json = write(w -> new LocalDateObjectWriter("yyyy*MM*dd")
                .write(w, LocalDate.of(2024, 1, 2), null, LocalDate.class, 0));
        Assert.assertEquals("\"2024*01*02\"", json);
    }

    @Test
    public void testLocalDateTimeObjectWriter() {
        String json = write(w -> new LocalDateTimeObjectWriter("yyyy/MM/dd HH:mm:ss")
                .write(w, LocalDateTime.of(2024, 1, 2, 3, 4, 5), null, LocalDateTime.class, 0));
        Assert.assertEquals("\"2024/01/02 03:04:05\"", json);
    }

    @Test
    public void testLocalTimeObjectWriter() {
        String json = write(w -> new LocalTimeObjectWriter("HH-mm-ss")
                .write(w, LocalTime.of(3, 4, 5), null, LocalTime.class, 0));
        Assert.assertEquals("\"03-04-05\"", json);
    }

    private static class SimpleDateFormatHolder {
        static Date parse(String s) {
            try {
                return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Test
    public void testDateObjectReaderBlank() {
        // fastjson2 的 JSONReader.getString 在脱离字段上下文时返回空串 → 全部适配器按空白处理
        Date date = new DateObjectReader("yyyy-MM-dd HH:mm:ss")
                .readObject(JSONReader.of("\"\""), (Type) Date.class, null, 0);
        Assert.assertNull(date);
    }

    @Test
    public void testReadersWithMockedReader() throws Exception {
        JSONReader reader = org.mockito.Mockito.mock(JSONReader.class);
        org.mockito.Mockito.when(reader.getString()).thenReturn("2024-01-02 03:04:05");
        Date date = new DateObjectReader("yyyy-MM-dd HH:mm:ss").readObject(reader, (Type) Date.class, null, 0);
        Assert.assertEquals("2024-01-02 03:04:05", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

        org.mockito.Mockito.when(reader.getString()).thenReturn("2024-01-02");
        LocalDate localDate = new LocalDateObjectReader("yyyy-MM-dd").readObject(reader, (Type) LocalDate.class, null, 0);
        Assert.assertEquals(LocalDate.of(2024, 1, 2), localDate);

        org.mockito.Mockito.when(reader.getString()).thenReturn("2024-01-02 03:04:05");
        LocalDateTime localDateTime = new LocalDateTimeObjectReader("yyyy-MM-dd HH:mm:ss")
                .readObject(reader, (Type) LocalDateTime.class, null, 0);
        Assert.assertEquals(LocalDateTime.of(2024, 1, 2, 3, 4, 5), localDateTime);

        org.mockito.Mockito.when(reader.getString()).thenReturn("03-04-05");
        LocalTime localTime = new LocalTimeObjectReader("HH-mm-ss").readObject(reader, (Type) LocalTime.class, null, 0);
        Assert.assertEquals(LocalTime.of(3, 4, 5), localTime);

        // 空白 → LocalTime 适配器返回 null
        JSONReader blankReader = org.mockito.Mockito.mock(JSONReader.class);
        org.mockito.Mockito.when(blankReader.getString()).thenReturn(" ");
        Assert.assertNull(new LocalTimeObjectReader("HH-mm-ss").readObject(blankReader, (Type) LocalTime.class, null, 0));
    }

}
