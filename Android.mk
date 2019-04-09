{apk}

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := Test
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := Test.apk
LOCAL_MODULE_CLASS := APPS
LOCAL_MODULE_SUFFIX := $(COMMON_ANDROID_PACKAGE_SUFFIX)
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true
LOCAL_MULTILIB := 32

LOCAL_OVERRIDES_PACKAGES := Launcher3

LOCAL_PREBUILT_JNI_LIBS := \
    @lib/armeabi-v7a/liba.so \
    @lib/armeabi-v7a/libb.so \
    @lib/armeabi-v7a/libc.so \

include $(BUILD_PREBUILT)



{Code}
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4

LOCAL_SRC_FILES := $(call all-java-files-under,src)
LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

LOCAL_SHARED_LIBRARIES := so libraryName

LOCAL_PROGUARD_ENABLED := disabled
LOCAL_PACKAGE_NAME := Test
LOCAL_CERTIFICATE := platform

LOCAL_AAPT_FLAGS := \
    --auto-add-overlay

include $(BUILD_PACKAGE)



{config}
PRODUCT_PACKAGES += \
    ProjeckName

PRODUCT_COPY_FILES += \
    vendor/custom/libs/liba.so:system/lib/liba.so
