package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.adapter.out.persistence.UserRepositoryAdapter
import com.teamhide.kream.user.application.exception.UserAlreadyExistException
import com.teamhide.kream.user.domain.model.User
import com.teamhide.kream.user.domain.usecase.RegisterUserCommand
import com.teamhide.kream.user.domain.usecase.RegisterUserUseCase
import com.teamhide.kream.user.domain.vo.Address
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RegisterUserService(
    private val userRepositoryAdapter: UserRepositoryAdapter,
) : RegisterUserUseCase {
    override fun execute(command: RegisterUserCommand): User {
        val user = User.create(
            email = command.email,
            nickname = command.nickname,
            password1 = command.password1,
            password2 = command.password2,
            address = Address(base = command.baseAddress, detail = command.detailAddress),
        )

        userRepositoryAdapter.apply {
            if (existsByEmail(email = command.email)) {
                throw UserAlreadyExistException()
            }
        }

        userRepositoryAdapter.save(user = user)

        return user
    }
}
