import com.android.build.gradle.BaseExtension

fun BaseExtension.baseAndroidConfig() {
    setCompileSdkVersion(AndroidConst.COMPILE_SKD)
    defaultConfig {
        minSdk = AndroidConst.MIN_SKD

        versionCode = AndroidConst.VERSION_CODE
        versionName = AndroidConst.VERSION_NAME

        testInstrumentationRunner = "dev.maxsiomin.todoapp.HiltTestRunner"
        consumerProguardFiles("consumer-rules.pro")

        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = AndroidConst.COMPILE_JDK_VERSION
        targetCompatibility = AndroidConst.COMPILE_JDK_VERSION
    }
    kotlinOptions {
        jvmTarget = AndroidConst.KOTLIN_JVM_TARGET
    }
}
