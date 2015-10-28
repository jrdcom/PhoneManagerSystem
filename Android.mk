LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4
LOCAL_STATIC_JAVA_LIBRARIES += jsonbeans
LOCAL_STATIC_JAVA_LIBRARIES += kryo
LOCAL_STATIC_JAVA_LIBRARIES += kryonet


LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := PhoneManagerSystem

LOCAL_CERTIFICATE := platform

include $(BUILD_PACKAGE)

# Also build all of the sub-targets under this one: the shared library.

include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
    kryo:libs/kryo-2.23.1-SNAPSHOT-all-debug.jar \
    kryonet:libs/libkryonet.jar \
    jsonbeans:libs/jsonbeans-0.5.jar

include $(BUILD_MULTI_PREBUILT)

include $(call all-makefiles-under,$(LOCAL_PATH))

#endif
