package org.venterok.safechest.objects

import org.venterok.safechest.Safechest

class ConfigVal {
    companion object {
        val config = Safechest.inst!!.config
    }
}