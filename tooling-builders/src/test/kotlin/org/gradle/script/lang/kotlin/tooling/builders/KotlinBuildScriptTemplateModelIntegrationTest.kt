package org.gradle.script.lang.kotlin.tooling.builders

import org.gradle.script.lang.kotlin.KotlinBuildScript
import org.gradle.script.lang.kotlin.resolver.KotlinBuildScriptDependenciesResolver
import org.gradle.script.lang.kotlin.tooling.models.KotlinBuildScriptTemplateModel

import org.gradle.tooling.GradleConnector

import org.gradle.internal.classloader.DefaultClassLoaderFactory
import org.gradle.internal.classpath.DefaultClassPath
import org.gradle.internal.concurrent.CompositeStoppable

import org.gradle.script.lang.kotlin.fixtures.AbstractIntegrationTest
import org.gradle.script.lang.kotlin.fixtures.customDaemonRegistry
import org.gradle.script.lang.kotlin.fixtures.customInstallation
import org.gradle.script.lang.kotlin.fixtures.withDaemonRegistry

import org.junit.Test

import java.io.File


class KotlinBuildScriptTemplateModelIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `can load script template using classpath model`() {

        withBuildScript("")

        val model = fetchKotlinScriptTemplateClassPathModelFor(projectRoot)

        loadClassesFrom(
            model.classPath,
            KotlinBuildScript::class.qualifiedName!!,
            KotlinBuildScriptDependenciesResolver::class.qualifiedName!!)
    }


    private
    fun fetchKotlinScriptTemplateClassPathModelFor(projectDir: File) =
        withDaemonRegistry(customDaemonRegistry()) {
            val connection = GradleConnector.newConnector()
                .forProjectDirectory(projectDir)
                .useGradleUserHomeDir(File(projectDir, "gradle-user-home"))
                .useInstallation(customInstallation())
                .connect()
            try {
                connection.getModel(KotlinBuildScriptTemplateModel::class.java)
            } finally {
                connection.close()
            }
        }


    private
    fun loadClassesFrom(classPath: List<File>, vararg classNames: String) {
        val loader = isolatedClassLoaderFor(classPath)
        try {
            classNames.forEach {
                loader.loadClass(it)
            }
        } finally {
            stop(loader)
        }
    }

    private
    fun isolatedClassLoaderFor(classPath: List<File>) =
        DefaultClassLoaderFactory().createIsolatedClassLoader(DefaultClassPath(classPath))

    private
    fun stop(loader: ClassLoader) {
        CompositeStoppable().add(loader).stop()
    }
}
