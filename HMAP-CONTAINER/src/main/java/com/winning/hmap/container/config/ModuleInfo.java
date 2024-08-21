package com.winning.hmap.container.config;

public enum ModuleInfo {
    PORTAL("portal", "com.winning.hmap.portal"),
    SUPERVISE("supervise", "com.winning.hmap.rcciaf"),
    TEST("test", "com.winning.hmap.xxx");

    private String moduleName;
    private String moduleBasePackage;

    ModuleInfo(String moduleName, String moduleBasePackage) {
        this.moduleName = moduleName;
        this.moduleBasePackage = moduleBasePackage;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getModuleBasePackage() {
        return moduleBasePackage;
    }
}
