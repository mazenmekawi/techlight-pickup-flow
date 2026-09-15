# Pickup Flow 1.1.0

Standalone Android Studio source project separated from `TechLight-Flow-v6.9.1-Source.zip`.

- Open this directory, which contains `settings.gradle`, in Android Studio.
- One application module: `:app`. No sibling-project source paths.
- Application ID: `sa.techlight.pickup.flow`. Original shared Java namespace: `sa.techlight.kitchen`.
- Original runtime Java files, manifest and resources are preserved byte-for-byte.
- Build setup from the original source: JDK 17, Gradle 8.13, Android SDK 35, AGP 8.9.2.
- Gradle Wrapper was not in the original source and is not bundled. Install Gradle 8.13 or generate a wrapper using it.

```sh
gradle :app:assembleDebug
gradle :app:testDebugUnitTest
bash tools/test-core.sh
```

No new APK, private signing keys, SDK binaries, downloaded dependency jars or font files are bundled.
A full Gradle build, installation, two-device pairing and real TechPro account validation were not rerun during separation.
See `README-AR.txt`, `VALIDATION.txt` and `SOURCE-ORIGIN.json`.
