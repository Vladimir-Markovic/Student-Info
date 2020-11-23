package com.vladmarkovic.studentinfo.data.summary.model

/**
 * Created by Vlad Markovic on 2019-09-10.
 *
 * Models the responses for Firestore reads - illustrates the often need separation
 * between data-domain-presentation models even though often representing similar data in
 * slightly different form. Here Firestore requires default constructors this
 * all vals/vars are initialised (as per child extending data classes).
 */
interface Response