import React, { useEffect, useState } from 'react';
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  ScrollView,
  NativeModules,
  Platform,
  Alert,
} from 'react-native';
import { StatusBar } from 'expo-status-bar';

const { AndroidHelper } = NativeModules;

export default function App() {
  const [networkStatus, setNetworkStatus] = useState<string>('Unknown');
  const [batteryLevel, setBatteryLevel] = useState<number>(-1);
  const [deviceName, setDeviceName] = useState<string>('Unknown');
  const [packageName, setPackageName] = useState<string>('Unknown');
  const [isNativeAvailable, setIsNativeAvailable] = useState<boolean>(false);
  const [isRustAvailable, setIsRustAvailable] = useState<boolean>(false);
  const [hashResult, setHashResult] = useState<string>('');

  useEffect(() => {
    if (Platform.OS === 'android' && AndroidHelper) {
      AndroidHelper.isConnected().then((connected: boolean) => {
        setNetworkStatus(connected ? 'Online 🌐' : 'Offline ❌');
      });

      AndroidHelper.getBatteryLevel().then((level: number) => {
        setBatteryLevel(level);
      });

      AndroidHelper.getDeviceName().then((name: string) => {
        setDeviceName(name);
      });

      AndroidHelper.getPackageName().then((pkg: string) => {
        setPackageName(pkg);
      });

      AndroidHelper.isNativeEngineAvailable().then((avail: boolean) => {
        setIsNativeAvailable(avail);
      });

      AndroidHelper.isRustAvailable().then((avail: boolean) => {
        setIsRustAvailable(avail);
      });
    }
  }, []);

  const handleVibrate = () => {
    if (Platform.OS === 'android' && AndroidHelper) {
      AndroidHelper.vibrate(200);
      AndroidHelper.playClickSound();
    } else {
      Alert.alert('Info', 'Android Helper is active on Android host');
    }
  };

  const handleTestHash = async () => {
    if (Platform.OS === 'android' && AndroidHelper) {
      const sha256Val = await AndroidHelper.sha256('Hello Expo & Android Helper 1.1.0');
      const sha3Val = await AndroidHelper.sha3('Hello Expo & Android Helper 1.1.0');
      setHashResult(`SHA-256: ${sha256Val.substring(0, 16)}...\nRust SHA-3: ${sha3Val.substring(0, 16)}...`);
    } else {
      setHashResult('Run on Android device/emulator');
    }
  };

  return (
    <View style={styles.container}>
      <StatusBar style="light" />
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <Text style={styles.title}>Android Helper 🚀</Text>
        <Text style={styles.subtitle}>React Native & Expo Integration Demo (v1.1.0-alpha01)</Text>

        <View style={styles.card}>
          <Text style={styles.cardTitle}>📱 Device Info</Text>
          <Text style={styles.infoText}>Device: {deviceName}</Text>

          <Text style={styles.infoText}>Package: {packageName}</Text>
          <Text style={styles.infoText}>Battery Level: {batteryLevel}%</Text>
          <Text style={styles.infoText}>Network Status: {networkStatus}</Text>
        </View>

        <View style={styles.card}>
          <Text style={styles.cardTitle}>⚡ Native Engines Status</Text>
          <Text style={styles.infoText}>
            C++ NDK Engine: {isNativeAvailable ? 'Active ⚡ (libandroidhelper.so)' : 'Fallback (Kotlin)'}
          </Text>
          <Text style={styles.infoText}>
            Rust Engine: {isRustAvailable ? 'Active 🦀 (libandroidhelper_rust.so)' : 'Fallback (Kotlin)'}
          </Text>
        </View>

        <TouchableOpacity style={styles.button} onPress={handleVibrate}>
          <Text style={styles.buttonText}>📳 Test Haptic & Tactile Audio</Text>
        </TouchableOpacity>

        <TouchableOpacity style={[styles.button, styles.buttonSecondary]} onPress={handleTestHash}>
          <Text style={styles.buttonText}>🔐 Test C++ & Rust Hash Engines</Text>
        </TouchableOpacity>

        {hashResult ? (
          <View style={styles.resultBox}>
            <Text style={styles.resultText}>{hashResult}</Text>
          </View>
        ) : null}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0f172a',
    paddingTop: 50,
  },
  scrollContent: {
    padding: 20,
    alignItems: 'center',
  },
  title: {
    fontSize: 26,
    fontWeight: 'bold',
    color: '#38bdf8',
    marginBottom: 6,
  },
  subtitle: {
    fontSize: 14,
    color: '#94a3b8',
    marginBottom: 24,
    textAlign: 'center',
  },
  card: {
    width: '100%',
    backgroundColor: '#1e293b',
    borderRadius: 12,
    padding: 16,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: '#334155',
  },
  cardTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#f8fafc',
    marginBottom: 12,
  },
  infoText: {
    fontSize: 15,
    color: '#cbd5e1',
    marginBottom: 8,
  },
  button: {
    width: '100%',
    backgroundColor: '#0284c7',
    paddingVertical: 14,
    borderRadius: 10,
    alignItems: 'center',
    marginBottom: 12,
  },
  buttonSecondary: {
    backgroundColor: '#475569',
  },
  buttonText: {
    color: '#ffffff',
    fontSize: 16,
    fontWeight: '600',
  },
  resultBox: {
    width: '100%',
    backgroundColor: '#0f172a',
    padding: 14,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#0284c7',
    marginTop: 8,
  },
  resultText: {
    color: '#38bdf8',
    fontFamily: 'monospace',
    fontSize: 13,
  },
});
