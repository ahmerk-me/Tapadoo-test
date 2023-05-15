package com.tapadootest.app.classes

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import java.text.SimpleDateFormat

class GsonDeserializeExclusion : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return f.declaredClass == SimpleDateFormat::class.java
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}