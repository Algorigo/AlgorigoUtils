package com.algorigo.library

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.reactivex.rxjava3.core.Single
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FileUtilTest {

    private lateinit var appContext: Context

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun createFileAndReadTextTest() {
        FileUtil
            .getExternalDirFileSingle(appContext, "algorigoUtils", "text.txt")
            .flatMap { file ->
                FileUtil
                    .saveStringToFile(file, "hello world\n")
                    .toSingleDefault(file)
            }
            .flatMap { file ->
                FileUtil
                    .saveStringToFile(file, "hello world\n")
                    .toSingleDefault(file)
            }
            .flatMap { file ->
                FileUtil
                    .saveStringToFile(file, "hello world\n")
                    .toSingleDefault(file)
            }
            .flatMap { file ->
                FileUtil
                    .saveStringToFile(file, "hello world\n")
                    .toSingleDefault(file)
            }
            .flatMap { file ->
                FileUtil
                    .removeLastLines(file, 2)
                    .toSingleDefault(file)
            }
            .flatMapCompletable {
                FileUtil.saveStringToFile(it, "hello world\n")
            }
            .andThen(FileUtil.getFilesFromExternalDirectorySingle(appContext, "algorigoUtils"))
            .flatMap {
                Single.zip(FileUtil.getLastLineString(it.first()), Single.fromCallable { FileUtil.getLinesNumber(it.first()) }) { t1, t2 ->
                    t1 to t2
                }
            }
            .test()
            .await()
            .assertValue {
                it.first == "hello world"
            }
            .assertValue {
                it.second == 3
            }
    }

    @Test
    fun readWriteByteArrayTest() {
        FileUtil
            .getExternalDirFileSingle(appContext, "algorigoUtils", "data.data")
            .flatMap { file ->
                FileUtil
                    .writeByteArrayToFileCompletable(file, byteArrayOf(0, 0, 0x00.toByte(), 0x09.toByte()))
                    .toSingleDefault(file)
            }
            .flatMap { file ->
                FileUtil.readFileToByteArraySingle(file)
            }
            .test()
            .await()
            .assertValue {
                it.size == 4
            }
            .assertValue {
                it.last() == 0x09.toByte()
            }

    }

    @After
    fun tearDown() {
        FileUtil.deleteFileOrDirectory(
            FileUtil.getExternalDirFile(appContext, "algorigoUtils")
        )
    }
}