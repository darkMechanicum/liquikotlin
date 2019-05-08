package com.tsarev.liquikotlin.util

import liquibase.resource.ResourceAccessor
import java.io.InputStream

/**
 * Dummy test accessor.
 */
class DummyAccessor(
    private val list: MutableSet<String> = HashSet(),
    private val resources: MutableSet<InputStream> = HashSet(),
    private val classLoader: ClassLoader = DummyAccessor::class.java.classLoader
) : ResourceAccessor {

    companion object {
        val instance = DummyAccessor()
    }

    override fun list(
        relativeTo: String?,
        path: String?,
        includeFiles: Boolean,
        includeDirectories: Boolean,
        recursive: Boolean
    ): MutableSet<String> = list

    override fun getResourcesAsStream(path: String?): MutableSet<InputStream> = resources

    override fun toClassLoader(): ClassLoader = classLoader
}