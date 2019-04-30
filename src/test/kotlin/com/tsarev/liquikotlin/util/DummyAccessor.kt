package com.tsarev.liquikotlin.util

import liquibase.resource.ResourceAccessor
import java.io.InputStream

/**
 * Dummy test accessor.
 */
class DummyAccessor private constructor() : ResourceAccessor {

    companion object {
        val instance = DummyAccessor()
    }

    override fun list(
        relativeTo: String?,
        path: String?,
        includeFiles: Boolean,
        includeDirectories: Boolean,
        recursive: Boolean
    ): MutableSet<String> = HashSet()

    override fun getResourcesAsStream(path: String?): MutableSet<InputStream> = HashSet()

    override fun toClassLoader(): ClassLoader = this.javaClass.classLoader
}