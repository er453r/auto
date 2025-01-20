package com.er453r.auto.docker

fun Map<String, String>.toList() = this.map{ "${it.key}=${it.value}" }