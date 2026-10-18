package com.zaitxcode.android.hardware

import android.os.Build
import com.zaitxcode.android.core.AppHelper
import java.lang.reflect.Method
import java.util.Locale

object DeviceUI {

    private fun getSystemProperty(key: String): String {
        return try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val getMethod: Method = systemPropertiesClass.getMethod("get", String::class.java)
            getMethod.invoke(null, key) as? String ?: ""
        } catch (_: Throwable) {
            ""
        }
    }

    private fun getBuildDisplayLower(): String {
        return (Build.DISPLAY ?: "").lowercase(Locale.US)
    }

    // --- Xiaomi / Redmi / POCO ---

    @JvmStatic
    fun isHyperOS(): Boolean {
        AppHelper.checkInitialized()
        val miuiVersion = getSystemProperty("ro.miui.ui.version.name")
        val hyperOsProp = getSystemProperty("ro.hyperos.version")
        val buildId = getBuildDisplayLower()
        return hyperOsProp.isNotBlank() ||
            miuiVersion.equals("V816", ignoreCase = true) ||
            buildId.contains("hyperos")
    }

    @JvmStatic
    fun isMiui(): Boolean {
        AppHelper.checkInitialized()
        val miuiVersion = getSystemProperty("ro.miui.ui.version.name")
        return miuiVersion.isNotBlank() && !isHyperOS()
    }

    @JvmStatic
    fun getXiaomiUiVersion(): String {
        AppHelper.checkInitialized()
        if (isHyperOS()) {
            val ver = getSystemProperty("ro.hyperos.version")
            return if (ver.isNotBlank()) "HyperOS $ver" else "HyperOS 1.0+"
        }
        val miuiVersion = getSystemProperty("ro.miui.ui.version.name")
        return if (miuiVersion.isNotBlank()) "MIUI $miuiVersion" else ""
    }

    // --- Oppo / OnePlus / Realme ---

    @JvmStatic
    fun isColorOS(): Boolean {
        AppHelper.checkInitialized()
        val oppoRom = getSystemProperty("ro.build.version.opporom")
        val colorOs = getSystemProperty("ro.coloros.version")
        val buildId = getBuildDisplayLower()
        return oppoRom.isNotBlank() || colorOs.isNotBlank() || buildId.contains("coloros")
    }

    @JvmStatic
    fun isOxygenOS(): Boolean {
        AppHelper.checkInitialized()
        val o2 = getSystemProperty("ro.oxygen.version")
        val buildId = getBuildDisplayLower()
        return o2.isNotBlank() || buildId.contains("oxygen")
    }

    @JvmStatic
    fun isRealmeUI(): Boolean {
        AppHelper.checkInitialized()
        val realme = getSystemProperty("ro.realme.version")
        val buildId = getBuildDisplayLower()
        return realme.isNotBlank() || buildId.contains("realme")
    }

    @JvmStatic
    fun getColorOSVersion(): String {
        AppHelper.checkInitialized()
        val oppoRom = getSystemProperty("ro.build.version.opporom")
        val colorOs = getSystemProperty("ro.coloros.version")
        return when {
            oppoRom.isNotBlank() -> "ColorOS $oppoRom"
            colorOs.isNotBlank() -> "ColorOS $colorOs"
            else -> ""
        }
    }

    // --- Samsung ---

    @JvmStatic
    fun isOneUI(): Boolean {
        AppHelper.checkInitialized()
        val manufacturer = (Build.MANUFACTURER ?: "").lowercase(Locale.US)
        if (manufacturer != "samsung") return false
        return try {
            val field = Build.VERSION::class.java.getDeclaredField("SEM_PLATFORM_INT")
            val versionInt = field.getInt(null)
            versionInt > 0
        } catch (_: Throwable) {
            getSystemProperty("ro.build.version.oneui").isNotBlank()
        }
    }

    @JvmStatic
    fun isTouchWiz(): Boolean {
        AppHelper.checkInitialized()
        val manufacturer = (Build.MANUFACTURER ?: "").lowercase(Locale.US)
        return manufacturer == "samsung" && !isOneUI()
    }

    @JvmStatic
    fun getOneUIVersion(): String {
        AppHelper.checkInitialized()
        if (!isOneUI()) return ""
        return try {
            val field = Build.VERSION::class.java.getDeclaredField("SEM_PLATFORM_INT")
            val versionInt = field.getInt(null)
            val major = (versionInt - 90000) / 10000
            val minor = (versionInt - 90000) % 10000 / 100
            if (major > 0) "One UI $major.$minor" else "One UI"
        } catch (_: Throwable) {
            "One UI"
        }
    }

    // --- Vivo / iQOO ---

    @JvmStatic
    fun isOriginOS(): Boolean {
        AppHelper.checkInitialized()
        val osName = getSystemProperty("ro.vivo.os.name").lowercase(Locale.US)
        val osVersion = getSystemProperty("ro.vivo.os.version").lowercase(Locale.US)
        return osName.contains("origin") || osVersion.contains("origin")
    }

    @JvmStatic
    fun isFuntouchOS(): Boolean {
        AppHelper.checkInitialized()
        val osName = getSystemProperty("ro.vivo.os.name").lowercase(Locale.US)
        val osVersion = getSystemProperty("ro.vivo.os.version").lowercase(Locale.US)
        return (osName.contains("funtouch") || osVersion.contains("funtouch")) && !isOriginOS()
    }

    // --- Huawei / Honor ---

    @JvmStatic
    fun isEMUI(): Boolean {
        AppHelper.checkInitialized()
        val emui = getSystemProperty("ro.build.version.emui")
        return emui.isNotBlank()
    }

    @JvmStatic
    fun isHarmonyOS(): Boolean {
        AppHelper.checkInitialized()
        val hwPlatform = getSystemProperty("hw_sc.build.platform.version")
        val buildId = getBuildDisplayLower()
        return hwPlatform.isNotBlank() || buildId.contains("harmonyos")
    }

    @JvmStatic
    fun isMagicOS(): Boolean {
        AppHelper.checkInitialized()
        val magic = getSystemProperty("ro.build.version.magic")
        return magic.isNotBlank()
    }

    // --- Nothing & Motorola & Pixel & Asus & Meizu & ZTE & Lenovo & Sony & Transsion ---

    @JvmStatic
    fun isNothingOS(): Boolean {
        AppHelper.checkInitialized()
        val nothing = getSystemProperty("ro.nothing.version")
        val buildId = getBuildDisplayLower()
        return nothing.isNotBlank() || buildId.contains("nothing") || (Build.MANUFACTURER ?: "").lowercase(Locale.US) == "nothing"
    }

    @JvmStatic
    fun isMyUX(): Boolean {
        AppHelper.checkInitialized()
        val m = (Build.MANUFACTURER ?: "").lowercase(Locale.US)
        return m == "motorola"
    }

    @JvmStatic
    fun isPixelUI(): Boolean {
        AppHelper.checkInitialized()
        val brand = (Build.BRAND ?: "").lowercase(Locale.US)
        return brand == "google"
    }

    @JvmStatic
    fun isZenUI(): Boolean {
        AppHelper.checkInitialized()
        val buildId = getBuildDisplayLower()
        return buildId.contains("zenui") || (Build.MANUFACTURER ?: "").lowercase(Locale.US) == "asus"
    }

    @JvmStatic
    fun isFlyme(): Boolean {
        AppHelper.checkInitialized()
        val flyme = getSystemProperty("ro.flyme.published")
        val buildId = getBuildDisplayLower()
        return flyme.isNotBlank() || buildId.contains("flyme")
    }

    @JvmStatic
    fun isMyOS(): Boolean {
        AppHelper.checkInitialized()
        val myos = getSystemProperty("ro.build.version.myos")
        return myos.isNotBlank()
    }

    @JvmStatic
    fun isRedMagicOS(): Boolean {
        AppHelper.checkInitialized()
        val redmagic = getSystemProperty("ro.build.version.redmagic")
        return redmagic.isNotBlank()
    }

    @JvmStatic
    fun isZui(): Boolean {
        AppHelper.checkInitialized()
        val zui = getSystemProperty("ro.lenovo.lvp.version")
        return zui.isNotBlank()
    }

    @JvmStatic
    fun isHiOS(): Boolean {
        AppHelper.checkInitialized()
        val hios = getSystemProperty("ro.gn.gnromver")
        val buildId = getBuildDisplayLower()
        return hios.contains("hios") || buildId.contains("hios")
    }

    @JvmStatic
    fun isXOS(): Boolean {
        AppHelper.checkInitialized()
        val buildId = getBuildDisplayLower()
        return buildId.contains("xos")
    }

    // --- Custom ROMs (Open Source & Community ROMs) ---

    @JvmStatic
    fun isLineageOS(): Boolean {
        AppHelper.checkInitialized()
        val ver = getSystemProperty("ro.lineage.version")
        val bver = getSystemProperty("ro.lineage.build.version")
        val buildId = getBuildDisplayLower()
        return ver.isNotBlank() || bver.isNotBlank() || buildId.contains("lineage")
    }

    @JvmStatic
    fun isPixelExperience(): Boolean {
        AppHelper.checkInitialized()
        val pe = getSystemProperty("ro.pixelexperience.version")
        val buildId = getBuildDisplayLower()
        return pe.isNotBlank() || buildId.contains("pixelexperience")
    }

    @JvmStatic
    fun isGrapheneOS(): Boolean {
        AppHelper.checkInitialized()
        val g = getSystemProperty("ro.grapheneos.version")
        val buildId = getBuildDisplayLower()
        return g.isNotBlank() || buildId.contains("grapheneos")
    }

    @JvmStatic
    fun isCalyxOS(): Boolean {
        AppHelper.checkInitialized()
        val c = getSystemProperty("ro.calyxos.version")
        val buildId = getBuildDisplayLower()
        return c.isNotBlank() || buildId.contains("calyxos")
    }

    @JvmStatic
    fun isCrDroid(): Boolean {
        AppHelper.checkInitialized()
        val cr = getSystemProperty("ro.crdroid.version")
        val buildId = getBuildDisplayLower()
        return cr.isNotBlank() || buildId.contains("crdroid")
    }

    @JvmStatic
    fun isArrowOS(): Boolean {
        AppHelper.checkInitialized()
        val arrow = getSystemProperty("ro.arrow.version")
        val buildId = getBuildDisplayLower()
        return arrow.isNotBlank() || buildId.contains("arrowos")
    }

    @JvmStatic
    fun isEvolutionX(): Boolean {
        AppHelper.checkInitialized()
        val evo = getSystemProperty("ro.evolution.version")
        val buildId = getBuildDisplayLower()
        return evo.isNotBlank() || buildId.contains("evolution")
    }

    @JvmStatic
    fun isHavocOS(): Boolean {
        AppHelper.checkInitialized()
        val havoc = getSystemProperty("ro.havoc.version")
        val buildId = getBuildDisplayLower()
        return havoc.isNotBlank() || buildId.contains("havoc")
    }

    @JvmStatic
    fun isResurrectionRemix(): Boolean {
        AppHelper.checkInitialized()
        val rr = getSystemProperty("ro.rr.version")
        val buildId = getBuildDisplayLower()
        return rr.isNotBlank() || buildId.contains("resurrection")
    }

    @JvmStatic
    fun isParanoidAndroid(): Boolean {
        AppHelper.checkInitialized()
        val pa = getSystemProperty("ro.aospa.version")
        val buildId = getBuildDisplayLower()
        return pa.isNotBlank() || buildId.contains("aospa") || buildId.contains("paranoid")
    }

    @JvmStatic
    fun isCorvusOS(): Boolean {
        AppHelper.checkInitialized()
        val corvus = getSystemProperty("ro.corvus.version")
        val buildId = getBuildDisplayLower()
        return corvus.isNotBlank() || buildId.contains("corvus")
    }

    @JvmStatic
    fun isDerpFest(): Boolean {
        AppHelper.checkInitialized()
        val derp = getSystemProperty("ro.derp.version")
        val buildId = getBuildDisplayLower()
        return derp.isNotBlank() || buildId.contains("derpfest")
    }

    @JvmStatic
    fun isCustomRom(): Boolean {
        AppHelper.checkInitialized()
        return isLineageOS() || isPixelExperience() || isGrapheneOS() || isCalyxOS() ||
            isCrDroid() || isArrowOS() || isEvolutionX() || isHavocOS() ||
            isResurrectionRemix() || isParanoidAndroid() || isCorvusOS() || isDerpFest()
    }

    @JvmStatic
    fun getCustomRomName(): String {
        AppHelper.checkInitialized()
        return when {
            isLineageOS() -> "LineageOS"
            isPixelExperience() -> "Pixel Experience"
            isGrapheneOS() -> "GrapheneOS"
            isCalyxOS() -> "CalyxOS"
            isCrDroid() -> "crDroid"
            isArrowOS() -> "ArrowOS"
            isEvolutionX() -> "Evolution X"
            isHavocOS() -> "Havoc-OS"
            isResurrectionRemix() -> "Resurrection Remix"
            isParanoidAndroid() -> "AOSPA / Paranoid Android"
            isCorvusOS() -> "Corvus OS"
            isDerpFest() -> "DerpFest"
            else -> ""
        }
    }

    @JvmStatic
    fun getOemSkinName(): String {
        AppHelper.checkInitialized()
        if (isCustomRom()) {
            return getCustomRomName()
        }
        return when {
            isHyperOS() -> "HyperOS"
            isMiui() -> "MIUI"
            isColorOS() -> "ColorOS"
            isOxygenOS() -> "OxygenOS"
            isRealmeUI() -> "Realme UI"
            isOneUI() -> "One UI"
            isTouchWiz() -> "TouchWiz"
            isOriginOS() -> "OriginOS"
            isFuntouchOS() -> "Funtouch OS"
            isHarmonyOS() -> "HarmonyOS"
            isMagicOS() -> "MagicOS"
            isEMUI() -> "EMUI"
            isNothingOS() -> "Nothing OS"
            isZenUI() -> "ZenUI"
            isFlyme() -> "Flyme"
            isMyOS() -> "MyOS"
            isRedMagicOS() -> "REDMAGIC OS"
            isZui() -> "ZUI"
            isHiOS() -> "HiOS"
            isXOS() -> "XOS"
            isPixelUI() -> "Pixel UI"
            else -> "Stock Android"
        }
    }
}
