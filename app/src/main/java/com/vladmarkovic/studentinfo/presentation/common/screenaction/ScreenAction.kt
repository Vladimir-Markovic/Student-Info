package com.vladmarkovic.studentinfo.presentation.common.screenaction

/**
 * Created by Vlad Markovic on 2019-09-12.
 *
 * Represent action which happen once of and should not be represented with state
 * as on resubscribing to LiveData we don't want them to happen again, as
 * they are relevant in point of time when they occur and not anymore later.
 * Such as showing the error message or navigating.
 */
interface ScreenAction