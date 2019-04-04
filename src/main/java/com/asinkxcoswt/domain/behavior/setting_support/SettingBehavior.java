package com.asinkxcoswt.domain.behavior.setting_support;

public class SettingBehavior implements SettingSupport {
    private SettingQueryService queryService;

    public SettingBehavior(SettingQueryService queryService) {
        this.queryService = queryService;
    }

    @Override
    public Setting getSettingValue(String key) {
        return queryService.findByKey(key);
    }
}
