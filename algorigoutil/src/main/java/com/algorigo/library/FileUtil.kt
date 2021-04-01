package com.algorigo.library

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.*
import java.util.*

object FileUtil {

    @Throws(IOException::class)
    fun getLinesNumber(file: File): Int {
        val br = BufferedReader(FileReader(file) as Reader)

        var lineCount = 0
        var line: String = ""
        while (br.readLine()?.also { line = it } != null) {
            if (line.length > 0) {
                lineCount++
            }
        }

        return lineCount
    }

    fun saveStringToFile(file: File, string: String): Completable {
        return Completable.create {
            val directory = file.parentFile
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    it.onError(IllegalStateException("mkdir failed"))
                    return@create
                }
            }
            val fileOutputStream = FileOutputStream(file, true)
            fileOutputStream.write(string.toByteArray())
            fileOutputStream.close()
            it.onComplete()
        }
    }

    fun removeLastLines(file: File, number: Int): Completable {
        return Completable.create {
            val lines = mutableListOf<String>()
            val reader = Scanner(FileInputStream(file), "UTF-8")
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine())
            }
            reader.close()

            if (lines.size >= number) {
                for (index in 0 until number) {
                    lines.removeAt(lines.size - 1)
                }

                val fileOutputStream = FileOutputStream(file, false)
                for (line in lines) {
                    fileOutputStream.write(line.plus("\n").toByteArray())
                }
                fileOutputStream.close()
                it.onComplete()
            } else {
                it.onError(IllegalStateException("There is no enough lines in file"))
            }
        }
            .subscribeOn(Schedulers.io())
    }

    fun getLastLineString(file: File): Single<String> {
        return Single.create<String> {
            val lines = mutableListOf<String>()
            val reader = Scanner(FileInputStream(file), "UTF-8")
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine())
            }
            reader.close()
            if (lines.size > 0) {
                it.onSuccess(lines.get(lines.size - 1))
            } else {
                it.onSuccess("")
            }
        }
    }
}
