import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

fun Project.getLocalProperty(key: String, file: String = "local.properties"): Any? {
    val properties = Properties()
    val localProperties = File(file)

    if (localProperties.isFile) {
        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    } else {
        error("File not found")
    }

    return properties.getProperty(key)
}
