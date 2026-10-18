package com.zaitxcode.android.apps.javaapp;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.zaitxcode.android.app.AppInfo;
import com.zaitxcode.android.app.AppState;
import com.zaitxcode.android.app.Notification;
import com.zaitxcode.android.app.Permission;
import com.zaitxcode.android.app.Signature;
import com.zaitxcode.android.browser.Browser;
import com.zaitxcode.android.content.Clipboard;
import com.zaitxcode.android.content.Intent;
import com.zaitxcode.android.core.AppHelper;
import com.zaitxcode.android.hardware.Audio;
import com.zaitxcode.android.hardware.Battery;
import com.zaitxcode.android.hardware.Biometric;
import com.zaitxcode.android.hardware.Device;
import com.zaitxcode.android.hardware.Display;
import com.zaitxcode.android.hardware.Vibration;
import com.zaitxcode.android.io.File;
import com.zaitxcode.android.io.Storage;
import com.zaitxcode.android.net.Network;
import com.zaitxcode.android.security.Encryption;
import com.zaitxcode.android.util.Time;
import com.zaitxcode.android.util.Validation;
import com.zaitxcode.android.view.Keyboard;
import com.zaitxcode.android.view.Screen;

import java.util.List;
import java.util.Locale;

import kotlin.Unit;

