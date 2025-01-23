package com.er453r.auto.image

import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, String>
