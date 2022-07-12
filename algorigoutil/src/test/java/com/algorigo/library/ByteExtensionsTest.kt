package com.algorigo.library

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.ByteOrder

class ByteExtensionsTest {

    @Test
    fun `Int를 ByteArray로 변환할 수 있다 (빅 엔디안)`() {
        val intValue = 2468
        val byteArray = intValue.toByteArray()

        val expected = byteArrayOf(0, 0, 0x09.toByte(), 0xA4.toByte())

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `ByteArray를 Int로 변환할 수 있다 (빅 엔디안)`() {
        val byteArray = byteArrayOf(0, 0, 0x09.toByte(), 0xA4.toByte())
        val intValue = byteArray.toInt()

        val expected = 2468

        assertEquals(expected, intValue)
    }

    @Test
    fun `Int를 4바이트 보다 작은 ByteArray로 압축시킬 수 있다 (빅 엔디안)`() {
        val intValue = 2468
        val byteArray = intValue.toByteArray(2)

        val expected = byteArrayOf(0x09.toByte(), 0xA4.toByte())

        assertArrayEquals(expected, byteArray)
        assertEquals(2, byteArray.size)
    }

    @Test
    fun `압축시킨 ByteArray를 Int로 변환할 수 있다 (빅 엔디안)`() {
        val intValue = 2468
        val compressedByteArray = intValue.toByteArray(2)

        val recoveredIntValue = compressedByteArray.toInt()
        val expected = 2468

        assertEquals(expected, recoveredIntValue)
    }

    @Test
    fun `IntArray를 ByteArray로 변환할 수 있다 (빅 엔디안)`() {
        val intArray = intArrayOf(1234, 2468, 5678)

        val byteArray = intArray.toByteArray()

        val expected = byteArrayOf(0, 0, 0x04.toByte(), 0xD2.toByte(), 0, 0, 0x09.toByte(), 0xA4.toByte(), 0, 0, 0x16.toByte(), 0x2E.toByte())

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `ByteArray를 IntArray로 변환할 수 있다 (빅 엔디안)`() {
        val byteArray = byteArrayOf(0, 0, 0x04.toByte(), 0xD2.toByte(), 0, 0, 0x09.toByte(), 0xA4.toByte(), 0, 0, 0x16.toByte(), 0x2E.toByte())

        val intArray = byteArray.toIntArray()

        val expected = intArrayOf(1234, 2468, 5678)

        assertArrayEquals(expected, intArray)
    }

    @Test
    fun `IntArray를 압축시켜서 ByteArray로 변환할 수 있다 (빅 엔디안)`() {
        val intArray = intArrayOf(1234, 2468, 5678)

        val byteArray = intArray.toByteArray(2)

        val expected = byteArrayOf(0x04.toByte(), 0xD2.toByte(), 0x09.toByte(), 0xA4.toByte(), 0x16.toByte(), 0x2E.toByte())

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `압축시킨 ByteArray를 IntArray로 변환할 수 있다 (빅 엔디안)`() {
        val byteArray = byteArrayOf(0x04.toByte(), 0xD2.toByte(), 0x09.toByte(), 0xA4.toByte(), 0x16.toByte(), 0x2E.toByte())

        val intArray = byteArray.toIntArray(2)

        val expected = intArrayOf(1234, 2468, 5678)

        assertArrayEquals(expected, intArray)
    }

    @Test
    fun `Long을 ByteArray로 변환할 수 있다 (빅 엔디안)`() {
        val longValue = 2468L
        val byteArray = longValue.toByteArray()

        val expected = byteArrayOf(0, 0, 0, 0, 0, 0, 0x09.toByte(), 0xA4.toByte())

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `ByteArray를 Long으로 변환할 수 있다 (빅 엔디안)`() {
        val byteArray = byteArrayOf(0, 0, 0, 0, 0, 0, 0x09.toByte(), 0xA4.toByte())
        val longValue = byteArray.toLong()

        val expected = 2468L

        assertEquals(expected, longValue)
    }

    @Test
    fun `Long을 8바이트 보다 작은 ByteArray로 압축시킬 수 있다 (빅 엔디안)`() {
        val longValue = 2468L
        val byteArray = longValue.toByteArray(2)

        val expected = byteArrayOf(0x09.toByte(), 0xA4.toByte())

        assertArrayEquals(expected, byteArray)
        assertEquals(2, byteArray.size)
    }

    @Test
    fun `압축시킨 ByteArray를 Long으로 변환할 수 있다 (빅 엔디안)`() {
        val longValue = 2468L
        val byteArray = longValue.toByteArray(2)

        val recoveredLongValue = byteArray.toLong()
        val expected = 2468L

        assertEquals(expected, recoveredLongValue)
    }

    @Test
    fun `LongArray를 ByteArray로 변환할 수 있다 (빅 엔디안)`() {
        val longArray = longArrayOf(1234L, 2468L, 5678L)

        val byteArray = longArray.toByteArray()

        val expected = byteArrayOf(0, 0, 0, 0, 0, 0, 0x04.toByte(), 0xD2.toByte(), 0, 0, 0, 0, 0, 0, 0x09.toByte(), 0xA4.toByte(), 0, 0, 0, 0, 0, 0, 0x16.toByte(), 0x2E.toByte())

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `ByteArray를 LongArray로 변환할 수 있다 (빅 엔디안)`() {
        val byteArray = byteArrayOf(0, 0, 0, 0, 0, 0, 0x04.toByte(), 0xD2.toByte(), 0, 0, 0, 0, 0, 0, 0x09.toByte(), 0xA4.toByte(), 0, 0, 0, 0, 0, 0, 0x16.toByte(), 0x2E.toByte())

        val longArray = byteArray.toLongArray()

        val expected = longArrayOf(1234L, 2468L, 5678L)

        assertArrayEquals(expected, longArray)
    }

    @Test
    fun `LongArray를 압축시켜서 ByteArray로 변환할 수 있다 (빅 엔디안)`() {
        val longArray = longArrayOf(1234L, 2468L, 5678L)

        val byteArray = longArray.toByteArray(2)

        val expected = byteArrayOf(0x04.toByte(), 0xD2.toByte(), 0x09.toByte(), 0xA4.toByte(), 0x16.toByte(), 0x2E.toByte())

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `압축시킨 ByteArray를 LongArray로 변환할 수 있다 (빅 엔디안)`() {
        val byteArray = byteArrayOf(0x04.toByte(), 0xD2.toByte(), 0x09.toByte(), 0xA4.toByte(), 0x16.toByte(), 0x2E.toByte())

        val longArray = byteArray.toLongArray(2)

        val expected = longArrayOf(1234L, 2468L, 5678L)

        assertArrayEquals(expected, longArray)
    }

    @Test
    fun `Int를 ByteArray로 변환할 수 있다 (리틀 엔디안)`() {
        val intValue = 2468
        val byteArray = intValue.toByteArray(byteOrder = ByteOrder.LITTLE_ENDIAN)

        val expected = byteArrayOf(0xA4.toByte(), 0x09.toByte(), 0, 0)

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `ByteArray를 Int로 변환할 수 있다 (리틀 엔디안)`() {
        val byteArray = byteArrayOf(0xA4.toByte(), 0x09.toByte(), 0, 0)
        val intValue = byteArray.toInt(byteOrder = ByteOrder.LITTLE_ENDIAN)

        val expected = 2468

        assertEquals(expected, intValue)
    }

    @Test
    fun `Int를 4바이트 보다 작은 ByteArray로 압축시킬 수 있다 (리틀 엔디안)`() {
        val intValue = 2468
        val byteArray = intValue.toByteArray(2, byteOrder = ByteOrder.LITTLE_ENDIAN)

        val expected = byteArrayOf(0xA4.toByte(), 0x09.toByte())

        assertArrayEquals(expected, byteArray)
        assertEquals(2, byteArray.size)
    }

    @Test
    fun `압축시킨 ByteArray를 Int로 변환할 수 있다 (리틀 엔디안)`() {
        val intValue = 2468
        val compressedByteArray = intValue.toByteArray(2, byteOrder = ByteOrder.LITTLE_ENDIAN)

        val recoveredIntValue = compressedByteArray.toInt(byteOrder = ByteOrder.LITTLE_ENDIAN)
        val expected = 2468

        assertEquals(expected, recoveredIntValue)
    }

    @Test
    fun `IntArray를 ByteArray로 변환할 수 있다 (리틀 엔디안)`() {
        val intArray = intArrayOf(1234, 2468, 5678)

        val byteArray = intArray.toByteArray(byteOrder = ByteOrder.LITTLE_ENDIAN)

        val expected = byteArrayOf(0xD2.toByte(), 0x04.toByte(), 0, 0, 0xA4.toByte(), 0x09.toByte(), 0, 0, 0x2E.toByte(), 0x16.toByte(), 0, 0)

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `ByteArray를 IntArray로 변환할 수 있다 (리틀 엔디안)`() {
        val byteArray = byteArrayOf(0xD2.toByte(), 0x04.toByte(), 0, 0, 0xA4.toByte(), 0x09.toByte(), 0, 0, 0x2E.toByte(), 0x16.toByte(), 0, 0)

        val intArray = byteArray.toIntArray(byteOrder = ByteOrder.LITTLE_ENDIAN)

        val expected = intArrayOf(1234, 2468, 5678)

        assertArrayEquals(expected, intArray)
    }

    @Test
    fun `IntArray를 압축시켜서 ByteArray로 변환할 수 있다 (리틀 엔디안)`() {
        val intArray = intArrayOf(1234, 2468, 5678)

        val byteArray = intArray.toByteArray(2, ByteOrder.LITTLE_ENDIAN)

        val expected = byteArrayOf(0xD2.toByte(), 0x04.toByte(), 0xA4.toByte(), 0x09.toByte(), 0x2E.toByte(), 0x16.toByte())

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `압축시킨 ByteArray를 IntArray로 변환할 수 있다 (리틀 엔디안)`() {
        val byteArray = byteArrayOf(0xD2.toByte(), 0x04.toByte(), 0xA4.toByte(), 0x09.toByte(), 0x2E.toByte(), 0x16.toByte())

        val intArray = byteArray.toIntArray(2, byteOrder = ByteOrder.LITTLE_ENDIAN)

        val expected = intArrayOf(1234, 2468, 5678)

        assertArrayEquals(expected, intArray)
    }

    @Test
    fun `Long을 ByteArray로 변환할 수 있다 (리틀 엔디안)`() {
        val longValue = 2468L
        val byteArray = longValue.toByteArray(byteOrder = ByteOrder.LITTLE_ENDIAN)

        val expected = byteArrayOf(0xA4.toByte(), 0x09.toByte(), 0, 0, 0, 0, 0, 0)

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `ByteArray를 Long으로 변환할 수 있다 (리틀 엔디안)`() {
        val byteArray = byteArrayOf(0xA4.toByte(), 0x09.toByte(), 0, 0, 0, 0, 0, 0)
        val longValue = byteArray.toLong(byteOrder = ByteOrder.LITTLE_ENDIAN)

        val expected = 2468L

        assertEquals(expected, longValue)
    }

    @Test
    fun `Long을 8바이트 보다 작은 ByteArray로 압축시킬 수 있다 (리틀 엔디안)`() {
        val longValue = 2468L
        val byteArray = longValue.toByteArray(2, byteOrder = ByteOrder.LITTLE_ENDIAN)

        val expected = byteArrayOf(0xA4.toByte(), 0x09.toByte())

        assertArrayEquals(expected, byteArray)
        assertEquals(2, byteArray.size)
    }

    @Test
    fun `압축시킨 ByteArray를 Long으로 변환할 수 있다 (리틀 엔디안)`() {
        val longValue = 2468L
        val byteArray = longValue.toByteArray(2, ByteOrder.LITTLE_ENDIAN)

        val recoveredLongValue = byteArray.toLong(ByteOrder.LITTLE_ENDIAN)

        val expected = 2468L

        assertEquals(expected, recoveredLongValue)
    }

    @Test
    fun `LongArray를 ByteArray로 변환할 수 있다 (리틀 엔디안)`() {
        val longArray = longArrayOf(1234L, 2468L, 5678L)

        val byteArray = longArray.toByteArray(byteOrder = ByteOrder.LITTLE_ENDIAN)

        val expected = byteArrayOf(0xD2.toByte(), 0x04.toByte(), 0, 0, 0, 0, 0, 0, 0xA4.toByte(), 0x09.toByte(), 0, 0, 0, 0, 0, 0, 0x2E.toByte(), 0x16.toByte(), 0, 0, 0, 0, 0, 0)

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `ByteArray를 LongArray로 변환할 수 있다 (리틀 엔디안)`() {
        val byteArray = byteArrayOf(0xD2.toByte(), 0x04.toByte(), 0, 0, 0, 0, 0, 0, 0xA4.toByte(), 0x09.toByte(), 0, 0, 0, 0, 0, 0, 0x2E.toByte(), 0x16.toByte(), 0, 0, 0, 0, 0, 0)

        val longArray = byteArray.toLongArray(byteOrder = ByteOrder.LITTLE_ENDIAN)

        val expected = longArrayOf(1234L, 2468L, 5678L)

        assertArrayEquals(expected, longArray)
    }

    @Test
    fun `LongArray를 압축시켜서 ByteArray로 변환할 수 있다 (리틀 엔디안)`() {
        val longArray = longArrayOf(1234L, 2468L, 5678L)

        val byteArray = longArray.toByteArray(2, ByteOrder.LITTLE_ENDIAN)

        val expected = byteArrayOf(0xD2.toByte(), 0x04.toByte(), 0xA4.toByte(), 0x09.toByte(), 0x2E.toByte(), 0x16.toByte())

        assertArrayEquals(expected, byteArray)
    }

    @Test
    fun `압축시킨 ByteArray를 LongArray로 변환할 수 있다 (리틀 엔디안)`() {
        val byteArray = byteArrayOf(0xD2.toByte(), 0x04.toByte(), 0xA4.toByte(), 0x09.toByte(), 0x2E.toByte(), 0x16.toByte())

        val longArray = byteArray.toLongArray(2, ByteOrder.LITTLE_ENDIAN)

        val expected = longArrayOf(1234L, 2468L, 5678L)

        assertArrayEquals(expected, longArray)
    }
}