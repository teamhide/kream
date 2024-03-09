package com.teamhide.kream.delivery.domain.repository

import com.teamhide.kream.delivery.domain.model.Delivery
import org.springframework.data.jpa.repository.JpaRepository

interface DeliveryRepository : JpaRepository<Delivery, Long>
