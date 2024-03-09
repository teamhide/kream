package com.teamhide.kream.user.domain.repository

import com.teamhide.kream.user.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>, UserQuerydslRepository
