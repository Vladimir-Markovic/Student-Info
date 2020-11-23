package com.vladmarkovic.studentinfo.domain.exception

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.vladmarkovic.studentinfo.R
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.lang.NullPointerException
import kotlin.test.assertEquals

/**
 * Created by Vlad Markovic on 2019-09-12.
 */
@RunWith(JUnitPlatform::class)
class StudentInfoExceptionTest : SubjectSpek<StudentInfoException>({

    given("an exception") {
        on("constructed with a message") {
            val errorMessage = "Error!"
            val exception = StudentInfoException(errorMessage)

            it("gives that as a message") {
                assertEquals(errorMessage, exception.message(mock()))
            }
        }

        on("constructed without passing in a message string") {
            val unknownErrorMessage = "Unknown error!"
            val exception = StudentInfoException(NullPointerException())
            val mockContext: Context = mock()
            whenever(mockContext.getString(R.string.error_unknown)).thenReturn(unknownErrorMessage)

            it("gives the error message stored as string resource") {
                assertEquals(unknownErrorMessage, exception.message(mockContext))
            }
        }
    }
})