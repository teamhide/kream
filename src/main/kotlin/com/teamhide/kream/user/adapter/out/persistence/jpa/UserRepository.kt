package com.teamhide.kream.user.adapter.out.persistence.jpa

import com.teamhide.kream.user.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>, UserQuerydslRepository
