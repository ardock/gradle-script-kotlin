package org.gradle.script.lang.kotlin.plugins.dsl

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.script.lang.kotlin.gradleScriptKotlinApi
import org.gradle.script.lang.kotlin.plugins.embedded.EmbeddedKotlinPlugin

/**
 * The `kotlin-dsl` plugin.
 */
open class KotlinDslPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.run {

            applyEmbeddedKotlinPlugin()
            addGradleKotlinDslDependency()
            configureCompilerPlugins()
        }
    }


    private
    fun Project.applyEmbeddedKotlinPlugin() {
        plugins.apply(EmbeddedKotlinPlugin::class.java)
    }


    private
    fun Project.addGradleKotlinDslDependency() {
        dependencies.add("compileOnly", gradleScriptKotlinApi())
    }


    private
    fun Project.configureCompilerPlugins() {
        // TODO sam-with-receiver
    }
}