/**
 * Standalone, pure-Java demo application for the AndroidHelper library.
 *
 * <p>Every helper module is invoked directly from Java, proving the full
 * Java/Kotlin interop surface of the library.</p>
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1001;

    private EditText inputEditText;
    private TextView outputTextView;
    private final StringBuilder outputBuffer = new StringBuilder();

    private String lastEncrypted = "";
    private String lastSecretKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEditText = findViewById(R.id.etInput);
        outputTextView = findViewById(R.id.tvOutput);

        bindButtons();

        appendOutput("Android Helper Java Demo started.");
        appendOutput("Every helper below is called directly from Java.");
    }

    private void bindButtons() {
        findViewById(R.id.btnNetworkState).setOnClickListener(v -> testNetworkState());
        findViewById(R.id.btnNetworkTransport).setOnClickListener(v -> testNetworkTransport());
        findViewById(R.id.btnGetIp).setOnClickListener(v -> testGetIp());
        findViewById(R.id.btnNetworkListener).setOnClickListener(v -> testNetworkListener());

        findViewById(R.id.btnVibrateShort).setOnClickListener(v -> testVibrate());
        findViewById(R.id.btnVibratePattern).setOnClickListener(v -> testVibratePattern());
        findViewById(R.id.btnBatteryInfo).setOnClickListener(v -> testBatteryInfo());
        findViewById(R.id.btnBiometricCheck).setOnClickListener(v -> testBiometric());
        findViewById(R.id.btnDeviceInfo).setOnClickListener(v -> testDeviceInfo());
        findViewById(R.id.btnDisplayInfo).setOnClickListener(v -> testDisplayInfo());
        findViewById(R.id.btnClickSound).setOnClickListener(v -> testClickSound());

        findViewById(R.id.btnCopyText).setOnClickListener(v -> testCopyText());
        findViewById(R.id.btnPasteText).setOnClickListener(v -> testPasteText());
        findViewById(R.id.btnOpenUrl).setOnClickListener(v -> testOpenUrl());
        findViewById(R.id.btnOpenSettings).setOnClickListener(v -> testOpenSettings());
        findViewById(R.id.btnWhatsApp).setOnClickListener(v -> testWhatsApp());
        findViewById(R.id.btnDial).setOnClickListener(v -> testDial());
        findViewById(R.id.btnSms).setOnClickListener(v -> testSms());
        findViewById(R.id.btnEmail).setOnClickListener(v -> testEmail());
        findViewById(R.id.btnShareText).setOnClickListener(v -> testShareText());
        findViewById(R.id.btnShareFile).setOnClickListener(v -> testShareFile());
        findViewById(R.id.btnOpenMap).setOnClickListener(v -> testOpenMap());
        findViewById(R.id.btnPlayStore).setOnClickListener(v -> testPlayStore());

        findViewById(R.id.btnShowNotification).setOnClickListener(v -> testShowNotification());
        findViewById(R.id.btnCancelNotifications).setOnClickListener(v -> testCancelNotifications());

        findViewById(R.id.btnCheckPermission).setOnClickListener(v -> testCheckPermission());
        findViewById(R.id.btnRequestCamera).setOnClickListener(v -> testRequestCamera());

        findViewById(R.id.btnAppInfo).setOnClickListener(v -> testAppInfo());
        findViewById(R.id.btnAppState).setOnClickListener(v -> testAppState());
        findViewById(R.id.btnSignatures).setOnClickListener(v -> testSignatures());
        findViewById(R.id.btnLogger).setOnClickListener(v -> testLogger());

        findViewById(R.id.btnTime).setOnClickListener(v -> testTime());
        findViewById(R.id.btnValidation).setOnClickListener(v -> testValidation());
        findViewById(R.id.btnStorage).setOnClickListener(v -> testStorage());
        findViewById(R.id.btnWriteFile).setOnClickListener(v -> testWriteFile());
        findViewById(R.id.btnReadFile).setOnClickListener(v -> testReadFile());
        findViewById(R.id.btnDeleteFile).setOnClickListener(v -> testDeleteFile());

        findViewById(R.id.btnHashes).setOnClickListener(v -> testHashes());
        findViewById(R.id.btnHmac).setOnClickListener(v -> testHmac());
        findViewById(R.id.btnBase64).setOnClickListener(v -> testBase64());
        findViewById(R.id.btnAesEncrypt).setOnClickListener(v -> testAesEncrypt());
        findViewById(R.id.btnAesDecrypt).setOnClickListener(v -> testAesDecrypt());

        findViewById(R.id.btnHideKeyboard).setOnClickListener(v -> testHideKeyboard());
        findViewById(R.id.btnBlockCapture).setOnClickListener(v -> testBlockCapture());
        findViewById(R.id.btnUnblockCapture).setOnClickListener(v -> testUnblockCapture());
    }

    // ==================== Network ====================

    private void testNetworkState() {
        appendOutput("\n=== Network State ===");
        appendOutput("Connected: " + Network.isConnected());
        appendOutput("Validated: " + Network.hasValidatedInternet());
        appendOutput("Metered: " + Network.isConnectionMetered());
    }

    private void testNetworkTransport() {
        appendOutput("\n=== Network Transport ===");
        appendOutput("Transport: " + Network.activeTransport());
        appendOutput("WiFi: " + Network.isWifiConnected());
        appendOutput("Cellular: " + Network.isCellularConnected());
        appendOutput("Ethernet: " + Network.isEthernetConnected());
        appendOutput("VPN: " + Network.isVpnConnected());
    }

    private void testGetIp() {
        appendOutput("\n=== IP Address ===");
        String ip = Network.getIpAddress();
        appendOutput("Local IP: " + (ip != null ? ip : "Not available"));
    }

    private void testNetworkListener() {
        appendOutput("\n=== Connection Listener ===");
        Network.addConnectionListener(connected -> {
            appendOutput("Listener fired. Connected: " + connected);
            return Unit.INSTANCE;
        });
        appendOutput("Listener registered (fires immediately with current state).");
    }

    // ==================== Hardware ====================

    private void testVibrate() {
        Vibration.vibrate(200L);
        appendOutput("\n=== Vibration ===");
        appendOutput("Vibrated for 200 ms");
    }

    private void testVibratePattern() {
        long[] pattern = {0L, 100L, 50L, 200L};
        Vibration.vibratePattern(pattern, -1);
        appendOutput("\n=== Vibration Pattern ===");
        appendOutput("Pattern vibration started");
    }

    private void testBatteryInfo() {
        appendOutput("\n=== Battery Info ===");
        appendOutput("Level: " + Battery.getBatteryLevel() + "%");
        appendOutput("Charging: " + Battery.isCharging());
        appendOutput("Charging type: " + Battery.getChargingType());
        appendOutput("Status: " + Battery.getBatteryStatus());
        appendOutput("Health: " + Battery.getBatteryHealth());
        appendOutput("Power save mode: " + Battery.isPowerSaveMode());
    }

    private void testBiometric() {
        appendOutput("\n=== Biometric ===");
        boolean available = Biometric.canAuthenticate();
        appendOutput("Biometric available: " + available);
        if (available) {
            Biometric.authenticate(
                    this,
                    "Biometric Authentication",
                    "Authenticate to test AndroidHelper",
                    null,
                    "Cancel",
                    () -> {
                        appendOutput("Authentication succeeded");
                        return Unit.INSTANCE;
                    },
                    (errorCode, errString) -> {
                        appendOutput("Authentication error " + errorCode + ": " + errString);
                        return Unit.INSTANCE;
                    },
                    () -> {
                        appendOutput("Authentication failed");
                        return Unit.INSTANCE;
                    }
            );
        } else {
            appendOutput("Biometric authentication is unavailable on this device");
        }
    }

    private void testDeviceInfo() {
        appendOutput("\n=== Device Info ===");
        appendOutput("Device: " + Device.deviceName());
        appendOutput("Brand: " + Device.brand());
        appendOutput("Manufacturer: " + Device.manufacturer());
        appendOutput("Model: " + Device.model());
        appendOutput("Android version: " + Device.androidVersion());
        appendOutput("SDK: " + Device.sdk());
        appendOutput("Tablet: " + Device.isTablet());
        appendOutput("Emulator: " + Device.isEmulator());
        appendOutput("Uptime ms: " + Device.getUptimeMillis());
        appendOutput("Total RAM: " + Device.getTotalRam());
        appendOutput("Free RAM: " + Device.getFreeRam());
        appendOutput("Dark mode: " + Device.isDarkMode());
        appendOutput("Language: " + Device.getDeviceLanguage());
    }

    private void testDisplayInfo() {
        appendOutput("\n=== Display Info ===");
        appendOutput("Portrait: " + Display.isPortrait());
        appendOutput("Landscape: " + Display.isLandscape());
        appendOutput("Width dp: " + Display.getScreenWidthDp());
        appendOutput("Height dp: " + Display.getScreenHeightDp());
        appendOutput("Density: " + Display.getScreenDensity());
        appendOutput("16 dp in px: " + Display.dpToPx(16f));
    }

    private void testClickSound() {
        Audio.playClickSound();
        appendOutput("\n=== Audio ===");
        appendOutput("Click sound played");
        appendOutput("Muted: " + Audio.isMuted());
        appendOutput("Music volume: " + Audio.getMusicVolume() + "%");
        appendOutput("Headphones: " + Audio.isHeadphonesConnected());
    }

    // ==================== Content ====================

    private void testCopyText() {
        String text = currentInput();
        Clipboard.copyText(text);
        appendOutput("\n=== Clipboard ===");
        appendOutput("Copied: " + text);
    }

    private void testPasteText() {
        appendOutput("\n=== Clipboard ===");
        appendOutput("Clipboard: " + Clipboard.getText());
    }

    private void testOpenUrl() {
        Browser.openUrl("https://docs.zaitxcode.com/androidhelper");
        appendOutput("\n=== Browser ===");
        appendOutput("Opening documentation URL");
    }

    private void testOpenSettings() {
        Intent.openAppSettings(this);
        appendOutput("\n=== Intent ===");
        appendOutput("Opening app settings");
    }

    private void testWhatsApp() {
        Intent.openWhatsApp("201234567890", "Hello from Java demo", this);
        appendOutput("\n=== Intent ===");
        appendOutput("Opening WhatsApp chat");
    }

    private void testDial() {
        Intent.dial("201234567890", this);
        appendOutput("\n=== Intent ===");
        appendOutput("Opening dialer");
    }

    private void testSms() {
        Intent.sendSms("201234567890", currentInput(), this);
        appendOutput("\n=== Intent ===");
        appendOutput("Opening SMS composer");
    }

    private void testEmail() {
        Intent.sendEmail("test@example.com", "AndroidHelper", currentInput(), this);
        appendOutput("\n=== Intent ===");
        appendOutput("Opening email composer");
    }

    private void testShareText() {
        Intent.shareText(currentInput(), this);
        appendOutput("\n=== Intent ===");
        appendOutput("Opening share sheet");
    }

    private void testShareFile() {
        try {
            java.io.File file = new java.io.File(getCacheDir(), "androidhelper-java.txt");
            java.io.FileOutputStream stream = new java.io.FileOutputStream(file);
            stream.write(currentInput().getBytes());
            stream.close();

            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent.shareFile(uri, "text/plain", "Share file", this);
            appendOutput("\n=== Intent ===");
            appendOutput("Sharing generated file: " + file.getName());
        } catch (Exception e) {
            appendOutput("\n=== Intent ===");
            appendOutput("Failed to share file: " + e.getMessage());
        }
    }

    private void testOpenMap() {
        Intent.openMap(30.0444, 31.2357, "Cairo", this);
        appendOutput("\n=== Intent ===");
        appendOutput("Opening map");
    }

    private void testPlayStore() {
        Intent.openPlayStore("com.whatsapp", this);
        appendOutput("\n=== Intent ===");
        appendOutput("Opening Play Store page");
    }

    // ==================== Notifications ====================

    private void testShowNotification() {
        Notification.createChannel("java_demo", "Java Demo Channel");
        Notification.showNotification(
                "java_demo",
                "AndroidHelper",
                currentInput(),
                R.drawable.ic_launcher,
                null,
                1001,
                "Java Demo Channel"
        );
        appendOutput("\n=== Notification ===");
        appendOutput("Notification posted");
    }

    private void testCancelNotifications() {
        Notification.cancelAll();
        Notification.deleteChannel("java_demo");
        appendOutput("\n=== Notification ===");
        appendOutput("All notifications cancelled");
    }

    // ==================== Permissions ====================

    private void testCheckPermission() {
        appendOutput("\n=== Permissions ===");
        appendOutput("Camera granted: " + Permission.isGranted(Manifest.permission.CAMERA));
        appendOutput("Internet granted: " + Permission.isGranted(Manifest.permission.INTERNET));

        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.INTERNET};
        appendOutput("All granted: " + Permission.areGranted(permissions));

        List<String> denied = Permission.deniedPermissions(permissions);
        appendOutput("Denied: " + denied);
    }

    private void testRequestCamera() {
        appendOutput("\n=== Permissions ===");
        appendOutput("Requesting camera permission");
        Permission.request(this, Manifest.permission.CAMERA, REQUEST_CAMERA);
    }

    // ==================== App ====================

    private void testAppInfo() {
        appendOutput("\n=== App Info ===");
        appendOutput("Package: " + AppInfo.packageName());
        appendOutput("Name: " + AppInfo.appName());
        appendOutput("Version name: " + AppInfo.versionName());
        appendOutput("Version code: " + AppInfo.versionCode());
        appendOutput("Debuggable: " + AppInfo.isDebuggable());
        appendOutput("Installer: " + AppInfo.installerPackageName());
        appendOutput("WhatsApp installed: " + AppInfo.isPackageInstalled("com.whatsapp"));
    }

    private void testAppState() {
        appendOutput("\n=== App State ===");
        appendOutput("Foreground: " + AppState.isAppInForeground());
        appendOutput("Background: " + AppState.isAppInBackground());
        appendOutput("Screen on: " + AppState.isScreenOn());
        appendOutput("Low RAM device: " + AppState.isLowRamDevice());
        appendOutput("Ignoring battery optimizations: " + AppState.isIgnoringBatteryOptimizations());
    }

    private void testSignatures() {
        appendOutput("\n=== App Signatures ===");
        appendOutput("Primary SHA-1: " + Signature.getAppPrimarySignatureSHA1());
        appendOutput("Signature count: " + Signature.getAppSignatures().size());
    }

    private void testLogger() {
        AppHelper.log("JavaDemo", "Info log triggered");
        AppHelper.logWarning("JavaDemo", "Warning log triggered");
        AppHelper.logError("JavaDemo", "Error log triggered");
        appendOutput("\n=== Logger ===");
        appendOutput("Info, warning and error logs triggered");
    }

    // ==================== Data ====================

    private void testTime() {
        long now = Time.now();
        appendOutput("\n=== Time ===");
        appendOutput("Now: " + Time.format(now, "yyyy-MM-dd HH:mm:ss", Locale.getDefault()));
        appendOutput("Time ago: " + Time.timeAgo(now - 300000L));
        appendOutput("Is today: " + Time.isToday(now));
        appendOutput("Start of day: " + Time.startOfDay(now));
        appendOutput("End of day: " + Time.endOfDay(now));
        appendOutput("Leap year 2024: " + Time.isLeapYear(2024));
        appendOutput("Days in month: " + Time.getDaysInMonth(2024, 2));
        appendOutput("Duration 3661s: " + Time.formatDuration(3661L));
    }

    private void testValidation() {
        appendOutput("\n=== Validation ===");
        appendOutput("Email: " + Validation.isValidEmail("test@example.com"));
        appendOutput("Phone: " + Validation.isValidPhone("201234567890"));
        appendOutput("URL: " + Validation.isValidUrl("https://example.com"));
        appendOutput("IP: " + Validation.isValidIpAddress("192.168.1.1"));
        appendOutput("Username: " + Validation.isValidUsername("user_01"));
        appendOutput("Password valid: " + Validation.isPasswordValid("secret1"));
        appendOutput("Strong password: " + Validation.isStrongPassword("Secret@1"));
        appendOutput("Credit card: " + Validation.isValidCreditCard("4532015112830366"));
        appendOutput("Hex color: " + Validation.isValidHexColor("#FF0000"));
        appendOutput("JSON: " + Validation.isValidJson("{\"app\": \"AndroidHelper\"}"));
    }

    private void testStorage() {
        appendOutput("\n=== Storage ===");
        appendOutput("Free: " + Storage.formatBytes(Storage.getFreeInternalStorage()));
        appendOutput("Used: " + Storage.formatBytes(Storage.getUsedInternalStorage()));
        appendOutput("Total: " + Storage.formatBytes(Storage.getTotalInternalStorage()));
        appendOutput("Cache size: " + Storage.formatBytes(Storage.getCacheSize()));
    }

    private void testWriteFile() {
        boolean written = File.writeText("java_demo.txt", currentInput(), this);
        boolean appended = File.appendText("java_demo.txt", "\nAppended line", this);
        appendOutput("\n=== File Write ===");
        appendOutput("Written: " + written);
        appendOutput("Appended: " + appended);
    }

    private void testReadFile() {
        String content = File.readText("java_demo.txt", this);
        appendOutput("\n=== File Read ===");
        appendOutput("Exists: " + File.exists("java_demo.txt", this));
        appendOutput("Size: " + File.size("java_demo.txt", this));
        appendOutput("Content:\n" + content);
    }

    private void testDeleteFile() {
        boolean deleted = File.delete("java_demo.txt", this);
        appendOutput("\n=== File Delete ===");
        appendOutput("Deleted: " + deleted);
    }

    // ==================== Security ====================

    private void testHashes() {
        String text = currentInput();
        appendOutput("\n=== Hashes ===");
        appendOutput("Input: " + text);
        appendOutput("MD5: " + Encryption.md5(text));
        appendOutput("SHA-1: " + Encryption.sha1(text));
        appendOutput("SHA-256: " + Encryption.sha256(text));
        appendOutput("SHA-512: " + Encryption.sha512(text));
    }

    private void testHmac() {
        appendOutput("\n=== HMAC ===");
        appendOutput("HMAC-SHA256: " + Encryption.hmacSha256(currentInput(), "secret"));
    }

    private void testBase64() {
        String text = currentInput();
        String encoded = Encryption.base64Encode(text);
        appendOutput("\n=== Base64 ===");
        appendOutput("Encoded: " + encoded);
        appendOutput("Decoded: " + Encryption.base64Decode(encoded));
        String urlEncoded = Encryption.base64UrlEncode(text);
        appendOutput("URL encoded: " + urlEncoded);
        appendOutput("URL decoded: " + Encryption.base64UrlDecode(urlEncoded));
    }

    private void testAesEncrypt() {
        lastSecretKey = "mySecretKey123";
        lastEncrypted = Encryption.aesEncrypt(currentInput(), lastSecretKey);
        appendOutput("\n=== AES Encrypt ===");
        appendOutput("Key: " + lastSecretKey);
        appendOutput("Encrypted: " + lastEncrypted);
    }

    private void testAesDecrypt() {
        if (lastEncrypted.isEmpty()) {
            testAesEncrypt();
        }
        appendOutput("\n=== AES Decrypt ===");
        appendOutput("Encrypted: " + lastEncrypted);
        appendOutput("Decrypted: " + Encryption.aesDecrypt(lastEncrypted, lastSecretKey));
    }

    // ==================== View ====================

    private void testHideKeyboard() {
        Keyboard.hideKeyboard(this);
        appendOutput("\n=== Keyboard ===");
        appendOutput("Hide keyboard requested");
    }

    private void testBlockCapture() {
        Screen.blockCapture(this);
        appendOutput("\n=== Screen ===");
        appendOutput("Screen capture blocked");
    }

    private void testUnblockCapture() {
        Screen.unblockCapture(this);
        appendOutput("\n=== Screen ===");
        appendOutput("Screen capture unblocked");
        testNewHelpers();
    }

    private void testNewHelpers() {
        appendOutput("\n=== New Helpers 1.1.0-alpha01 ===");
        appendOutput("OEM Skin: " + com.zaitxcode.android.hardware.DeviceUI.getOemSkinName());
        appendOutput("Is HyperOS: " + com.zaitxcode.android.hardware.DeviceUI.isHyperOS());
        appendOutput("Is ColorOS: " + com.zaitxcode.android.hardware.DeviceUI.isColorOS());
        appendOutput("Is One UI: " + com.zaitxcode.android.hardware.DeviceUI.isOneUI());
        appendOutput("Is Custom ROM: " + com.zaitxcode.android.hardware.DeviceUI.isCustomRom());
        appendOutput("Sensors Ambient Light: " + com.zaitxcode.android.hardware.Sensors.getAmbientLightLux() + " Lux");
        appendOutput("Rust Engine Available: " + com.zaitxcode.android.security.RustEngine.isAvailable());
        appendOutput("Rust SHA-3: " + com.zaitxcode.android.security.RustEngine.sha3(currentInput()));
        appendOutput("AES-GCM Encrypted: " + com.zaitxcode.android.security.CryptoGcm.aesGcmEncrypt(currentInput(), "secret123"));
    }

    // ==================== Helpers ====================

    private String currentInput() {
        String text = inputEditText.getText().toString();
        return text.isEmpty() ? "Hello AndroidHelper" : text;
    }

    private void appendOutput(String text) {
        outputBuffer.append(text).append('\n');
        runOnUiThread(() -> outputTextView.setText(outputBuffer.toString()));
    }
}
