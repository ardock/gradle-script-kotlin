plugins {
    id("com.gradle.build-scan") version "1.7.2"
}

buildScan {
    setLicenseAgreementUrl("https://gradle.com/terms-of-service")
    setLicenseAgree("yes")
}
