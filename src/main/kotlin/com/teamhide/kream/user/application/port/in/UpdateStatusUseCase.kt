package com.teamhide.kream.user.application.port.`in`

import com.teamhide.kream.user.domain.vo.UserStatus

data class UpdateStatusCommand(val userId: Long, val status: UserStatus)

interface UpdateStatusUseCase {
    fun execute(command: UpdateStatusCommand)
}
