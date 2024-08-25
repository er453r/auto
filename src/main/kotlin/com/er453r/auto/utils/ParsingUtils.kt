package com.er453r.auto.utils

fun String.destructured(regex: Regex) = regex.matchEntire(this)
    ?.destructured
    ?: throw IllegalArgumentException("Incorrect line $this")
