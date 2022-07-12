package com.algorigo.library

import androidx.annotation.IntRange
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun Int.toByteArray(@IntRange(from = 1, to = 4) compressBytes: Int = Int.SIZE_BYTES, byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN): ByteArray {
    return List(compressBytes) {
        when (byteOrder) {
            ByteOrder.BIG_ENDIAN -> (this shr (((compressBytes - 1) - it) * 8)).toByte()
            ByteOrder.LITTLE_ENDIAN -> (this shr (8 * it)).toByte()
            else -> throw IllegalArgumentException("ByteOrder is not found")
        }
    }.toByteArray()
}

fun ByteArray.toInt(byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN): Int {
    require(size in 1..4) { "size must be between 1 and 4"}

    val deCompressedByteArray = (when (byteOrder) {
        ByteOrder.BIG_ENDIAN -> List(Int.SIZE_BYTES - size) { 0x00.toByte() } + this.toList()
        ByteOrder.LITTLE_ENDIAN -> this.toList() + List(Int.SIZE_BYTES - size) { 0x00.toByte() }
        else -> throw IllegalArgumentException("ByteOrder is not found")
    }).toByteArray()

    return ByteBuffer
        .wrap(deCompressedByteArray)
        .order(byteOrder)
        .int
}

fun IntArray.toByteArray(@IntRange(from = 1, to = 4) compressBytes: Int = Int.SIZE_BYTES, byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN): ByteArray {
    return map { intValue ->
        intValue
            .toByteArray(compressBytes, byteOrder)
            .toList()
    }
        .flatten()
        .toByteArray()
}

fun ByteArray.toIntArray(@IntRange(from = 1, to = 4) compressBytes: Int = Int.SIZE_BYTES, byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN): IntArray {
    return asList()
        .chunked(compressBytes)
        .map {
            it
                .toByteArray()
                .toInt(byteOrder)
        }
        .toIntArray()
}

fun Long.toByteArray(@IntRange(from = 1, to = 8) compressBytes: Int = Long.SIZE_BYTES, byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN): ByteArray {
    return List(compressBytes) {
        when (byteOrder) {
            ByteOrder.BIG_ENDIAN -> (this shr (((compressBytes - 1) - it) * 8)).toByte()
            ByteOrder.LITTLE_ENDIAN -> (this shr (8 * it)).toByte()
            else -> throw IllegalArgumentException("ByteOrder is not found")
        }
    }
        .toByteArray()
}

fun ByteArray.toLong(byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN): Long {
    require(size in 1..8) { "size must be between 1 and 8"}

    val deCompressedByteArray = (when (byteOrder) {
        ByteOrder.BIG_ENDIAN -> List(Long.SIZE_BYTES - size) { 0x00.toByte() } + this.toList()
        ByteOrder.LITTLE_ENDIAN -> this.toList() + List(Long.SIZE_BYTES - size) { 0x00.toByte() }
        else -> throw IllegalArgumentException("ByteOrder is not found")
    }).toByteArray()

    return ByteBuffer
        .wrap(deCompressedByteArray)
        .order(byteOrder)
        .long
}

fun LongArray.toByteArray(@IntRange(from = 1, to = 8) compressBytes: Int = Long.SIZE_BYTES, byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN): ByteArray {
    return map { longValue ->
        longValue
            .toByteArray(compressBytes, byteOrder)
            .toList()
    }
        .flatten()
        .toByteArray()
}

fun ByteArray.toLongArray(@IntRange(from = 1, to = 8) compressBytes: Int = Long.SIZE_BYTES, byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN): LongArray {
    return asList()
        .chunked(compressBytes)
        .map {
            it
                .toByteArray()
                .toLong(byteOrder)
        }
        .toLongArray()
}
