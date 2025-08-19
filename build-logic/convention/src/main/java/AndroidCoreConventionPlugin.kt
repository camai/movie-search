import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidCoreConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("jg.moviesearch.android.library")
                apply("jg.moviesearch.android.hilt") // Hilt 자동 적용

                // UI 플러그인 적용
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }
        }
    }
}