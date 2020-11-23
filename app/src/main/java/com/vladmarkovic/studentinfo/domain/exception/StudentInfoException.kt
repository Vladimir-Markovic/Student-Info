package com.vladmarkovic.studentinfo.domain.exception

import android.content.Context
import com.vladmarkovic.studentinfo.R

/**
 * Created by Vlad Markovic on 2019-09-12.
 *
 * Used for scoping exceptions to application defined exception.
 * All defined application exceptions should extend it.
 */
open class StudentInfoException(override val message: String? = null,
                                override val cause: Throwable? = null,
                                open val origin: Any? = null,
                                open val messageId: Int = R.string.error_unknown) : Throwable() {

    constructor(messageRes: Int) : this(null, null, null, messageRes)

    constructor(cause: Throwable) : this(cause.message, cause, null, R.string.error_unknown)

    constructor(messageRes: Int, cause: Throwable) : this(null, cause, null, messageRes)

    fun message(context: Context): String = message ?: context.getString(messageId)

    override fun equals(other: Any?): Boolean = other is StudentInfoException &&
            javaClass.simpleName == other.javaClass.simpleName && message == other.message &&
            cause?.javaClass?.simpleName == other.cause?.javaClass?.simpleName && origin == other.origin &&
            messageId == other.messageId

    override fun hashCode(): Int {
        var result = message?.hashCode() ?: 0
        result = 31 * result + (cause?.hashCode() ?: 0)
        result = 31 * result + (origin?.hashCode() ?: 0)
        result = 31 * result + messageId
        return result
    }
}