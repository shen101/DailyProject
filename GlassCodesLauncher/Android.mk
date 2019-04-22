LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES := \
	android-support-v4 \
	rxjava

LOCAL_SRC_FILES := $(call all-java-files-under,src)

LOCAL_SRC_FILES += aidl/com/android/internal/telephony/ITelephony.aidl

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res \
    frameworks/support/v7/cardview/res

LOCAL_PROGUARD_ENABLED := disabled

LOCAL_PACKAGE_NAME := GlassCodesLauncher
LOCAL_CERTIFICATE := platform

LOCAL_AAPT_FLAGS := \
    --auto-add-overlay \
    --extra-packages android.support.v7.cardview

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
    rxjava:libs/rxjava-2.2.0.jar

include $(BUILD_MULTI_PREBUILT)

# Use the following include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
