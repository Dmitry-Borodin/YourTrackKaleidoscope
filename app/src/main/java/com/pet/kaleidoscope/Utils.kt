package com.pet.kaleidoscope

import android.util.Base64
import java.nio.charset.Charset
import java.util.*

/**
 * @author Dmitry Borodin on 3/10/19.
 */


fun String.decode() : String = Base64.decode(this, Base64.DEFAULT).toString(java.nio.charset.StandardCharsets.UTF_8)

fun String.encode() : String = Base64.encode(this.toByteArray(java.nio.charset.StandardCharsets.UTF_8), Base64.DEFAULT)
    .toString(java.nio.charset.StandardCharsets.UTF_8)