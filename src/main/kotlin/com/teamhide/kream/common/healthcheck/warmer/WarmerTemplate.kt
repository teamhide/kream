package com.teamhide.kream.common.healthcheck.warmer

abstract class WarmerTemplate {
    private var isDone = false

    abstract fun doRun()

    @Synchronized
    fun run() {
        if (!isDone) {
            doRun()
            markDone()
        }
    }

    private fun markDone() {
        isDone = true
    }
}
