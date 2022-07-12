package com.algorigo.library

import android.content.Context
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.*
import java.util.*

object FileUtil {

    @Throws(IOException::class)
    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
    fun getExternalDirFile(context: Context, dirPath: String, fileName: String? = null, environment: String? = null): File {
        val directory = File(
            context.getExternalFilesDir(environment)?.absolutePath,
            dirPath
        )

        if (!directory.exists()) {
            directory.mkdirs()
        }
        return if (fileName != null) {
            File(directory, fileName)
        } else {
            directory
        }
    }

    @JvmStatic
    fun getExternalDirFileSingle(context: Context, dirPath: String, filePath: String? = null, environment: String? = null): Single<File> {
        return Single.create { emitter ->
            val directory = File(
                context.getExternalFilesDir(environment)?.absolutePath,
                dirPath
            )

            if (!directory.exists()) {
                directory.mkdirs()
            }

            if (filePath != null) {
                emitter.onSuccess(File(directory, filePath))
            } else {
                emitter.onSuccess(directory)
            }
        }
    }

    @JvmStatic
    fun getFilesFromExternalDirectorySingle(context: Context, filePath: String = "", environment: String? = null): Single<List<File>> {
        return Single.create { emitter ->
            val externalDirPath = context.getExternalFilesDir(environment)?.absolutePath
            val file = File("$externalDirPath/$filePath")

            if (file.exists()) {
                emitter.onSuccess(
                    file
                        .listFiles()!!
                        .toList()
                )
            } else {
                emitter.onError(NullPointerException("${file.absolutePath} file is not existed"))
            }
        }
    }

    @JvmStatic
    fun writeByteArrayToFile(file: File, byteArray: ByteArray) {
        val fileOutputStream = FileOutputStream(file)
        try {
            fileOutputStream.write(byteArray)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileOutputStream.close()
        }
    }

    @JvmStatic
    fun writeByteArrayToFileCompletable(file: File, byteArray: ByteArray): Completable {
        return Completable.create { emitter ->
            try {
                writeByteArrayToFile(file, byteArray)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    @JvmStatic
    fun readFileToByteArray(file: File): ByteArray {
        val bufLen = 4096 // 4KB
        val buf = ByteArray(bufLen)

        var readLen: Int
        ByteArrayOutputStream().use { o ->
            file
                .inputStream()
                .use { i ->
                    while (i.read(buf, 0, bufLen).also { readLen = it } != -1) {
                        o.write(buf, 0, readLen)
                    }
                    return o.toByteArray()
                }
        }
    }

    @JvmStatic
    fun readFileToByteArraySingle(file: File): Single<ByteArray> {
        return Single.create { emitter ->
            try {
                val byteArray = readFileToByteArray(file)
                emitter.onSuccess(byteArray)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    @JvmStatic
    fun deleteFileOrDirectory(file: File): Boolean {
        return if (file.exists()) {
            if (file.isDirectory) {
                file.deleteRecursively()
            } else {
                file.delete()
            }
        } else {
            false
        }
    }
}
