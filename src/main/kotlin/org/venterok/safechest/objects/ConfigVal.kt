package org.venterok.safechest.objects

import org.venterok.safechest.SafeChest

class ConfigVal {
    companion object {
        val config = SafeChest.inst!!.config
    }
}