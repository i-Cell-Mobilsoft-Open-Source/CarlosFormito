import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension

fun PublishingExtension.registerCarlosRepository(project: Project) {
    repositories {
        maven {
            name = "GitHubPackages"
            url = project.uri("https://maven.pkg.github.com/icellmobilsoft/CarlosFormito")
            credentials {
                username = getLocalProperty("gpr.usr") as? String? ?: System.getenv("GPR_USER")
                password = getLocalProperty("gpr.key") as? String? ?: System.getenv("GPR_API_KEY")
            }
        }
    }
}
