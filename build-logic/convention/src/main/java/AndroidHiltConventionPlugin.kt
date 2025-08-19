import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                // KSP 플러그인 적용
                apply("com.google.devtools.ksp")
            }

            // libs 카탈로그 접근
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                // Hilt 의존성 추가
                add("implementation", libs.findLibrary("hilt-android").get())
                add("ksp", libs.findLibrary("hilt-compiler").get())
                
                // 테스트 의존성 추가
                add("testImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
            }
        }
    }
} 